package com.github.tartaricacid.touhoulittlemaid.network.message;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

/**
 * 与扫帚骑乘有关的消息，从服务端到客户端
 */
public class OpenPlayerInventoryMessage {
    public static final ResourceLocation ID = getResourceLocation("open_player_inv");
    public static final int OPEN_PLAYER_INVENTORY = 0;

    public static FriendlyByteBuf encode(int action) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(action);
        return buf;
    }

    public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int action = buf.readInt();
        client.execute(() -> onHandle(action));
    }

    @Environment(EnvType.CLIENT)
    private static void onHandle(int action) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (action == OPEN_PLAYER_INVENTORY) {
            // 打开玩家背包
            Minecraft.getInstance().setScreen(new InventoryScreen(player));
        }
    }
}