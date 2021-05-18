package com.LubieKakao1212.opencu.pulse;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.util.math.Vec3d;

public class VectorPulse extends EntityPulse {

    protected double vX, vY, vZ;

    public VectorPulse() {
        super();
        setVector(0, 1, 0);
    }

    public void setVector(double x, double y, double z) {
        double distanceSqr = x*x + y*y + z*z;

        double distance = Math.sqrt(distanceSqr);

        vX = x / distance;
        vY = y / distance;
        vZ = z / distance;
    }

    @Override
    public void execute() {
        filter();

        for(Entity e : entityList)
        {
            double vX = this.vX * baseForce;
            double vY = this.vY * baseForce;
            double vZ = this.vZ * baseForce;

            addVelocity(e, vX, vY, vZ);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagList vector = new NBTTagList();
        vector.appendTag(new NBTTagDouble(vX));
        vector.appendTag(new NBTTagDouble(vY));
        vector.appendTag(new NBTTagDouble(vZ));
        tag.setTag("vector", vector);
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        NBTTagList vector = tag.getTagList("vector", 6);
        vX = vector.getDoubleAt(0);
        vY = vector.getDoubleAt(1);
        vZ = vector.getDoubleAt(2);
    }
}
