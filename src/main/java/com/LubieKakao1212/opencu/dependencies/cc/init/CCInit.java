package com.LubieKakao1212.opencu.dependencies.cc.init;

import com.LubieKakao1212.opencu.dependencies.cc.event.CapabilityHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CCInit {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(CapabilityHandler.class);
    }

}
