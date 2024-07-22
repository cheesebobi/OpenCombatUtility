package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.item.ItemAimTool;
import com.LubieKakao1212.opencu.common.item.ItemLinkTool;
import com.LubieKakao1212.opencu.fabric.item.ModItemGroups;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CUItems implements ItemRegistryContainer {

    public static final Item LINK_TOOL = new ItemLinkTool(new FabricItemSettings().maxCount(1));
    public static final Item DISPENSER_GOLD = new Item(new FabricItemSettings());
    public static final Item DISPENSER_DIAMOND = new Item(new FabricItemSettings());
    public static final Item DISPENSER_NETHERITE = new Item(new FabricItemSettings());
    public static final Item AIM_TOOL = new ItemAimTool(new FabricItemSettings().maxCount(1));

    private static Item registerGroup(Item item) {
        ItemGroupEvents.modifyEntriesEvent(ModItemGroups.OPEN_COMBAT_UTILITY).register(entries -> entries.add(item));
        return item;
    }

    public static void registerItems() {
        registerItemWithGroup("link_tool", LINK_TOOL);
        registerItemWithGroup("dispenser_gold", DISPENSER_GOLD);
        registerItemWithGroup("dispenser_diamond", DISPENSER_DIAMOND);
        registerItemWithGroup("dispenser_netherite", DISPENSER_NETHERITE);
        registerItemWithGroup("aim_tool", AIM_TOOL);
    }

    private static Item registerItemWithGroup(String name, Item item) {
        registerItem(name, item);
        registerGroup(item);
        return item;
    }

    private static Item registerItem(String name, Item item) {
        Registry.register(Registries.ITEM, new Identifier(OpenCUModCommon.MODID, name), item);
        return item;
    }
}
