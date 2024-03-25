package com.lubiekakao1212.opencu.forge.block;

import com.lubiekakao1212.opencu.forge.block.entity.BlockEntityRepulsorImpl;
import com.lubiekakao1212.opencu.init.CUBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockRepulsor extends Block implements BlockEntityProvider {

    public BlockRepulsor(Settings properties) {
        super(properties);
    }

    @Override
    public BlockRenderType getRenderType(BlockState pState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityRepulsorImpl(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pBlockEntityType == CUBlockEntities.REPULSOR.get() ? BlockEntityRepulsorImpl::tick : null;
    }

    /*//TODO For Debug purposes only
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pPlayer.isCrouching()) {
            if(!pLevel.isClientSide) {
                BlockEntityRepulsor ber = (BlockEntityRepulsor) pLevel.getBlockEntity(pPos);
            }
            return Intera;
        }
        return InteractionResult.PASS;
    }*/
}