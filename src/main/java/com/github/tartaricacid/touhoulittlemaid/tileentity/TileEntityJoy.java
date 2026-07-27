package com.github.tartaricacid.touhoulittlemaid.tileentity;

import cn.sh1rocu.touhoulittlemaid.api.extension.IBlockEntityPersistentData;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class TileEntityJoy extends BlockEntity implements IBlockEntityPersistentData {
    private static final String SIT_ID = "SitId";
    private UUID sitId = Util.NIL_UUID;

    public TileEntityJoy(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        tlm$getPersistentData().putUUID(SIT_ID, this.sitId);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.sitId = tlm$getPersistentData().getUUID(SIT_ID);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void refresh() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
        }
    }

    public BlockPos getWorldPosition() {
        return this.worldPosition;
    }

//    @Override
//    @Environment(EnvType.CLIENT)
//    public AABB getRenderBoundingBox() {
//        return new AABB(worldPosition.offset(-2, 0, -2), worldPosition.offset(2, 1, 2));
//    }

    public UUID getSitId() {
        return this.sitId;
    }

    public void setSitId(UUID sitId) {
        this.sitId = sitId;
    }
}
