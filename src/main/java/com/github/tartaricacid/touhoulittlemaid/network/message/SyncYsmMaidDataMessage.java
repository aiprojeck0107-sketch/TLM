package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.compat.ysm.event.UpdateRemoteStructEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ByteBufUtils;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SyncYsmMaidDataMessage {
    public static final ResourceLocation ID = getResourceLocation("sync_ysm_maid_data");

    public static FriendlyByteBuf encode(int entityId, String rouletteAnim, boolean isRouletteAnimPlaying, Object2FloatOpenHashMap<String> roamingVars) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        buf.writeUtf(rouletteAnim);
        buf.writeBoolean(isRouletteAnimPlaying);
        ByteBufUtils.writeObject2FloatOpenHashMap(roamingVars, buf);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int entityId = buf.readInt();
        String rouletteAnim = buf.readUtf();
        boolean isRouletteAnimPlaying = buf.readBoolean();
        Object2FloatOpenHashMap<String> roamingVars = ByteBufUtils.readObject2FloatOpenHashMap(buf);
        client.execute(() -> onHandle(entityId, rouletteAnim, isRouletteAnimPlaying, roamingVars));
    }

    @Environment(EnvType.CLIENT)
    private static void onHandle(int entityId, String rouletteAnim, boolean isRouletteAnimPlaying, Object2FloatOpenHashMap<String> roamingVars) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        Entity entity = level.getEntity(entityId);
        if (!(entity instanceof EntityMaid maid)) {
            return;
        }
        UpdateRemoteStructEvent.EVENT.invoker().post(new UpdateRemoteStructEvent(maid, roamingVars));
        if (isRouletteAnimPlaying) {
            maid.playRouletteAnim(rouletteAnim);
        } else {
            maid.stopRouletteAnim();
        }
    }
}
