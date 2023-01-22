package com.LubieKakao1212.opencu.capability.provider;

import com.LubieKakao1212.opencu.capability.dispenser.DispenserCapability;
import com.LubieKakao1212.opencu.capability.dispenser.IDispenser;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DispenserProvider implements ICapabilityProvider {

    private LazyOptional<IDispenser> dispenser;

    public DispenserProvider(IDispenser dispenser) {
        this.dispenser = LazyOptional.of(() -> dispenser);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if(capability == DispenserCapability.DISPENSER_CAPABILITY){
            return (LazyOptional<T>)dispenser;
        }
        return null;
    }
}
