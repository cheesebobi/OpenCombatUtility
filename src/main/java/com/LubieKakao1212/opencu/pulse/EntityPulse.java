package com.LubieKakao1212.opencu.pulse;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.LubieKakao1212.opencu.network.packet.EntityAddVelocityPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EntityPulse {

    protected double radius;
    protected double baseForce;
    protected double posX, posY, posZ;
    protected World world;
    protected List<Entity> entityList;

    private boolean whitelist;

    public EntityPulse() {
        this.entityList = new ArrayList<>();
        this.whitelist = false;
    }

    public void lock(World world, double x, double y, double z) {
        this.world = world;
        posX = x;
        posY = y;
        posZ = z;

        entityList = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(0,0,0,0,0,0).offset(x,y,z).grow(radius));
        //entityList = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(0,0,0,0,0,0).offset(x,y,z).grow(radius));
        /*entityList.stream().forEach(e -> {
            if(e instanceof EntityPlayer) {
                OpenCUMod.logger.warn(e.getName());
            }
        });*/
    }

    public void resetLock()
    {
        entityList.clear();
    }

    public void filter(ArrayList<String> list) {
        entityList = entityList.stream().filter( e -> { return whitelist ? list.contains(e.getName()) : !list.contains(e.getName()); }).collect(Collectors.toList());
    }

    protected void filter() {
        entityList = entityList.stream().filter(entity -> {
            double relX = entity.posX - posX;
            double relY = entity.posY - posY;
            double relZ = entity.posZ - posZ;

            return relX*relX+relY*relY+relZ*relZ < radius * radius;
        }).collect(Collectors.toList());
    }

    public abstract void execute();

    public void setWhitelist(boolean whitelist) {
        OpenCUMod.logger.warn(this.whitelist);
        this.whitelist = whitelist;
    }

    public void setVector(double x, double y, double z) { }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setBaseForce(double baseForce) {
        this.baseForce = baseForce;
    }

    public double getRadius() {
        return radius;
    }

    public int getEntityCount() {
        return entityList.size();
    }

    public double getBaseForce() {
        return baseForce;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setDouble("radius", radius);
        tag.setDouble("force", baseForce);
        tag.setBoolean("whitelist", whitelist);
        return tag;
    }

    public void readFromNBT(NBTTagCompound tag) {
        radius = tag.getDouble("radius");
        baseForce = tag.getDouble("force");
        whitelist = tag.getBoolean("whitelist");
    }

    protected static void addVelocity(Entity e, double vX, double vY, double vZ) {
        if(e instanceof EntityPlayerMP)
        {
            NetworkHandler.sendTo((EntityPlayerMP)e, new EntityAddVelocityPacket(vX, vY, vZ));
            if(e.motionY > 0){
                e.fallDistance = 0;
            }
        }else {
            e.addVelocity(vX, vY, vZ);
            if(e instanceof EntityLivingBase && e.motionY > 0)
            {
                e.fallDistance = 0;
            }
        }
    }

}
