package com.github.tartaricacid.touhoulittlemaid.event;


import cn.sh1rocu.touhoulittlemaid.api.event.PlayerLoggedInEvent;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.minecraft.server.level.ServerPlayer;

public final class EnterServerEvent {
    public static void onAttachCapabilityEvent(PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            InitTrigger.GIVE_SMART_SLAB_CONFIG.trigger(serverPlayer);
            InitTrigger.GIVE_PATCHOULI_BOOK_CONFIG.trigger(serverPlayer);
        }
    }
}
