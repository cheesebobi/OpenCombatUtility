package com.LubieKakao1212.opencu.item;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.TagCommand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/*public class CUMultiItem extends Item {

    private static CapabilityInitializer NO_CAPABILITY = tag -> null;

    private List<Variant> variants;

    public CUMultiItem(Properties props) {
        super(props);
        this.setHasSubtypes(true);
        variants = new ArrayList<>();
    }


    @Override
    public String getNameString(ItemStack stack) {
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
        ICapabilityProvider Create(CompoundTag tag);
    }

}*/
