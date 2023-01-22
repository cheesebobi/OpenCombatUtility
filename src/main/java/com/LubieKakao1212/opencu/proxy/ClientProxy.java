/*
package com.LubieKakao1212.opencu.proxy;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.block.entity.BlockEntityRepulsor;
import com.LubieKakao1212.opencu.block.entity.renderer.RendererOmniDispenser;
import com.LubieKakao1212.opencu.block.entity.renderer.RendererRepulsor;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLPreInitializationEvent event) {
        super.init(event);
        OBJLoader.INSTANCE.addDomain(OpenCUMod.MODID);
        ClientRegistry.bindTileEntitySpecialRenderer(BlockEntityRepulsor.class, new RendererRepulsor());
        ClientRegistry.bindTileEntitySpecialRenderer(BlockEntityOmniDispenser.class, new RendererOmniDispenser());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(OpenCUMod.MODID + ":" + id, "inventory"));
    }
}*/
