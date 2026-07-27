package com.github.tartaricacid.touhoulittlemaid.item.enchantment;

import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SpeedyEnchantment extends Enchantment {
    public SpeedyEnchantment() {
        super(Rarity.RARE, /*InitEnchantments.GOHEI*/EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    /**
     * 消耗 25 35
     */
    @Override
    public int getMinCost(int level) {
        return 15 + level * 10;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof ItemHakureiGohei;
    }
}
