package com.LubieKakao1212.opencu.block.tileentity;

import com.LubieKakao1212.opencu.block.tileentity.renderer.Color;
import com.LubieKakao1212.opencu.config.OpenCUConfig;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.*;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TileEntityAngryDispenser extends TileEntity implements Environment, ITickable {

    private final ConcurrentLinkedQueue<DispenseAction> actionQueue = new ConcurrentLinkedQueue<>();
    private final ComponentConnector node;
    private final ItemStackHandler inventory;
    private DispenseAction lastAction;

    private PotionType debugPotion;

    public TileEntityAngryDispenser() {
        setAim(0,0);

        inventory = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {

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
    @Callback(doc = "function(): true or false, string", direct = true)
    public Object[] dispense(Context context, Arguments args) {
        actionQueue.add(lastAction);
        context.consumeCallBudget(1 / 3);
        return new Object[] { };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(yaw:number, pitch:number)", direct = true)
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
