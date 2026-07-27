package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class RefreshMaidBrainMessage {
    public static final ResourceLocation ID = getResourceLocation("refresh_maid_brain");

    public static FriendlyByteBuf encode(int entityId) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int entityId = buf.readInt();
        server.execute(() -> {
            Entity entity = player.level.getEntity(entityId);
            if (entity instanceof EntityMaid maid && maid.isOwnedBy(player)) {
                maid.refreshBrain((ServerLevel) player.level);
            }
        });
    }

}