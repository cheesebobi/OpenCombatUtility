package com.LubieKakao1212.opencu.common.block.entity;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.device.IFramedDevice;
import com.LubieKakao1212.opencu.common.device.event.*;
import com.LubieKakao1212.opencu.common.device.state.IDeviceState;
import com.LubieKakao1212.opencu.common.gui.container.ModularFrameMenu;
import com.LubieKakao1212.opencu.common.peripheral.device.IDeviceApi;
import com.LubieKakao1212.opencu.common.transaction.IAmmoContext;
import com.LubieKakao1212.opencu.common.transaction.IEnergyContext;
import com.LubieKakao1212.opencu.common.transaction.ILeftoverItemContext;
import com.LubieKakao1212.opencu.common.transaction.IScopedContext;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
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
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BlockEntityModularFrame extends BlockEntity implements NamedScreenHandlerFactory, IRedstoneControlled, IEventNode {

    public static final int screenPropertyCount = 5;
    public static final int xPropertyIndex = 0;
    public static final int yPropertyIndex = 1;
    public static final int zPropertyIndex = 2;
    public static final int requiresLockPropertyIndex = 3;
    public static final int redstoneControlPropertyIndex = 4;

    public static final double aimIdenticalityEpsilon = Constants.degToRad * 0.1;

    public static final int autoShootInterval = 10;

    private static final long lateInitServerDelay = 3;

    private final AtomicInteger actionsToPerform = new AtomicInteger(0);
    private boolean requiresLock;
    private RedstoneControlType redstoneControlType;
    private long redstoneActivationTimer = 0;
    private final Set<Direction> rsState = EnumSet.noneOf(Direction.class);

    //region Events
    private DistributingWorldEventNode eventDistributor;
    //endregion

    //region Aim
    private Aim currentAim;
    private Aim targetAim;
    private boolean lockedOn;
    //Client
    private Aim lastAim;
    //endregion

    //region Device
    private IFramedDevice currentDevice;
    private IDeviceState currentDeviceState;
    //Client
    private ItemStack currentDeviceItem = null;
    //endregion

    private long age = 0;

    //region Renderer
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
    //endregion

    private final PropertyDelegate screenProperties;

    public BlockEntityModularFrame(BlockPos pos, BlockState blockState) {
        super(CUBlockEntities.modularFrame(), pos, blockState);
        lastAim = new Aim(0, 0);
        setCurrentAim(new Aim(0, 0));

        targetAim = new Aim(0 ,0);
        requiresLock = false;
        redstoneControlType = RedstoneControlType.PULSE;

        eventDistributor = new DistributingWorldEventNode(pos);

        screenProperties = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case xPropertyIndex -> pos.getX();
                    case yPropertyIndex -> pos.getY();
                    case zPropertyIndex -> pos.getZ();
                    case requiresLockPropertyIndex -> requiresLock ? 1 : 0;
                    case redstoneControlPropertyIndex -> redstoneControlType.order;
                    default -> -1;
                };
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int size() {
                return screenPropertyCount;
            }
        };
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
            be.doInit(be::initServer);
            be.doAgedInit(be::lateInitServer, lateInitServerDelay);
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

            if(++be.redstoneActivationTimer % autoShootInterval == 0) {
                var power = world.isReceivingRedstonePower(pos);
                if(power && be.redstoneControlType == RedstoneControlType.HIGH) {
                    be.actionsToPerform.getAndIncrement();
                }
                else if(!power && be.redstoneControlType == RedstoneControlType.LOW) {
                    be.actionsToPerform.getAndIncrement();
                }
            }

            if(be.requiresLock && !be.isAligned()) {
                be.actionsToPerform.set(0);
            }
            else while(be.actionsToPerform.get() > 0) {
                be.actionsToPerform.getAndDecrement();
                be.shoot(be.currentAim);
            }
        }else
        {
            be.doInit(be::initClient);
            be.clientAge++;
        }
    }

    //region init
    public void doInit(Runnable init) {
        doAgedInit(init, 0);
    }

    public void doAgedInit(Runnable lateInit, long targetAge) {
        if(age++ == targetAge) {
            lateInit.run();
        }
    }

    public void initServer() {

    }

    public void lateInitServer() {
        updateDispenser();
        sendDispenserAimUpdate();
    }

    public void initClient() {
        setCurrentAim(targetAim);
        requestDispenserUpdate();
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        eventDistributor.setWorld(world);
    }

    //endregion

    @Override
    public void handleEvent(IEventData data) {
        if(data instanceof SetAimEvent event) {
            setTargetAim(event.aim);
        }
        else if(data instanceof LookAtEvent event) {
            if(event.isWorldSpace) {
                aimAtWorld(event.target);
            }
            else
            {
                aimAt(event.target);
            }
        }
        else {
            currentDevice.handleEvent(data);
        }
    }

    public DistributingWorldEventNode getEventDistributor() {
        return eventDistributor;
    }


    public void activate() {
        actionsToPerform.getAndIncrement();
    }

    public void pulseActivate(Direction direction, boolean state) {
        var lastState = rsState.contains(direction);

        if(state) {
            rsState.add(direction);
        }
        else {
            rsState.remove(direction);
        }

        if(redstoneControlType == RedstoneControlType.PULSE && !lastState && state) {
            activate();
        }
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
        aimAt(new Vector3d(worldPos).sub(new Vector3m(pos.toCenterPos())));
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

    public boolean isRequiresLock() {
        return requiresLock;
    }

    public void setRequiresLock(boolean requiresLock) {
        this.requiresLock = requiresLock;
    }

    public RedstoneControlType getRedstoneControlType() {
        return redstoneControlType;
    }

    @Override
    public void cycleRedstoneControl() {
        redstoneControlType = redstoneControlType.cycleNext();
    }

    public void setRedstoneControlType(RedstoneControlType type) {
        this.redstoneControlType = type;
    }

    @Override
    public void writeNbt(@NotNull NbtCompound compound) {
        compound.putDouble("pitch", currentAim.getPitch());
        compound.putDouble("yaw", currentAim.getYaw());
        compound.putDouble("targetPitch", targetAim.getYaw());
        compound.putDouble("targetYaw", targetAim.getYaw());
        compound.putBoolean("requiresLock", requiresLock);
        compound.putInt("redstoneControl", redstoneControlType.order);

        compound.put("distributor", eventDistributor.serialize());

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

        redstoneControlType = RedstoneControlType.fromIndex(compound.getInt("redstoneControl"));
        requiresLock = compound.getBoolean("requiresLock");

        eventDistributor.deserialize(compound.getList("distributor", NbtElement.COMPOUND_TYPE));

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
        return new ModularFrameMenu(containerId, inventory, this::createSlot, ScreenHandlerContext.create(world, pos), screenProperties);
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
