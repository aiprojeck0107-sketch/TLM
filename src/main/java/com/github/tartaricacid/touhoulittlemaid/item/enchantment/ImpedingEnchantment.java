package com.github.tartaricacid.touhoulittlemaid.item.enchantment;

import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ImpedingEnchantment extends Enchantment {
    public ImpedingEnchantment() {
        super(Rarity.UNCOMMON, /*InitEnchantments.GOHEI*/EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    /**
     * 消耗 10 18 26 34
     */
    @Override
    public int getMinCost(int level) {
        return 10 + (level - 1) * 8;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof ItemHakureiGohei;
    }
}
