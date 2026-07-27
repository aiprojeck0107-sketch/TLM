package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;

import java.util.List;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SendEffectMessage {
    public static final ResourceLocation ID = getResourceLocation("send_effect");

    public static FriendlyByteBuf encode(int id, List<EffectData> effects) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        buf.writeInt(effects.size());
        for (EffectData effect : effects) {
            effect.toBytes(buf);
        }
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        int size = buf.readInt();
        List<EffectData> effects = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            effects.add(EffectData.fromBytes(buf));
        }
        client.execute(() -> handle(id, size, effects));
    }

    @Environment(EnvType.CLIENT)
    private static void handle(int id, int size, List<EffectData> effects) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity entity = mc.level.getEntity(id);
        if (entity instanceof EntityMaid maid && maid.isAlive()) {
            maid.setEffects(effects);
        }
    }

    public static class EffectData {
        public String descriptionId;
        public int amplifier;
        public int duration;
        public int category;

        public EffectData(MobEffectInstance effect) {
            this.descriptionId = effect.getDescriptionId();
            this.amplifier = effect.getAmplifier();
            this.duration = effect.getDuration();
            this.category = effect.getEffect().getCategory().ordinal();
        }

        public EffectData(String descriptionId, int amplifier, int duration, int category) {
            this.descriptionId = descriptionId;
            this.amplifier = amplifier;
            this.duration = duration;
            this.category = category;
        }

        public static EffectData fromBytes(FriendlyByteBuf buf) {
            return new EffectData(buf.readUtf(), buf.readInt(), buf.readInt(), buf.readInt());
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeUtf(this.descriptionId);
            buf.writeInt(this.amplifier);
            buf.writeInt(this.duration);
            buf.writeInt(this.category);
        }
    }
}