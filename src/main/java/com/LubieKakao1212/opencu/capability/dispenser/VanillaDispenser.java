package com.LubieKakao1212.opencu.capability.dispenser;

import com.LubieKakao1212.opencu.lib.math.AimUtil;
import com.LubieKakao1212.opencu.lib.math.MathUtil;
import glm.quat.Quat;
import glm.vec._3.Vec3;
import li.cil.oc.api.network.Connector;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VanillaDispenser implements IDispenser {

    public static final double spread = 4 * MathUtil.degToRad;
    public static final double defaultForce = 1f;

    @Override
    public DispenseResult Shoot(Connector context, World world, ItemStack shotItem, BlockPos pos, Quat aim) {
        DispenseEntry entry = DispenserRegistry.DISPENSER.getDispenseResult(shotItem, world);
        Entity entity = entry.getEntity();

        Vec3 forward = AimUtil.calculateForwardWithSpread(aim, (float)(spread * entry.getSpreadMultiplier()));

        entity.setPosition(pos.getX() + 0.5 + forward.x, pos.getY() + 0.5 + forward.y, pos.getZ() + 0.5 + forward.z);

        double velocity = defaultForce / entry.getMass();

        entity.motionX = forward.x * velocity;
        entity.motionY = forward.y * velocity;
        entity.motionZ = forward.z * velocity;

        world.spawnEntity(entity);

        return new DispenseResult(entry.getEnergyMultiplier(), entry.getLeftover());
    }

    @Override
    public String trySetSpread(double spread) {
        return "Current dispenser does not support variable spread.";
    }

    @Override
    public String trySetForce(double force) {
        return "Current dispenser does not support variable force.";
    }

    @Override
    public float getBaseEnergyRequired() {
        return 0;
    }

    @Override
    public float getAlignmentSpeed() {
        return MathUtil.degToRad * 1;
    }

}
