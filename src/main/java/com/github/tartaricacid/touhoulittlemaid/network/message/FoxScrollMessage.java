package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.FoxScrollScreen;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class FoxScrollMessage {
    public static final ResourceLocation ID = getResourceLocation("fox_scroll");

    public static FriendlyByteBuf encode(Map<String, List<FoxScrollData>> data) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(data.size());
        data.forEach((dim, scrollData) -> {
            buf.writeVarInt(scrollData.size());
            buf.writeUtf(dim);
            scrollData.forEach(foxScrollData -> FoxScrollData.encode(foxScrollData, buf));
        });
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        Map<String, List<FoxScrollData>> data = Maps.newHashMap();
        int dimLength = buf.readVarInt();
        for (int i = 0; i < dimLength; i++) {
            List<FoxScrollData> scrollData = Lists.newArrayList();
            int dataLength = buf.readVarInt();
            String dim = buf.readUtf();
            for (int j = 0; j < dataLength; j++) {
                scrollData.add(FoxScrollData.decode(buf));
            }
            data.put(dim, scrollData);
        }
        client.execute(() -> onHandle(data));
    }

    @Environment(EnvType.CLIENT)
    private static void onHandle(Map<String, List<FoxScrollData>> data) {
        Minecraft.getInstance().setScreen(new FoxScrollScreen(data));
    }

    public static class FoxScrollData {
        private final BlockPos pos;
        private final Component name;
        private final long timestamp;

        public FoxScrollData(BlockPos pos, Component name, long timestamp) {
            this.pos = pos;
            this.name = name;
            this.timestamp = timestamp;
        }

        public static void encode(FoxScrollData data, FriendlyByteBuf buf) {
            buf.writeBlockPos(data.pos);
            buf.writeComponent(data.name);
            buf.writeLong(data.timestamp);
        }

        public static FoxScrollData decode(FriendlyByteBuf buf) {
            return new FoxScrollData(buf.readBlockPos(), buf.readComponent(), buf.readLong());
        }

        public BlockPos getPos() {
            return pos;
        }

        public Component getName() {
            return name;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
