package com.github.tartaricacid.touhoulittlemaid.compat.carryon;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class BlackList {
    private static final String CARRY_ON_ID = "carryon";

    public static final Set<String> blockBlackList = Sets.newHashSet(
            BuiltInRegistries.BLOCK.keySet().stream().filter(id -> id.getNamespace().equals(TouhouLittleMaid.MOD_ID))
                    .map(ResourceLocation::toString).toList()
    );
    public static final Set<String> entityBlackList = Sets.newHashSet(
            TouhouLittleMaid.MOD_ID + ":tombstone", TouhouLittleMaid.MOD_ID + ":sit", TouhouLittleMaid.MOD_ID + ":broom"
    );

    public static void addBlackList() {
/*        BuiltInRegistries.BLOCK.keySet().stream().filter(id -> id.getNamespace().equals(TouhouLittleMaid.MOD_ID))
                .forEach(id -> InterModComms.sendTo(CARRY_ON_ID, "blacklistBlock", id::toString));
        InterModComms.sendTo(CARRY_ON_ID, "blacklistEntity", () -> TouhouLittleMaid.MOD_ID + ":tombstone");
        InterModComms.sendTo(CARRY_ON_ID, "blacklistEntity", () -> TouhouLittleMaid.MOD_ID + ":sit");
        InterModComms.sendTo(CARRY_ON_ID, "blacklistEntity", () -> TouhouLittleMaid.MOD_ID + ":broom");*/
    }
}
