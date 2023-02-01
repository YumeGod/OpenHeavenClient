/*
 * Decompiled with CFR 0.152.
 */
package javax.vecmath;

import javax.vecmath.Tuple2d;

public class Point2d
extends Tuple2d {
    static final long serialVersionUID = 1133748791492571954L;

    public final double distanceSquared(Point2d p1) {
        double dx = this.x - p1.x;
        double dy = this.y - p1.y;
        return dx * dx + dy * dy;
    }

    public final double distance(Point2d p1) {
        double dx = this.x - p1.x;
        double dy = this.y - p1.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public final double distanceL1(Point2d p1) {
        return Math.abs(this.x - p1.x) + Math.abs(this.y - p1.y);
    }

    public final double distanceLinf(Point2d p1) {
        return Math.max(Math.abs(this.x - p1.x), Math.abs(this.y - p1.y));
    }
}

