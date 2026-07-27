package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.item.enchantment.EndersEnderEnchantment;
import com.github.tartaricacid.touhoulittlemaid.item.enchantment.ImpedingEnchantment;
import com.github.tartaricacid.touhoulittlemaid.item.enchantment.SpeedyEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class InitEnchantments {
    public static void init() {

    }

    public static final Enchantment IMPEDING = register("impeding", new ImpedingEnchantment());
    public static final Enchantment SPEEDY = register("speedy", new SpeedyEnchantment());
    public static final Enchantment ENDERS_ENDER = register("enders_ender", new EndersEnderEnchantment());

    // public static final EnchantmentCategory GOHEI = EnchantmentCategory.create("gohei", item -> item instanceof ItemHakureiGohei);

    private static Enchantment register(String name, Enchantment enchantment) {

        return Registry.register(BuiltInRegistries.ENCHANTMENT, new ResourceLocation(TouhouLittleMaid.MOD_ID, name), enchantment);
    }
}
