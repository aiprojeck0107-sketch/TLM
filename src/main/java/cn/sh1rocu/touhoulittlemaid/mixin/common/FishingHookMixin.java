package cn.sh1rocu.touhoulittlemaid.mixin.common;


import cn.sh1rocu.touhoulittlemaid.util.forge.EventHooks;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingHook.class)
public class FishingHookMixin {
    @WrapWithCondition(method = "checkCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/FishingHook;onHit(Lnet/minecraft/world/phys/HitResult;)V"))
    private boolean tlm$onImpact(FishingHook projectile, HitResult result) {
        if (result.getType() == HitResult.Type.MISS)
            return true;
        return !EventHooks.onProjectileImpact(projectile, result);
    }
}