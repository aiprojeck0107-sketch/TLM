package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.concurrent.CompletableFuture;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SpawnParticleMessage {
    public static final ResourceLocation ID = getResourceLocation("spawn_particle");

    public static FriendlyByteBuf encode(int entityId, Type particleType) {
        return encode(entityId, particleType, 0);
    }

    public static FriendlyByteBuf encode(int entityId, Type particleType, int delayTicks) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        buf.writeInt(particleType.ordinal());
        buf.writeVarInt(delayTicks);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int entityId = buf.readInt();
        Type particleType = getTypeByIndex(buf.readInt());
        int delayTicks = buf.readVarInt();
        if (delayTicks <= 0) {
            client.execute(() -> handleSpawnParticle(entityId, particleType));
        } else {
            client.execute(() -> CompletableFuture.runAsync(() -> handleSpawnParticleDelay(entityId, particleType, delayTicks), Util.backgroundExecutor()));
        }
    }

    @Environment(EnvType.CLIENT)
    private static void handleSpawnParticleDelay(int entityId, Type particleType, int delayTicks) {
        try {
            Thread.sleep(delayTicks * 50L);
            Minecraft.getInstance().submitAsync(() -> handleSpawnParticle(entityId, particleType));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Environment(EnvType.CLIENT)
    private static void handleSpawnParticle(int entityId, Type particleType) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity e = mc.level.getEntity(entityId);
        if (e instanceof EntityMaid maid && e.isAlive()) {
            switch (particleType) {
                case EXPLOSION:
                    maid.spawnExplosionParticle();
                    return;
                case BUBBLE:
                    maid.spawnBubbleParticle();
                    return;
                case HEART:
                    maid.spawnHeartParticle();
                    return;
                case RANK_UP:
                    maid.spawnRankUpParticle();
                    return;
                case HEAL:
                    maid.spawnRestoreHealthParticle(maid.getRandom().nextInt(3) + 7);
                    return;
                default:
            }
        }
    }

    private static Type getTypeByIndex(int index) {
        return Type.values()[Mth.clamp(index, 0, Type.values().length - 1)];
    }

    public enum Type {
        /**
         * 粒子类型
         */
        EXPLOSION, BUBBLE, HEART, RANK_UP, HEAL
    }
}