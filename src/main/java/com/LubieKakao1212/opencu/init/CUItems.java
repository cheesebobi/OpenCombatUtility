package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OCUCreativeTabs;
import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserConfigurable;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserConstant;
import com.LubieKakao1212.opencu.capability.provider.DispenserProvider;
import com.LubieKakao1212.opencu.capability.util.CapabilityInitializer;
import com.LubieKakao1212.opencu.config.OpenCUConfig;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

@ObjectHolder(OpenCUMod.MODID)
public class CUItems {

    /*public static CUMultiItem item = new CUMultiItem("item");

    public static CUMultiItem dispenser = new CUMultiItem("dispenser");*/

    @ObjectHolder(ID.DISPENSER_T2)
    public static Item DISPENSER_T2;

    @ObjectHolder(ID.DISPENSER_T3)
    public static Item DISPENSER_T3;

    private static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OpenCUMod.MODID);

    static {
        /*item.addVariant("vector_mesh");

        dispenser.addVariant("dispenser_t2", TIER2_DISPENSER);
        dispenser.addVariant("dispenser_t3", TIER3_DISPENSER);*/

        ITEMS.register(ID.VECTOR_MESH, () -> new Item(new Item.Properties().tab(OCUCreativeTabs.tabCUMain)));
        ITEMS.register(ID.DISPENSER_T2, () -> new Item(new Item.Properties().tab(OCUCreativeTabs.tabCUMain)));
        ITEMS.register(ID.DISPENSER_T3, () -> new Item(new Item.Properties().tab(OCUCreativeTabs.tabCUMain)));
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
