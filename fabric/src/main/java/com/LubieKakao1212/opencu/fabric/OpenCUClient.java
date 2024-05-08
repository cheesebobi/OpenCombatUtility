package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.common.block.entity.renderer.RendererModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.renderer.RendererRepulsor;
import com.LubieKakao1212.opencu.common.gui.ModularFrameScreen;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.registry.CUMenu;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class OpenCUClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NetworkUtilImpl.clientInit();
        registerRenderers();
    }

    private void registerRenderers() {
        BlockEntityRendererRegistry.register(CUBlockEntities.repulsor(), RendererRepulsor::new);
        BlockEntityRendererRegistry.register(CUBlockEntities.modularFrame(), RendererModularFrame::new);

        HandledScreens.register(CUMenu.modularFrame(), ModularFrameScreen::new);
    }
}
