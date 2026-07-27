package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class ToggleTabMessage {
    public static final ResourceLocation ID = getResourceLocation("toggle_tab");

    public static FriendlyByteBuf encode(int entityId, int tabId) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        buf.writeInt(tabId);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer sender, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int entityId = buf.readInt();
        int tabId = buf.readInt();
        server.execute(() -> {
            Entity entity = sender.level.getEntity(entityId);
            if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                maid.openMaidGui(sender, tabId);
            }
        });
    }
}
