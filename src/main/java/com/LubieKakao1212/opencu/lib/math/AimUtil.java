package com.LubieKakao1212.opencu.lib.math;


import glm.quat.Quat;
import glm.vec._3.Vec3;

import java.util.Random;

public class AimUtil {

    private static Random random = new Random();

    private static final Vec3 forward = new Vec3(0., 0., 1.);
    private static final Vec3 back = new Vec3(0., 0., -1.);
    private static final Vec3 left = new Vec3(1., 0., 0.);
    private static final Vec3 right = new Vec3(-1., 0., 0.);
    private static final Vec3 up = new Vec3(0., 1., 0.);
    private static final Vec3 down = new Vec3(0., -1., 0.);

    public static Vec3 calculateForwardWithSpread(Quat aim, float spread) {
        Vec3 forward = calculateForward(aim);
        Vec3 side = perpendicular(forward);

        Quat rotSide = Quat.angleAxis_(random.nextFloat() * spread / 2f, side);

        Quat rotRound = Quat.angleAxis_((float)(random.nextDouble() * 360), forward);
        return rotRound.mul(rotSide).mul(forward);
    }

    public static Vec3 calculateForward(Quat aim) {
        return aim.mul_(forward);
    }

    public static Vec3 perpendicular(Vec3 vector) {
        if(MathUtil.equals(vector, forward))
        {
            return new Vec3(0, -vector.z, vector.y);
        }
        return new Vec3(-vector.y, vector.x, 0);
    }

    public static Quat aimRad(float pitch, float yaw) {
        Quat xQuat = Quat.angleAxis_(pitch * MathUtil.radToDeg, right);
        Quat yQuat = Quat.angleAxis_(yaw * MathUtil.radToDeg, down);

        return yQuat.mul(xQuat);
    }
}
