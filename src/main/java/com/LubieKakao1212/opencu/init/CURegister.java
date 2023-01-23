package com.LubieKakao1212.opencu.init;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class CURegister {

    private static final List<DeferredRegister> registers = new ArrayList<>();

    public static void register(DeferredRegister register) {
        registers.add(register);
    }

    public static void init() {

        CUBlocks.init();
        CUBlockEntities.init();
        CUMenu.init();
        CUItems.init();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        for(DeferredRegister register : registers) {
            register.register(bus);
        }

    }

}
