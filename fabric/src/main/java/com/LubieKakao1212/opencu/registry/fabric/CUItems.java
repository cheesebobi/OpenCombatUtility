package com.LubieKakao1212.opencu.registry.fabric;

import com.LubieKakao1212.opencu.common.item.ItemAimTool;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class CUItems implements ItemRegistryContainer {

    public static final Item AIM_TOOL = new ItemAimTool(new FabricItemSettings().maxCount(1));

}