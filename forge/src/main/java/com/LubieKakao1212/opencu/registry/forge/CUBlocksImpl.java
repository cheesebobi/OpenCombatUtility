package com.LubieKakao1212.opencu.registry.forge;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.forge.block.BlockModularFrame;
import com.LubieKakao1212.opencu.forge.block.BlockRepulsor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class CUBlocksImpl {
    public static RegistryObject<Block> REPULSOR;

    public static RegistryObject<Block> OMNI_DISPENSER;

    private static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OpenCUModCommon.MODID);

    static
    {
        REPULSOR = blockItem(BLOCKS.register(ID.REPULSOR, () -> new BlockRepulsor(AbstractBlock.Settings.of(Material.METAL).strength(3f).nonOpaque())));
        OMNI_DISPENSER = blockItem(BLOCKS.register(ID.OMNI_DISPENSER, () -> new BlockModularFrame(AbstractBlock.Settings.of(Material.METAL).strength(3f).nonOpaque())));

        //TODO check if this works
        CURegister.register(BLOCKS);
    }

    public static void init() {
        //CURegister.register(BLOCKS);
    }

    public static RegistryObject<Block> blockItem(RegistryObject<Block> obj) {
        CUItems.register(obj.getId().getPath(), () -> new BlockItem(obj.get(), new Item.Settings()));
        return obj;
    }

    @NotNull
    public static Block repulsor() {
        return REPULSOR.get();
    }

    @NotNull
    public static Block modularFrame() {
        return OMNI_DISPENSER.get();
    }

    public static class ID {
        public static final String REPULSOR = "repulsor";
        public static final String OMNI_DISPENSER = "adv_dispenser";
    }

}
