package com.LubieKakao1212.opencu.dependencies.cc.capability;

import com.LubieKakao1212.opencu.dependencies.cc.event.CapabilityHandler;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PeripheralCapabilityProvider implements ICapabilityProvider {

    private LazyOptional<IPeripheral> peripheral;

    public PeripheralCapabilityProvider(NonNullSupplier<IPeripheral> peripheral) {
        this.peripheral = LazyOptional.of(peripheral);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityHandler.PERIPHERAL) {
            return (LazyOptional<T>)peripheral;
        }
        return LazyOptional.empty();
    }

}
