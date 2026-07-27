package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.event.MaidAreaRenderEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.SchedulePos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SyncMaidAreaMessage {
    public static final ResourceLocation ID = getResourceLocation("sync_maid_area");

    public static FriendlyByteBuf encode(int id, SchedulePos schedulePos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(id);
        buf.writeBlockPos(schedulePos.getWorkPos());
        buf.writeBlockPos(schedulePos.getIdlePos());
        buf.writeBlockPos(schedulePos.getSleepPos());
        buf.writeResourceLocation(schedulePos.getDimension());
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int maidId = buf.readVarInt();
        BlockPos workPos = buf.readBlockPos();
        BlockPos idlePos = buf.readBlockPos();
        BlockPos sleepPos = buf.readBlockPos();
        ResourceLocation dimension = buf.readResourceLocation();
        SchedulePos pos = new SchedulePos(workPos, idlePos, sleepPos, dimension);
        client.execute(() -> writePos(maidId, pos));
    }

    @Environment(EnvType.CLIENT)
    private static void writePos(int id, SchedulePos schedulePos) {
        MaidAreaRenderEvent.addSchedulePos(id, schedulePos);
    }

}
