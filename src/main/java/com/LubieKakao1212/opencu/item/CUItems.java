package com.LubieKakao1212.opencu.item;

import com.LubieKakao1212.opencu.capability.dispenser.DispenserConfigurable;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserConstant;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserRegistry;
import com.LubieKakao1212.opencu.capability.provider.DispenserProvider;
import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.opencu.lib.math.MathUtil;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class CUItems {

    public static final CUMultiItem.CapabilityInitializer VANILLA_DISPENSER = tag -> new DispenserProvider(new DispenserConstant(
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
            OpenCUConfig.omniDispenser.tier2.force,
            OpenCUConfig.omniDispenser.tier2.base_energy
    ));

    public static final CUMultiItem.CapabilityInitializer TIER3_DISPENSER = tag -> new DispenserProvider(new DispenserConfigurable(
            DispenserRegistry.TIER3_DISPENSER,
            (float) OpenCUConfig.omniDispenser.tier3.rotationSpeed / 20.f,
            OpenCUConfig.omniDispenser.tier3.spread,
            OpenCUConfig.omniDispenser.tier3.force,
            OpenCUConfig.omniDispenser.tier3.base_energy
    ));

    public static List<CUItem> items = new ArrayList<>();

    public static CUMultiItem item = new CUMultiItem("item");

    public static CUMultiItem dispenser = new CUMultiItem("dispenser");

    static {
        item.addVariant("vector_mesh");

        dispenser.addVariant("dispenser_t2", TIER2_DISPENSER);
        dispenser.addVariant("dispenser_t3", TIER3_DISPENSER);
    }

    public static void register(RegistryEvent.Register<Item> event) {

        for(CUItem item : items) {
            item.finalizeSetup();
        }

        event.getRegistry().registerAll(CUItems.items.toArray(new Item[0]));
    }

    public static void registerModels() {

        for(CUItem item : items) {
            item.registerModel();
        }

    }
}
