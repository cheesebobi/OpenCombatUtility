package com.LubieKakao1212.opencu.block;

import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class BlockOmniDispenserFrame extends Block implements BlockEntityProvider {

    public BlockOmniDispenserFrame(AbstractBlock.Settings properties) {
        super(properties);
    }

    @Override
    public BlockRenderType getRenderType(BlockState pState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!level.isClient){
            //OCUGuis.openGUI("dispenser", player, level, pos.getX(), pos.getY(), pos.getZ());

            BlockEntityOmniDispenser dis = (BlockEntityOmniDispenser) level.getBlockEntity(pos);

            NetworkHooks.openScreen((ServerPlayerEntity) player, dis, pos);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void afterBreak(World level, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        LazyOptional<IItemHandler> inventory = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null);

        inventory.ifPresent((inv) -> {
            for(int i=0; i<inv.getSlots(); i++) {
                ItemScatterer.spawn(level, pos.getX(), pos.getY(), pos.getZ(), inv.extractItem(i, 999, false));
            }
        });

        super.afterBreak(level, player, pos, state, blockEntity, tool);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityOmniDispenser(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return BlockEntityOmniDispenser::tick;
    }
}
