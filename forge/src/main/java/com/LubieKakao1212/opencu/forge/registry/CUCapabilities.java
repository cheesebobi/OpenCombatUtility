package com.LubieKakao1212.opencu.forge.registry;


import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CUCapabilities {

    public static final Capability<IFramedDevice> DISPENSER = CapabilityManager.get(new CapabilityToken<>() { } );

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IFramedDevice.class);
    }


}
