package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidGomokuAI;
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

public class GomokuToClientMessage {
    public static final ResourceLocation ID = getResourceLocation("gomoku_to_client");

    public static FriendlyByteBuf encode(BlockPos pos, int[][] chessData, Point point, int count) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeVarInt(chessData.length);
        for (int[] row : chessData) {
            buf.writeVarIntArray(row);
        }
        buf.writeVarInt(point.x);
        buf.writeVarInt(point.y);
        buf.writeVarInt(point.type);
        buf.writeVarInt(count);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        BlockPos blockPos = buf.readBlockPos();
        int length = buf.readVarInt();
        int[][] chessData = new int[length][length];
        for (int i = 0; i < length; i++) {
            chessData[i] = buf.readVarIntArray();
        }
        Point pointIn = new Point(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
        int count = buf.readVarInt();
        client.execute(() -> CompletableFuture.runAsync(() -> onHandle(blockPos, chessData, pointIn, count), Util.backgroundExecutor()));
    }

    @Environment(EnvType.CLIENT)
    private static void onHandle(BlockPos pos, int[][] chessData, Point point, int count) {
        Point aiPoint = MaidGomokuAI.getService(count).getPoint(chessData, point);
        int time = (int) (Math.random() * 1250) + 250;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        FriendlyByteBuf buf = GomokuToServerMessage.encode(pos, aiPoint);
        Minecraft.getInstance().submitAsync(() -> ClientPlayNetworking.send(GomokuToServerMessage.ID, buf));
    }

}
