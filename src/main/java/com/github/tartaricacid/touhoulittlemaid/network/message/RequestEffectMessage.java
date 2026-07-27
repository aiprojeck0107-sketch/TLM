package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;

import java.util.List;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class RequestEffectMessage {
    public static final ResourceLocation ID = getResourceLocation("request_effect");

    public static FriendlyByteBuf encode(int id) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        server.execute(() -> {
            Entity entity = player.level.getEntity(id);
            if (entity instanceof EntityMaid maid && maid.isOwnedBy(player)) {
                List<SendEffectMessage.EffectData> effects = Lists.newArrayList();
                for (MobEffectInstance effect : maid.getActiveEffects()) {
                    effects.add(new SendEffectMessage.EffectData(effect));
                }
                ServerPlayNetworking.send(player, SendEffectMessage.ID, SendEffectMessage.encode(id, effects));
            }
        });
    }
}
