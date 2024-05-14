package com.LubieKakao1212.opencu.fabric.apilookup;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.device.DispenserConstant;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.registry.CUDispensers;
import com.LubieKakao1212.opencu.registry.CUIds;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.item.Items;

import java.util.function.Supplier;

public class APILookupIDispenser {

    public static final Supplier<IFramedDevice> VANILLA_DISPENSER = () -> new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            (float) OpenCUConfigCommon.vanillaDispenserDevice().rotationSpeed() / 20.f,
            OpenCUConfigCommon.vanillaDispenserDevice().spread(),
            OpenCUConfigCommon.vanillaDispenserDevice().force(),
            OpenCUConfigCommon.vanillaDispenserDevice().baseEnergy()
    );

    public static final Supplier<IFramedDevice> VANILLA_DROPPER = () -> new DispenserConstant(
            CUDispensers.VANILLA_DROPPER,
            (float) OpenCUConfigCommon.vanillaDispenserDevice().rotationSpeed() / 20.f,
            OpenCUConfigCommon.vanillaDispenserDevice().spread(),
            OpenCUConfigCommon.vanillaDispenserDevice().force(),
            OpenCUConfigCommon.vanillaDispenserDevice().baseEnergy()
    );

    private static final IFramedDevice vanilla_dispenser = VANILLA_DISPENSER.get();
    private static final IFramedDevice vanilla_dropper = VANILLA_DROPPER.get();

    public static ItemApiLookup<IFramedDevice, Void> DISPENSER = ItemApiLookup.get(CUIds.DISPENSER_API, IFramedDevice.class, Void.class);

    public static void init() {
        DISPENSER.registerForItems((stack, ctx) -> vanilla_dispenser, Items.DISPENSER);
        DISPENSER.registerForItems((stack, ctx) -> vanilla_dropper, Items.DROPPER);
    }

}