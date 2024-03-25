package com.lubiekakao1212.opencu.forge.capability.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

@FunctionalInterface
public interface CapabilityInitializer {
    ICapabilityProvider create(NbtCompound tag);
}
