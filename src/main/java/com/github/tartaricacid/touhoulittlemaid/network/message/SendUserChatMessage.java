package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.ChatClientInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SendUserChatMessage {
    public static final ResourceLocation ID = getResourceLocation("send_user_chat");

    public static FriendlyByteBuf encode(int maidId, String message, ChatClientInfo clientInfo) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(maidId);
        buf.writeUtf(message);
        clientInfo.encode(buf);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int maidId = buf.readVarInt();
        String message = buf.readUtf();
        ChatClientInfo clientInfo = ChatClientInfo.decode(buf);
        server.execute(() -> onHandle(maidId, message, clientInfo, player));
    }

    private static void onHandle(int maidId, String message, ChatClientInfo clientInfo, ServerPlayer sender) {
        Entity entity = sender.level.getEntity(maidId);
        if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender) && maid.isAlive()) {
            maid.getAiChatManager().chat(message, clientInfo, sender);
        }
    }
}
