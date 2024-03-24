package com.LubieKakao1212.opencu.common.registry;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityRepulsor;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.entity.BlockEntityType;

public class CUBlockEntities {

    @ExpectPlatform
    public static BlockEntityType<BlockEntityRepulsor> repulsor() {
        return null;
    }

    @ExpectPlatform
    public static BlockEntityType<BlockEntityModularFrame> modularFrame() {
        return null;
    }

}
