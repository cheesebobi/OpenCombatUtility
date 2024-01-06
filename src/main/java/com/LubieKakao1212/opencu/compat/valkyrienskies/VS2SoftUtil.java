package com.LubieKakao1212.opencu.compat.valkyrienskies;

import com.LubieKakao1212.opencu.OpenCUMod;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class VS2SoftUtil {

    public static double getDistanceSqr(Level level, Vector3d p1, Vector3d p2) {
        if(OpenCUMod.hasValkyrienSkies()) {
            return VSGameUtilsKt.squaredDistanceBetweenInclShips(level,
                    p1.x(), p1.y(), p1.z(),
                    p2.x(), p2.y(), p2.z());
        }
        return p1.distance(p2);
    }

    public static double getDistance(Level level, Vector3d p1, Vector3d p2) {
        return Math.sqrt(getDistanceSqr(level, p1, p2));
    }

}
