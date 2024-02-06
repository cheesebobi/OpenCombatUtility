package com.LubieKakao1212.opencu.pulse;

import com.LubieKakao1212.opencu.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.config.OpenCUConfigCommon;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector3d;

import java.util.List;
import java.util.stream.Collectors;

public class PulseUtil {

    public static List<Entity> getAffectedEntities(Level level, Vector3d pos, double radius) {
        double radiusSqr = radius * radius;
        return level.getEntities(null,
                new AABB(0,0,0,0,0,0)
                        .move(new Vec3(pos.x, pos.y, pos.z))
                        .inflate(radius)
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
