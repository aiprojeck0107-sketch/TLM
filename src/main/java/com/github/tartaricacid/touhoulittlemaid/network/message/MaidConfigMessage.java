package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.SchedulePos;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class MaidConfigMessage {
    public static final ResourceLocation ID = getResourceLocation("maid_config");

    public static FriendlyByteBuf encode(int id, boolean home, boolean pick, boolean ride, MaidSchedule schedule) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        buf.writeBoolean(home);
        buf.writeBoolean(pick);
        buf.writeBoolean(ride);
        buf.writeEnum(schedule);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        boolean home = buf.readBoolean();
        boolean pick = buf.readBoolean();
        boolean ride = buf.readBoolean();
        MaidSchedule schedule = buf.readEnum(MaidSchedule.class);
        server.execute(() -> {
            Entity entity = player.level.getEntity(id);
            if (entity instanceof EntityMaid maid && maid.isOwnedBy(player)) {
                if (maid.isHomeModeEnable() != home) {
                    handleHome(id, home, pick, ride, schedule, player, maid);
                }
                if (maid.isPickup() != pick) {
                    maid.setPickup(pick);
                }
                if (maid.isRideable() != ride) {
                    maid.setRideable(ride);
                    Entity vehicle = maid.getVehicle();
                    if (!ride && vehicle != null && !isStopRideBlocklist(vehicle)) {
                        maid.stopRiding();
                    }
                }
                if (maid.getSchedule() != schedule) {
                    maid.setSchedule(schedule);
                    maid.getSchedulePos().restrictTo(maid);
                    if (maid.isHomeModeEnable()) {
                        BehaviorUtils.setWalkAndLookTargetMemories(maid, maid.getRestrictCenter(), 0.7f, 3);
                    }
                    if (maid.getOwner() instanceof ServerPlayer serverPlayer) {
                        InitTrigger.MAID_EVENT.trigger(serverPlayer, TriggerType.SWITCH_SCHEDULE);
                    }
                }
            }
        });
    }

    private static boolean isStopRideBlocklist(Entity vehicle) {
        // 娱乐方块骑乘不受影响
        boolean isSit = vehicle instanceof EntitySit;
        // 飞行中的扫帚不能脱离，有风险
        boolean isBroom = vehicle instanceof EntityBroom broom && !broom.onGround();
        return isSit || isBroom;
    }

    private static void handleHome(int id, boolean home, boolean pick, boolean ride, MaidSchedule schedule, ServerPlayer player, EntityMaid maid) {
        if (home) {
            SchedulePos schedulePos = maid.getSchedulePos();
            if (schedulePos.isConfigured()) {
                ResourceLocation dimension = schedulePos.getDimension();
                if (!dimension.equals(maid.level.dimension().location())) {
                    FriendlyByteBuf buf = CheckScheduleMessage.encode("message.touhou_little_maid.check_schedule_pos.dimension");
                    ServerPlayNetworking.send(player, CheckScheduleMessage.ID, buf);
                    return;
                }
                BlockPos nearestPos = schedulePos.getNearestPos(maid);
                if (nearestPos != null && nearestPos.distSqr(maid.blockPosition()) > 32 * 32) {
                    FriendlyByteBuf buf = CheckScheduleMessage.encode("message.touhou_little_maid.check_schedule_pos.too_far");
                    ServerPlayNetworking.send(player, CheckScheduleMessage.ID, buf);
                    return;
                }
            }
            schedulePos.setHomeModeEnable(maid, maid.blockPosition());
        } else {
            maid.restrictTo(BlockPos.ZERO, MaidConfig.MAID_NON_HOME_RANGE.get());
        }
        maid.setHomeModeEnable(home);
    }

}