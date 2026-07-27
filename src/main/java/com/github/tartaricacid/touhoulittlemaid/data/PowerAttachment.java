package com.github.tartaricacid.touhoulittlemaid.data;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@SuppressWarnings("UnstableApiUsage")
public class PowerAttachment {
    public static final float MAX_POWER = 5.0f;
    public static final Codec<PowerAttachment> CODEC = RecordCodecBuilder.create(ins -> ins.group(Codec.FLOAT.fieldOf("power").forGetter(o -> o.power))
            .apply(ins, PowerAttachment::new));
    public static final AttachmentType<PowerAttachment> TYPE = AttachmentRegistry.<PowerAttachment>builder()
            .initializer(() -> new PowerAttachment(0))
            .copyOnDeath()
            .persistent(CODEC)
            .buildAndRegister(new ResourceLocation(TouhouLittleMaid.MOD_ID, "power"));

    private float power;

    public PowerAttachment(float power) {
        this.power = power;
    }

    public void add(float points) {
        if (points + this.power <= MAX_POWER) {
            this.power += points;
        } else {
            this.power = MAX_POWER;
        }
    }

    public void min(float points) {
        if (points <= this.power) {
            this.power -= points;
        } else {
            this.power = 0.0f;
        }
    }

    public void set(float points) {
        this.power = Mth.clamp(points, 0, MAX_POWER);
    }

    public float get() {
        return this.power;
    }
}
