package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAndItemTransformEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SpawnParticleMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemFilm extends AbstractStoreMaidItem {
    private static final String ID_TAG = "id";

    public ItemFilm() {
        super((new Properties()).stacksTo(1));
    }

    public static ItemStack maidToFilm(EntityMaid maid) {
        ItemStack film = InitItems.FILM.getDefaultInstance();
        CompoundTag filmTag = new CompoundTag();
        CompoundTag maidTag = new CompoundTag();
        maid.setHomeModeEnable(false);
        maid.saveWithoutId(maidTag);
        removeMaidSomeData(maidTag);
        maidTag.putString(ID_TAG, Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(InitEntities.MAID)).toString());

        var event = new MaidAndItemTransformEvent.ToItem(maid, film, maidTag);
        MaidAndItemTransformEvent.TO_ITEM.invoker().onToItem(event);

        filmTag.put(MAID_INFO, maidTag);
        film.setTag(filmTag);
        return film;
    }

    public static void filmToMaid(ItemStack film, Level worldIn, BlockPos pos, Player player) {
        CompoundTag data = getMaidData(film);
        ResourceLocation entityId = new ResourceLocation(data.getString(ID_TAG));
        ResourceLocation maidId = BuiltInRegistries.ENTITY_TYPE.getKey(InitEntities.MAID);

        if (entityId.equals(maidId)) {
            EntityMaid maid = new EntityMaid(worldIn);

            var event = new MaidAndItemTransformEvent.ToMaid(maid, film, data);
            MaidAndItemTransformEvent.TO_MAID.invoker().onToMaid(event);

            maid.readAdditionalSaveData(data);
            maid.setPos(pos.getX(), pos.getY(), pos.getZ());
            // 实体生成必须在服务端应用
            if (!worldIn.isClientSide) {
                worldIn.addFreshEntity(maid);
                NetworkHandler.sendToNearby(maid, SpawnParticleMessage.ID, SpawnParticleMessage.encode(maid.getId(), SpawnParticleMessage.Type.EXPLOSION));
                worldIn.playSound(null, pos, InitSounds.ALTAR_CRAFT, SoundSource.VOICE, 1.0f, 1.0f);
            }
            film.shrink(1);
            return;
        }

        if (!worldIn.isClientSide) {
            player.sendSystemMessage(Component.translatable("tooltips.touhou_little_maid.film.no_data.desc"));
        }
    }

    private static void removeMaidSomeData(CompoundTag nbt) {
        nbt.remove(EntityMaid.MAID_BACKPACK_TYPE);
        nbt.remove(EntityMaid.MAID_INVENTORY_TAG);
        nbt.remove(EntityMaid.MAID_BAUBLE_INVENTORY_TAG);
        nbt.remove(EntityMaid.EXPERIENCE_TAG);
        nbt.remove("ArmorItems");
        nbt.remove("HandItems");
        nbt.remove("Leash");
        nbt.remove("Health");
        nbt.remove("HurtTime");
        nbt.remove("DeathTime");
        nbt.remove("HurtByTimestamp");
        nbt.remove("Pos");
        nbt.remove("Motion");
        nbt.remove("FallDistance");
        nbt.remove("Fire");
        nbt.remove("Air");
        nbt.remove("TicksFrozen");
        nbt.remove("HasVisualFire");
        nbt.remove("Passengers");
        nbt.remove("ActiveEffects");
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (!hasMaidData(stack)) {
            tooltip.add(Component.translatable("tooltips.touhou_little_maid.film.no_data.desc").withStyle(ChatFormatting.DARK_RED));
        }
    }
}
