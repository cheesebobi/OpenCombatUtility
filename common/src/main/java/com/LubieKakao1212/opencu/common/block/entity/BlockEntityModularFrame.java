package com.LubieKakao1212.opencu.common.block.entity;

import com.LubieKakao1212.opencu.common.dispenser.DispenseResult;
import com.LubieKakao1212.opencu.common.dispenser.IDispenser;
import com.LubieKakao1212.opencu.common.gui.container.ModularFrameMenu;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketServerRequestDispenserUpdate;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenserAim;
import com.LubieKakao1212.opencu.common.network.packet.dispenser.PacketClientUpdateDispenser;
import com.LubieKakao1212.opencu.common.registry.CUBlockEntities;
import com.LubieKakao1212.opencu.common.storage.IItemStorage;
import com.LubieKakao1212.opencu.common.util.PlatformUtil;
import com.lubiekakao1212.qulib.math.AimUtilKt;
import com.lubiekakao1212.qulib.math.Constants;
import com.lubiekakao1212.qulib.math.MathUtilKt;
import com.lubiekakao1212.qulib.math.extensions.QuaterniondExtensionsKt;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Pair;
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

    private final IItemStorage inventory;
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

    public BlockEntityModularFrame(BlockPos pos, BlockState blockState, @NotNull IItemStorage inventoryIn) {
        super(CUBlockEntities.modularFrame(), pos, blockState);
        currentAction = new DispenseAction(new Quaterniond());

        lastAction = new DispenseAction(new Quaterniond().identity());
        setCurrentAction(new Quaterniond());

        targetAim = new Quaterniond().identity();

        this.inventory = inventoryIn;

        /*inventory = new ItemStackHandler(10) {
            @Override
            protected void onContentsChanged(int slot) {
                if(slot == 0) {
                    updateDispenser();
                }
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(slot == 0)
                {
                    return stack.getCapability(DispenserCapability.DISPENSER_CAPABILITY).isPresent();
                }
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                if(slot == 0)
                {
                    return 1;
                }else {
                    return 64;
                }
            }
        };*/
        //inventoryCap = LazyOptional.of(() -> inventory);

        //energyCap = OpenCUConfigCommon.DISPENSER.energyConfig.createCapFromConfig();
    }

    private void updateDispenser() {
        ItemStack dispenserStack = inventory.getStack(0);
        currentDispenser = PlatformUtil.getDispenser(dispenserStack);//dispenserStack.getCapability(DispenserCapability.DISPENSER_CAPABILITY).resolve().orElseGet(() -> null);
        if(world != null && !world.isClient) {
            BlockPos pos = getPos();
            PlatformUtil.Network.sendToAllTracking(new PacketClientUpdateDispenser(pos, dispenserStack), world, pos);
            //NetworkHandler.sendToServer(new UpdateDispenserPacket.FromClient(pos, dispenserStack));
        } else
        {
            //TODO Mark for update
        }
    }

    private void sendDispenserAimUpdate() {
        PlatformUtil.Network.sendToAllTracking(
                PacketClientUpdateDispenserAim.create(pos, currentAction.aim, false),
                world, pos);
    }

    private void requestDispenserUpdate() {
        PlatformUtil.Network.sendToServer(new PacketServerRequestDispenserUpdate(pos));
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
        if(currentDispenser != null) {
            Pair<ItemStack, Integer> ammo = findAmmo();
            if(ammo != null) {
                DispenseResult result = currentDispenser.shoot(this, world, ammo.getLeft(), pos, action.aim);
                useAmmo(ammo.getRight(), result.getLeftover());
            }
        }
    }

    private Pair<ItemStack, Integer> findAmmo() {
        for(int i=1; i<10; i++) {
            ItemStack stack = inventory.extract(i, 1, true);
            if(!stack.isEmpty()) {
                return new Pair<>(stack, i);
            }
        }
        return null;
    }

    private void useAmmo(int slot, ItemStack leftover) {
        inventory.extract(slot, 1, false);

        if(!leftover.isEmpty()) {
            slot = -1;

            for (int i = 9; i > 0; i--) {
                if (inventory.getStack(i).isEmpty()) {
                    if(slot == -1)
                    {
                        slot = i;
                    }
                }else
                {
                    if(inventory.insert(i, leftover, false).isEmpty()) {
                        slot = -1;
                        return;
                    }
                }
            }

            if(slot > 0) {
                inventory.insert(slot, leftover, false);
            }else
            {
                assert world != null;
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), leftover);
            }
        }
    }

    public boolean isUsableBy(PlayerEntity player) {
        assert world != null;
        return player.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) <= 64D && world.getBlockEntity(pos) == this;
    }

    public IItemStorage getInventory() {
        return inventory;
    }

    @Override
    public void writeNbt(@NotNull NbtCompound compound) {
        //Inventory
        compound.put("inventory", inventory.serialize());

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
        NbtCompound inventoryTag = compound.getCompound("inventory");
        if(inventory != null) {
            inventory.deserialize(inventoryTag);
        }

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

    /*@Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if(capability == ForgeCapabilities.ITEM_HANDLER) {
            return (LazyOptional<T>)inventoryCap;
        }
        if(capability == ForgeCapabilities.ENERGY) {
            return (LazyOptional<T>)energyCap;
        }
        return super.getCapability(capability, facing);
    }*/

    public void sendDispenserUpdateTo(ServerPlayerEntity player) {
        PlatformUtil.Network.sendToPlayer(new PacketClientUpdateDispenser(pos, inventory.getStack(0)), player);
        PlatformUtil.Network.sendToPlayer(PacketClientUpdateDispenserAim.create(pos, currentAction.aim, true), player);
    }

    public ItemStack getCurrentDispenserItem() {
        return currentDispenserItem;
    }

    public DispenseAction getCurrentAction() {
        return currentAction;
    }

    public DispenseAction getLastAction() {
        return lastAction;
    }

    public void setLastAction(Quaterniond aim) {
        lastAction.aim = aim;
    }

    public void setCurrentAction(Quaterniond newAction) {
        //this.lastAction = currentAction;
        this.currentAction = new DispenseAction(newAction);
        this.deltaAngle = QuaterniondExtensionsKt.smallAngle(lastAction.aim(), currentAction.aim());
    }

    public void setCurrentDispenserItem(ItemStack currentDispenserItem) {
        this.currentDispenserItem = currentDispenserItem;
    }

    @Override
    public Text getDisplayName() {
        //TODO Translation
        return Text.of("dispenser");
    }

    @Override
    public ScreenHandler createMenu(int containerId, @NotNull PlayerInventory inventory, @NotNull PlayerEntity player) {
        assert world != null;
        return new ModularFrameMenu(containerId, inventory, world, getPos());
    }

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

}
