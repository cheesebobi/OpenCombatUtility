package com.LubieKakao1212.opencu.common.block.entity;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.dispenser.DispenseResult;
import com.LubieKakao1212.opencu.common.dispenser.IDispenser;
import com.LubieKakao1212.opencu.common.gui.container.ModularFrameMenu;
import com.LubieKakao1212.opencu.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketServerRequestDispenserUpdate;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenserAim;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenser;
import com.LubieKakao1212.opencu.PlatformUtil;
import com.lubiekakao1212.qulib.math.Aim;
import com.lubiekakao1212.qulib.math.Constants;
import com.lubiekakao1212.qulib.math.MathUtilKt;
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
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class BlockEntityModularFrame extends BlockEntity implements NamedScreenHandlerFactory {

    public static final double aimIdenticalityEpsilon = Constants.degToRad * 0.1;

    private final AtomicInteger actionsToPerform = new AtomicInteger(0);

    private Aim currentAim;
    private Aim targetAim;

    private IDispenser currentDispenser;

    private long initialiseDelay = 3;

    private boolean isInitialised = false;

    private ItemStack currentDispenserItem = null;

    private boolean lockedOn;

    private Aim lastAim = null;

    public long clientAge;
    public long clientPrevFrameAge;
    public float clientPrevFramePartialTick;

    public double deltaAnglePitch;
    public double deltaAngleYaw;

    public BlockEntityModularFrame(BlockPos pos, BlockState blockState) {
        super(CUBlockEntities.modularFrame(), pos, blockState);
        lastAim = new Aim(0, 0);
        setCurrentAim(new Aim(0, 0));

        targetAim = new Aim(0 ,0);
    }

    protected void updateDispenser() {
        ItemStack dispenserStack = getCurrentDispenserItem();
        currentDispenser = PlatformUtil.getDispenser(dispenserStack);
        if(world != null && !world.isClient) {
            BlockPos pos = getPos();
            NetworkUtil.sendToAllTracking(new PacketClientUpdateDispenser(pos, dispenserStack), (ServerWorld) world, pos);
        } else
        {
            //TODO Mark for update
        }
    }

    private void sendDispenserAimUpdate() {
        NetworkUtil.sendToAllTracking(
                PacketClientUpdateDispenserAim.create(pos, currentAim, false),
                (ServerWorld) world, pos);
    }

    public static <T> void tick(World level, BlockPos pos, BlockState state, T blockEntity) {
        BlockEntityModularFrame dispenser = (BlockEntityModularFrame)blockEntity;
        if (!level.isClient) {
            if(dispenser.initialiseDelay-- == 0) {
                dispenser.updateDispenser();
                dispenser.sendDispenserAimUpdate();
            }
            if(dispenser.currentDispenser != null) {
                if(!dispenser.currentAim.equals(dispenser.targetAim, aimIdenticalityEpsilon)) {
                    dispenser.currentAim.stepPerAxis(dispenser.targetAim,
                            dispenser.currentDispenser.getAlignmentSpeed() * Constants.degToRad * 0.25,
                            dispenser.currentDispenser.getAlignmentSpeed() * Constants.degToRad,
                            dispenser.currentAim);

                    dispenser.sendDispenserAimUpdate();
                    dispenser.markDirty();
                } else {
                    dispenser.setTargetAim(new Aim(dispenser.targetAim.getPitch(), dispenser.targetAim.getYaw()));
                    if(!dispenser.lockedOn)
                    {
                        dispenser.markDirty();
                        dispenser.sendDispenserAimUpdate();
                        dispenser.lockedOn = true;
                    }
                }
            }
            while(dispenser.actionsToPerform.get() > 0)
            {
                dispenser.actionsToPerform.getAndDecrement();
                dispenser.shoot(dispenser.currentAim);
            }
        }else
        {
            if(!dispenser.isInitialised) {
                dispenser.isInitialised = true;
                dispenser.setCurrentAim(dispenser.targetAim);
                dispenser.requestDispenserUpdate();
            }
            dispenser.clientAge++;
        }
    }

    public void dispense() {
        actionsToPerform.getAndIncrement();
    }

    public void aim(double pitch, double yaw) {
        setTargetAim(new Aim(pitch, yaw));
    }

    public boolean isAligned() {
        return targetAim.equals(currentAim, aimIdenticalityEpsilon);
    }

    /*public double aimingStatus() {
        return smallAngle(targetAim, currentAction.aim());
    }*/

    public String setForce(double force) {
        return currentDispenser.trySetForce(force);
    }

    public String setSpread(double spread) {
        return currentDispenser.trySetSpread(spread);
    }

    public double getMinSpread() {
        return currentDispenser.getMaxSpread();
    }

    public double getMaxSpread() {
        return currentDispenser.getMaxSpread();
    }

    public void setTargetAim(Aim aim) {
        targetAim = aim;
        markDirty();
    }

    private void shoot(Aim action) {
        assert world != null;
        if(currentDispenser != null) {
            try(ActionContext ctx = getNewContext()) {
                var ammo = useAmmo(ctx);
                if (ammo != null) {
                    DispenseResult result = currentDispenser.shoot(this, world, ammo, pos, action);
                    if(result.wasSuccessful()) {
                        var leftover = result.leftover();
                        if(!(leftover = handleLeftover(ctx, leftover)).isEmpty()){
                            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), leftover);
                        }
                        ctx.commit();
                    }
                }
            }
        }
    }

    protected abstract ActionContext getNewContext();
    protected abstract ItemStack useAmmo(ActionContext ctx);
    protected abstract ItemStack handleLeftover(ActionContext ctx, ItemStack leftover);

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
        compound.put("dispenser", currentDispenser != null ? currentDispenser.serialize() : new NbtCompound());

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

        if(currentDispenser != null && compound.contains("dispenser", NbtElement.COMPOUND_TYPE)) {
            currentDispenser.deserialize(compound.getCompound("dispenser"));
        }
    }

    public void sendDispenserUpdateTo(ServerPlayerEntity player) {
        NetworkUtil.sendToPlayer(new PacketClientUpdateDispenser(pos, getCurrentDispenserItem()), player);
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

    public ItemStack getCurrentDispenserItem() {
        assert this.world != null;
        if(this.world.isClient) {
            return currentDispenserItem;
        }
        return getCurrentDispenserItemServer();
    }

    protected abstract ItemStack getCurrentDispenserItemServer();

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
    public void setCurrentDispenserItem(ItemStack currentDispenserItem) {
        this.currentDispenserItem = currentDispenserItem;
    }

    /**
     * Client method
     */
    private void requestDispenserUpdate() {
        NetworkUtil.sendToServer(new PacketServerRequestDispenserUpdate(pos));
    }
    //endregion

    public interface ActionContext extends AutoCloseable {
        @Override
        void close();

        void commit();
    }
}
