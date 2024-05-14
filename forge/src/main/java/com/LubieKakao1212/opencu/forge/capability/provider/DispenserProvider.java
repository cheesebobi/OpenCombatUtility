package com.LubieKakao1212.opencu.forge.capability.provider;

import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.forge.registry.CUCapabilities;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DispenserProvider implements ICapabilityProvider {

    private LazyOptional<IFramedDevice> dispenser;

    public DispenserProvider(IFramedDevice dispenser) {
        this.dispenser = LazyOptional.of(() -> dispenser);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if(capability == CUCapabilities.DISPENSER) {
            return (LazyOptional<T>)dispenser;
        }
        return LazyOptional.empty();
    }
}
