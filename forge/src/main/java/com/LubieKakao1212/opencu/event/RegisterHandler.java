package com.LubieKakao1212.opencu.event;

import com.LubieKakao1212.opencu.capability.dispenser.IDispenser;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterHandler {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IDispenser.class);
    }


}
