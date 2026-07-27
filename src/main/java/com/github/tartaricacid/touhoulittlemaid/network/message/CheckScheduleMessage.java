package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.other.CheckSchedulePosGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class CheckScheduleMessage {
    public static final ResourceLocation ID = getResourceLocation("check_schedule_pos");

    public static FriendlyByteBuf encode(String tips) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeUtf(tips);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        String tips = buf.readUtf();
        client.execute(() -> onHandle(tips));
    }

    @Environment(EnvType.CLIENT)
    private static void onHandle(String tips) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        if (mc.screen instanceof AbstractMaidContainerGui<?> parent) {
            mc.setScreen(new CheckSchedulePosGui(parent, Component.translatable(tips)));
        }
    }
}
