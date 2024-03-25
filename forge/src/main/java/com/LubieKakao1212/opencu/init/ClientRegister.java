package com.LubieKakao1212.opencu.init;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = OpenCUMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegister {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        //ItemBlockRenderTypes.setRenderLayer(CUBlocks.REPULSOR, RenderType.cutout());
        //ItemBlockRenderTypes.setRenderLayer(CUBlocks.OMNI_DISPENSER, RenderType.cutout());
        CUGuis.inti();
    }


    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CUBlockEntities.REPULSOR.get(), RendererRepulsor::new);
        event.registerBlockEntityRenderer(CUBlockEntities.OMNI_DISPENSER.get(), RendererOmniDispenser::new);
    }


}
