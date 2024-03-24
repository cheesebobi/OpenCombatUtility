package com.LubieKakao1212.opencu.common.pulse;

import com.LubieKakao1212.opencu.common.util.EntityUtil;
import com.lubiekakao1212.qulib.math.extensions.Vector3dExtensionsKt;
import org.joml.Vector3d;

import static java.lang.Math.*;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class Pulses {
    private static final float epsilon = 0.0001f;
    private static final float epsilonSqr = epsilon * epsilon;

    public static void repulsorPulse(World level, Vector3d pos, Vector3d direction, double radius, double force) {
        List<Entity> entityList = PulseUtil.getAffectedEntities(level, pos, radius);

        /*if(OpenCUMod.hasValkyrienSkies()) {
            pos = VSGameUtilsKt.toWorldCoordinates(level, pos);
        }*/

        force = PulseUtil.getScaledForce(force);

        for(Entity e : entityList) {
            double dX = e.getX() - pos.x;
            double dY = e.getY() - pos.y;
            double dZ = e.getZ() - pos.z;

            double distanceSqr = dX*dX + dY*dY + dZ*dZ;

            //if we have a very small delta we consider it to have an up direction to avoid floating point precision errors
            if(distanceSqr < epsilonSqr) {
                EntityUtil.addVelocity(e, 0, force, 0);
                continue;
            }

            double distance = Math.sqrt(distanceSqr);

            double vX = dX/distance * force;
            double vY = dY/distance * force;
            double vZ = dZ/distance * force;

            EntityUtil.addVelocity(e, vX, vY, vZ);
        }
    }

    public static void vectorPulse(World level, Vector3d pos, Vector3d direction, double radius, double force) {
        List<Entity> entityList = PulseUtil.getAffectedEntities(level, pos, radius);
        Vector3d directionForce = direction.mul(PulseUtil.getScaledForce(force));

        /*if(OpenCUMod.hasValkyrienSkies()) {
            Ship ship = VSGameUtilsKt.getShipManagingPos(level, pos);
            if(ship != null){
                ShipTransform transform = ship.getTransform();
                directionForce = transform.transformDirectionNoScalingFromShipToWorld(directionForce, directionForce);
            }
        }*/

        for(Entity e : entityList)
        {
            EntityUtil.addVelocity(e, directionForce);
        }
    }

    public static void stasisPulse(World level, Vector3d pos, Vector3d direction, double radius, double force) {
        List<Entity> entityList = PulseUtil.getAffectedEntities(level, pos, radius);

        double stasisFactor = min(1.0, abs(force));

        stasisFactor = 1 - stasisFactor;

        for(Entity e : entityList) {
            Vector3d movement = Vector3dExtensionsKt.from(new Vector3d(), e.getVelocity());

            if(movement.lengthSquared() < epsilonSqr) {
                EntityUtil.scaleVelocity(e, 0);
                continue;
            }

            EntityUtil.scaleVelocity(e, stasisFactor);
        }
    }


}
