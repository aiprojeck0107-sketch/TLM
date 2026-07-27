package com.github.tartaricacid.touhoulittlemaid.client.event;

import cn.sh1rocu.touhoulittlemaid.api.event.PlaySoundSourceEvent;
import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidSoundInstance;
import com.mojang.blaze3d.audio.SoundBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PlayMaidSoundEvent {
    public static void onPlaySoundSource(PlaySoundSourceEvent event) {
        if (event.getSound() instanceof MaidSoundInstance instance) {
            SoundBuffer soundBuffer = instance.getSoundBuffer();
            if (soundBuffer != null) {
                event.getChannel().attachStaticBuffer(soundBuffer);
                event.getChannel().play();
            }
        }
    }
}
