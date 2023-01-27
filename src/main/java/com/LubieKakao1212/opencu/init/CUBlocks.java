package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OCUCreativeTabs;
import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.BlockOmniDispenserFrame;
import com.LubieKakao1212.opencu.block.BlockRepulsor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.common.value.qual.StaticallyExecutable;
import org.jetbrains.annotations.BlockingExecutor;

@ObjectHolder(OpenCUMod.MODID)
public class CUBlocks {

    @ObjectHolder(ID.REPULSOR)
    public static Block REPULSOR;

    @ObjectHolder(ID.OMNI_DISPENSER)
    public static Block OMNI_DISPENSER;

    private static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OpenCUMod.MODID);

    static
    {
        blockItem(BLOCKS.register(ID.REPULSOR, () -> new BlockRepulsor(BlockBehaviour.Properties.of(Material.METAL).strength(1f).noOcclusion())));
        blockItem(BLOCKS.register(ID.OMNI_DISPENSER, () -> new BlockOmniDispenserFrame(BlockBehaviour.Properties.of(Material.METAL).strength(1f).noOcclusion())));

        //TODO check if this works
        CURegister.register(BLOCKS);
    }

    public static void init() {
        //CURegister.register(BLOCKS);
    }

    public static void blockItem(RegistryObject<Block> obj) {
        CUItems.register(obj.getId().getPath(), () -> new BlockItem(obj.get(), new Item.Properties().tab(OCUCreativeTabs.tabCUMain)));
    }


    public static class ID {
        public static final String REPULSOR = "repulsor";
        public static final String OMNI_DISPENSER = "omni_dispenser";
    }


}
