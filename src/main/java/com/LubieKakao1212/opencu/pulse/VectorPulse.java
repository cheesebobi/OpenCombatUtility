package com.LubieKakao1212.opencu.pulse;

import com.LubieKakao1212.opencu.OpenCUMod;
import com.LubieKakao1212.opencu.util.EntityUtil;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3d;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.core.api.ships.properties.ShipTransform;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

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

        Vector3d force = new Vector3d(vX, vY, vZ);
        force.mul(baseForce);

        if(OpenCUMod.hasValkyrienSkies()) {
            Ship ship = VSGameUtilsKt.getShipManagingPos(level, posX, posY, posZ);
            if(ship != null){
                ShipTransform transform = ship.getTransform();
                force = transform.transformDirectionNoScalingFromShipToWorld(force, force);
            }
        }

        for(Entity e : entityList)
        {
            EntityUtil.addVelocity(e, force);
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag tag) {
        ListTag vector = new ListTag();
        vector.add(DoubleTag.valueOf(vX));
        vector.add(DoubleTag.valueOf(vY));
        vector.add(DoubleTag.valueOf(vZ));
        tag.put("vector", vector);
        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        ListTag vector = tag.getList("vector", 6);
        vX = vector.getDouble(0);
        vY = vector.getDouble(1);
        vZ = vector.getDouble(2);
    }
}
