package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.ShooterDeviceState;
import com.lubiekakao1212.qulib.math.Aim;
import com.lubiekakao1212.qulib.math.AimUtilKt;
import com.lubiekakao1212.qulib.math.Constants;
import com.lubiekakao1212.qulib.math.extensions.Vector3dExtensions;
import com.lubiekakao1212.qulib.random.RandomEx;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.joml.Vector3d;

public abstract class ShooterBase implements IFramedDevice {

    private static final RandomEx random = new RandomEx();

    private final ShotMappings mappings;
    private final double alignmentSpeed;

    public ShooterBase(ShotMappings mappings, double alignmentSpeed) {
        this.mappings = mappings;
        this.alignmentSpeed = alignmentSpeed;
    }

    @Override
    public void activate(BlockEntityModularFrame frame, IDeviceState stateIn, World world, BlockPos pos, Aim aim, BlockEntityModularFrame.ModularFrameContext ctx) {
        var shotItem = ctx.ammo().useAmmoFirst(ctx.ctx());

        //TODO proper empty item handling
        if(shotItem == null || shotItem.isEmpty()) {
            return;
        }

        ShotEntry entry = getMappings().getShotResult(shotItem);

        var state = (ShooterDeviceState) stateIn;

        var energyUsage = (long)(state.getBaseEnergyUsage() * entry.getEnergyMultiplier());
        if(ctx.energy().useEnergy(energyUsage, ctx.ctx()) == energyUsage) {
            //region Shooting
            Entity entity = entry.getEntity(shotItem, world, state);

            Vector3d forward = AimUtilKt.randomSpread(random, aim.toQuaternion(Direction.EAST, Direction.UP), (state.getSpread() * entry.getSpreadMultiplier() * Constants.degToRad), Vector3dExtensions.INSTANCE.getSOUTH());

            entity.setPosition(pos.getX() + 0.5 + forward.x, pos.getY() + 0.5 + forward.y, pos.getZ() + 0.5 + forward.z);
            entity.setPitch(0f);
            entity.setYaw(0f);

            double velocity = state.getForce() / entry.getMass();

            entity.setVelocity(forward.x * velocity, forward.y * velocity, forward.z * velocity);

            entry.getPostShootAction().Execute(entity, forward, velocity, state);
            entity.velocityDirty = true;

            world.spawnEntity(entity);

            entry.getPostSpawnAction().Execute(entity, forward, velocity, state);
            //endregion

            ctx.ctx().commit();
        }
    }

    @Override
    public void tick(BlockEntityModularFrame frame, IDeviceState state, World world, BlockPos pos, Aim aim, BlockEntityModularFrame.ModularFrameContext ctx) {

    }

    @Override
    public double getPitchAlignmentSpeed() {
        return alignmentSpeed;
    }

    @Override
    public double getYawAlignmentSpeed() {
        return alignmentSpeed;
    }

    protected ShotMappings getMappings() {
        return this.mappings;
    }
}

