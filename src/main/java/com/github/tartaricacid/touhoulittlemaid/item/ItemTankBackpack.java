package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemTankBackpack extends ItemMaidBackpack {
    public static ItemStack getTankBackpack(TankBackpackData data) {
        ItemStack backpack = InitItems.TANK_BACKPACK.getDefaultInstance();
        CompoundTag tags = backpack.getOrCreateTagElement("Tanks");
        data.getTank().writeNbt(tags);
        return backpack;
    }

    public static void setTankBackpack(EntityMaid maid, TankBackpackData data, ItemStack backpack) {
        CompoundTag tags = backpack.getTagElement("Tanks");
        if (tags != null) {
            data.loadTank(tags, maid);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag nbt = stack.getTagElement("Tanks");
        if (nbt != null) {
            MutableComponent fluidInfo;
            FluidVariant fluidStack = FluidVariant.fromNbt(nbt.getCompound("variant"));
            if (fluidStack.getFluid() == Fluids.EMPTY || nbt.getLong("amount") == 0) {
                fluidInfo = Component.translatable("tooltips.touhou_little_maid.tank_backpack.empty_fluid").withStyle(ChatFormatting.GRAY);
            } else {
                fluidInfo = Component.translatable("tooltips.touhou_little_maid.tank_backpack.fluid",
                        FluidVariantAttributes.getName(fluidStack),
                        nbt.getLong("amount") / 81).withStyle(ChatFormatting.GRAY);
            }
            tooltip.add(fluidInfo);
        }
    }
}
