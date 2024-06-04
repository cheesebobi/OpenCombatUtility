package com.LubieKakao1212.opencu.common.peripheral;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import com.LubieKakao1212.opencu.registry.CUIds;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModularFramePeripheral implements IPeripheral {

    public static final String TYPE = CUIds.MODULAR_FRAME.toString();

    private BlockEntityModularFrame target;

    public ModularFramePeripheral(BlockEntityModularFrame modularFrame) {
        this.target = modularFrame;
    }

    /**
     * Should return a string that uniquely identifies this type of peripheral.
     * This can be queried from lua by calling {@code peripheral.getType()}
     *
     * @return A string identifying the type of peripheral.
     */
    @NotNull
    @Override
    public String getType() {
        return TYPE;
    }

    /**
     * Determine whether this peripheral is equivalent to another one.
     * <p>
     * The minimal example should at least check whether they are the same object. However, you may wish to check if
     * they point to the same block or tile entity.
     *
     * @param other The peripheral to compare against. This may be {@code null}.
     * @return Whether these peripherals are equivalent.
     */
    @Override
    public boolean equals(@Nullable IPeripheral other) {
        if(other instanceof ModularFramePeripheral) {
            ModularFramePeripheral otherDispenser = ((ModularFramePeripheral) other);
            return otherDispenser.target.getPos().equals(target.getPos());
        }
        return false;
    }

    @Nullable
    @Override
    public Object getTarget() {
        return target;
    }

    @LuaFunction
    public final boolean aim(double yaw, double pitch) {
        target.aim(pitch, yaw);
        return true;
    }

    @LuaFunction
    public final boolean isAligned() {
        return target.isAligned();
    }

    @LuaFunction
    public final boolean isRequiresLock() {
        return target.isRequiresLock();
    }

    @LuaFunction
    public final void setRequiresLock(boolean requiresLock) {
        target.setRequiresLock(requiresLock);
    }

    @LuaFunction
    public final void setRedstoneControl(String type) throws LuaException{
        try {
            var rsct = RedstoneControlType.valueOf(type);
            target.setRedstoneControlType(rsct);
        }
        catch (IllegalArgumentException e) {
            throw new LuaException(e.getMessage());
        }
    }

    @LuaFunction
    public final String setRedstoneControl() {
        return target.getRedstoneControlType().toString();
    }

    @LuaFunction
    public final IDeviceApi getDeviceApi() {
        return target.getCurrentDeviceApi();
    }

    @LuaFunction
    public final boolean activate() {
        target.activate();
        return true;
    }

}
