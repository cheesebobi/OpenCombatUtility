package com.LubieKakao1212.opencu.item;

import com.LubieKakao1212.opencu.CUCreativeTabs;
import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class CUItem extends Item {

    private String name;

    public CUItem(String name) {
        this.name = name;
        this.setRegistryName(new ResourceLocation(OpenCUMod.MODID, name));
        this.setUnlocalizedName(OpenCUMod.MODID+":"+name);
        this.setCreativeTab(CUCreativeTabs.tabCUMain);
        CUItems.items.add(this);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel() {
        OpenCUMod.proxy.registerItemRenderer(this, 0, name);
    }

    public void finalizeSetup() {

    }


}
