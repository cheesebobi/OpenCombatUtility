package com.LubieKakao1212.opencu.pulse;

import net.minecraft.world.level.Level;
import org.joml.Vector3d;

@FunctionalInterface
public interface IPulse {

    void doPulse(Level level, Vector3d pos, Vector3d direction, double radius, double force);

}
