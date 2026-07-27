package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidSoundInstance;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class PlayMaidSoundMessage {
    public static final ResourceLocation ID = getResourceLocation("play_maid_sound");

    public static FriendlyByteBuf encode(ResourceLocation soundEvent, String id, int entityId) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeResourceLocation(soundEvent);
        buf.writeUtf(id);
        buf.writeVarInt(entityId);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        ResourceLocation soundEvent = buf.readResourceLocation();
        String id = buf.readUtf();
        int entityId = buf.readVarInt();
        client.execute(() -> playSound(soundEvent, id, entityId));
    }

    @Environment(EnvType.CLIENT)
    private static void playSound(ResourceLocation soundEvent, String id, int entityId) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            Entity entity = mc.level.getEntity(entityId);
            if (entity instanceof EntityMaid maid) {
                SoundEvent event = BuiltInRegistries.SOUND_EVENT.get(soundEvent);
                if (event != null) {
                    mc.getSoundManager().play(new MaidSoundInstance(event, id, maid));

                }
            }
        }
    }

}
