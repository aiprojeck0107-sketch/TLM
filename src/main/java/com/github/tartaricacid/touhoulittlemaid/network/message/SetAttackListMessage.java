package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.data.inner.AttackListData;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTaskData;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SetAttackListMessage {
    public static final ResourceLocation ID = getResourceLocation("set_attack_list");

    public static FriendlyByteBuf encode(int entityId, Map<ResourceLocation, MonsterType> attackGroups) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        buf.writeVarInt(attackGroups.size());
        for (ResourceLocation id : attackGroups.keySet()) {
            buf.writeResourceLocation(id);
            buf.writeEnum(attackGroups.get(id));
        }
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer sender, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int entityId = buf.readInt();
        Map<ResourceLocation, MonsterType> attackGroupsOutput = Maps.newHashMap();
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            ResourceLocation id = buf.readResourceLocation();
            MonsterType type = buf.readEnum(MonsterType.class);
            attackGroupsOutput.put(id, type);
        }
        server.execute(() -> writeList(entityId, attackGroupsOutput, sender));
    }

    private static void writeList(int entityId, Map<ResourceLocation, MonsterType> attackGroups, Player sender) {
        Entity entity = sender.level.getEntity(entityId);
        if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
            maid.setAndSyncData(InitTaskData.ATTACK_LIST, new AttackListData(attackGroups));
        }
    }
}
