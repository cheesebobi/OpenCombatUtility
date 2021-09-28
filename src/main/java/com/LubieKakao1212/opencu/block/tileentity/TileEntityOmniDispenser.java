package com.LubieKakao1212.opencu.block.tileentity;

import com.LubieKakao1212.opencu.capability.dispenser.DispenseResult;
import com.LubieKakao1212.opencu.capability.dispenser.DispenserCapability;
import com.LubieKakao1212.opencu.capability.dispenser.IDispenser;
import com.LubieKakao1212.opencu.lib.math.AimUtil;
import com.LubieKakao1212.opencu.lib.math.MathUtil;
import com.LubieKakao1212.opencu.lib.util.GlmMath;
import com.LubieKakao1212.opencu.lib.util.GlmNBT;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserAimPacket;
import com.LubieKakao1212.opencu.network.packet.dispenser.UpdateDispenserPacket;
import glm.Glm;
import glm.quat.Quat;
import glm.vec._2.Vec2;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TileEntityOmniDispenser extends TileEntity implements Environment, ITickable {

    private final ConcurrentLinkedQueue<DispenseAction> actionQueue = new ConcurrentLinkedQueue<>();
    private final ComponentConnector node;
    private final ItemStackHandler inventory;
    private DispenseAction currentAction;
    private Quat targetAim;

    @SideOnly(Side.CLIENT)
    private ItemStack currentDispenserItem = null;
    @SideOnly(Side.CLIENT)
    private DispenseAction lastAction = null;

    private IDispenser currentDispenser;

    public TileEntityOmniDispenser() {
        currentAction = new DispenseAction(new Quat());
        targetAim = new Quat();

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

        node = API.network.newNode(this, Visibility.Network).withComponent("omnidispenser").withConnector().create();
    }

    private void updateDispenser() {
        ItemStack dispenserStack = inventory.getStackInSlot(0);
        currentDispenser = dispenserStack.getCapability(DispenserCapability.DISPENSER_CAPABILITY, null);
        NetworkHandler.sendToAllTracking(new UpdateDispenserPacket(pos, dispenserStack), pos, world.provider.getDimension());
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
            if (node != null && node.network() == null) {
                API.network.joinOrCreateNetwork(this);
            }
            if(currentDispenser != null) {
                currentAction.aim = GlmMath.step(currentAction.aim, targetAim, currentDispenser.getAlignmentSpeed());
            }
            NetworkHandler.sendToAllTracking(new UpdateDispenserAimPacket(pos, currentAction.aim), pos, world.provider.getDimension());
            while(actionQueue.size() > 0)
            {
                shoot(actionQueue.poll());
            }
        }
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(): true or false, string", direct = true, limit = 3)
    public Object[] dispense(Context context, Arguments args) {
        actionQueue.add(currentAction);
        return new Object[] { };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(yaw:number, pitch:number)", direct = false, limit = 1)
    public Object[] aim(Context context, Arguments args) {
        double yaw = args.checkDouble(0);
        double pitch = args.checkDouble(1);

        setAim(AimUtil.aimRad((float)MathUtil.loop(pitch, -MathUtil.piHalf, MathUtil.piHalf), (float)MathUtil.loop(yaw, -MathUtil.pi, MathUtil.pi)));
        return new Object[0];
    }

    public void setAim(Quat aim) {
        targetAim = aim;
    }

    public void shoot(DispenseAction action) {
        if(currentDispenser != null) {
            Tuple<ItemStack, Integer> ammo = findAmmo();
            if(ammo != null) {
                DispenseResult result = currentDispenser.Shoot(node, world, ammo.getFirst(), pos, action.aim);
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

        compound.setTag("aim", GlmNBT.writeQuat(currentAction.aim));

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
            setAim(GlmNBT.readQuat(compound.getTagList("aim", Constants.NBT.TAG_ANY_NUMERIC)));
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

    @SideOnly(Side.CLIENT)
    public ItemStack getCurrentDispenserItem() {
        return currentDispenserItem;
    }

    @SideOnly(Side.CLIENT)
    public DispenseAction getCurrentAction() {
        return currentAction;
    }

    @SideOnly(Side.CLIENT)
    public DispenseAction getLastAction() {
        return lastAction;
    }

    @SideOnly(Side.CLIENT)
    public void setCurrentAction(Quat newAction) {
        this.lastAction = currentAction;
        this.currentAction = new DispenseAction(newAction);
    }

    @SideOnly(Side.CLIENT)
    public void setCurrentDispenserItem(ItemStack currentDispenserItem) {
        this.currentDispenserItem = currentDispenserItem;
    }

    public class DispenseAction {
        private Quat aim;

        public DispenseAction(Quat aim)
        {
            this.aim = aim;
        }

        public Quat aim() {
            return aim;
        }

    }
}
