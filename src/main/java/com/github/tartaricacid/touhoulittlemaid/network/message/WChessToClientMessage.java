package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.api.game.chess.Position;
import com.github.tartaricacid.touhoulittlemaid.api.game.chess.Search;
import com.github.tartaricacid.touhoulittlemaid.util.WChessUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class WChessToClientMessage {
    public static final ResourceLocation ID = getResourceLocation("wchess_to_client");

    public static FriendlyByteBuf encode(BlockPos pos, String fenData) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeUtf(fenData);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        String fenData = buf.readUtf();
        client.execute(() -> CompletableFuture.runAsync(() -> onHandle(pos, fenData), Util.backgroundExecutor()));
    }

    @Environment(EnvType.CLIENT)
    private static void onHandle(BlockPos pos, String fenData) {
        int levelTime = 1000;
        long timeStart = System.currentTimeMillis();
        int move = 0;

        Position position = new Position();
        position.fromFen(fenData);

        // 先判断玩家是否赢了
        // 是的，我放客户端，减轻服务端压力，理论上你可直接传布尔值判断女仆输掉来作弊
        boolean maidLost = WChessUtil.isMaid(position) && position.isMate();
        boolean playerLost = false;
        if (!maidLost) {
            // TODO: 暂时不做女仆的棋技系统
            move = new Search(position, 12).searchMain(levelTime);
            // 玩家是否输了
            playerLost = position.makeMove(move) && WChessUtil.isPlayer(position) && position.isMate();
        }

        // 如果时间还有剩余，那么 sleep 一会儿
        long timeRemain = Math.max(0, levelTime - (int) (System.currentTimeMillis() - timeStart));
        try {
            if (timeRemain > 0) {
                Thread.sleep(timeRemain);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        final int moveFinal = move;
        final boolean playerLostFinal = playerLost;
        Minecraft.getInstance().submitAsync(() -> ClientPlayNetworking.send(WChessToServerMessage.ID,
                WChessToServerMessage.encode(pos, moveFinal, maidLost, playerLostFinal)));
    }
}
