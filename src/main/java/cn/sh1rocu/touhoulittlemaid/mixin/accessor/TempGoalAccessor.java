package cn.sh1rocu.touhoulittlemaid.mixin.accessor;

import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TemptGoal.class)
public interface TempGoalAccessor {
    @Accessor("items")
    Ingredient tlm$getItems();
}
