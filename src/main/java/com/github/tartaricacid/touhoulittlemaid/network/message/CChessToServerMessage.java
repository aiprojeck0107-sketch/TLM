package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.block.BlockCChess;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class CChessToServerMessage {
    public static final ResourceLocation ID = getResourceLocation("cchess_to_server");

    public static FriendlyByteBuf encode(BlockPos pos, int moveFinal, boolean maidLost, boolean playerLostFinal) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeVarInt(moveFinal);
        buf.writeBoolean(maidLost);
        buf.writeBoolean(playerLostFinal);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        int move = buf.readVarInt();
        boolean maidLost = buf.readBoolean();
        boolean playerLost = buf.readBoolean();
        server.execute(() -> {
            Level level = player.level;
            if (!level.isLoaded(pos)) {
                return;
            }
            BlockCChess.maidMove(player, level, pos, move, maidLost, playerLost);
        });
    }
}
