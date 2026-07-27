package cn.sh1rocu.touhoulittlemaid.api.extension;

import net.minecraft.resources.ResourceLocation;

public interface ILootContext {
    ResourceLocation tlm$getQueriedLootTableId();

    void tlm$setQueriedLootTableId(ResourceLocation queriedLootTableId);
}
