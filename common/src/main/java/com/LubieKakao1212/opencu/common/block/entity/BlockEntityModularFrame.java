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
import com.lubiekakao1212.qulib.math.AimUtilKt;
import com.lubiekakao1212.qulib.math.Constants;
import com.lubiekakao1212.qulib.math.MathUtilKt;
import com.lubiekakao1212.qulib.math.extensions.QuaterniondExtensionsKt;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;

import java.util.concurrent.ConcurrentLinkedQueue;

import static com.lubiekakao1212.qulib.math.extensions.QuaterniondExtensionsKt.smallAngle;

public abstract class BlockEntityModularFrame extends BlockEntity implements NamedScreenHandlerFactory {

    public static final double aimIdenticalityEpsilon = Constants.degToRad * 0.1;

    private final ConcurrentLinkedQueue<DispenseAction> actionQueue = new ConcurrentLinkedQueue<>();

    //private final IItemStorage inventory;
    //private final LazyOptional<ItemStackHandler> inventoryCap;
    //private final LazyOptional<InternalEnergyStorage> energyCap;

    private DispenseAction currentAction;
    private Quaterniond targetAim;

    private IDispenser currentDispenser;

    private long initialiseDelay = 3;

    private boolean isInitialised = false;

    private ItemStack currentDispenserItem = null;

    private DispenseAction lastAction = null;

    public long clientAge;
    public long clientPrevFrameAge;
    public float clientPrevFramePartialTick;
    public double deltaAngle;

    public BlockEntityModularFrame(BlockPos pos, BlockState blockState) {
        super(CUBlockEntities.modularFrame(), pos, blockState);
        currentAction = new DispenseAction(new Quaterniond());

        lastAction = new DispenseAction(new Quaterniond().identity());
        setCurrentAction(new Quaterniond());

        targetAim = new Quaterniond().identity();
        //energyCap = OpenCUConfigCommon.DISPENSER.energyConfig.createCapFromConfig();
    }

    protected void updateDispenser() {
        ItemStack dispenserStack = getCurrentDispenserItem();
        currentDispenser = PlatformUtil.getDispenser(dispenserStack);//dispenserStack.getCapability(DispenserCapability.DISPENSER_CAPABILITY).resolve().orElseGet(() -> null);
        if(world != null && !world.isClient) {
            BlockPos pos = getPos();
            NetworkUtil.sendToAllTracking(new PacketClientUpdateDispenser(pos, dispenserStack), (ServerWorld) world, pos);
            //NetworkHandler.sendToServer(new UpdateDispenserPacket.FromClient(position, dispenserStack));
        } else
        {
            //TODO Mark for update
        }
    }

    private void sendDispenserAimUpdate() {
        NetworkUtil.sendToAllTracking(
                PacketClientUpdateDispenserAim.create(pos, currentAction.aim, false),
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
                if(QuaterniondExtensionsKt.smallAngle(dispenser.targetAim, dispenser.currentAction.aim) > aimIdenticalityEpsilon) {
                    dispenser.currentAction.aim = QuaterniondExtensionsKt.step(
                            dispenser.currentAction.aim,
                            dispenser.targetAim,
                            dispenser.currentDispenser.getAlignmentSpeed() * Constants.degToRad, new Quaterniond());
                    dispenser.sendDispenserAimUpdate();
                } else {
                    dispenser.currentAction.aim = dispenser.targetAim;
                    if(!dispenser.currentAction.lockedOn)
                    {
                        dispenser.sendDispenserAimUpdate();
                        dispenser.currentAction.lockedOn = true;
                    }
                }
            }
            while(!dispenser.actionQueue.isEmpty())
            {
                dispenser.actionQueue.poll();
                dispenser.shoot(dispenser.currentAction);
            }
        }else
        {
            if(!dispenser.isInitialised) {
                dispenser.isInitialised = true;
                dispenser.setCurrentAction(dispenser.targetAim);
                dispenser.requestDispenserUpdate();
            }
            dispenser.clientAge++;
        }
    }

    public void dispense() {
        actionQueue.add(currentAction);
    }

    public void aim(double pitch, double yaw) {
        //TODO add pi to yaw if |pitch| > piHalf
        setAim(AimUtilKt.aimRad(new Quaterniond(), MathUtilKt.loop(pitch, -Constants.piHalf, Constants.piHalf), MathUtilKt.loop(yaw, -Constants.pi, Constants.pi), Direction.EAST, Direction.UP));
    }

    public boolean isAligned() {
        return smallAngle(targetAim, currentAction.aim) < aimIdenticalityEpsilon;
    }

    public double aimingStatus() {
        return smallAngle(targetAim, currentAction.aim());
    }

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

    public void setAim(Quaterniond aim) {
        targetAim = aim.normalize();
        currentAction.lockedOn = false;
    }

    private void shoot(DispenseAction action) {
        assert world != null;
        if(currentDispenser != null) {
            try(ActionContext ctx = getNewContext()) {
                var ammo = useAmmo(ctx);
                if (ammo != null) {
                    DispenseResult result = currentDispenser.shoot(this, world, ammo, pos, action.aim);
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
        //Inventory
        //compound.put("inventory", inventory.serialize());

        //Energy
        //energyCap.ifPresent(energyStorage -> compound.put("energy", energyStorage.serializeNBT()));

        compound.put("aim", QuaterniondExtensionsKt.serializeNBT(currentAction.aim));
        compound.put("dispenser", currentDispenser != null ? currentDispenser.serialize() : new NbtCompound());

        super.writeNbt(compound);
    }

    @Override
    public void readNbt(@NotNull NbtCompound compound) {
        super.readNbt(compound);

        //Inventory
        /*NbtCompound inventoryTag = compound.getCompound("inventory");
        if(inventory != null) {
            inventory.deserialize(inventoryTag);
        }*/

        //TODO Energy
        /*NbtElement energyTag = compound.get("energy");
        if(energyTag != null) {
            energyCap.ifPresent((energy) -> energy.deserializeNBT(energyTag));
        }*/

        if(compound.contains("aim", NbtElement.LIST_TYPE)) {
            setAim(QuaterniondExtensionsKt.deserializeNBT(new Quaterniond(), compound.getList("aim", NbtElement.DOUBLE_TYPE)));
        }

        if(currentDispenser != null && compound.contains("dispenser", NbtElement.COMPOUND_TYPE)) {
            currentDispenser.deserialize(compound.getCompound("dispenser"));
        }
    }

    public void sendDispenserUpdateTo(ServerPlayerEntity player) {
        NetworkUtil.sendToPlayer(new PacketClientUpdateDispenser(pos, getCurrentDispenserItem()), player);
        NetworkUtil.sendToPlayer(PacketClientUpdateDispenserAim.create(pos, currentAction.aim, true), player);
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
    public DispenseAction getCurrentAction() {
        return currentAction;
    }

    /**
     * Client method
     */
    public DispenseAction getLastAction() {
        return lastAction;
    }

    /**
     * Client method
     */
    public void setLastAction(Quaterniond aim) {
        lastAction.aim = aim;
    }

    /**
     * Client method
     */
    public void setCurrentAction(Quaterniond newAction) {
        //this.lastAction = currentAction;
        this.currentAction = new DispenseAction(newAction);
        this.deltaAngle = QuaterniondExtensionsKt.smallAngle(lastAction.aim(), currentAction.aim());
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

    public static class DispenseAction {
        private Quaterniond aim;

        private boolean lockedOn;

        public DispenseAction(Quaterniond aim) {
            this.aim = aim;
        }

        public Quaterniond aim() {
            return aim;
        }
    }

    public interface ActionContext extends AutoCloseable {
        @Override
        void close();

        void commit();
    }
}
