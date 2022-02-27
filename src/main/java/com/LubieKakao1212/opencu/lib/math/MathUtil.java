package com.LubieKakao1212.opencu.lib.math;

import org.joml.Vector3d;

public class MathUtil {

    public static final float degToRad = (float)(Math.PI/180.);
    public static final float radToDeg = (float)(180./Math.PI);
    public static final float piHalf = (float) Math.PI / 2.f;
    public static final float pi = (float) Math.PI;

    private static final float epsilon = 0.001f;
    private static final Vector3d epsilonVec3 = new Vector3d(epsilon, epsilon, epsilon);

    public static boolean equals(Vector3d one, Vector3d two) {
        Vector3d delta = new Vector3d(one).sub(two).absolute();

        return delta.length() < epsilon;
    }

    public static double loop(double a, double min, double max) {
        double b = a - min;
        double c = b % (max - min);

        return c + min;
    }

    public static float loop(float a, float min, float max) {
        float b = a - min;
        float c = b % (max - min);

        return c + min;
    }


}
