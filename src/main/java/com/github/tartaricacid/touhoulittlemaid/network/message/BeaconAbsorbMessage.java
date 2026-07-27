package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class BeaconAbsorbMessage {
    public static final ResourceLocation ID = getResourceLocation("beacon_absorb");

    public static FriendlyByteBuf encode(float x, float y, float z) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        float x = buf.readFloat();
        float y = buf.readFloat();
        float z = buf.readFloat();
        client.execute(() -> spawnParticle(x, y, z));
    }

    @Environment(EnvType.CLIENT)
    private static void spawnParticle(float x, float y, float z) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            EntityPowerPoint.spawnExplosionParticle(mc.level, x, y, z, mc.level.random);
        }
    }
}
