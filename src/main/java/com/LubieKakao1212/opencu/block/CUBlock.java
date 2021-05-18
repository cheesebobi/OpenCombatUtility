package com.LubieKakao1212.opencu.block;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.pulse.RepulsorPulse;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CUBlock extends Block {

    protected String name;

    public CUBlock(Material material, String name) {
        super(material);

        this.name = name;

        setUnlocalizedName(OpenCUMod.MODID + "." + name);
        setRegistryName(name);
        CUBlocks.addBlock(this);
    }

    public void registerItemModel() {
        OpenCUMod.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }

    @Override
    public CUBlock setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
