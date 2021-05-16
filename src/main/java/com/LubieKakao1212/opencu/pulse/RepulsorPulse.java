package com.LubieKakao1212.opencu.pulse;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.network.NetworkHandler;
import com.LubieKakao1212.opencu.network.packet.EntityAddVelocityPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;

public class RepulsorPulse extends EntityPulse {

    public RepulsorPulse(double radius, double force) {
        super(radius, force);
    }

    @Override
    public void execute() {
        filter();

        for(Entity e : entityList)
        {
            if(e instanceof EntityLivingBase)
            {
                e.fallDistance = 0;
            }
            double dX = e.posX - posX;
            double dY = e.posY - posY;
            double dZ = e.posZ - posZ;

            double distanceSqr = dX*dX + dY*dY + dZ*dZ;

            double distance = Math.sqrt(distanceSqr);

            double vX = dX/distance * force;
            double vY = dY/distance * force;
            double vZ = dZ/distance * force;

            if(e instanceof EntityPlayerMP)
            {
                NetworkHandler.sendTo((EntityPlayerMP)e, new EntityAddVelocityPacket(vX, vY, vZ));
            }else {
                e.addVelocity(vX, vY, vZ);
            }
        }
    }
}
