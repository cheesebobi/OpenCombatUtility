package com.LubieKakao1212.opencu.block.tileentity;

import com.LubieKakao1212.opencu.capability.dispenser.DispenseResult;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserCapability;
import com.LubieKakao1212.opencu.capability.dispenser.IDispenser;
import com.LubieKakao1212.opencu.lib.math.AimUtil;
import com.LubieKakao1212.opencu.lib.math.MathUtil;
import com.LubieKakao1212.opencu.lib.util.JomlNBT;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.LubieKakao1212.opencu.network.packet.dispenser.RequestDispenserUpdatePacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserAimPacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserPacket;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.joml.Quaterniond;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TileEntityOmniDispenser extends TileEntity implements Environment, ITickable {

    public static final double aimIdenticalityEpsilon = MathUtil.degToRad * 0.1;

    private final ConcurrentLinkedQueue<DispenseAction> actionQueue = new ConcurrentLinkedQueue<>();
    private final ComponentConnector node;
    private final ItemStackHandler inventory;
    private DispenseAction currentAction;
    private Quaterniond targetAim;

    private IDispenser currentDispenser;

    private long initialiseDelay = 3;

    private boolean isInitialised = false;

    private ItemStack currentDispenserItem = null;

    private DispenseAction lastAction = null;

    public TileEntityOmniDispenser() {
        currentAction = new DispenseAction(new Quaterniond());
        targetAim = new Quaterniond().identity();

        inventory = new ItemStackHandler(10) {
            @Override
            protected void onContentsChanged(int slot) {
                if(slot == 0) {
                    updateDispenser();
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(slot == 0)
                {
                    return stack.hasCapability(DispenserCapability.DISPENSER_CAPABILITY, null);
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
        };

        node = API.network.newNode(this, Visibility.Network).withComponent("adv_dispenser").withConnector().create();
    }

    private void updateDispenser() {
        ItemStack dispenserStack = inventory.getStackInSlot(0);
        currentDispenser = dispenserStack.getCapability(DispenserCapability.DISPENSER_CAPABILITY, null);
        if(world != null) {
            NetworkHandler.sendToAllTracking(new UpdateDispenserPacket(pos, dispenserStack), pos, world.provider.getDimension());
        }else
        {
            //TODO Mark for update
        }
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        if (node != null) node.remove();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (node != null) node.remove();
    }

    @Override
    public Node node() {
        return node;
    }

    @Override
    public void onConnect(Node node) {
        markDirty();
    }

    @Override
    public void onDisconnect(Node node) {
        markDirty();
    }

    @Override
    public void onMessage(Message message) { }

    @Override
    public void update() {
        if (!world.isRemote) {
            if(this.initialiseDelay-- == 0){
                updateDispenser();
                NetworkHandler.sendToAllTracking(new UpdateDispenserAimPacket(pos, currentAction.aim), pos, world.provider.getDimension());
            }
            if (node != null && node.network() == null) {
                API.network.joinOrCreateNetwork(this);
            }
            if(currentDispenser != null) {
                if(AimUtil.smallerAngle(targetAim, currentAction.aim) > aimIdenticalityEpsilon) {
                    AimUtil.step(currentAction.aim, targetAim, currentDispenser.getAlignmentSpeed() * MathUtil.degToRad);
                    NetworkHandler.sendToAllTracking(new UpdateDispenserAimPacket(pos, currentAction.aim), pos, world.provider.getDimension());
                } else {
                    currentAction.aim = targetAim;
                    if(!currentAction.lockedOn)
                    {
                        NetworkHandler.sendToAllTracking(new UpdateDispenserAimPacket(pos, currentAction.aim), pos, world.provider.getDimension());
                        currentAction.lockedOn = true;
                    }
                }
            }
            while(actionQueue.size() > 0)
            {
                actionQueue.poll();
                shoot(currentAction, 1f);
            }
        }else
        {
            if(!this.isInitialised) {
                this.isInitialised = true;
                this.setCurrentAction(targetAim);
                NetworkHandler.sendToServer(new RequestDispenserUpdatePacket(this.pos));
            }
        }
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(): true or false, string", direct = true)
    public Object[] dispense(Context context, Arguments args) {
        actionQueue.add(currentAction);
        return new Object[] { };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(yaw:number, pitch:number)", direct = true)
    public Object[] aim(Context context, Arguments args) {
        double yaw = args.checkDouble(0);
        double pitch = args.checkDouble(1);

        setAim(AimUtil.aimRad((float)MathUtil.loop(pitch, -MathUtil.piHalf, MathUtil.piHalf), (float)MathUtil.loop(yaw, -MathUtil.pi, MathUtil.pi)));
        return new Object[0];
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(): bool", direct = true)
    public Object[] isAligned(Context context, Arguments args) {
        return new Object[]{ AimUtil.smallerAngle(targetAim, currentAction.aim) < aimIdenticalityEpsilon};
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(): number", direct = true)
    public Object[] aimingStatus(Context context, Arguments args) {
        return new Object[]{ AimUtil.smallerAngle(targetAim, currentAction.aim) };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(force:number): true or false, string", direct = true)
    public Object[] setForce(Context context, Arguments args) {
        String message = currentDispenser.trySetForce(args.checkDouble(0));

        if(message != null) {
            return new Object[] { false, message };
        }
        else {
            return new Object[] { true };
        }
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(spread:number): true or false, string", direct = true)
    public Object[] setSpread(Context context, Arguments args) {
        String message = currentDispenser.trySetSpread(args.checkDouble(0));

        if(message != null) {
            return new Object[] { false, message };
        }
        else {
            return new Object[] { true };
        }
    }

    @SuppressWarnings("unused")
    @Callback
    public Object[] getMinSpread(Context context, Arguments args) {
        return new Object[] { currentDispenser.getMaxSpread() };
    }

    @SuppressWarnings("unused")
    @Callback
    public Object[] getMaxSpread(Context context, Arguments args) {
        return new Object[] { currentDispenser.getMaxSpread() };
    }

    public void setAim(Quaterniond aim) {
        targetAim = aim.normalize();
        currentAction.lockedOn = false;
    }

    public void shoot(DispenseAction action, double energyMultiplier) {
        if(currentDispenser != null) {
            Tuple<ItemStack, Integer> ammo = findAmmo();
            if(ammo != null) {
                DispenseResult result = currentDispenser.Shoot(node, world, ammo.getFirst(), pos, action.aim, energyMultiplier);
                useAmmo(ammo.getSecond(), result.getLeftover());
            }
        }
    }

    private Tuple<ItemStack, Integer> findAmmo() {
        for(int i=1; i<10; i++) {
            ItemStack stack = inventory.extractItem(i, 1, true);
            if(!stack.isEmpty()) {
                return new Tuple<>(stack, i);
            }
        }
        return null;
    }

    private void useAmmo(int slot, ItemStack leftover) {
        inventory.extractItem(slot, 1, false);

        if(!leftover.isEmpty()) {
            slot = -1;

            for (int i = 9; i > 0; i--) {
                if (inventory.getStackInSlot(i).isEmpty()) {
                    if(slot == -1)
                    {
                        slot = i;
                    }
                }else
                {
                    if(inventory.insertItem(i, leftover, false).isEmpty()) {
                        slot = -1;
                        return;
                    }
                }
            }

            if(slot > 0) {
                inventory.insertItem(slot, leftover, false);
            }else
            {
                Block.spawnAsEntity(world, pos, leftover);
            }
        }
    }

    public boolean isUsableBy(EntityPlayer player) {
        return player.getDistanceSq(pos) <= 64D && world.getTileEntity(pos) == this;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        //Node
        NBTTagCompound nodeTag = new NBTTagCompound();
        node.save(nodeTag);
        compound.setTag("node", nodeTag);

        //Inventory
        compound.setTag("inventory", inventory.serializeNBT());

        compound.setTag("aim", JomlNBT.writeQuaternion(currentAction.aim));

        compound.setTag("dispenser", currentDispenser != null ? currentDispenser.serializeNBT() : new NBTTagCompound());

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        //Node
        NBTTagCompound nodeTag = compound.getCompoundTag("node");
        if(nodeTag != null && node != null) {
            node.load(nodeTag);
        }

        //Inventory
        NBTTagCompound inventoryTag = compound.getCompoundTag("inventory");
        if(inventoryTag != null && inventory != null) {
            inventory.deserializeNBT(inventoryTag);
        }

        if(compound.hasKey("aim", Constants.NBT.TAG_LIST)) {
            setAim(JomlNBT.readQuaternion(compound.getTagList("aim", Constants.NBT.TAG_DOUBLE)));
        }

        if(currentDispenser != null && compound.hasKey("dispenser", Constants.NBT.TAG_COMPOUND)) {
            currentDispenser.deserializeNBT(compound.getCompoundTag("dispenser"));
        }
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T)inventory;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    public void SendDispenserUpdateTo(EntityPlayerMP player) {
        NetworkHandler.sendTo(player, new UpdateDispenserPacket(pos, inventory.getStackInSlot(0)));
        NetworkHandler.sendTo(player, new UpdateDispenserAimPacket(pos, currentAction.aim));
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

    public void setCurrentAction(Quaterniond newAction) {
        this.lastAction = currentAction;
        this.currentAction = new DispenseAction(newAction);
    }

    public void setCurrentDispenserItem(ItemStack currentDispenserItem) {
        this.currentDispenserItem = currentDispenserItem;
    }


    public class DispenseAction {
        private Quaterniond aim;

        private boolean lockedOn;

        public DispenseAction(Quaterniond aim)
        {
            this.aim = aim;
        }

        public Quaterniond aim() {
            return aim;
        }
    }
}
