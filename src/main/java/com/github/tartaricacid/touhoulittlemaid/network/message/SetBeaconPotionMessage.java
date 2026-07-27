package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class SetBeaconPotionMessage {
    public static final ResourceLocation ID = getResourceLocation("set_beacon_potion");

    public static FriendlyByteBuf encode(BlockPos pos, int potionIndex) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeInt(potionIndex);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer sender, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        int potionIndex = buf.readInt();
        server.execute(() -> {
            Level world = sender.level();
            if (world.isLoaded(pos)) {
                BlockEntity te = world.getBlockEntity(pos);
                if (te instanceof TileEntityMaidBeacon) {
                    ((TileEntityMaidBeacon) te).setPotionIndex(potionIndex);
                }
            }
        });
    }
}
