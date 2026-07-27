package cn.sh1rocu.touhoulittlemaid.mixin.common;

import cn.sh1rocu.touhoulittlemaid.api.event.PlayerLoggedInEvent;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void tlm$playerLoggedIn(Connection netManager, ServerPlayer player, CallbackInfo ci) {
        PlayerLoggedInEvent event = new PlayerLoggedInEvent(player);
        PlayerLoggedInEvent.CALLBACK.invoker().post(event);
    }
}