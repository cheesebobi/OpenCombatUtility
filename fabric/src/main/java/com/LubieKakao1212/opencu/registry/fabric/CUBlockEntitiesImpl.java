package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityRepulsor;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityRepulsorImpl;
import io.wispforest.owo.registration.reflect.BlockEntityRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class CUBlockEntitiesImpl implements BlockEntityRegistryContainer {

    public static final BlockEntityType<BlockEntityRepulsor> REPULSOR = FabricBlockEntityTypeBuilder.<BlockEntityRepulsor>create(BlockEntityRepulsorImpl::new).addBlock(CUBlocksImpl.REPULSOR).build();
    public static final BlockEntityType<BlockEntityModularFrame> MODULAR_FRAME = FabricBlockEntityTypeBuilder.<BlockEntityModularFrame>create(BlockEntityModularFrameImpl::new).addBlock(CUBlocksImpl.MODULAR_FRAME).build();

    public static BlockEntityType<BlockEntityRepulsor> repulsor() {
        return REPULSOR;
    }

    public static BlockEntityType<BlockEntityModularFrame> modularFrame() {
        return MODULAR_FRAME;
    }

}
