package com.LubieKakao1212.opencu.forge.client;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.renderer.RendererModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.renderer.RendererRepulsor;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.registry.forge.CUGuis;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = OpenCUModCommon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegister {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        CUGuis.inti();
    }


    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CUBlockEntities.repulsor(), RendererRepulsor::new);
        event.registerBlockEntityRenderer(CUBlockEntities.modularFrame(), RendererModularFrame::new);
    }


}
