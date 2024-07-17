package com.example.covayapp.graphic;


import java.io.Serializable;

public final class Vector implements Serializable {
    public static final Vector NULL = new Vector(0.0d, 0.0d);
    public static final Vector X = new Vector(1.0d, 0.0d);
    public static final Vector Y = new Vector(0.0d, 1.0d);
    private static final long serialVersionUID = 1;
    public final double x;
    public final double y;

    public float x() {
        return (float) this.x;
    }

    public float y() {
        return (float) this.y;
    }

    public Vector(double x2, double y2) {
        this.x = x2;
        this.y = y2;
    }

    public Vector add(Vector a) {
        return new Vector(this.x + a.x, this.y + a.y);
    }

    public Vector sub(Vector a) {
        return new Vector(this.x - a.x, this.y - a.y);
    }

    public Vector neg() {
        return new Vector(-this.x, -this.y);
    }

    public Vector scale(double a) {
        return new Vector(this.x * a, this.y * a);
    }

    public double dot(Vector a) {
        return (this.x * a.x) + (this.y * a.y);
    }

    public double modSquared() {
        return dot(this);
    }

    public double mod() {
        return Math.sqrt(modSquared());
    }

    public Vector normalize() {
        return scale(1.0d / mod());
    }

    public Vector rotPlus90() {
        return new Vector(-this.y, this.x);
    }

    public Vector rotMinus90() {
        return new Vector(this.y, -this.x);
    }

    public double angle() {
        return Math.atan2(this.y, this.x);
    }

    public static Vector fromAngle(double ang) {
        return new Vector(Math.cos(ang), Math.sin(ang));
    }

    public static Vector fromPolar(double ang, double mod) {
        return new Vector(Math.cos(ang) * mod, Math.sin(ang) * mod);
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.x);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        long temp2 = Double.doubleToLongBits(this.y);
        return (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Vector other = (Vector) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return getClass().getSimpleName() + "(" + this.x + ", " + this.y + ")";
    }
}
