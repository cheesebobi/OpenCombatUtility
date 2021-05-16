package com.LubieKakao1212.opencu.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class CUBlocks {

    private static final List<BlockBase> blocks = new ArrayList<>();

    public static BlockRepulsor repulsor;

    static
    {
        //new BlockBase(Material.ANVIL,"test");
        repulsor = new BlockRepulsor(Material.IRON, "repulsor");
    }


    public static void addBlock(BlockBase base) {
        blocks.add(base);
    }


    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(blocks.toArray(new BlockBase[0]));
    }

    public static void registerBlocksItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                blocks.stream().map(block -> {return block.createItemBlock();}).toArray(Item[]::new)
        );
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        for(BlockBase b : blocks)
        {
            b.registerItemModel();
        }
    }

}
