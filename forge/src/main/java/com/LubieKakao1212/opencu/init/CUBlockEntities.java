package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OCUCreativeTabs;
import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.BlockOmniDispenserFrame;
import com.LubieKakao1212.opencu.block.BlockRepulsor;
import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.block.entity.BlockEntityRepulsor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;

public class CUBlockEntities {

    public static RegistryObject<BlockEntityType<BlockEntityRepulsor>> REPULSOR;

    public static RegistryObject<BlockEntityType<BlockEntityOmniDispenser>> OMNI_DISPENSER;

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OpenCUMod.MODID);

    static
    {
        REPULSOR = BLOCK_ENTITIES.register(ID.REPULSOR, () -> BlockEntityType.Builder.create(BlockEntityRepulsor::new, CUBlocks.REPULSOR.get()).build(null));
        OMNI_DISPENSER = BLOCK_ENTITIES.register(ID.OMNI_DISPENSER, () -> BlockEntityType.Builder.create(BlockEntityOmniDispenser::new, CUBlocks.OMNI_DISPENSER.get()).build(null));
    }

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCK_ENTITIES.register(bus);
    }


    public static class ID {
        public static final String REPULSOR = "repulsor";
        public static final String OMNI_DISPENSER = "omni_dispenser";
    }

}
