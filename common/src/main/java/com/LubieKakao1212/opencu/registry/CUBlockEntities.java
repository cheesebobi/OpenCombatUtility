package com.LubieKakao1212.opencu.registry;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityRepulsor;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public class CUBlockEntities {

    @NotNull
    @ExpectPlatform
    public static BlockEntityType<BlockEntityRepulsor> repulsor() {
        return null;
    }

    @NotNull
    @ExpectPlatform
    public static BlockEntityType<BlockEntityModularFrame> modularFrame() {
        return null;
    }

}
