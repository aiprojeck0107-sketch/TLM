package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SetMaidSoundIdMessage {
    public static final ResourceLocation ID = getResourceLocation("set_maid_sound_id");

    public static FriendlyByteBuf encode(int entityId, String soundId) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        buf.writeUtf(soundId);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer sender, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int entityId = buf.readInt();
        String soundId = buf.readUtf();
        server.execute(() -> {
            Entity entity = sender.level.getEntity(entityId);
            if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                maid.setSoundPackId(soundId);
                InitTrigger.MAID_EVENT.trigger(sender, TriggerType.CHANGE_MAID_SOUND);
            }
        });
    }

}
