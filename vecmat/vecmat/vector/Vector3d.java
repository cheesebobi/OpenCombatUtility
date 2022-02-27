package com.LubieKakao1212.vecmat.vector;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class Vector3d {

    private double x, y, z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double length(){
        return Math.sqrt(lengthSq());
    }

    public double lengthSq() {
        return dot(this);
    }

    public double dot(Vector3d v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3d cross(Vector3d v) {
        return new Vector3d(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        );
    }

    public Vector3d copy() {
        return new Vector3d(x, y, z);
    }

    public Vector3d normalize() {
        double len = length();
        this.x /= len;
        this.y /= len;
        this.z /= len;
        return this;
    }

    public Vector3d normalized() {
        return copy().normalize();
    }

    public Vector3d multiply(double a) {
        this.x *= a;
        this.y *= a;
        this.z *= a;

        return this;
    }

    public Vector3d multiplied(double a) {
        return copy().multiply(a);
    }

    public Vector3d divide(double a) {
        this.x /= a;
        this.y /= a;
        this.z /= a;

        return this;
    }

    public Vector3d divided(double a) {
        return copy().divide(a);
    }

    public Vector3d scale(Vector3d v) {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;

        return this;
    }

    public Vector3d scaled(Vector3d v) {
        return copy().scale(v);
    }

    public Vector3d add(Vector3d v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;

        return this;
    }

    public Vector3d added(Vector3d v) {
        return copy().add(v);
    }

    public Vector3d subtract(Vector3d v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;

        return this;
    }

    public Vector3d subtracted(Vector3d v) {
        return copy().subtract(v);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public static Vector3d of(EnumFacing facing) {
        switch(facing) {
            case UP:
                return up();
            case DOWN:
                return down();
            case NORTH:
                return north();
            case SOUTH:
                return south();
            case EAST:
                return east();
            case WEST:
                return west();
            default:
                return zero();
        }
    }

    public static Vector3d up() {
        return new Vector3d(0, 1, 0);
    }

    public static Vector3d down() {
        return new Vector3d(0, -1, 0);
    }

    public static Vector3d north() {
        return new Vector3d(0, 0, -1);
    }

    public static Vector3d south() {
        return new Vector3d(0, 0, 1);
    }

    public static Vector3d east() {
        return new Vector3d(1, 0, 0);
    }

    public static Vector3d west() {
        return new Vector3d(-1, 0, 0);
    }

    public static Vector3d one() { return new Vector3d(1, 1, 1); }

    public static Vector3d zero() { return new Vector3d(0, 0, 0); }
}
