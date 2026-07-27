package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidConfigManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class MaidSubConfigMessage {
    public static final ResourceLocation ID = getResourceLocation("maid_sub_config");

    public static FriendlyByteBuf encode(int id, MaidConfigManager.SyncNetwork syncNetwork) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(id);
        MaidConfigManager.SyncNetwork.encode(syncNetwork, buf);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readVarInt();
        MaidConfigManager.SyncNetwork syncNetwork = MaidConfigManager.SyncNetwork.decode(buf);
        server.execute(() -> {
            Entity entity = player.level.getEntity(id);
            if (entity instanceof EntityMaid maid && maid.isOwnedBy(player)) {
                MaidConfigManager.SyncNetwork.handle(syncNetwork, maid);
            }
        });
    }
}
