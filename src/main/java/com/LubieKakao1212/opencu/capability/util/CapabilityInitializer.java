package com.LubieKakao1212.opencu.capability.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

@FunctionalInterface
public interface CapabilityInitializer {
    ICapabilityProvider Create(CompoundTag tag);
}
