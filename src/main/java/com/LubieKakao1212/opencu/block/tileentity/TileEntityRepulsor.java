package com.LubieKakao1212.opencu.block.tileentity;

import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.opencu.pulse.EntityPulse;
import com.LubieKakao1212.opencu.pulse.RepulsorPulse;
import com.LubieKakao1212.opencu.pulse.VectorPulse;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Supplier;

public class TileEntityRepulsor extends TileEntity implements Environment, ITickable {

    private static final Supplier<EntityPulse>[] pulseFactories = new Supplier[] { RepulsorPulse::new, VectorPulse::new };

    private final ComponentConnector node;
    private EntityPulse pulse;
    private final ArrayList<String> filter = new ArrayList<>();
    private int pulseType;

    public int pulseTicksLeft;
    public static final int pulseTicks = 10;

    public TileEntityRepulsor() {
        setPulse(0);
        node = API.network.newNode(this, Visibility.Network).withComponent("repulsor").withConnector().create();
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (node != null && node.network() == null) {
                API.network.joinOrCreateNetwork(this);
            }
            if (pulseTicksLeft > 0) {
                pulseTicksLeft--;
                IBlockState state = world.getBlockState(getPos());
                world.notifyBlockUpdate(getPos(), state, state, 2);
            }
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
    public void onMessage(Message message) {

    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(mode:number): true or false, string")
    public Object[] recalibrate(Context context, Arguments args) {
        int id = args.checkInteger(0);
        if(id < 0) {
            return new Object[] { false, "id cannot be negative" };
        }
        if(id > pulseFactories.length) {
            return new Object[] { false, "id to large" };
        }

        setPulse(id);
        return new Object[] { true };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(radius:number): true or false, string")
    public Object[] setRadius(Context context, Arguments args) {
        double radius = args.checkDouble(0);
        if(radius > OpenCUConfig.repulsor.repulsorMaxRadius)
        {
            return new Object[]{ false, "Radius to large" };
        }
        pulse.setRadius(radius);
        return new Object[] { true };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(force:number): true or false, string")
    public Object[] setForce(Context context, Arguments args) {
        double force = args.checkDouble(0);
        if(force > 1.0)
        {
            return new Object[]{ false, "Force to large" };
        }
        pulse.setBaseForce(force);
        return new Object[]{ true };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(x:number, y:number, z:number): true")
    public Object[] setVector(Context context, Arguments args) {
        double x = args.checkInteger(0);
        double y = args.checkInteger(1);
        double z = args.checkInteger(2);
        pulse.setVector(x, y, z);
        return new Object[] { true };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(whitelist:boolean): true")
    public Object[] setWhitelist(Context context, Arguments args) {
        pulse.setWhitelist(args.checkBoolean(0));
        return new Object[] { true };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(name:string): true")
    public Object[] addToFilter(Context context, Arguments args) {
        filter.add(args.checkString(0));
        return new Object[] { true };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(name:string): true")
    public Object[] removeFromFilter(Context context, Arguments args) {
        filter.remove(args.checkString(0));
        return new Object[] { true };
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(): ...")
    public Object[] getFilter(Context context, Arguments args)
    {
        return filter.toArray();
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(x:number, y:number, z:number): true or false, string", limit = 1)
    public Object[] pulse(Context context, Arguments args) {
        double x1 = args.checkDouble(0);
        double y1 = args.checkDouble(1);
        double z1 = args.checkDouble(2);
        double distanceSqr = x1*x1 + y1*y1 + z1*z1;

        if(distanceSqr > OpenCUConfig.repulsor.repulsorMaxOffset * OpenCUConfig.repulsor.repulsorMaxOffset)
        {
            return new Object[]{ false, "Offset to large" };
        }
        pulse.lock(this.world, x1 + pos.getX() + 0.5, y1 + pos.getY() + 0.5, z1 + pos.getZ() + 0.5);
        pulse.filter(filter);
        double radiusRatio = pulse.getRadius() * pulse.getRadius() * pulse.getRadius() / (OpenCUConfig.repulsor.repulsorMaxRadius * OpenCUConfig.repulsor.repulsorMaxRadius * OpenCUConfig.repulsor.repulsorMaxRadius);
        double forceRatio = pulse.getBaseForce() / OpenCUConfig.repulsor.repulsorForceScale;
        double distanceRatio = Math.sqrt(distanceSqr) / OpenCUConfig.repulsor.repulsorDistanceCost;
        int energyUsage = MathHelper.floor(distanceRatio * OpenCUConfig.repulsor.repulsorDistanceCost + radiusRatio * OpenCUConfig.repulsor.repulsorVolumeCost + (Math.pow(2, Math.abs(forceRatio))-1.0) * OpenCUConfig.repulsor.repulsorForceCost);
        if(!node.tryChangeBuffer(-energyUsage))//if(energyBuffer.getEnergyStored() < energyUsage)
        {
            return new Object[]{ false, "Not enough energy stored!!!", energyUsage };
        }
        pulse.execute();
        //context.pause(0.1);
        pulseTicksLeft = pulseTicks;

        markDirty();

        IBlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 2);
        return new Object[] { true };
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound nodeTag = new NBTTagCompound();
        node.save(nodeTag);
        compound.setTag("node", nodeTag);
        NBTTagCompound pulseTag = new NBTTagCompound();
        pulseTag.setInteger("type", pulseType);
        compound.setTag("pulse", pulse.writeToNBT(pulseTag));
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if(compound.hasKey("node", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound nodeTag = compound.getCompoundTag("node");
            node.load(nodeTag);
        }

        if(compound.hasKey("pulse", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound pulseTag = compound.getCompoundTag("pulse");
            setPulse(pulseTag.getInteger("type"));
            pulse.readFromNBT(pulseTag);
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return super.getCapability(capability, facing);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("anim", pulseTicksLeft);
        return new SPacketUpdateTileEntity(pos, 0, data);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        pulseTicksLeft = pkt.getNbtCompound().getInteger("anim");
    }

    public void setPulse(int type) {
        pulse = pulseFactories[type].get();
        this.pulseType = type;
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }
}