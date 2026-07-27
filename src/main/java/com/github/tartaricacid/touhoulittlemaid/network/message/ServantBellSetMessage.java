package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemServantBell;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class ServantBellSetMessage {
    public static final ResourceLocation ID = getResourceLocation("servant_bell_set");

    public static FriendlyByteBuf encode(int id, String tip) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        buf.writeUtf(tip);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer sender, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        String tip = buf.readUtf();
        server.execute(() -> {
            if (sender.level.getEntity(id) instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                ItemServantBell.recordMaidInfo(sender.getMainHandItem(), maid.getUUID(), tip);
            }
        });
    }
}
