package com.LubieKakao1212.opencu.util;

import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.LubieKakao1212.opencu.network.packet.PlayerAddVelocityPacket;
import com.LubieKakao1212.opencu.network.packet.PlayerScaleVelocityPacket;
import org.joml.Vector3d;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class EntityUtil {

    public static void addVelocity(Entity e, Vector3d deltaV) {
        addVelocity(e, deltaV.x, deltaV.y, deltaV.z);
    }

    public static void addVelocity(Entity e, double vX, double vY, double vZ) {
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

    public static void scaleVelocity(Entity e, double scale) {
        if(e instanceof ServerPlayer)
        {
            NetworkHandler.sendTo((ServerPlayer)e, new PlayerScaleVelocityPacket(scale));
        }else {
            Vec3 movement = e.getDeltaMovement();
            //TODO unground arrows
            /*if(e instanceof AbstractArrow) {
                AbstractArrow p = (AbstractArrow) e;
            }*/
            e.setDeltaMovement(movement.multiply(scale, scale, scale));
            if(e instanceof LivingEntity && movement.y > 0)
            {
                e.fallDistance = 0;
            }
        }
    }
}
