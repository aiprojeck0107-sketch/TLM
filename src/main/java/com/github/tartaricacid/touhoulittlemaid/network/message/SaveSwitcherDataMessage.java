package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityModelSwitcher;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SaveSwitcherDataMessage {
    public static final ResourceLocation ID = getResourceLocation("save_switcher_data");

    public static FriendlyByteBuf encode(BlockPos pos, List<TileEntityModelSwitcher.ModeInfo> modeInfos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeVarInt(modeInfos.size());
        for (TileEntityModelSwitcher.ModeInfo info : modeInfos) {
            info.toBuf(buf);
        }
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        int size = buf.readVarInt();
        List<TileEntityModelSwitcher.ModeInfo> modeInfos = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            modeInfos.add(TileEntityModelSwitcher.ModeInfo.fromBuf(buf));
        }
        server.execute(() -> {
            Level world = player.level();
            if (world.isLoaded(pos)) {
                BlockEntity te = world.getBlockEntity(pos);
                if (te instanceof TileEntityModelSwitcher) {
                    ((TileEntityModelSwitcher) te).setInfoList(modeInfos);
                }
            }
        });
    }

}
