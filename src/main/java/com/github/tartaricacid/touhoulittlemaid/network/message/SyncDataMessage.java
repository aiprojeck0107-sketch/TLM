package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.data.MaidNumAttachment;
import com.github.tartaricacid.touhoulittlemaid.data.PowerAttachment;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SyncDataMessage {
    public static final ResourceLocation ID = getResourceLocation("sync_data");

    public static FriendlyByteBuf encode(float power, int maidNum) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeFloat(power);
        buf.writeVarInt(maidNum);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        float power = buf.readFloat();
        int maidNum = buf.readVarInt();
        client.execute(() -> handleData(power, maidNum));
    }

    @Environment(EnvType.CLIENT)
    private static void handleData(float power, final int maidNum) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }
        mc.player.setAttached(InitDataAttachment.POWER_NUM, new PowerAttachment(power));
        mc.player.setAttached(InitDataAttachment.MAID_NUM, new MaidNumAttachment(maidNum));
    }
}