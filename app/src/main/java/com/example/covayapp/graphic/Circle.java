package com.example.covayapp.graphic;

import java.io.Serializable;

public final class Circle implements Serializable {
    private static final long serialVersionUID = 1;
    public final Vector c;
    public final double r;

    public Circle(Vector c2, double r2) {
        if (r2 <= 0.0d) {
            throw new IllegalArgumentException("Radius must be positive");
        }
        this.c = c2;
        this.r = r2;
    }

    public int hashCode() {
        int result = (this.c == null ? 0 : this.c.hashCode()) + 31;
        long temp = Double.doubleToLongBits(this.r);
        return (result * 31) + ((int) ((temp >>> 32) ^ temp));
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
        Circle other = (Circle) obj;
        if (this.c == null) {
            if (other.c != null) {
                return false;
            }
        } else if (!this.c.equals(other.c)) {
            return false;
        }
        if (Double.doubleToLongBits(this.r) != Double.doubleToLongBits(other.r)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return getClass().getSimpleName() + "(c: " + this.c + ", r: " + this.r + ")";
    }
}
