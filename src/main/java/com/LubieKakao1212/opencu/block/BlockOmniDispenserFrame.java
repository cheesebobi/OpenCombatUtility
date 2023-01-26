package com.LubieKakao1212.opencu.block;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Function;

public class BlockOmniDispenserFrame extends Block implements EntityBlock {

    public BlockOmniDispenserFrame(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide){
            //OCUGuis.openGUI("dispenser", player, level, pos.getX(), pos.getY(), pos.getZ());

            BlockEntityOmniDispenser dis = (BlockEntityOmniDispenser) level.getBlockEntity(pos);

            NetworkHooks.openGui((ServerPlayer) player, dis, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity be = level.getBlockEntity(pos);

        LazyOptional<IItemHandler> inventory = be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        inventory.ifPresent((inv) -> {
            for(int i=0; i<inv.getSlots(); i++) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), inv.extractItem(i, 999, false));
            }
        });

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityOmniDispenser(pos, state);
    }
}
