package com.LubieKakao1212.opencu.dependencies.cc.capability;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.entity.BlockEntityOmniDispenser;
import com.LubieKakao1212.opencu.init.CUBlocks;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DispenserPeripheral implements IPeripheral {

    public static final String TYPE = new ResourceLocation(OpenCUMod.MODID, CUBlocks.ID.OMNI_DISPENSER).toString();

    private BlockEntityOmniDispenser target;

    public DispenserPeripheral(BlockEntityOmniDispenser dispenser) {
        this.target = dispenser;
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
        if(other != null && other instanceof DispenserPeripheral) {
            DispenserPeripheral otherDispenser = ((DispenserPeripheral) other);
            return otherDispenser.target.getBlockPos().equals(target.getBlockPos());
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
    public final double aimingStatus() {
        return target.aimingStatus();
    }

    @LuaFunction
    public final Object[] setForce(double force) {
        String message = target.setForce(force);
        if(message != null) {
            return new Object[] { false, message };
        }
        else {
            return new Object[] { true };
        }
    }

    @LuaFunction
    public final Object[] setSpread(double force) {
        String message = target.setSpread(force);
        if(message != null) {
            return new Object[] { false, message };
        }
        else {
            return new Object[] { true };
        }
    }

    @LuaFunction
    public final double getMinSpread() {
        return target.getMinSpread();
    }

    @LuaFunction
    public final double getMaxSpread() {
        return target.getMaxSpread();
    }

    @LuaFunction
    public final boolean dispense() {
        target.dispense();
        return true;
    }

}
