package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class MaidModelMessage {
    public static final ResourceLocation ID = getResourceLocation("maid_model");

    public static FriendlyByteBuf encode(int id, ResourceLocation modelId) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        buf.writeResourceLocation(modelId);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        ResourceLocation modelId = buf.readResourceLocation();
        server.execute(() -> {
            Entity entity = player.level.getEntity(id);
            if (entity instanceof EntityMaid maid && maid.isOwnedBy(player)) {
                if (player.isCreative() || MaidConfig.MAID_CHANGE_MODEL.get()) {
                    maid.setIsYsmModel(false);
                    maid.setModelId(modelId.toString());
                    InitTrigger.MAID_EVENT.trigger(player, TriggerType.CHANGE_MAID_MODEL);
                } else {
                    player.sendSystemMessage(Component.translatable("message.touhou_little_maid.change_model.disabled"));
                }
            }
        });
    }
}
