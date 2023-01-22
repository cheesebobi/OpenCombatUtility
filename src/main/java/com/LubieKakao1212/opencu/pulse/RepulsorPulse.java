package com.LubieKakao1212.opencu.pulse;

import net.minecraft.world.entity.Entity;

public class RepulsorPulse extends EntityPulse {

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

            double distance = Math.sqrt(distanceSqr);

            double vX = dX/distance * baseForce;
            double vY = dY/distance * baseForce;
            double vZ = dZ/distance * baseForce;

            addVelocity(e, vX, vY, vZ);
        }
    }
}
