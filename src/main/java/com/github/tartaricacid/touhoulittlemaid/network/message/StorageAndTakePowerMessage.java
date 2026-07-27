package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.data.MaidNumAttachment;
import com.github.tartaricacid.touhoulittlemaid.data.PowerAttachment;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class StorageAndTakePowerMessage {
    public static final ResourceLocation ID = getResourceLocation("save_and_take_power");

    public static FriendlyByteBuf encode(BlockPos pos, float powerNum, boolean isStorage) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeFloat(powerNum);
        buf.writeBoolean(isStorage);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer sender, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        float powerNum = buf.readFloat();
        boolean isStorage = buf.readBoolean();
        server.execute(() -> {
            Level world = sender.level();
            if (world.isLoaded(pos)) {
                BlockEntity te = world.getBlockEntity(pos);
                if (te instanceof TileEntityMaidBeacon beacon) {
                    PowerAttachment power = sender.getAttachedOrCreate(InitDataAttachment.POWER_NUM);
                    MaidNumAttachment maidNum = sender.getAttachedOrCreate(InitDataAttachment.MAID_NUM);
                    if (isStorage) {
                        storageLogic(powerNum, power, beacon);
                    } else {
                        takeLogic(powerNum, power, beacon);
                    }

                    ServerPlayNetworking.send(sender, SyncDataMessage.ID, SyncDataMessage.encode(power.get(), maidNum.get()));
                }
            }
        });
    }

    private static void storageLogic(float powerNum, PowerAttachment playerPower, TileEntityMaidBeacon beacon) {
        boolean playerPowerIsEnough = powerNum <= playerPower.get();
        boolean beaconNotFull = powerNum + beacon.getStoragePower() <= beacon.getMaxStorage();
        if (playerPowerIsEnough) {
            if (beaconNotFull) {
                playerPower.min(powerNum);
                beacon.setStoragePower(beacon.getStoragePower() + powerNum);
            } else {
                playerPower.min(beacon.getMaxStorage() - beacon.getStoragePower());
                beacon.setStoragePower(beacon.getMaxStorage());
            }
        }
    }

    private static void takeLogic(float powerNum, PowerAttachment playerPower, TileEntityMaidBeacon beacon) {
        boolean beaconIsEnough = powerNum <= beacon.getStoragePower();
        boolean playerNotFull = powerNum + playerPower.get() < PowerAttachment.MAX_POWER;
        if (beaconIsEnough) {
            if (playerNotFull) {
                beacon.setStoragePower(beacon.getStoragePower() - powerNum);
                playerPower.add(powerNum);
            } else {
                beacon.setStoragePower(beacon.getStoragePower() - PowerAttachment.MAX_POWER + playerPower.get());
                playerPower.set(PowerAttachment.MAX_POWER);
            }
        }
    }

}
