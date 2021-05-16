package com.LubieKakao1212.opencu.block.tileentity;

import com.LubieKakao1212.opencu.config.OpenCUConfig;
import com.LubieKakao1212.opencu.energy.ReciveOnlyEnergyStorageWrapper;
import com.LubieKakao1212.opencu.pulse.RepulsorPulse;
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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityRepulsor extends TileEntity implements Environment, ITickable {

    private final Component node;
    private RepulsorPulse pulse;
    private final ArrayList<String> filter = new ArrayList<>();
    public EnergyStorage energyBuffer;
    public ReciveOnlyEnergyStorageWrapper energyWrapper;

    public int pulseTicksLeft;
    public static final int pulseTicks = 10;

    public TileEntityRepulsor() {
        pulse = new RepulsorPulse(0, 0);
        node = API.network.newNode(this, Visibility.Network).withComponent("repulsor").create();
        int maxEnergyStored = MathHelper.floor(OpenCUConfig.repulsorDistanceCost
                + OpenCUConfig.repulsorVolumeCost
                + OpenCUConfig.repulsorForceCost);

        energyBuffer = new EnergyStorage(maxEnergyStored, OpenCUConfig.repulsorMaxTransferRate, maxEnergyStored);
        energyWrapper = new ReciveOnlyEnergyStorageWrapper(energyBuffer);
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
        //node.connect(this.node);
        markDirty();
    }

    @Override
    public void onDisconnect(Node node) {
        //node.disconnect(this.node);
        markDirty();
    }

    @Override
    public void onMessage(Message message) {

    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(radius:number, force:number): true or false, string")
    public Object[] prime(Context context, Arguments args) {
        double radius = args.checkDouble(0);
        double force = args.checkDouble(1);
        if(radius > OpenCUConfig.repulsorMaxRadius)
        {
            return new Object[]{ false, "Radius to large" };
        }
        if(force > 1.0)
        {
            return new Object[]{ false, "Force to large" };
        }
        pulse = new RepulsorPulse(radius, force*OpenCUConfig.repulsorMaxForce);
        return new Object[0];
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(whitelist:boolean): nil")
    public Object[] setWhitelist(Context context, Arguments args) {
        pulse.setWhitelist(args.checkBoolean(0));
        return new Object[0];
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(whitelist:boolean): nil")
    public Object[] addToFilter(Context context, Arguments args) {
        filter.add(args.checkString(0));
        return new Object[0];
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(whitelist:boolean): nil")
    public Object[] removeFromFilter(Context context, Arguments args) {
        filter.add(args.checkString(0));
        return new Object[0];
    }

    @SuppressWarnings("unused")
    @Callback(doc = "function(): ...")
    public Object[] getFilter(Context context, Arguments args)
    {
        return filter.toArray();
    }

    @Callback(doc = "function(x:number, y:number, z:number): true or false, string")
    public Object[] pulse(Context context, Arguments args) {
        double x1 = args.checkDouble(0);
        double y1 = args.checkDouble(1);
        double z1 = args.checkDouble(2);
        double distanceSqr = x1*x1 + y1*y1 + z1*z1;

        if(distanceSqr > OpenCUConfig.repulsorMaxOffset * OpenCUConfig.repulsorMaxOffset)
        {
            return new Object[]{ false, "Offset to large" };
        }
        pulse.lock(this.world, x1 + pos.getX() + 0.5, y1 + pos.getY() + 0.5, z1 + pos.getZ() + 0.5);
        pulse.filter(filter);
        double radiusRatio = pulse.getRadius() * pulse.getRadius() * pulse.getRadius() / (OpenCUConfig.repulsorMaxRadius * OpenCUConfig.repulsorMaxRadius * OpenCUConfig.repulsorMaxRadius);
        double forceRatio = pulse.getForce() / OpenCUConfig.repulsorMaxForce;
        double distanceRatio = Math.sqrt(distanceSqr) / OpenCUConfig.repulsorDistanceCost;
        int energyUsage = MathHelper.floor(distanceRatio * OpenCUConfig.repulsorDistanceCost + radiusRatio * OpenCUConfig.repulsorVolumeCost + (Math.pow(2, Math.abs(forceRatio))-1.0) * OpenCUConfig.repulsorForceCost);
        if(energyBuffer.getEnergyStored() < energyUsage)
        {
            return new Object[]{ false, "Not enough energy stored!!!", energyUsage };
        }
        energyBuffer.extractEnergy(energyUsage, false);
        pulse.execute();
        context.pause(0.05);
        pulseTicksLeft = pulseTicks;

        markDirty();

        IBlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 2);
        return new Object[0];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound nodeTag = new NBTTagCompound();
        node.save(nodeTag);
        compound.setTag("node", nodeTag);

        NBTTagCompound energyTag = new NBTTagCompound();
        energyTag.setInteger("current", energyBuffer.getEnergyStored());
        compound.setTag("energy", energyTag);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagCompound nodeTag = compound.getCompoundTag("node");
        if(nodeTag != null && node != null) {
            node.load(nodeTag);
        }
        NBTTagCompound energyTag = compound.getCompoundTag("energy");
        if(energyTag != null) {
            int maxEnergyStored = MathHelper.floor(OpenCUConfig.repulsorDistanceCost
                    + OpenCUConfig.repulsorVolumeCost
                    + OpenCUConfig.repulsorForceCost);

            energyBuffer = new EnergyStorage(maxEnergyStored, OpenCUConfig.repulsorMaxTransferRate, maxEnergyStored, energyTag.getInteger("current"));
            energyWrapper = new ReciveOnlyEnergyStorageWrapper(energyBuffer);
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
        {
            return true;
        }
/*        if(capability == Capabilities.EnvironmentCapability)
        {
            return true;
        }*/
        return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
        {
            if(facing == null)
            {
                return (T)energyBuffer;
            }
            return (T)energyWrapper;
        }
/*        if(capability == Capabilities.EnvironmentCapability)
        {
            return (T)this;
        }*/
        return super.getCapability(capability, facing);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("anim", pulseTicksLeft);
        SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(pos, 0, data);
        return packet;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        pulseTicksLeft = pkt.getNbtCompound().getInteger("anim");
    }
}