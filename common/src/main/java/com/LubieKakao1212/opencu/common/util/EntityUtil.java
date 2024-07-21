package com.LubieKakao1212.opencu.common.util;

import com.lubiekakao1212.qulib.math.mc.Vector3m;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

public class EntityUtil {

    public static void addVelocity(Entity e, Vector3d deltaV) {
        addVelocity(e, deltaV.x, deltaV.y, deltaV.z);
    }

    public static void addVelocity(Entity e, double vX, double vY, double vZ) {
        if(e instanceof PersistentProjectileEntity arrow) {
            if(arrow.inGround) {
                arrow.inGround = false;
                arrow.setVelocity(0, 0, 0);
            }
        }
        Vec3d movement = e.getVelocity().add(vX, vY, vZ);
        e.setVelocity(movement);
        if(e instanceof LivingEntity && movement.y > 0)
        {
            e.fallDistance = 0;
        }
        e.velocityDirty = true;
        e.velocityModified = true;
    }

    public static void scaleVelocity(Entity e, double scale) {
        Vec3d movement = e.getVelocity();
        e.setVelocity(movement.multiply(scale, scale, scale));
        if(e instanceof LivingEntity && movement.y > 0)
        {
            e.fallDistance = 0;
        }
        e.velocityDirty = true;
        e.velocityModified = true;
    }

    private static Vector3m blockFaceToNormal(Vector3m surfacePos) {
        var abs = surfacePos.absolute(new Vector3d());
        var sign = new Vector3d(
                Math.signum(surfacePos.x),
                Math.signum(surfacePos.y),
                Math.signum(surfacePos.z)
        );
        if(abs.x > abs.y && abs.x > abs.z) {
            return new Vector3m(sign.x, 0, 0);
        }
        else if(abs.y > abs.x && abs.y > abs.z) {
            return new Vector3m(0, sign.y, 0);
        }
        else if(abs.z > abs.x && abs.z > abs.y) {
            return new Vector3m(0, 0, sign.z);
        }

        //Impossible
        throw new RuntimeException();
    }

}
