package com.LubieKakao1212.opencu.fabric.event;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;

public class BlockEntityLoadHandler {

    public static void init() {
        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register(BlockEntityLoadHandler::onLoad);
    }

    static void onLoad(BlockEntity blockEntity, ClientWorld world) {
        if(blockEntity instanceof BlockEntityModularFrame frame) {
            frame.requestDispenserUpdate();
        }
    }


}
