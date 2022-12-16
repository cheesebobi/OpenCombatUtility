package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OCUCreativeTabs;
import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.BlockOmniDispenserFrame;
import com.LubieKakao1212.opencu.block.BlockRepulsor;
import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.block.entity.BlockEntityRepulsor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;

public class CUBlockEntities {

    @ObjectHolder(CUBlocks.ID.REPULSOR)
    public static BlockEntityType<BlockEntityRepulsor> REPULSOR;

    @ObjectHolder(CUBlocks.ID.OMNI_DISPENSER)
    public static BlockEntityType<BlockEntityOmniDispenser> OMNI_DISPENSER;

    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, OpenCUMod.MODID);

    static
    {
        BLOCK_ENTITIES.register(ID.REPULSOR, () -> BlockEntityType.Builder.of(BlockEntityRepulsor::new, CUBlocks.REPULSOR).build(null));
        BLOCK_ENTITIES.register(ID.OMNI_DISPENSER, () -> BlockEntityType.Builder.of(BlockEntityOmniDispenser::new, CUBlocks.OMNI_DISPENSER).build(null));
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
