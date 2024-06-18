package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.common.gui.ModularFrameScreen;
import com.LubieKakao1212.opencu.fabric.apilookup.APILookupPeripheral;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.fabric.apilookup.APILookupIDispenser;
import com.LubieKakao1212.opencu.fabric.apilookup.APILookupRebornEnergy;
import com.LubieKakao1212.opencu.fabric.event.TooltipHandler;
import com.LubieKakao1212.opencu.fabric.event.UseHandler;
import com.LubieKakao1212.opencu.registry.CUDispensers;
import com.LubieKakao1212.opencu.registry.CUMenu;
import com.LubieKakao1212.opencu.registry.fabric.*;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class OpenCUModFabric implements ModInitializer {

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        NetworkUtilImpl.init();

        FieldRegistrationHandler.register(CUBlocksImpl.class, OpenCUModCommon.MODID, false);
        FieldRegistrationHandler.register(CUPulseImpl.class, OpenCUModCommon.MODID, false);
        FieldRegistrationHandler.register(CUBlockEntitiesImpl.class, OpenCUModCommon.MODID, false);
        FieldRegistrationHandler.register(CUMenuImpl.class, OpenCUModCommon.MODID, false);
        FieldRegistrationHandler.register(CUItems.class, OpenCUModCommon.MODID, false);

        if(FabricLoader.getInstance().isModLoaded("computercraft")) {
            APILookupPeripheral.register();
        }

        APILookupRebornEnergy.register();
        APILookupIDispenser.init();

        TooltipHandler.init();
        UseHandler.init();


        ServerLifecycleEvents.SERVER_STARTING.register((server) -> CUDispensers.init());
    }
}
