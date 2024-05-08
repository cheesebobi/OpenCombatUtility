package com.LubieKakao1212.opencu.registry.forge;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityRepulsor;
import com.LubieKakao1212.opencu.forge.block.entity.BlockEntityModularFrameImpl;
import com.LubieKakao1212.opencu.forge.block.entity.BlockEntityRepulsorImpl;
import com.LubieKakao1212.opencu.registry.CUIds;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CUBlockEntitiesImpl {

    public static RegistryObject<BlockEntityType<BlockEntityRepulsor>> REPULSOR;

    public static RegistryObject<BlockEntityType<BlockEntityModularFrame>> MODULAR_FRAME;

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OpenCUModCommon.MODID);

    static {
        REPULSOR = BLOCK_ENTITIES.register(CUIds.Str.REPULSOR, () -> BlockEntityType.Builder.<BlockEntityRepulsor>create(BlockEntityRepulsorImpl::new, CUBlocksImpl.REPULSOR.get()).build(null));
        MODULAR_FRAME = BLOCK_ENTITIES.register(CUIds.Str.DISPENSER, () -> BlockEntityType.Builder.<BlockEntityModularFrame>create(BlockEntityModularFrameImpl::new, CUBlocksImpl.OMNI_DISPENSER.get()).build(null));

        CURegister.register(BLOCK_ENTITIES);
    }

    public static void init() {
//        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
//        BLOCK_ENTITIES.register(bus);
    }

    public static BlockEntityType<BlockEntityRepulsor> repulsor() {
        return REPULSOR.get();
    }

    public static BlockEntityType<BlockEntityModularFrame> modularFrame() {
        return MODULAR_FRAME.get();
    }

}
