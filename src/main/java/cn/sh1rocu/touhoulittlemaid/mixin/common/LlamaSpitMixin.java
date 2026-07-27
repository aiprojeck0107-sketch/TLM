package cn.sh1rocu.touhoulittlemaid.mixin.common;

import cn.sh1rocu.touhoulittlemaid.util.forge.EventHooks;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LlamaSpit.class)
public class LlamaSpitMixin {
    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/LlamaSpit;onHit(Lnet/minecraft/world/phys/HitResult;)V"))
    private boolean tlm$onProjectileImpact(LlamaSpit projectile, HitResult result) {
        if (result.getType() == HitResult.Type.MISS)
            return true;
        return !EventHooks.onProjectileImpact(projectile, result);
    }
}