package com.LubieKakao1212.opencu.capability.dispenser.vanilla;

import com.LubieKakao1212.opencu.capability.dispenser.*;
import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.opencu.lib.math.AimUtil;
import com.LubieKakao1212.opencu.lib.math.MathUtil;
import glm.quat.Quat;
import glm.vec._3.Vec3;
import li.cil.oc.api.network.Connector;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VanillaDispenser extends DispenserBase {

    public static final double spread = 5 * MathUtil.degToRad;
    public static final double defaultForce = 1f;

    @Override
    public double getSpread() {
        return spread;
    }

    @Override
    public double getForce() {
        return defaultForce;
    }

    @Override
    protected DispenserMappings getMappings() {
        return DispenserRegistry.VANILLA_DISPENSER;
    }

}
