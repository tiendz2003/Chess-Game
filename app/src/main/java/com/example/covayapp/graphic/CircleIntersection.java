package com.example.covayapp.graphic;

import androidx.annotation.NonNull;

public final class CircleIntersection {
    public final Circle c1;
    public final Circle c2;
    public final double distanceC1cC2c;
    public final double distanceC1cRadicalLine;
    public final double distanceC2cRadicalLine;
    public final double distanceRadicalPointIntersectionPoints;
    public final Vector intersectionPoint;
    public final Vector intersectionPoint1;
    public final Vector intersectionPoint2;
    public final Vector radicalPoint;
    public final Type type;
    public final Vector versorC1cC2c;
    public final Vector versorRadicalLine;

    public enum Type {
        COINCIDENT(-1),
        CONCENTRIC_CONTAINED(0),
        ECCENTRIC_CONTAINED(0),
        INTERNALLY_TANGENT(1),
        OVERLAPPING(2),
        EXTERNALLY_TANGENT(1),
        SEPARATE(0);

        private final int n;

        private Type(int n2) {
            this.n = n2;
        }

        public int getIntersectionPointCount() {
            return this.n;
        }

        public boolean isConcentric() {
            return this == COINCIDENT || this == CONCENTRIC_CONTAINED;
        }

        public boolean isContained() {
            return this == CONCENTRIC_CONTAINED || this == ECCENTRIC_CONTAINED;
        }

        public boolean isTangent() {
            return this.n == 1;
        }

        public boolean isDisjoint() {
            return this.n == 0;
        }
    }

    public CircleIntersection(Circle c12, Circle c22) {
        this.c1 = c12;
        this.c2 = c22;
        Vector vectorC1cC2c = c22.c.sub(c12.c);
        this.distanceC1cC2c = vectorC1cC2c.mod();
        if (this.distanceC1cC2c == 0.0d) {
            if (c12.r == c22.r) {
                this.type = Type.COINCIDENT;
            } else {
                this.type = Type.CONCENTRIC_CONTAINED;
            }
            this.radicalPoint = null;
            this.distanceC1cRadicalLine = 0.0d;
            this.distanceC2cRadicalLine = 0.0d;
            this.versorC1cC2c = null;
            this.versorRadicalLine = null;
            this.intersectionPoint = null;
            this.intersectionPoint1 = null;
            this.intersectionPoint2 = null;
            this.distanceRadicalPointIntersectionPoints = 0.0d;
            return;
        }
        this.versorC1cC2c = vectorC1cC2c.scale(1.0d / this.distanceC1cC2c);
        this.distanceC1cRadicalLine = ((sq(this.distanceC1cC2c) + sq(c12.r)) - sq(c22.r)) / (2.0d * this.distanceC1cC2c);
        this.distanceC2cRadicalLine = this.distanceC1cC2c - this.distanceC1cRadicalLine;
        this.radicalPoint = c12.c.add(this.versorC1cC2c.scale(this.distanceC1cRadicalLine));
        this.versorRadicalLine = this.versorC1cC2c.rotPlus90();
        double sqH = sq(c12.r) - sq(this.distanceC1cRadicalLine);
        if (sqH > 0.0d) {
            this.type = Type.OVERLAPPING;
            this.intersectionPoint = null;
            this.distanceRadicalPointIntersectionPoints = Math.sqrt(sqH);
            this.intersectionPoint1 = this.radicalPoint.add(this.versorRadicalLine.scale(this.distanceRadicalPointIntersectionPoints));
            this.intersectionPoint2 = this.radicalPoint.add(this.versorRadicalLine.scale(-this.distanceRadicalPointIntersectionPoints));
            return;
        }
        boolean external = this.distanceC1cC2c > Math.max(c12.r, c22.r);
        if (sqH == 0.0d) {
            this.type = external ? Type.EXTERNALLY_TANGENT : Type.INTERNALLY_TANGENT;
            this.intersectionPoint = this.radicalPoint;
            this.intersectionPoint1 = null;
            this.intersectionPoint2 = null;
            this.distanceRadicalPointIntersectionPoints = 0.0d;
            return;
        }
        this.type = external ? Type.SEPARATE : Type.ECCENTRIC_CONTAINED;
        this.intersectionPoint = null;
        this.intersectionPoint1 = null;
        this.intersectionPoint2 = null;
        this.distanceRadicalPointIntersectionPoints = 0.0d;
    }

    public Vector[] getIntersectionPoints() {
        Vector v1;
        Vector v2;
        switch (this.type.getIntersectionPointCount()) {
            case 0:
                return new Vector[0];
            case 1:
                return new Vector[]{this.intersectionPoint};
            case 2:
                if (this.intersectionPoint1.x() < this.intersectionPoint2.x()) {
                    v1 = new Vector((double) this.intersectionPoint1.x(), (double) this.intersectionPoint1.y());
                    v2 = new Vector((double) this.intersectionPoint2.x(), (double) this.intersectionPoint2.y());
                } else if (this.intersectionPoint1.x() != this.intersectionPoint2.x()) {
                    v1 = new Vector((double) this.intersectionPoint2.x(), (double) this.intersectionPoint2.y());
                    v2 = new Vector((double) this.intersectionPoint1.x(), (double) this.intersectionPoint1.y());
                } else if (this.intersectionPoint1.y() < this.intersectionPoint2.y()) {
                    v1 = new Vector((double) this.intersectionPoint1.x(), (double) this.intersectionPoint1.y());
                    v2 = new Vector((double) this.intersectionPoint2.x(), (double) this.intersectionPoint2.y());
                } else {
                    v1 = new Vector((double) this.intersectionPoint2.x(), (double) this.intersectionPoint2.y());
                    v2 = new Vector((double) this.intersectionPoint1.x(), (double) this.intersectionPoint1.y());
                }
                return new Vector[]{v1, v2};
            default:
                throw new IllegalStateException("Coincident circles");
        }
    }

    private double sq(double a) {
        return a * a;
    }

    @NonNull
    public String toString() {
        return getClass().getSimpleName() + "(c1: " + this.c1 + ", c2: " + this.c2 + ", type: " + this.type + ", distanceC1cC2c: " + this.distanceC1cC2c + ", radicalPoint: " + this.radicalPoint + ", distanceC1cRadicalLine: " + this.distanceC1cRadicalLine + ", distanceC2cRadicalLine: " + this.distanceC2cRadicalLine + ", versorC1cC2c: " + this.versorC1cC2c + ", versorRadicalLine: " + this.versorRadicalLine + ", intersectionPoint: " + this.intersectionPoint + ", intersectionPoint1: " + this.intersectionPoint1 + ", intersectionPoint2: " + this.intersectionPoint2 + ", distanceRadicalPointIntersectionPoints: " + this.distanceRadicalPointIntersectionPoints + ")";
    }
}