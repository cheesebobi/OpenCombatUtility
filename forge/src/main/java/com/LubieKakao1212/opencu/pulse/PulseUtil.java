package com.LubieKakao1212.opencu.pulse;

import com.LubieKakao1212.opencu.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.config.OpenCUConfigCommon;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector3d;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PulseUtil {

    public static List<Entity> getAffectedEntities(World level, Vector3d pos, double radius) {
        double radiusSqr = radius * radius;
        return level.getOtherEntities(null,
                new Box(0,0,0,0,0,0)
                        .offset(new Vec3d(pos.x, pos.y, pos.z))
                        .expand(radius)
                ).stream().filter(entity ->
                VS2SoftUtil.getDistanceSqr(level,
                        pos,
                        new Vector3d(entity.getX(), entity.getY(), entity.getZ())
                ) < radiusSqr).collect(Collectors.toList());
    }

    public static double getScaledForce(double baseForce) {
        return baseForce * OpenCUConfigCommon.REPULSOR.getForceScale();
    }

    @ApiStatus.Experimental
    public static List<Entity> applyFilter(List<Entity> entities, List<String> filters) {
        return entities;
    }

}
