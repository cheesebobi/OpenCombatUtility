package com.LubieKakao1212.opencu.item;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CUMultiItem extends CUItem {

    private static CapabilityInitializer NO_CAPABILITY = tag -> null;

    private List<Variant> variants;

    public CUMultiItem(String name) {
        super(name);
        this.setHasSubtypes(true);
        variants = new ArrayList<>();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        for(int i = 0; i < variants.size(); i++) {
            OpenCUMod.proxy.registerItemRenderer(this, i, variants.get(i).id);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + OpenCUMod.MODID + "." + variants.get(stack.getItemDamage()).id;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab))
        {
            for(int i = 0; i < variants.size(); i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    public void addVariant(String variant) {
        variants.add(new Variant(variant, NO_CAPABILITY));
    }

    public void addVariant(String variant, CapabilityInitializer capabilities) {
        variants.add(new Variant(variant, capabilities));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int damage = stack.getItemDamage();
        String key = String.format("item.opencu.%s.desc", variants.get(damage).id);
        String[] lines = I18n.format(key).split("\\\\");
        tooltip.addAll(Lists.newArrayList(lines));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return variants.get(stack.getItemDamage()).capabilities.Create(nbt);
    }

    @Override
    public int getMetadata(ItemStack stack) {
        return stack.getItemDamage();
    }

    @Override
    public void finalizeSetup() {

    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    private static class Variant {
        public String id;
        public CapabilityInitializer capabilities;

        public Variant(String id, CapabilityInitializer capabilities) {
            this.id = id;
            this.capabilities = capabilities;
        }

    }

    @FunctionalInterface
    public interface CapabilityInitializer {
        ICapabilityProvider Create(NBTTagCompound tag);
    }

}
