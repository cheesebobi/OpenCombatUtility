package com.LubieKakao1212.opencu.common.compat.valkyrienskies;

import net.minecraft.world.World;
import org.joml.Vector3d;
//import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class VS2SoftUtil {

    public static double getDistanceSqr(World level, Vector3d p1, Vector3d p2) {
        //TODO Apparently VS2 for 1.19.4 does not exist
        /*if(OpenCUMod.hasValkyrienSkies()) {
            return VSGameUtilsKt.squaredDistanceBetweenInclShips(level,
                    p1.x(), p1.y(), p1.z(),
                    p2.x(), p2.y(), p2.z());
        }*/
        return p1.distanceSquared(p2);
    }

    public static double getDistance(World level, Vector3d p1, Vector3d p2) {
        return Math.sqrt(getDistanceSqr(level, p1, p2));
    }

}
