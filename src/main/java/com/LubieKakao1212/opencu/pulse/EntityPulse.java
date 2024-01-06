package com.LubieKakao1212.opencu.pulse;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.config.OpenCUConfigCommon;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.LubieKakao1212.opencu.network.packet.PlayerAddVelocityPacket;
import org.joml.Vector3d;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.ValkyrienSkiesMod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class EntityPulse {

    protected double radius;
    protected double radiusSqr;
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
        entityList = entityList.stream().filter( e -> whitelist == list.contains(e.getName().getContents())).collect(Collectors.toList());
    }

    protected void filter() {
        Stream<Entity> stream = entityList.stream();
        entityList = stream.filter(entity ->
                VS2SoftUtil.getDistanceSqr(level,
                        new Vector3d(posX, posY, posZ),
                        new Vector3d(entity.getX(), entity.getY(), entity.getZ())
                ) < radiusSqr).collect(Collectors.toList());
    }

    public abstract void execute();

    public void setWhitelist(boolean whitelist) {
        this.whitelist = whitelist;
    }

    public void setVector(double x, double y, double z) { }

    public void setRadius(double radius) {
        this.radius = radius;
        this.radiusSqr = radius * radius;
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

    public double getScaledForce() {
        return baseForce * OpenCUConfigCommon.REPULSOR.getForceScale();
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

}
