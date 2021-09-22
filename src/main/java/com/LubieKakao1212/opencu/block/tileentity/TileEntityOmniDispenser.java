package com.LubieKakao1212.opencu.block.tileentity;

import com.LubieKakao1212.opencu.block.tileentity.renderer.Color;
import com.LubieKakao1212.opencu.config.OpenCUConfig;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.*;
import li.cil.oc.common.tileentity.traits.ComponentInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TileEntityOmniDispenser extends TileEntity implements Environment, ITickable, EnvironmentHost {

    private final ConcurrentLinkedQueue<DispenseAction> actionQueue = new ConcurrentLinkedQueue<>();
    private final ComponentConnector node;
    private final ItemStackHandler inventory;
    private DispenseAction lastAction;

    private PotionType debugPotion;

    public TileEntityOmniDispenser() {
        setAim(0,0);

        inventory = new ItemStackHandler(10) {
            @Override
            protected void onContentsChanged(int slot) {

            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(slot == 0)
                {
                    return ItemStack.areItemsEqual(stack, new ItemStack(Blocks.DISPENSER));
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

        node = API.network.newNode(this, Visibility.Network).withComponent("omnipenser").withConnector().create();
        int maxEnergyStored = MathHelper.floor(OpenCUConfig.repulsorDistanceCost
                + OpenCUConfig.repulsorVolumeCost
                + OpenCUConfig.repulsorForceCost);
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
            while(actionQueue.size() > 0)
            {
                shoot(actionQueue.poll());
            }
        }
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(): true or false, string", direct = true, limit = 3)
    public Object[] dispense(Context context, Arguments args) {
        actionQueue.add(lastAction);
        return new Object[] { };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(yaw:number, pitch:number)", direct = true, limit = 1)
    public Object[] aim(Context context, Arguments args) {
        double yaw = args.checkDouble(0);
        double pitch = args.checkDouble(1);

        setAim(yaw, pitch);
        return new Object[0];
    }

    @Callback()
    public Object[] color(Context context, Arguments args) {
        String pot = args.checkString(0);
        switch(pot) {
            case "r":
            case "red": {
                debugPotion = PotionTypes.HEALING;
                break;
            }
            case "g":
            case "green": {
                debugPotion = PotionTypes.LEAPING;
                break;
            }
            case "b":
            case "blue":{
                debugPotion = PotionTypes.NIGHT_VISION;
                break;
            }
            case "c":
            case "clear": {
                debugPotion = null;
                break;
            }
        }
        return new Object[0];
    }

    public void setAim(double yaw, double pitch) {
        double sy = Math.sin(yaw);
        double cy = Math.cos(yaw);
        double sp = Math.sin(pitch);
        double cp = Math.cos(pitch);

        double zNorm = cy * cp;
        double xNorm = sy * cp;
        double yNorm = sp;

        double max = Math.max(Math.abs(zNorm), Math.max(Math.abs(yNorm), Math.abs(xNorm)));

        double xOff = (xNorm / max) + 0.5;
        double yOff = (yNorm / max) + 0.5;
        double zOff = (zNorm / max) + 0.5;
        lastAction = new DispenseAction();

        lastAction.yaw = yaw;
        lastAction.pitch = pitch;

        lastAction.xNorm = xNorm;
        lastAction.yNorm = yNorm;
        lastAction.zNorm = zNorm;

        lastAction.xOff = xOff;
        lastAction.yOff = yOff;
        lastAction.zOff = zOff;
    }

    public void shoot(DispenseAction action) {
        EntityTippedArrow arrow = new EntityTippedArrow(world, this.getPos().getX()+action.xOff, this.getPos().getY()+action.yOff, this.getPos().getZ()+action.zOff);
        arrow.setVelocity(action.xNorm, action.yNorm, action.zNorm);
        arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;

        if(debugPotion != null) {
            ItemStack arrowStack = new ItemStack(Items.TIPPED_ARROW);
            PotionUtils.addPotionToItemStack(arrowStack, debugPotion);

            arrow.setPotionEffect(arrowStack);
        }else
        {
            ItemStack arrowStack = new ItemStack(Items.ARROW);
            arrow.setPotionEffect(arrowStack);
        }

        world.spawnEntity(arrow);
    }

    @Override
    public World world() {
        return world;
    }

    @Override
    public double xPosition() {
        return this.pos.getX()+0.5;
    }

    @Override
    public double yPosition() {
        return this.pos.getY()+0.5;
    }

    @Override
    public double zPosition() {
        return this.pos.getZ()+0.5;
    }

    @Override
    public void markChanged() {
        this.markDirty();
    }

    public boolean isUsableBy(EntityPlayer player) {
        return player.getDistanceSq(pos) <= 64D && world.getTileEntity(pos) == this;
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

    private class DispenseAction {
        private double yaw; //-pi - pi
        private double pitch; //-pi/2 - pi/2;
        private double xNorm;
        private double yNorm;
        private double zNorm;
        private double xOff;
        private double yOff;
        private double zOff;
    }
}
