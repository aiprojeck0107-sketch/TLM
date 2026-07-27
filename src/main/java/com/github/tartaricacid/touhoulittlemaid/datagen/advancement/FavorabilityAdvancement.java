package com.github.tartaricacid.touhoulittlemaid.datagen.advancement;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.MaidEventTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;


public class FavorabilityAdvancement {
    public static void generate(Consumer<Advancement> saver) {
        Advancement root = make(InitItems.BOOKSHELF, "maid_sit_joy")
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_SIT_JOY))
                .rewards(AdvancementRewards.Builder.experience(50))
                .save(saver, id("favorability/maid_sit_joy").toString());

        generateFavorability(saver, root);

        generateJoy(saver, root);
    }

    private static void generateJoy(Consumer<Advancement> saver, Advancement root) {
        Advancement joy = make(InitItems.PICNIC_BASKET, "maid_picnic_eat").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_PICNIC_EAT))
                .save(saver, id("favorability/maid_picnic_eat").toString());

        Advancement gomoku = makeGoal(InitItems.GOMOKU, "win_gomoku").parent(joy)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.WIN_GOMOKU))
                .save(saver, id("favorability/win_gomoku").toString());

        Advancement cchess = makeGoal(InitItems.CCHESS, "win_cchess").parent(gomoku)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.WIN_CCHESS))
                .save(saver, id("favorability/win_cchess").toString());

        makeGoal(InitItems.WCHESS, "win_wchess").parent(cchess)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.WIN_WCHESS))
                .save(saver, id("favorability/win_wchess").toString());

        make(InitItems.MAID_BED, "maid_sleep").parent(joy)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.MAID_SLEEP))
                .save(saver, id("favorability/maid_sleep").toString());
    }

    private static void generateFavorability(Consumer<Advancement> saver, Advancement root) {
        Advancement increased = make(InitItems.FAVORABILITY_TOOL_ADD, "favorability_increased").parent(root)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.FAVORABILITY_INCREASED))
                .save(saver, id("favorability/favorability_increased").toString());

        makeGoal(InitItems.FAVORABILITY_TOOL_FULL, "favorability_increased_max").parent(increased)
                .addCriterion("maid_event", MaidEventTrigger.create(TriggerType.FAVORABILITY_INCREASED_MAX))
                .save(saver, id("favorability/favorability_increased_max").toString());
    }

    private static Advancement.Builder make(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.favorability.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.favorability.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.TASK, true, true, false);
    }

    private static Advancement.Builder makeGoal(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.touhou_little_maid.favorability.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.touhou_little_maid.favorability.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/advancements/backgrounds/stone.png"),
                FrameType.GOAL, true, true, false);
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, id);
    }
}
