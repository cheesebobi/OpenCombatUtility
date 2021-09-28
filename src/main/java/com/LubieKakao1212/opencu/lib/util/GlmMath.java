package com.LubieKakao1212.opencu.lib.util;

import glm.quat.Quat;
import net.minecraft.util.EnumFacing;

public class GlmMath {

    public static Quat slerp(Quat a, Quat b , float t) {
        //Quat angle = a.conjugate_().mul(b);
        float cosAngle = a.dot(b);
        boolean flag = false;

        if(Math.abs(cosAngle) > 1.0){
            return a;
        }

        if(cosAngle < 0) {
            flag = true;
            cosAngle = -cosAngle;
        }

        double halfAngle = Math.acos(cosAngle);
        double sinAngle = Math.sqrt(1. - cosAngle * cosAngle);

        if(sinAngle < 0.001) {
            return a.mul_(1. - t).add(b.mul_(t)).normalize();
        }

        double v1 = Math.sin((1. - t) * halfAngle);
        double v2 = Math.sin(t * halfAngle);

        return a.mul_(v1).add(b.mul_(v2)).normalize();//a.mul(t).add(b.mul(1f - t));
    }

    public static Quat step(Quat a, Quat b, float maxStep) {
        float cosAngle = a.dot(b);
        double angle = 2. * Math.acos(cosAngle);

        double step = maxStep / angle;

        step = Math.min(Math.max(step, 0.), 1.);

        return slerp(a, b, (float) step);
    }

    public static Quat axisMirror(Quat a, EnumFacing.Axis axis) {
        switch(axis) {
            case X:
            {
                return new Quat(-a.w, -a.x, a.y, a.z);
            }
            case Y:
            {
                return new Quat(-a.w, a.x, -a.y, a.z);
            }
            case Z:
            {
                return new Quat(-a.w, a.x, a.y, -a.z);
            }
        }
        return a;
    }

}
