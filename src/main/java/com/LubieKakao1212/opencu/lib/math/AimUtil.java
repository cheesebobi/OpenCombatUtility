package com.LubieKakao1212.opencu.lib.math;

import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.Random;

public class AimUtil {

    private static Random random = new Random();

    private static final Vector3d forward = new Vector3d(0., 0., 1.);
    private static final Vector3d back = new Vector3d(0., 0., -1.);
    private static final Vector3d left = new Vector3d(1., 0., 0.);
    private static final Vector3d right = new Vector3d(-1., 0., 0.);
    private static final Vector3d up = new Vector3d(0., 1., 0.);
    private static final Vector3d down = new Vector3d(0., -1., 0.);

    public static Vector3d calculateForwardWithSpread(Quaterniond aim, double spread) {
        Vector3d forward = calculateForward(aim);
        Vector3d side = perpendicular(forward);

        Quaterniond rotSide = new Quaterniond().fromAxisAngleRad(side, random.nextFloat() * spread / 2f);

        Quaterniond rotRound = new Quaterniond().fromAxisAngleRad(forward, (random.nextDouble() * Math.PI));
        return rotRound.mul(rotSide).transform(forward);
    }

    public static Vector3d calculateForward(Quaterniond aim) {
        return aim.transform(new Vector3d(forward));
    }

    public static Vector3d perpendicular(Vector3d vector) {
        if(MathUtil.equals(vector, forward)) {
            return new Vector3d(0, -vector.z, vector.y);
        }
        return new Vector3d(-vector.y, vector.x, 0);
    }

    public static Quaterniond aimRad(float pitch, float yaw) {
        return new Quaterniond().fromAxisAngleRad(right, pitch)
                .mul(new Quaterniond().fromAxisAngleRad(down, yaw));
    }

    public static double angle(Quaterniond a, Quaterniond b) {
        return a.difference(b, new Quaterniond()).angle();
    }

    public static Quaterniond step(Quaterniond from, Quaterniond to, double maxAngle, Quaterniond dst) {
        double angle = angle(from, to);

        double step = maxAngle / angle;
        step = Math.min(Math.max(step, 0.), 1.);

        return from.slerp(to, step, dst).normalize();
    }

    public static Quaterniond step(Quaterniond from, Quaterniond to, double maxAngle) {
        return step(from, to, maxAngle, from);
    }
}