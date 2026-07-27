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

public class YsmMaidModelMessage {
    public static final ResourceLocation ID = getResourceLocation("ysm_maid_model");

    public static FriendlyByteBuf encode(int maidId, String modeId, String texture, Component name) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(maidId);
        buf.writeUtf(modeId);
        buf.writeUtf(texture);
        buf.writeComponent(name);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer sender, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int maidId = buf.readInt();
        String modeId = buf.readUtf();
        String texture = buf.readUtf();
        Component name = buf.readComponent();
        server.execute(() -> {
            Entity entity = sender.level.getEntity(maidId);
            if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                if (sender.isCreative() || MaidConfig.MAID_CHANGE_MODEL.get()) {
                    maid.setIsYsmModel(true);
                    maid.setYsmModel(modeId, texture, name);
                    InitTrigger.MAID_EVENT.trigger(sender, TriggerType.CHANGE_MAID_MODEL);
                } else {
                    sender.sendSystemMessage(Component.translatable("message.touhou_little_maid.change_model.disabled"));
                }
            }
        });
    }
}
