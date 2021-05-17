package com.LubieKakao1212.opencu.item;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class CUMultiItem extends CUItem {

    private List<String> variants;

    public CUMultiItem(String name) {
        super(name);
        this.setHasSubtypes(true);
        variants = new ArrayList<>();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        for(int i = 0; i < variants.size(); i++) {
            OpenCUMod.proxy.registerItemRenderer(this, i, variants.get(i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + OpenCUMod.MODID + "." + variants.get(stack.getItemDamage());
    }

    public void addVariant(String variant) {
        variants.add(variant);
    }

    @Override
    public void finalizeSetup() {
        this.setMaxDamage(variants.size());
    }
}
