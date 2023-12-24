package com.LubieKakao1212.opencu.pulse;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3d;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class RepulsorPulse extends EntityPulse {

    private static final float epsilon = 0.0001f;
    private static final float epsilonSqr = epsilon * epsilon;

    public RepulsorPulse() {
        super();
    }

    @Override
    public void execute() {
        filter();

        Vector3d wPos = new Vector3d(posX, posY, posZ);

        if(OpenCUMod.hasValkyrienSkies()) {
            wPos = VSGameUtilsKt.toWorldCoordinates(level, wPos);
        }

        for(Entity e : entityList)
        {
            double dX = e.getX() - wPos.x;
            double dY = e.getY() - wPos.y;
            double dZ = e.getZ() - wPos.z;

            double distanceSqr = dX*dX + dY*dY + dZ*dZ;

            //if we have a very small delta we consider it to have an up direction to avoid floating point precision errors
            if(distanceSqr < epsilonSqr) {
                EntityUtil.addVelocity(e, 0, baseForce, 0);
                continue;
            }

            double distance = Math.sqrt(distanceSqr);

            double vX = dX/distance * baseForce;
            double vY = dY/distance * baseForce;
            double vZ = dZ/distance * baseForce;

            EntityUtil.addVelocity(e, vX, vY, vZ);
        }
    }
}
