package com.LubieKakao1212.opencu.pulse;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.config.OpenCUConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class EntityPulse {

    protected double radius;
    protected double force;
    protected double posX, posY, posZ;
    protected World world;
    protected List<Entity> entityList;

    private boolean whitelist;

    public EntityPulse(double radius, double force) {
        this.radius = radius;
        this.force = force;
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

    /*public void filter(List<UUID> list) {
        entityList = entityList.stream().filter( e -> { return whitelist ? list.contains(e.getUniqueID()) : !list.contains(e.getUniqueID()); }).collect(Collectors.toList());
    }*/

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
        this.whitelist = whitelist;
    }

    public double getRadius() {
        return radius;
    }

    public int getEntityCount() {
        return entityList.size();
    }

    public double getForce() {
        return force;
    }
}
