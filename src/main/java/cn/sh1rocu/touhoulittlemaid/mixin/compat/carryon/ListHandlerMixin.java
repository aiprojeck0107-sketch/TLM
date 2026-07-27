package cn.sh1rocu.touhoulittlemaid.mixin.compat.carryon;

import com.github.tartaricacid.touhoulittlemaid.compat.carryon.BlackList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tschipp.carryon.common.config.ListHandler;

@Mixin(ListHandler.class)
public class ListHandlerMixin {
    @Inject(
            remap = false,
            method = "initConfigLists",
            at = @At("TAIL")
    )
    private static void tlm$addBlacklist(CallbackInfo ci) {
        BlackList.blockBlackList.forEach(ListHandler::addForbiddenTiles);
        BlackList.entityBlackList.forEach(ListHandler::addForbiddenEntities);
    }
}
