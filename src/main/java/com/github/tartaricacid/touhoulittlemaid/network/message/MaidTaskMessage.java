package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTaskEnableEvent;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.TabIndex;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.getResourceLocation;

public class MaidTaskMessage {
    public static final ResourceLocation ID = getResourceLocation("maid_task");

    public static FriendlyByteBuf encode(int id, ResourceLocation uid) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        buf.writeResourceLocation(uid);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        ResourceLocation uid = buf.readResourceLocation();
        server.execute(() -> {
            Entity entity = player.level.getEntity(id);
            if (entity instanceof EntityMaid maid && maid.isOwnedBy(player)) {
                IMaidTask task = TaskManager.findTask(uid).orElse(TaskManager.getIdleTask());
                MaidTaskEnableEvent event = new MaidTaskEnableEvent(task, maid);
                MaidTaskEnableEvent.CALLBACK.invoker().onMaidTaskEnable(event);
                if (task != TaskManager.getIdleTask() && event.isCanceled()) {
                    return;
                }
                if (!task.isEnable(maid)) {
                    return;
                }
                maid.setTask(task);
                if (!TaskManager.getIdleTask().equals(task) && maid.getOwner() instanceof ServerPlayer serverPlayer) {
                    InitTrigger.MAID_EVENT.trigger(serverPlayer, TriggerType.SWITCH_TASK);
                }
                // 如果此时玩家打开的是配置界面
                if (player.containerMenu instanceof TaskConfigContainer) {
                    maid.openMaidGui(player, TabIndex.TASK_CONFIG);
                }
            }
        });
    }
}
