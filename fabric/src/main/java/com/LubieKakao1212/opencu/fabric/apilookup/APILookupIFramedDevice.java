package com.LubieKakao1212.opencu.fabric.apilookup;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.device.ArrayControllerDevice;
import com.LubieKakao1212.opencu.common.device.DispenserConstant;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.TrackerBase;
import com.LubieKakao1212.opencu.registry.CUDispensers;
import com.LubieKakao1212.opencu.registry.CUIds;
import com.LubieKakao1212.opencu.registry.fabric.CUItems;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.item.Items;

import java.util.function.Supplier;

public class APILookupIFramedDevice {

    public static final Supplier<IFramedDevice> VANILLA_DISPENSER = () -> new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            (float) OpenCUConfigCommon.vanillaDispenserDevice().rotationSpeed() / 20.f,
            OpenCUConfigCommon.vanillaDispenserDevice().spread(),
            OpenCUConfigCommon.vanillaDispenserDevice().force(),
            OpenCUConfigCommon.vanillaDispenserDevice().baseEnergy(),
            OpenCUConfigCommon.vanillaDispenserDevice().power()
    );

    public static final Supplier<IFramedDevice> VANILLA_DROPPER = () -> new DispenserConstant(
            CUDispensers.VANILLA_DROPPER,
            (float) OpenCUConfigCommon.vanillaDispenserDevice().rotationSpeed() / 20.f,
            OpenCUConfigCommon.vanillaDispenserDevice().spread(),
            OpenCUConfigCommon.vanillaDispenserDevice().force(),
            OpenCUConfigCommon.vanillaDispenserDevice().baseEnergy(),
            OpenCUConfigCommon.vanillaDispenserDevice().power()
    );

    private static final IFramedDevice vanilla_dropper = VANILLA_DROPPER.get();
    private static final IFramedDevice vanilla_dispenser = VANILLA_DISPENSER.get();
    private static final IFramedDevice gold_dispenser = new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            (float) OpenCUConfigCommon.goldenDispenserDevice().rotationSpeed() / 20.f,
            OpenCUConfigCommon.goldenDispenserDevice().spread(),
            OpenCUConfigCommon.goldenDispenserDevice().force(),
            OpenCUConfigCommon.goldenDispenserDevice().baseEnergy(),
            OpenCUConfigCommon.goldenDispenserDevice().power());

    private static final IFramedDevice diamond_dispenser = new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            (float) OpenCUConfigCommon.diamondDispenserDevice().rotationSpeed() / 20.f,
            OpenCUConfigCommon.diamondDispenserDevice().spread(),
            OpenCUConfigCommon.diamondDispenserDevice().force(),
            OpenCUConfigCommon.diamondDispenserDevice().baseEnergy(),
            OpenCUConfigCommon.diamondDispenserDevice().power()
    );

    private static final IFramedDevice netherite_dispenser = new DispenserConstant(
            CUDispensers.VANILLA_DISPENSER,
            (float) OpenCUConfigCommon.netheriteDispenserDevice().rotationSpeed() / 20.f,
            OpenCUConfigCommon.netheriteDispenserDevice().spread(),
            OpenCUConfigCommon.netheriteDispenserDevice().force(),
            OpenCUConfigCommon.netheriteDispenserDevice().baseEnergy(),
            OpenCUConfigCommon.netheriteDispenserDevice().power()
    );


    private static final IFramedDevice simple_tracker = new TrackerBase();
    private static final IFramedDevice array_controller = new ArrayControllerDevice();

    public static ItemApiLookup<IFramedDevice, Void> DISPENSER = ItemApiLookup.get(CUIds.DISPENSER_API, IFramedDevice.class, Void.class);

    public static void init() {
        DISPENSER.registerForItems((stack, ctx) -> vanilla_dropper, Items.DROPPER);
        DISPENSER.registerForItems((stack, ctx) -> vanilla_dispenser, Items.DISPENSER);
        DISPENSER.registerForItems((stack, ctx) -> gold_dispenser, CUItems.DISPENSER_GOLD);
        DISPENSER.registerForItems((stack, ctx) -> diamond_dispenser, CUItems.DISPENSER_DIAMOND);
        DISPENSER.registerForItems((stack, ctx) -> netherite_dispenser, CUItems.DISPENSER_NETHERITE);

        DISPENSER.registerForItems((stack, ctx) -> simple_tracker, Items.ENDER_EYE);
        //Broken
        //DISPENSER.registerForItems((stack, ctx) -> array_controller, Items.ENDER_PEARL);
    }

}
