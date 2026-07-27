package cn.sh1rocu.touhoulittlemaid.util.enchant;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentUtil {
    public static boolean canEnchant(ItemStack stack, Enchantment enchantment) {
        return enchantment != null && enchantment.canEnchant(stack);
    }
}
