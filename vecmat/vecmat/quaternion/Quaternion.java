package com.LubieKakao1212.vecmat.quaternion;

import com.LubieKakao1212.vecmat.vector.Vector3d;

public class Quaternion {

    private double s;
    private Vector3d v;

    private Quaternion(double s, Vector3d v) {
        this.s = s;
        this.v = v;
    }

    public double length(){
        return Math.sqrt(lengthSq());
    }

    public double lengthSq() {
        return dot(this);
    }

    public double dot(Quaternion q) {
        return s * q.s + v.dot(q.v);
    }

    public Quaternion copy() {
        return new Quaternion(s, v.copy());
    }

    public Quaternion normalize() {
        double len = length();
        divide(len);
        return this;
    }

    public Quaternion normalized() {
        return copy().normalize();
    }

    public Quaternion multiply(double a) {
        this.s *= a;
        v.multiply(a);
        return this;
    }

    public Quaternion multiplied(double a) {
        return copy().multiply(a);
    }

    public Quaternion multiply(Quaternion q) {
        double scalar = s * q.s - v.dot(q.v);

        q.v.multiply(s)
            .add(v.multiplied(q.s))
            .add(v.cross(q.v));

        this.s = scalar;
        return this;
    }

    public Quaternion multiplied(Quaternion q) {
        return q.copy().multiply(q);
    }

    public Quaternion divide(double a) {
        this.s /= a;
        v.divide(a);
        return this;
    }

    public Quaternion divided(double a) {
        return copy().divide(a);
    }

    public Quaternion add(Quaternion q) {
        this.s += q.s;
        this.v.add(q.v);
        return this;
    }

    public Quaternion added(Quaternion q) {
        return copy().add(q);
    }

    public Quaternion subtract(Quaternion q) {
        this.s -= q.s;
        this.v.subtract(q.v);
        return this;
    }

    public Quaternion subtracted(Quaternion q) {
        return copy().subtract(q);
    }

    public static Quaternion axisAngle(Vector3d ) {

    }
}
