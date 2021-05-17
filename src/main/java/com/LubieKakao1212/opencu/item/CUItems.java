package com.LubieKakao1212.opencu.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class CUItems {

    public static List<CUItem> items = new ArrayList<>();

    public static CUMultiItem item = new CUMultiItem("item");

    static {
        item.addVariant("vector_mesh");
    }


    public static void register(RegistryEvent.Register<Item> event) {

        for(CUItem item : items) {
            item.finalizeSetup();
        }

        event.getRegistry().registerAll(CUItems.items.toArray(new Item[0]));
    }

    public static void registerModels() {

        for(CUItem item : items) {
            item.registerModel();
        }

    }
}
