package com.LubieKakao1212.opencu.common.block.entity;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.gui.container.ModularFrameMenu;
import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import com.LubieKakao1212.opencu.common.transaction.IAmmoContext;
import com.LubieKakao1212.opencu.common.transaction.IEnergyContext;
import com.LubieKakao1212.opencu.common.transaction.ILeftoverItemContext;
import com.LubieKakao1212.opencu.common.transaction.IScopedContext;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketServerRequestDispenserUpdate;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenserAim;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenser;
import com.LubieKakao1212.opencu.PlatformUtil;
import com.lubiekakao1212.qulib.math.Aim;
import com.lubiekakao1212.qulib.math.Constants;
import com.lubiekakao1212.qulib.math.MathUtilKt;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BlockEntityModularFrame extends BlockEntity implements NamedScreenHandlerFactory {

    public static final double aimIdenticalityEpsilon = Constants.degToRad * 0.1;

    private final AtomicInteger actionsToPerform = new AtomicInteger(0);

    private Aim currentAim;
    private Aim targetAim;
    private boolean lockedOn;
    //Client
    private Aim lastAim = null;

    private IFramedDevice currentDevice;
    private IDeviceState currentDeviceState;
    //Client
    private ItemStack currentDeviceItem = null;

    private long initialiseDelay = 3;

    private boolean isInitialised = false;

    //Client
    public long clientAge;
    //Client
    public long clientPrevFrameAge;
    //Client
    public float clientPrevFramePartialTick;

    //Client
    public double deltaAnglePitch;
    //Client
    public double deltaAngleYaw;

    public BlockEntityModularFrame(BlockPos pos, BlockState blockState) {
        super(CUBlockEntities.modularFrame(), pos, blockState);
        lastAim = new Aim(0, 0);
        setCurrentAim(new Aim(0, 0));

        targetAim = new Aim(0 ,0);
    }

    protected void updateDispenser() {
        ItemStack dispenserStack = getCurrentDeviceItem();
        currentDevice = PlatformUtil.getDispenser(dispenserStack);
        if(currentDevice != null) {
            currentDeviceState = currentDevice.getNewState();
        } else {
            currentDeviceState = null;
        }

        if(world != null && !world.isClient) {
            BlockPos pos = getPos();
            NetworkUtil.sendToAllTracking(new PacketClientUpdateDispenser(pos, dispenserStack), (ServerWorld) world, pos);
        } else {
            //TODO Mark for update
        }
    }

    private void sendDispenserAimUpdate() {
        NetworkUtil.sendToAllTracking(
                PacketClientUpdateDispenserAim.create(pos, currentAim, false),
                (ServerWorld) world, pos);
    }

    public static <T> void tick(World world, BlockPos pos, BlockState state, T blockEntity) {
        BlockEntityModularFrame be = (BlockEntityModularFrame)blockEntity;
        if (!world.isClient) {
            if(be.initialiseDelay-- == 0) {
                be.updateDispenser();
                be.sendDispenserAimUpdate();
            }
            if(be.currentDevice != null) {
                var device = be.currentDevice;
                var deviceState = be.currentDeviceState;
                var currentAim = be.currentAim;
                var targetAim = be.targetAim;
                if(!currentAim.equals(targetAim, aimIdenticalityEpsilon)) {
                    currentAim.stepPerAxis(targetAim,
                            device.getPitchAlignmentSpeed() * Constants.degToRad,
                            device.getYawAlignmentSpeed() * Constants.degToRad,
                            be.currentAim);

                    be.sendDispenserAimUpdate();
                    be.markDirty();
                } else {
                    be.setTargetAim(new Aim(targetAim.getPitch(), targetAim.getYaw()));
                    if(!be.lockedOn)
                    {
                        be.markDirty();
                        be.sendDispenserAimUpdate();
                        be.lockedOn = true;
                    }
                }

                try(var ctx = be.getNewContext()) {
                    device.tick(be, deviceState, world, pos, currentAim, ctx);
                }
            }
            while(be.actionsToPerform.get() > 0)
            {
                be.actionsToPerform.getAndDecrement();
                be.shoot(be.currentAim);
            }
        }else
        {
            if(!be.isInitialised) {
                be.isInitialised = true;
                be.setCurrentAim(be.targetAim);
                be.requestDispenserUpdate();
            }
            be.clientAge++;
        }
    }

    public void dispense() {
        actionsToPerform.getAndIncrement();
    }

    public void aim(double pitch, double yaw) {
        setTargetAim(new Aim(pitch, yaw));
    }

    public void aimAt(Vector3d relativePos) {
        relativePos.negate();
        var yaw = Math.atan2(relativePos.x, -relativePos.z);
        var pitch = Math.atan2(-relativePos.y, new Vector2d(relativePos.x, relativePos.z).length());

        setTargetAim(new Aim(pitch, yaw));
    }

    public void aimAtWorld(Vector3d worldPos) {
        aimAt(worldPos.sub(new Vector3m(pos.toCenterPos())));
    }

    public boolean isAligned() {
        return targetAim.equals(currentAim, aimIdenticalityEpsilon);
    }

    public void setTargetAim(Aim aim) {
        targetAim = aim;
        markDirty();
    }

    private void shoot(Aim aim) {
        assert world != null;
        if(currentDevice != null) {
            try(ModularFrameContext ctx = getNewContext()) {
                currentDevice.activate(this, currentDeviceState, world, pos, aim, ctx);
            }
        }
    }

    protected abstract ModularFrameContext getNewContext();

    /**
     * Creates a slot for gui
     * @param idx slot index 0 => device; 1-9 => ammo
     */
    public abstract Slot createSlot(int idx, int x, int y);

    public boolean isUsableBy(PlayerEntity player) {
        assert world != null;
        return player.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) <= 64D && world.getBlockEntity(pos) == this;
    }

    @Override
    public void writeNbt(@NotNull NbtCompound compound) {
        compound.putDouble("pitch", currentAim.getPitch());
        compound.putDouble("yaw", currentAim.getYaw());
        compound.putDouble("targetPitch", targetAim.getYaw());
        compound.putDouble("targetYaw", targetAim.getYaw());

        compound.put("dispenser", currentDevice != null ? currentDevice.getNewState().serialize() : new NbtCompound());

        super.writeNbt(compound);
    }

    @Override
    public void readNbt(@NotNull NbtCompound compound) {
        super.readNbt(compound);

        var pitch = compound.getDouble("pitch");
        var yaw = compound.getDouble("yaw");
        var targetPitch = compound.getDouble("targetPitch");
        var targetYaw = compound.getDouble("targetYaw");

        setTargetAim(new Aim(targetPitch, targetYaw));
        currentAim = new Aim(pitch, yaw);

        if(currentDevice != null && compound.contains("dispenser", NbtElement.COMPOUND_TYPE)) {
            currentDevice.getNewState().deserialize(compound.getCompound("dispenser"));
        }
    }

    public void sendDispenserUpdateTo(ServerPlayerEntity player) {
        NetworkUtil.sendToPlayer(new PacketClientUpdateDispenser(pos, getCurrentDeviceItem()), player);
        NetworkUtil.sendToPlayer(PacketClientUpdateDispenserAim.create(pos, currentAim, true), player);
    }

    @Override
    public Text getDisplayName() {
        //TODO Translation
        return Text.of("dispenser");
    }

    @Override
    public ScreenHandler createMenu(int containerId, @NotNull PlayerInventory inventory, @NotNull PlayerEntity player) {
        assert world != null;
        return new ModularFrameMenu(containerId, inventory, this::createSlot, ScreenHandlerContext.create(world, pos));
    }

    public ItemStack getCurrentDeviceItem() {
        assert this.world != null;
        if(this.world.isClient) {
            return currentDeviceItem;
        }
        return getCurrentDeviceItemServer();
    }

    public IDeviceApi getCurrentDeviceApi() {
        return currentDevice.getNewState().getApi();
    }

    protected abstract ItemStack getCurrentDeviceItemServer();

    //region Clinet Methods

    /**
     * Client method
     */
    public Aim getCurrentAim() {
        return currentAim;
    }

    /**
     * Client method
     */
    public Aim getLastAim() {
        return lastAim;
    }

    /**
     * Client method
     */
    public void setLastAim(Aim aim) {
        lastAim = aim;
    }

    /**
     * Client method
     */
    public void setCurrentAim(Aim newAim) {
        //this.lastAction = currentAction;
        this.currentAim = newAim;

        this.deltaAnglePitch = Math.abs(newAim.getPitch() - lastAim.getPitch());
        this.deltaAngleYaw = MathUtilKt.angleDistance(lastAim.getYaw(), newAim.getYaw());
        //QuaterniondExtensionsKt.smallAngle(lastAction.aim(), currentAction.aim());
    }

    /**
     * Client method
     */
    public void setCurrentDeviceItem(ItemStack currentDeviceItem) {
        this.currentDeviceItem = currentDeviceItem;
    }

    /**
     * Client method
     */
    public void requestDispenserUpdate() {
        NetworkUtil.sendToServer(new PacketServerRequestDispenserUpdate(pos));
    }
    //endregion

    public record ModularFrameContext(IScopedContext ctx, IEnergyContext energy, IAmmoContext ammo, ILeftoverItemContext leftover) implements AutoCloseable {

        @Override
        public void close() {
            ctx.close();
            leftover.close();
        }
    }
}
