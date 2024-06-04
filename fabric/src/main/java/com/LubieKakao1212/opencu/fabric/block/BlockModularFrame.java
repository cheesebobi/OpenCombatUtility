package com.LubieKakao1212.opencu.fabric.block;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlockModularFrame extends BlockWithEntity {

    public BlockModularFrame(AbstractBlock.Settings properties) {
        super(properties);
    }

    @Override
    public BlockRenderType getRenderType(BlockState pState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            //region debud
            if(player.isSneaking()) {
                var be = (BlockEntityModularFrameImpl)world.getBlockEntity(pos);
                player.sendMessage(Text.of(be.exposedEnegyStorage.getAmount() + "/" + OpenCUConfigCommon.capacitor().energyCapacity()));
            }
            //endregion

            NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);

            if(factory != null) {
                player.openHandledScreen(factory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(!state.isOf(newState.getBlock())) {
            var blockEntity = (BlockEntityModularFrameImpl) CUBlockEntities.modularFrame().get(world, pos);
            if(blockEntity != null) {
                blockEntity.scatterInventory();
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityModularFrameImpl(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return BlockEntityModularFrameImpl::tick;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if(world.isClient) {
            OpenCUModCommon.LOGGER.info("client");
        } else {
            OpenCUModCommon.LOGGER.info("server");
        }

        var frame = CUBlockEntities.modularFrame().get(world, pos);

        if(frame == null) {
            OpenCUModCommon.LOGGER.warn("wrong BlockEntity at: " + pos);
        }

        var delta = sourcePos.subtract(pos);
        var dir = Direction.fromVector(delta);

        var power = world.isEmittingRedstonePower(sourcePos, dir);

        frame.pulseActivate(dir, power);
    }
}
