package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

/**
 * 用于同步客户端播放动画的消息
 * 目前只包含拾取雪球的动画
 */
public class MaidAnimationMessage {
    public static final ResourceLocation ID = getResourceLocation("maid_animation");

    public static final int NONE = 0;
    public static final int PICK_UP_SNOWBALL = 1;
    public static final int SWF_AIM = 2;
    public static final int SWF_RELOAD = 3;
    public static final int SWF_FIRE = 4;

    public static FriendlyByteBuf pickUpSnowball(EntityMaid maid) {
        // 播放丢雪球动画之前，先禁止女仆移动
        // 标记服务端事件
        maid.animationId = PICK_UP_SNOWBALL;
        maid.animationRecordTime = System.currentTimeMillis();
        // 返回消息
        return encode(maid.getId(), PICK_UP_SNOWBALL);
    }

    public static FriendlyByteBuf encode(int maidId, int animationId) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(maidId);
        buf.writeInt(animationId);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int maidId = buf.readInt();
        int animationId = buf.readInt();
        client.execute(() -> handle(maidId, animationId));
    }

    @Environment(EnvType.CLIENT)
    private static void handle(int maidId, int animationId) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        if (level.getEntity(maidId) instanceof EntityMaid maid) {
            maid.animationId = animationId;
            maid.animationRecordTime = System.currentTimeMillis();
            maid.shouldReset = true;
        }
    }
}