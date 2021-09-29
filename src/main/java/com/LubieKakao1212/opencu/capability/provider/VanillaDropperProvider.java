package com.LubieKakao1212.opencu.capability.provider;

import com.LubieKakao1212.opencu.capability.dispenser.DispenserCapability;
import com.LubieKakao1212.opencu.capability.dispenser.vanilla.VanillaDispenser;
import com.LubieKakao1212.opencu.capability.dispenser.vanilla.VanillaDropper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VanillaDropperProvider implements ICapabilityProvider {

    private VanillaDropper dispenser;

    public VanillaDropperProvider() {
        this.dispenser = new VanillaDropper();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == DispenserCapability.DISPENSER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == DispenserCapability.DISPENSER_CAPABILITY){
            return (T)dispenser;
        }
        return null;
    }

}
