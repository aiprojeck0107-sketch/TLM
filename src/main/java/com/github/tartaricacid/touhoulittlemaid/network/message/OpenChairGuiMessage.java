package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.cache.CacheIconManager;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class OpenChairGuiMessage {
    public static final ResourceLocation ID = getResourceLocation("open_chair_gui");

    public static FriendlyByteBuf encode(int id) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        client.execute(() -> handleOpenGui(id));
    }

    @Environment(EnvType.CLIENT)
    private static void handleOpenGui(int id) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity e = mc.level.getEntity(id);
        if (mc.player != null && mc.player.isAlive() && e instanceof EntityChair chair && e.isAlive()) {
            CacheIconManager.openChairModelGui(chair);
        }
    }
}
