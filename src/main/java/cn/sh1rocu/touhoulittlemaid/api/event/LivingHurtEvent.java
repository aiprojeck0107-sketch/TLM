package cn.sh1rocu.touhoulittlemaid.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static cn.sh1rocu.touhoulittlemaid.TouhouLittleMaidFabric.*;

public class LivingHurtEvent extends CancellableEvent {
    private final LivingEntity livingEntity;
    private final DamageSource source;
    private float amount;
    public static final Event<Callback> CALLBACK = EventFactory.createWithPhases(Callback.class, callbacks -> event -> {
        for (Callback e : callbacks)
            e.onLivingHurt(event);
    }, HIGHEST, HIGH, Event.DEFAULT_PHASE, LOW, LOWEST);

    public LivingHurtEvent(LivingEntity entity, DamageSource source, float amount) {
        this.livingEntity = entity;
        this.source = source;
        this.amount = amount;
    }

    public LivingEntity getEntity() {
        return livingEntity;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public interface Callback {
        void onLivingHurt(LivingHurtEvent event);
    }
}