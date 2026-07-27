package cn.sh1rocu.touhoulittlemaid.util.itemhandler;

import net.minecraft.nbt.Tag;

public interface INBTSerializable<T extends Tag> {
    T serializeNBT();

    void deserializeNBT(T var1);
}
