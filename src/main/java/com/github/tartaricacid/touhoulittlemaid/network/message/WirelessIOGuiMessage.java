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

public class WirelessIOGuiMessage {
    public static final ResourceLocation ID = getResourceLocation("wireless_io_gui");

    public static FriendlyByteBuf encode(boolean isMaidToChest, boolean isBlacklist) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(isMaidToChest);
        buf.writeBoolean(isBlacklist);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer sender, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        boolean isMaidToChest = buf.readBoolean();
        boolean isBlacklist = buf.readBoolean();
        server.execute(() -> {
            ItemStack handItem = sender.getMainHandItem();
            if (handItem.getItem() == InitItems.WIRELESS_IO) {
                ItemWirelessIO.setMode(handItem, isMaidToChest);
                ItemWirelessIO.setFilterMode(handItem, isBlacklist);
            }
        });
    }

}
