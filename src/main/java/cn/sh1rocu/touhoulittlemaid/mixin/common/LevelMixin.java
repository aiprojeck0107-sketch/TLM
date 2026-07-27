package cn.sh1rocu.touhoulittlemaid.mixin.common;

import cn.sh1rocu.touhoulittlemaid.api.event.ExplosionEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Level.class)
public class LevelMixin {
    @Inject(
            method = "explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;Z)Lnet/minecraft/world/level/Explosion;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Explosion;explode()V",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    public void tlm$onStartExplosion(Entity entity, DamageSource damageSource, ExplosionDamageCalculator explosionDamageCalculator, double x, double y, double z, float radius, boolean causesFire, Level.ExplosionInteraction explosionInteraction, boolean spawnParticles, CallbackInfoReturnable<Explosion> cir, Explosion.BlockInteraction blockInteraction, Explosion explosion) {
        if (ExplosionEvents.START.invoker().onExplosionStart((Level) (Object) this, explosion))
            cir.setReturnValue(explosion);
    }
}
