package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class DismountMessage {
    public static final int DISMOUNT_BROOM = 1;
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "dismount");

    public static FriendlyByteBuf encode(int action) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(action);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer sender, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int action = buf.readInt();
        server.execute(() -> onHandle(action, sender));
    }

    private static void onHandle(int action, ServerPlayer sender) {
        // 处理卸载扫帚的逻辑
        if (action == DISMOUNT_BROOM && sender.getVehicle() instanceof EntityBroom) {
            sender.stopRiding();
        }
    }
}