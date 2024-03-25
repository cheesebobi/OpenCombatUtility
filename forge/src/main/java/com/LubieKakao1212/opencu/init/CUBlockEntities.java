package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.forge.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.forge.block.entity.BlockEntityRepulsorImpl;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CUBlockEntities {

    public static RegistryObject<BlockEntityType<BlockEntityRepulsorImpl>> REPULSOR;

    public static RegistryObject<BlockEntityType<BlockEntityModularFrameImpl>> OMNI_DISPENSER;

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OpenCUMod.MODID);

    static
    {
        REPULSOR = BLOCK_ENTITIES.register(ID.REPULSOR, () -> BlockEntityType.Builder.create(BlockEntityRepulsorImpl::new, CUBlocks.REPULSOR.get()).build(null));
        OMNI_DISPENSER = BLOCK_ENTITIES.register(ID.OMNI_DISPENSER, () -> BlockEntityType.Builder.create(BlockEntityModularFrameImpl::new, CUBlocks.OMNI_DISPENSER.get()).build(null));
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
