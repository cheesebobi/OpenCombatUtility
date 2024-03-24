package com.LubieKakao1212.opencu.common.pulse;

import net.minecraft.world.World;
import org.joml.Vector3d;

@FunctionalInterface
public interface IPulse {

    void doPulse(World level, Vector3d pos, Vector3d direction, double radius, double force);

}
