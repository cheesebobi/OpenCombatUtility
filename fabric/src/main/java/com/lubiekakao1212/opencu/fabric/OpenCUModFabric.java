package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.registry.fabric.CUBlockEntitiesImpl;
import com.LubieKakao1212.opencu.registry.fabric.CUBlocks;
import com.LubieKakao1212.opencu.registry.fabric.CUPulseImpl;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class OpenCUModFabric implements ModInitializer, ClientModInitializer {

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        NetworkUtilImpl.init();

        FieldRegistrationHandler.register(CUBlocks.class, OpenCUModCommon.MODID, false);
        FieldRegistrationHandler.register(CUPulseImpl.class, OpenCUModCommon.MODID, false);
        FieldRegistrationHandler.register(CUBlockEntitiesImpl.class, OpenCUModCommon.MODID, false);
    }

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        NetworkUtilImpl.clientInit();
    }
}
