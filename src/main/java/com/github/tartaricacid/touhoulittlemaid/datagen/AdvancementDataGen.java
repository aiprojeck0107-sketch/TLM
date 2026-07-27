package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.datagen.advancement.BaseAdvancement;
import com.github.tartaricacid.touhoulittlemaid.datagen.advancement.ChallengeAdvancement;
import com.github.tartaricacid.touhoulittlemaid.datagen.advancement.FavorabilityAdvancement;
import com.github.tartaricacid.touhoulittlemaid.datagen.advancement.MaidBaseAdvancement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;

import java.util.function.Consumer;

public class AdvancementDataGen extends FabricAdvancementProvider {
    public AdvancementDataGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        BaseAdvancement.generate(consumer);
        MaidBaseAdvancement.generate(consumer);
        FavorabilityAdvancement.generate(consumer);
        ChallengeAdvancement.generate(consumer);
    }
}
