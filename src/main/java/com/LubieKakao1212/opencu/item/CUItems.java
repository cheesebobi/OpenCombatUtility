package com.LubieKakao1212.opencu.item;

import com.LubieKakao1212.opencu.OCUCreativeTabs;
import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserConfigurable;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserConstant;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserRegistry;
import com.LubieKakao1212.opencu.capability.provider.DispenserProvider;
import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.opencu.lib.math.MathUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class CUItems {

    /*public static final CUMultiItem.CapabilityInitializer VANILLA_DISPENSER = tag -> new DispenserProvider(new DispenserConstant(
            DispenserRegistry.VANILLA_DISPENSER,
            (float) OpenCUConfig.omniDispenser.vanilla.rotationSpeed / 20.f,
            OpenCUConfig.omniDispenser.vanilla.spread,
            OpenCUConfig.omniDispenser.vanilla.force,
            OpenCUConfig.omniDispenser.vanilla.base_energy
    ));

    public static final CUMultiItem.CapabilityInitializer VANILLA_DROPPER = tag -> new DispenserProvider(new DispenserConstant(
            DispenserRegistry.VANILLA_DROPPER,
            (float) OpenCUConfig.omniDispenser.vanilla.rotationSpeed / 20.f,
            OpenCUConfig.omniDispenser.vanilla.spread,
            OpenCUConfig.omniDispenser.vanilla.force,
            OpenCUConfig.omniDispenser.vanilla.base_energy
    ));

    public static final CUMultiItem.CapabilityInitializer TIER2_DISPENSER = tag -> new DispenserProvider(new DispenserConfigurable(
            DispenserRegistry.TIER2_DISPENSER,
            (float) OpenCUConfig.omniDispenser.tier2.rotationSpeed / 20.f,
            OpenCUConfig.omniDispenser.tier2.spread,
            OpenCUConfig.omniDispenser.tier2.spread_max,
            OpenCUConfig.omniDispenser.tier2.force,
            OpenCUConfig.omniDispenser.tier2.base_energy
    ));

    public static final CUMultiItem.CapabilityInitializer TIER3_DISPENSER = tag -> new DispenserProvider(new DispenserConfigurable(
            DispenserRegistry.TIER3_DISPENSER,
            (float) OpenCUConfig.omniDispenser.tier3.rotationSpeed / 20.f,
            OpenCUConfig.omniDispenser.tier3.spread,
            OpenCUConfig.omniDispenser.tier3.spread_max,
            OpenCUConfig.omniDispenser.tier3.force,
            OpenCUConfig.omniDispenser.tier3.base_energy
    ));*/

    /*public static CUMultiItem item = new CUMultiItem("item");

    public static CUMultiItem dispenser = new CUMultiItem("dispenser");*/

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OpenCUMod.MODID);

    static {
        /*item.addVariant("vector_mesh");

        dispenser.addVariant("dispenser_t2", TIER2_DISPENSER);
        dispenser.addVariant("dispenser_t3", TIER3_DISPENSER);*/

        ITEMS.register(ID.VECTOR_MESH, () -> new Item(new Item.Properties().tab(OCUCreativeTabs.tabCUMain)));
    }

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
    }

    public static class ID {
        public static final String VECTOR_MESH = "vector_mesh";

        //public static final String DISPENSER_T1 = ;
    }

}
