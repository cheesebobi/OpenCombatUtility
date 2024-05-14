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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.joml.Vector3d;

public abstract class ShooterBase implements IFramedDevice {

    private static final RandomEx random = new RandomEx();

    private final DispenserMappings mappings;
    private final double alignmentSpeed;

    public ShooterBase(DispenserMappings mappings, double alignmentSpeed) {
        this.mappings = mappings;
        this.alignmentSpeed = alignmentSpeed;
    }

    @Override
    public DispenseResult activate(BlockEntityModularFrame shooter, IDeviceState stateIn, World level, ItemStack shotItem, BlockPos pos, Aim aim) {
        DispenseEntry entry = getMappings().getDispenseResult(shotItem);

        DispenseResult result;// = new DispenseResult(shotItem);

        var state = (ShooterDeviceState) stateIn;

        //TODO energy
        //shooter.getCapability(ForgeCapabilities.ENERGY).ifPresent((energy) -> {

            //region Energy Handling
            /*
            if(!(energy instanceof InternalEnergyStorage)) {
                return;
            }
            InternalEnergyStorage energyStorage = (InternalEnergyStorage) energy;

            int energyRequired = (int)(entry.getEnergyMultiplier() * energyConsumption);

            if(energyStorage.getEnergyStored() < energyRequired) {
                return;
            }

            energyStorage.extractEnergyInternal(energyRequired, false);
            */
            //endregion


            //region Shooting
            Entity entity = entry.getEntity(shotItem, level);

            Vector3d forward = AimUtilKt.randomSpread(random, aim.toQuaternion(Direction.EAST, Direction.UP), (state.getSpread() * entry.getSpreadMultiplier() * Constants.degToRad), Vector3dExtensions.INSTANCE.getSOUTH());

            entity.setPosition(pos.getX() + 0.5 + forward.x, pos.getY() + 0.5 + forward.y, pos.getZ() + 0.5 + forward.z);
            entity.setPitch(0f);
            entity.setYaw(0f);

            double velocity = state.getForce() / entry.getMass();

            entity.setVelocity(forward.x * velocity, forward.y * velocity, forward.z * velocity);

            entry.getPostShootAction().Execute(entity, forward, velocity);

            level.spawnEntity(entity);

            entry.getPostSpawnAction().Execute(entity, forward, velocity);

            result = new DispenseResult(true, entry.getLeftover());//.leftover = entry.getLeftover();
            //endregion
        //});

        return result;
    }

    @Override
    public double getPitchAlignmentSpeed() {
        return alignmentSpeed;
    }

    @Override
    public double getYawAlignmentSpeed() {
        return alignmentSpeed;
    }

    protected DispenserMappings getMappings() {
        return this.mappings;
    }
}

