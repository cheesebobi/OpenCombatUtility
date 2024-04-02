package com.LubieKakao1212.opencu.fabric.block;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityRepulsor;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityRepulsorImpl;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockRepulsor extends Block implements BlockEntityProvider {

    public BlockRepulsor(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityRepulsorImpl(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == CUBlockEntities.repulsor() ? BlockEntityRepulsor::tick : null;
    }
}
