/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.math;

import heaven.main.utils.math.Angle;
import javax.vecmath.Vector3d;

public class AngleUtility {
    private final float smooth;

    public AngleUtility(float smooth) {
        this.smooth = smooth;
    }

    public static Angle calculateAngle(Vector3d class1600, Vector3d class1601) {
        Angle class1602 = new Angle();
        class1600.x -= class1601.x;
        class1600.y -= class1601.y;
        class1600.z -= class1601.z;
        class1602.setYaw((float)(Math.atan2(class1600.z, class1600.x) * 57.29577951308232) - 90.0f);
        class1602.setPitch(-((float)(Math.atan2(class1600.y, Math.hypot(class1600.x, class1600.z)) * 57.29577951308232)));
        return class1602.constrantAngle();
    }

    public Angle smoothAngle(Angle destination, Angle source) {
        Angle angles = new Angle(source.getYaw() - destination.getYaw(), source.getPitch() - destination.getPitch()).constrantAngle();
        angles.setYaw(source.getYaw() - angles.getYaw() / 100.0f * this.smooth);
        angles.setPitch(source.getPitch() - angles.getPitch() / 100.0f * this.smooth);
        return angles.constrantAngle();
    }
}

