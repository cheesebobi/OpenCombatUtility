package com.LubieKakao1212.opencu.pulse;

import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.LubieKakao1212.opencu.network.packet.PlayerAddVelocityPacket;
import com.LubieKakao1212.qulib.libs.joml.Vector3d;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EntityPulse {

    protected double radius;
    protected double baseForce;
    protected double posX, posY, posZ;
    protected Level level;
    protected List<Entity> entityList;

    private boolean whitelist;

    public EntityPulse() {
        this.entityList = new ArrayList<>();
        this.whitelist = false;
    }

    public void lock(Level level, double x, double y, double z) {
        this.level = level;
        posX = x;
        posY = y;
        posZ = z;

        entityList = level.getEntities(null, new AABB(0,0,0,0,0,0).move(x,y,z).inflate(radius));
    }

    public void resetLock()
    {
        entityList.clear();
    }

    public void filter(ArrayList<String> list) {
        entityList = entityList.stream().filter( e -> { return whitelist ? list.contains(e.getName().getContents()) : !list.contains(e.getName().getContents()); }).collect(Collectors.toList());
    }

    protected void filter() {
        entityList = entityList.stream().filter(entity -> {
            double relX = entity.getX() - posX;
            double relY = entity.getY() - posY;
            double relZ = entity.getZ() - posZ;

            return relX*relX+relY*relY+relZ*relZ < radius * radius;
        }).collect(Collectors.toList());
    }

    public abstract void execute();

    public void setWhitelist(boolean whitelist) {
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

    public CompoundTag writeToNBT(CompoundTag tag) {
        tag.putDouble("radius", radius);
        tag.putDouble("force", baseForce);
        tag.putBoolean("whitelist", whitelist);
        return tag;
    }

    public void readFromNBT(CompoundTag tag) {
        radius = tag.getDouble("radius");
        baseForce = tag.getDouble("force");
        whitelist = tag.getBoolean("whitelist");
    }

    protected static void addVelocity(Entity e, Vector3d deltaV) {
        addVelocity(e, deltaV.x, deltaV.y, deltaV.z);
    }

    protected static void addVelocity(Entity e, double vX, double vY, double vZ) {
        if(e instanceof ServerPlayer)
        {
            NetworkHandler.sendTo((ServerPlayer)e, new PlayerAddVelocityPacket(vX, vY, vZ));
        }else {
            Vec3 movement = e.getDeltaMovement().add(vX, vY, vZ);
            //TODO unground arrows
            /*if(e instanceof AbstractArrow) {
                AbstractArrow p = (AbstractArrow) e;
            }*/
            e.setDeltaMovement(movement);
            if(e instanceof LivingEntity && movement.y > 0)
            {
                e.fallDistance = 0;
            }
        }
    }

}
