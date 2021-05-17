package com.LubieKakao1212.opencu.pulse;

import net.minecraft.entity.Entity;

public class RepulsorPulse extends EntityPulse {

    public RepulsorPulse(double radius, double force) {
        super(radius, force);
    }

    @Override
    public void execute() {
        filter();

        for(Entity e : entityList)
        {
            double dX = e.posX - posX;
            double dY = e.posY - posY;
            double dZ = e.posZ - posZ;

            double distanceSqr = dX*dX + dY*dY + dZ*dZ;

            double distance = Math.sqrt(distanceSqr);

            double vX = dX/distance * baseForce;
            double vY = dY/distance * baseForce;
            double vZ = dZ/distance * baseForce;

            addVelocity(e, vX, vY, vZ);
        }
    }
}
