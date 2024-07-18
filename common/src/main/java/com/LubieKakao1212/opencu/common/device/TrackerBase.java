package com.LubieKakao1212.opencu.common.device;

import com.LubieKakao1212.opencu.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.device.event.data.LookAtEvent;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.device.state.TrackerDeviceState;
import com.LubieKakao1212.opencu.common.util.EndBoolean;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import com.lubiekakao1212.qulib.math.Aim;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.stream.Collectors;

public class TrackerBase implements IFramedDevice {

    private final double defaultTrackingRange;
    private final double defaultEnergyPerTick;
    private final double defaultEnergyPerActiveConnectionPerTick;

    public TrackerBase(OpenCUConfigCommon.TrackerDeviceConfig config) {
        this(config.range(), config.energyPerTick(), config.energyPerActiveConnectionPerTick());
    }

    public TrackerBase(double defaultTrackingRange, double defaultEnergyPerTick, double defaultEnergyPerActiveConnectionPerTick) {
        this.defaultTrackingRange = defaultTrackingRange;
        this.defaultEnergyPerTick = defaultEnergyPerTick;
        this.defaultEnergyPerActiveConnectionPerTick = defaultEnergyPerActiveConnectionPerTick;
    }

    @Override
    public void activate(BlockEntityModularFrame frame, IDeviceState state, World world, BlockPos pos, Aim aim, BlockEntityModularFrame.ModularFrameContext ctx) {

    }

    @Override
    public void tick(BlockEntityModularFrame frame, IDeviceState state, World world, BlockPos pos, Aim aim, BlockEntityModularFrame.ModularFrameContext ctx) {
        var trackerState = (TrackerDeviceState) state;

        var rsPulser = trackerState.rsPulseTimer;

        var rsct = frame.getRedstoneControlTypeRaw();

        try (var pulse = new EndBoolean(rsPulser::tick)) {
            try (var rsout = new EndBoolean((value) ->
                    frame.setEmittingRedstone(
                            (value ^ rsct == RedstoneControlType.LOW) &&
                                    rsct != RedstoneControlType.DISABLED))) {
                ctx.ctx().push();
                if (!drainEnergy(ctx, trackerState, trackerState.getEnergyPerTick())) {
                    ctx.ctx().pop();
                    return;
                }
                ctx.ctx().pop();

                var ammo = ctx.ammo();
                var filters = ammo.availableAmmo().stream().map(ItemStack::getName).map(Text::getString).collect(Collectors.toSet());

                var range = trackerState.getTrackingRange();
                var rangeSq = range * range;

                var entities = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), new Box(pos).expand(trackerState.getTrackingRange()), (e) -> true);

                if (entities.isEmpty()) {
                    return;
                }

                var nearestDistSq = Double.POSITIVE_INFINITY;
                Entity nearestEntity = null;

                for (var entity : entities) {
                    var distSq = entity.getPos().squaredDistanceTo(pos.toCenterPos());
                    if (distSq > nearestDistSq) {
                        continue;
                    }
                    if (distSq > rangeSq) {
                        continue;
                    }
                    if (filters.contains(entity.getName().getString())) {
                        continue;
                    }
                    if (entity.isPlayer()) {
                        PlayerEntity player = (PlayerEntity) entity;
                        if (player.isCreative() || player.isSpectator()) {
                            continue;
                        }
                    }
                    if (entity.isDead()){
                        continue;
                    }

                    // Check if there's a block obstructing the view by reversing the raycast direction
                    Vec3d start = entity.getBoundingBox().getCenter();
                    Vec3d end = pos.toCenterPos();
                    BlockHitResult blockHitResult = world.raycast(new RaycastContext(
                            start,
                            end,
                            RaycastContext.ShapeType.COLLIDER,
                            RaycastContext.FluidHandling.NONE,
                            entity
                    ));

                    //Rather silly but it works
                    if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                        BlockPos hitPos = blockHitResult.getBlockPos();
                        var blockState = world.getBlockState(hitPos);
                        if (!blockState.getBlock().toString().contains("modular_frame")){
                            continue;
                        }
                    }

                    nearestDistSq = distSq;
                    nearestEntity = entity;
                }

                if (nearestEntity != null) {
                    var energyUsage = ((TrackerDeviceState) state).getEnergyPerActiveConnectionPerTick();

                    var eventDistributor = frame.getEventDistributor();

                    energyUsage *= eventDistributor.recipientCount();

                    ctx.ctx().push();
                    if (!drainEnergy(ctx, trackerState, energyUsage)) {
                        ctx.ctx().pop();
                        return;
                    }
                    ctx.ctx().pop();

                    rsout.result = rsct == RedstoneControlType.PULSE ? rsPulser.shouldActivate() : true;
                    pulse.result = true;

                    var target = new Vector3m(nearestEntity.getBoundingBox().getCenter().add(0, nearestEntity.getStandingEyeHeight()/2, 0));
                    frame.aimAtWorld(target);

                    var lookAt = new LookAtEvent(target, true);

                    eventDistributor.handleEvent(lookAt);

                    // Commit the outer context
                    ctx.ctx().commit();
                }
            }
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
        return new TrackerDeviceState(defaultTrackingRange, defaultEnergyPerTick, defaultEnergyPerActiveConnectionPerTick);
    }

    private boolean drainEnergy(BlockEntityModularFrame.ModularFrameContext ctx, TrackerDeviceState state, double amount) {
        state.energyLeftover += amount;

        var energyToUse = (long) state.energyLeftover;
        state.energyLeftover = state.energyLeftover - energyToUse;

        if (state.noEnergy && energyToUse == 0) {
            return false;
        }
        var used = ctx.energy().useEnergy(energyToUse, ctx.ctx());
        if (used != energyToUse) {
            state.noEnergy = true;
            return false;
        }
        else {
            state.noEnergy = false;
            ctx.ctx().commit();
            return true;
        }
    }

}