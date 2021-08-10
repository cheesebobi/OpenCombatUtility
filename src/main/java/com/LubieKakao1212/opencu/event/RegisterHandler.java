package com.LubieKakao1212.opencu.event;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.CUBlocks;
import com.LubieKakao1212.opencu.block.tileentity.TileEntityAngryDispenser;
import com.LubieKakao1212.opencu.block.tileentity.TileEntityRepulsor;
import com.LubieKakao1212.opencu.item.CUItem;
import com.LubieKakao1212.opencu.item.CUItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class RegisterHandler {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        CUBlocks.registerBlocks(event);

        GameRegistry.registerTileEntity(TileEntityRepulsor.class, new ResourceLocation(OpenCUMod.MODID, "repulsor"));
        GameRegistry.registerTileEntity(TileEntityAngryDispenser.class, new ResourceLocation(OpenCUMod.MODID, "angry_dispenser"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        CUBlocks.registerBlocksItems(event);
        CUItems.register(event);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        CUBlocks.registerModels();
        CUItems.registerModels();
    }

}
