package com.LubieKakao1212.opencu.dependencies.cc.capability;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.block.entity.BlockEntityRepulsor;
import com.LubieKakao1212.opencu.config.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.init.CUBlocks;
import com.LubieKakao1212.opencu.init.CUPulse;
import com.LubieKakao1212.opencu.pulse.EntityPulseType;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.HashMap;
import java.util.Map;

public class RepulsorPeripheral implements IPeripheral {

    public static final EntityPulseType[] defaultPulseTypes = new EntityPulseType[] { CUPulse.REPULSOR, CUPulse.VECTOR, CUPulse.STASIS };

    public static final String TYPE = new ResourceLocation(OpenCUMod.MODID, CUBlocks.ID.REPULSOR).toString();

    private BlockEntityRepulsor target;

    public RepulsorPeripheral(BlockEntityRepulsor repulsor) {
        this.target = repulsor;
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
        if(other instanceof RepulsorPeripheral) {
            RepulsorPeripheral otherRepulsor = ((RepulsorPeripheral) other);
            return otherRepulsor.getTargetRepulsor().getBlockPos().equals(getTargetRepulsor().getBlockPos());
        }
        return false;
    }

    /**
     * Get the object that this peripheral provides methods for. This will generally be the tile entity
     * or block, but may be an inventory, entity, etc...
     *
     * @return The object this peripheral targets
     */
    @Nullable
    @Override
    public Object getTarget() {
        return target;
    }

    public BlockEntityRepulsor getTargetRepulsor() {
        return target;
    }

    @LuaFunction
    public final Object[] recalibrate(String type) {
        try
        {
            target.setPulse(new ResourceLocation(type));
        }
        catch(RuntimeException e) {
            return new Object[] { false, e.getMessage() };
        }
        return new Object[] { true };
    }

    @LuaFunction
    public final Object[] recalibrateByIdx(int type) {
        if(type < 0) {
            return new Object[] { false, "id cannot be negative" };
        }

        if(type >= defaultPulseTypes.length) {
            return new Object[] { false, "id to large" };
        }

        target.setPulse(defaultPulseTypes[type]);
        return new Object[] { true };
    }

    @LuaFunction
    public final Object[] setRadius(double radius) {
        if(radius > OpenCUConfigCommon.REPULSOR.getMaxRadius())
        {
            return new Object[]{ false, "Radius to large" };
        }
        target.setRadius(radius);
        return new Object[] { true };
    }

    @LuaFunction
    public final MethodResult getConvertedObfuscatedNonFictionalInstrumentGuide() {
        return getConfig();
    }

    @LuaFunction
    public final MethodResult getConfig() {
        //TODO do stuff
        Map<String, Object> result = new HashMap<>();
        if(OpenCUMod.hasValkyrienSkies()) {
            result.put("canPushShips", OpenCUConfigCommon.REPULSOR.getAffectsVSShips());
        }
        return MethodResult.of(result);
    }

    @LuaFunction
    public final Object[] setForce(double force) {
        if(Math.abs(force) > 1.0)
        {
            return new Object[]{ false, "Force to large" };
        }
        target.setForce(force);
        return new Object[]{ true };
    }

    @LuaFunction
    public final boolean setVector(double x, double y, double z) {
        target.setVector(x, y, z);
        return true;
    }

    @LuaFunction(mainThread = true)
    public final Object[] pulse(double xOffset, double yOffset, double zOffset) {
        BlockEntityRepulsor.PulseExecutionResult result = target.pulse(new Vector3d(xOffset, yOffset, zOffset));

        if(result.wasSuccessfull) {
            return new Object[] {true};
        }else {
            return new Object[] {false, result.errorDescription};
        }
    }
}
