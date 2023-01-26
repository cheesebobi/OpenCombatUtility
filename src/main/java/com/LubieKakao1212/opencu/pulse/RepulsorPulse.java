package com.LubieKakao1212.opencu.pulse;

import net.minecraft.world.entity.Entity;

public class RepulsorPulse extends EntityPulse {

    private static final float epsilon = 0.0001f;
    private static final float epsilonSqr = epsilon * epsilon;

    public RepulsorPulse() {
        super();
    }

    @Override
    public void execute() {
        filter();

        for(Entity e : entityList)
        {
            double dX = e.getX() - posX;
            double dY = e.getY() - posY;
            double dZ = e.getZ() - posZ;

            double distanceSqr = dX*dX + dY*dY + dZ*dZ;

            //if we have a very small delta we consider it to have an up direction to avoid floating point precision errors
            if(distanceSqr < epsilonSqr) {
                addVelocity(e, 0, baseForce, 0);
                continue;
            }

            double distance = Math.sqrt(distanceSqr);

            double vX = dX/distance * baseForce;
            double vY = dY/distance * baseForce;
            double vZ = dZ/distance * baseForce;

            addVelocity(e, vX, vY, vZ);
        }
    }
}
