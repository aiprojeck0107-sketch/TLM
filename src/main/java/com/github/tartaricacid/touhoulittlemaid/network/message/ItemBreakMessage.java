package com.github.tartaricacid.touhoulittlemaid.network.message;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class ItemBreakMessage {
    public static final ResourceLocation ID = getResourceLocation("item_break");

    public static FriendlyByteBuf encode(int id, ItemStack item) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        buf.writeItem(item);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        ItemStack item = buf.readItem();
        client.execute(() -> handleBreakItem(id, item));
    }

    @Environment(EnvType.CLIENT)
    private static void handleBreakItem(int id, ItemStack item) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity e = mc.level.getEntity(id);
        if (e instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
            livingEntity.breakItem(item);
        }
    }

}
