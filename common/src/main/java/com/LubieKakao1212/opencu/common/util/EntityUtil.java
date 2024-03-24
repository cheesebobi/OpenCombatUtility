package com.LubieKakao1212.opencu.common.util;

import com.LubieKakao1212.opencu.common.network.packet.PacketClientPlayerAddVelocity;
import com.LubieKakao1212.opencu.common.network.packet.PacketClientPlayerScaleVelocity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

public class EntityUtil {

    public static void addVelocity(Entity e, Vector3d deltaV) {
        addVelocity(e, deltaV.x, deltaV.y, deltaV.z);
    }

    public static void addVelocity(Entity e, double vX, double vY, double vZ) {
        if(e instanceof ServerPlayerEntity)
        {
            PlatformUtil.Network.sendToPlayer(PacketClientPlayerAddVelocity.create(vX, vY, vZ), (ServerPlayerEntity)e);
        }else {
            Vec3d movement = e.getVelocity().add(vX, vY, vZ);
            //TODO unground arrows
            /*if(e instanceof AbstractArrow) {
                AbstractArrow p = (AbstractArrow) e;
            }*/
            e.setVelocity(movement);
            if(e instanceof LivingEntity && movement.y > 0)
            {
                e.fallDistance = 0;
            }
        }
    }

    public static void scaleVelocity(Entity e, double scale) {
        if(e instanceof ServerPlayerEntity)
        {
            PlatformUtil.Network.sendToPlayer(new PacketClientPlayerScaleVelocity((float)scale), (ServerPlayerEntity)e);
        }else {
            Vec3d movement = e.getVelocity();
            //TODO unground arrows
            /*if(e instanceof AbstractArrow) {
                AbstractArrow p = (AbstractArrow) e;
            }*/
            e.setVelocity(movement.multiply(scale, scale, scale));
            if(e instanceof LivingEntity && movement.y > 0)
            {
                e.fallDistance = 0;
            }
        }
    }
}
