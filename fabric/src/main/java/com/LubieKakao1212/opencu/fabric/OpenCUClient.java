package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.common.block.entity.renderer.RendererModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.renderer.RendererRepulsor;
import com.LubieKakao1212.opencu.common.gui.ModularFrameScreen;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.registry.CUMenu;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class OpenCUClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NetworkUtilImpl.clientInit();
        registerRenderers();
    }

    private void registerRenderers() {
        BlockEntityRendererFactories.register(CUBlockEntities.repulsor(), RendererRepulsor::new);
        BlockEntityRendererFactories.register(CUBlockEntities.modularFrame(), RendererModularFrame::new);

        HandledScreens.register(CUMenu.modularFrame(), ModularFrameScreen::new);
    }
}
