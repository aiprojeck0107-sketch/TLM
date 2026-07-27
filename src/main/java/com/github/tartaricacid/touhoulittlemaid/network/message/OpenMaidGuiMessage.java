package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.TabIndex;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class OpenMaidGuiMessage {
    public static final ResourceLocation ID = getResourceLocation("open_maid_gui");

    public static FriendlyByteBuf encode(int entityId) {
        return encode(entityId, TabIndex.MAIN);
    }

    public static FriendlyByteBuf encode(int entityId, int tabId) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(entityId);
        buf.writeVarInt(tabId);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int entityId = buf.readVarInt();
        int tabId = buf.readVarInt();
        server.execute(() -> handle(entityId, tabId, player));
    }

    private static void handle(int entityId, int tabId, @Nullable ServerPlayer player) {
        if (player == null) {
            return;
        }
        Entity entity = player.level.getEntity(entityId);
        if (entity instanceof EntityMaid maid && stillValid(player, maid)) {
            maid.openMaidGui(player, tabId);
        }
    }


    private static boolean stillValid(Player playerIn, EntityMaid maid) {
        return maid.isOwnedBy(playerIn) && !maid.isSleeping() && maid.isAlive() && maid.distanceTo(playerIn) < 5.0F;
    }
}