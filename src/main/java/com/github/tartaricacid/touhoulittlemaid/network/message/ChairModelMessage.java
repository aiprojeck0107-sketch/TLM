package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.ChairConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
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

public class ChairModelMessage {
    public static final ResourceLocation ID = getResourceLocation("chair_model");

    public static FriendlyByteBuf encode(int id, ResourceLocation modelId, float mountedHeight, boolean tameableCanRide, boolean noGravity) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        buf.writeResourceLocation(modelId);
        buf.writeFloat(mountedHeight);
        buf.writeBoolean(tameableCanRide);
        buf.writeBoolean(noGravity);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        ResourceLocation modelId = buf.readResourceLocation();
        float mountedHeight = buf.readFloat();
        boolean tameableCanRide = buf.readBoolean();
        boolean noGravity = buf.readBoolean();
        server.execute(() -> {
            Entity entity = player.level.getEntity(id);
            boolean canChangeModel = ChairConfig.CHAIR_CHANGE_MODEL.get() || player.isCreative();

            if (entity instanceof EntityChair) {
                if (canChangeModel) {
                    EntityChair chair = (EntityChair) entity;
                    chair.setModelId(modelId.toString());
                    chair.setMountedHeight(mountedHeight);
                    chair.setTameableCanRide(tameableCanRide);
                    chair.setNoGravity(noGravity);
                    if (!tameableCanRide && !chair.getPassengers().isEmpty()) {
                        chair.ejectPassengers();
                    }
                    InitTrigger.MAID_EVENT.trigger(player, TriggerType.CHANGE_CHAIR_MODEL);
                } else {
                    if (player.isAlive()) {
                        player.sendSystemMessage(Component.translatable("message.touhou_little_maid.change_model.disabled"));
                    }
                }
            }
        });
    }
}
