package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class WirelessIOSlotConfigMessage {
    public static final ResourceLocation ID = getResourceLocation("wireless_slot_config");
    private static final byte[] EMPTY = new byte[]{};

    public static FriendlyByteBuf encode() {
        return encode(EMPTY);
    }

    public static FriendlyByteBuf encode(byte[] configData) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByteArray(configData);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer sender, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        byte[] configData = buf.readByteArray();
        server.execute(() -> {
            ItemStack handItem = sender.getMainHandItem();
            if (handItem.getItem() == InitItems.WIRELESS_IO) {
                if (configData.length > 0) {
                    ItemWirelessIO.setSlotConfig(handItem, configData);
                }
                sender.openMenu((ItemWirelessIO) handItem.getItem());
            }
        });
    }

}
