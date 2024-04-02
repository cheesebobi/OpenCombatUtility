package com.LubieKakao1212.opencu.fabric;

import com.LubieKakao1212.opencu.common.block.entity.renderer.RendererRepulsor;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;

public class OpenCUClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NetworkUtilImpl.clientInit();
        registerRenderers();
    }

    private void registerRenderers() {
        BlockEntityRendererRegistry.register(CUBlockEntities.repulsor(), RendererRepulsor::new);
    }
}
