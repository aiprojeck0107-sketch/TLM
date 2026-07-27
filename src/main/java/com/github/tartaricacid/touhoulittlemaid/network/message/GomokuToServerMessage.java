package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Point;
import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.Statue;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidGomokuAI;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class GomokuToServerMessage {
    public static final ResourceLocation ID = getResourceLocation("gomoku_to_server");

    public static FriendlyByteBuf encode(BlockPos pos, Point point) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeVarInt(point.x);
        buf.writeVarInt(point.y);
        buf.writeVarInt(point.type);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        Point aiPoint = new Point(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
        server.execute(() -> {
            Level level = player.level;
            if (!level.isLoaded(pos)) {
                return;
            }
            if (level.getBlockEntity(pos) instanceof TileEntityGomoku gomoku) {
                Statue statue = gomoku.getStatue();
                if (statue != Statue.IN_PROGRESS || gomoku.isPlayerTurn() || gomoku.getChessCounter() <= 0) {
                    return;
                }
                gomoku.setChessData(aiPoint.x, aiPoint.y, aiPoint.type);
                gomoku.setStatue(MaidGomokuAI.getStatue(gomoku.getChessData(), aiPoint));
                statue = gomoku.getStatue();
                if (level instanceof ServerLevel serverLevel && serverLevel.getEntity(gomoku.getSitId()) instanceof EntitySit sit && sit.getFirstPassenger() instanceof EntityMaid maid) {
                    maid.swing(InteractionHand.MAIN_HAND);
                    if (statue == Statue.WIN) {
                        maid.getGameRecordManager().markStatue(true);
                    }
                }
                level.playSound(null, pos, InitSounds.GOMOKU, SoundSource.BLOCKS, 1.0f, 0.8F + level.random.nextFloat() * 0.4F);
                if (statue == Statue.IN_PROGRESS) {
                    gomoku.setPlayerTurn(true);
                }
                gomoku.refresh();
            }
        });
    }

}
