package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CUItems {

    public static final RegistryObject<Item> DISPENSER_T2;

    public static final RegistryObject<Item> DISPENSER_T3;

    public static final RegistryObject<Item> VECTOR_MESH;

    private static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OpenCUMod.MODID);

    static {
        VECTOR_MESH = ITEMS.register(ID.VECTOR_MESH, () -> new Item(new Item.Settings()));
        DISPENSER_T2 = ITEMS.register(ID.DISPENSER_T2, () -> new Item(new Item.Settings()));
        DISPENSER_T3 = ITEMS.register(ID.DISPENSER_T3, () -> new Item(new Item.Settings()));
    }

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
    }

    public static void register(String id, Supplier<Item> item) {
        ITEMS.register(id, item);
    }


    public static class ID {
        public static final String VECTOR_MESH = "vector_mesh";

        public static final String DISPENSER_T2 = "dispenser_tier2";
        public static final String DISPENSER_T3 = "dispenser_tier3";
    }
}
