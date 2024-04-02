package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.CUPeripheral;
import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.registry.fabric.CUBlockEntitiesImpl;
import com.LubieKakao1212.opencu.registry.fabric.CUBlocks;
import com.LubieKakao1212.opencu.registry.fabric.CUPulseImpl;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class OpenCUModFabric implements ModInitializer {

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        NetworkUtilImpl.init();

        FieldRegistrationHandler.register(CUBlocks.class, OpenCUModCommon.MODID, false);
        FieldRegistrationHandler.register(CUPulseImpl.class, OpenCUModCommon.MODID, false);
        FieldRegistrationHandler.register(CUBlockEntitiesImpl.class, OpenCUModCommon.MODID, false);

        if(FabricLoader.getInstance().isModLoaded("computercraft")) {
            CUPeripheral.register();
        }
    }


}
