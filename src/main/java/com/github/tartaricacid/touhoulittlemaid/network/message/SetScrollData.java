package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.item.ItemFoxScroll;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SetScrollData {
    public static final ResourceLocation ID = getResourceLocation("set_scroll");

    public static FriendlyByteBuf encode(String dimension, BlockPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeUtf(dimension);
        buf.writeBlockPos(pos);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer sender, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        String dimension = buf.readUtf();
        BlockPos pos = buf.readBlockPos();
        server.execute(() -> {
            ItemStack item = sender.getMainHandItem();
            if (item.getItem() instanceof ItemFoxScroll) {
                ItemFoxScroll.setTrackInfo(item, dimension, pos);
            }
        });
    }
}
