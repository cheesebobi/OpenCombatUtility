package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.fabric.block.BlockModularFrame;
import com.LubieKakao1212.opencu.fabric.block.BlockRepulsor;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import org.jetbrains.annotations.NotNull;

public class CUBlocksImpl implements BlockRegistryContainer {

    public static Block REPULSOR = new BlockRepulsor(FabricBlockSettings.of(Material.METAL).strength(3f).nonOpaque());
    public static Block MODULAR_FRAME = new BlockModularFrame(FabricBlockSettings.of(Material.METAL).strength(3f).nonOpaque());

    @NotNull
    public static Block repulsor() {
        return REPULSOR;
    }

    @NotNull
    public static Block modularFrame() {
        return MODULAR_FRAME;
    }
}
