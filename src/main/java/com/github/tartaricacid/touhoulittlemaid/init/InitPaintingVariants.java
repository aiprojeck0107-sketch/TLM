package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class InitPaintingVariants {
    public static void init() {

    }

    public static final PaintingVariant WINE_FOX = register("wine_fox", new PaintingVariant(32, 48));

    private static PaintingVariant register(String name, PaintingVariant paintingVariant) {
        return Registry.register(BuiltInRegistries.PAINTING_VARIANT, new ResourceLocation(TouhouLittleMaid.MOD_ID, name), paintingVariant);
    }
}
