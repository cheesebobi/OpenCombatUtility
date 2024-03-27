package com.lubiekakao1212.opencu.registry.forge;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class CURegister {

    private static final List<DeferredRegister<?>> registers = new ArrayList<>();

    public static void register(DeferredRegister<?> register) {
        registers.add(register);
    }

    public static void init() {
        CUBlocks.init();
        CUBlockEntitiesImpl.init();
        CUMenuImpl.init();
        CUItems.init();
        CUPulseImpl.init();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        for(DeferredRegister<?> register : registers) {
            register.register(bus);
        }

    }

}
