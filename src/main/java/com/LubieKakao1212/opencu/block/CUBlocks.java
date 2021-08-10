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

    private static final List<CUBlock> blocks = new ArrayList<>();

    public static BlockRepulsor repulsor;
    public static BlockAngryDispenser angryDispenser;

    static
    {
        repulsor = new BlockRepulsor(Material.IRON, "repulsor");
        angryDispenser = new BlockAngryDispenser(Material.IRON, "angry_dispenser");
    }

    public static void addBlock(CUBlock base) {
        blocks.add(base);
    }

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(blocks.toArray(new CUBlock[0]));
    }

    public static void registerBlocksItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                blocks.stream().map(block -> {return block.createItemBlock();}).toArray(Item[]::new)
        );
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        for(CUBlock b : blocks)
        {
            b.registerItemModel();
        }
    }

}
