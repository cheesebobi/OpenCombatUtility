package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OCUCreativeTabs;
import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.BlockOmniDispenserFrame;
import com.LubieKakao1212.opencu.block.BlockRepulsor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.common.value.qual.StaticallyExecutable;
import org.jetbrains.annotations.BlockingExecutor;

public class CUBlocks {

    public static RegistryObject<Block> REPULSOR;

    public static RegistryObject<Block> OMNI_DISPENSER;

    private static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OpenCUMod.MODID);

    static
    {
        REPULSOR = blockItem(BLOCKS.register(ID.REPULSOR, () -> new BlockRepulsor(AbstractBlock.Settings.of(Material.METAL).strength(3f).nonOpaque())));
        OMNI_DISPENSER = blockItem(BLOCKS.register(ID.OMNI_DISPENSER, () -> new BlockOmniDispenserFrame(AbstractBlock.Settings.of(Material.METAL).strength(3f).nonOpaque())));

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

    public static class ID {
        public static final String REPULSOR = "repulsor";
        public static final String OMNI_DISPENSER = "adv_dispenser";
    }

}