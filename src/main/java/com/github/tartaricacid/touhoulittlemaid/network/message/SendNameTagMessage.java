package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SendNameTagMessage {
    private static final int MAX_STRING_LENGTH = 1024;
    public static final ResourceLocation ID = getResourceLocation("send_name_tag");

    public static FriendlyByteBuf encode(int id, String name, boolean alwaysShow) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        buf.writeUtf(name, MAX_STRING_LENGTH);
        buf.writeBoolean(alwaysShow);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        String name = buf.readUtf(MAX_STRING_LENGTH);
        boolean alwaysShow = buf.readBoolean();
        server.execute(() -> {
            Entity entity = player.level.getEntity(id);
            if (entity instanceof EntityMaid) {
                setMaidNameTag(name, alwaysShow, player, (EntityMaid) entity);
            }
        });
    }

    private static void setMaidNameTag(String original, boolean alwaysShow, ServerPlayer player, EntityMaid maid) {
        String name = original.substring(0, Math.min(32, original.length()));
        if (player.equals(maid.getOwner()) && player.getMainHandItem().getItem() == Items.NAME_TAG) {
            maid.setCustomName(Component.literal(name));
            maid.setCustomNameVisible(alwaysShow);
            maid.setPersistenceRequired();
            if (!player.isCreative()) {
                player.getMainHandItem().shrink(1);
            }
        }
    }
}
