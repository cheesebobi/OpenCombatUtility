package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.fabric.block.BlockRepulsor;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

public class CUBlocks implements BlockRegistryContainer {

    public static Block REPULSOR = new BlockRepulsor(FabricBlockSettings.of(Material.METAL).strength(3f).nonOpaque());

}
