package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityRepulsor;
import com.LubieKakao1212.opencu.fabric.block.BlockRepulsor;
import com.LubieKakao1212.opencu.fabric.block.entity.BlockEntityRepulsorImpl;
import com.google.common.collect.Sets;
import io.wispforest.owo.registration.reflect.BlockEntityRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;

public class CUBlockEntitiesImpl implements BlockEntityRegistryContainer {

    public static final BlockEntityType<BlockEntityRepulsor> REPULSOR = FabricBlockEntityTypeBuilder.<BlockEntityRepulsor>create(BlockEntityRepulsorImpl::new).addBlock(CUBlocks.REPULSOR).build();

    public static BlockEntityType<BlockEntityRepulsor> repulsor() {
        return REPULSOR;
    }

    public static BlockEntityType<BlockEntityModularFrame> modularFrame() {
        return null;
    }

}
