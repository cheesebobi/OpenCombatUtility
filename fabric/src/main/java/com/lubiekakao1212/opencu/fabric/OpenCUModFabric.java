package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.NetworkUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class OpenCUModFabric implements ModInitializer, ClientModInitializer {

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        NetworkUtilImpl.init();
    }

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        NetworkUtilImpl.clientInit();
    }
}
