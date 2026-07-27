package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Deprecated(since = "1.1.13")
public class ItemMonsterList extends Item {
    public ItemMonsterList() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.monster_list.desc").withStyle(ChatFormatting.DARK_RED));
    }
}