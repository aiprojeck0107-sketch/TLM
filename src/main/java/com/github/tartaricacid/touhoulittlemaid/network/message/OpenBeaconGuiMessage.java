package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.gui.block.MaidBeaconGui;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class OpenBeaconGuiMessage {
    public static final ResourceLocation ID = getResourceLocation("open_beacon_gui");

    public static FriendlyByteBuf encode(BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        client.execute(() -> handleOpenGui(pos));
    }

    @Environment(EnvType.CLIENT)
    private static void handleOpenGui(BlockPos pos) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        BlockEntity te = mc.level.getBlockEntity(pos);
        if (mc.player != null && mc.player.isAlive() && te instanceof TileEntityMaidBeacon) {
            mc.setScreen(new MaidBeaconGui((TileEntityMaidBeacon) te));
        }
    }

}
