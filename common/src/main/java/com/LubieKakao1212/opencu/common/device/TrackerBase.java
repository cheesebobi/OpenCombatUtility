package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.TrackerDeviceState;
import com.lubiekakao1212.qulib.math.Aim;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.stream.Collectors;

public class TrackerBase implements IFramedDevice {

    /**
     * @param ctx used to fetch ammo, use energy, and add leftovers
     */
    @Override
    public void activate(BlockEntityModularFrame shooter, IDeviceState state, World world, BlockPos pos, Aim aim, BlockEntityModularFrame.ModularFrameContext ctx) {

    }

    @Override
    public void tick(BlockEntityModularFrame shooter, IDeviceState state, World world, BlockPos pos, Aim aim, BlockEntityModularFrame.ModularFrameContext ctx) {
        var trackerState = (TrackerDeviceState) state;

        trackerState.energyLeftover += trackerState.getEnergyPerTick();

        var energyToUse = (long)trackerState.energyLeftover;
        trackerState.energyLeftover = trackerState.energyLeftover - energyToUse;

        if(trackerState.noEnergy && energyToUse == 0) {
            return;
        }
        if(ctx.energy().useEnergy(energyToUse, ctx.ctx()) != energyToUse) {
            return;
        }

        ctx.ctx().commit();

        var ammo = ctx.ammo();
        var filters = ammo.availableAmmo().stream().map(ItemStack::getName).map(Text::getString).collect(Collectors.toSet());

        var range = trackerState.getTrackingRange();
        var rangeSq = range * range;

        var entities = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), new Box(pos).expand(trackerState.getTrackingRange()), (e) -> true);

        if(entities.isEmpty()) {
            return;
        }

        var nearestDistSq = Double.POSITIVE_INFINITY;
        Entity nearestEntity = null;

        for(var entity : entities) {
            var distSq = entity.getPos().squaredDistanceTo(pos.toCenterPos());
            if(distSq > nearestDistSq) {
                continue;
            }
            if(distSq > rangeSq){
                continue;
            }
            if(filters.contains(entity.getName().getString())) {
                continue;
            }

            nearestDistSq = distSq;
            nearestEntity = entity;
        }

        if(nearestEntity != null) {
            //var delta = new Vector3m(pos.toCenterPos().subtract(nearestEntity.getBoundingBox().getCenter()));
            shooter.aimAtWorld(new Vector3m(nearestEntity.getBoundingBox().getCenter()));
        }
    }

    @Override
    public double getPitchAlignmentSpeed() {
        return 180;
    }

    @Override
    public double getYawAlignmentSpeed() {
        return 180;
    }

    @Override
    public IDeviceState getNewState() {
        return new TrackerDeviceState(5, 0.75);
    }
}
