package com.lubiekakao1212.opencu.forge.registry;


import com.lubiekakao1212.opencu.common.dispenser.IDispenser;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CUCapabilities {

    public static final Capability<IDispenser> DISPENSER = CapabilityManager.get(new CapabilityToken<>() { } );

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IDispenser.class);
    }


}
