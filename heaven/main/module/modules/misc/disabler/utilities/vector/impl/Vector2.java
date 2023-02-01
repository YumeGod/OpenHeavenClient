/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc.disabler.utilities.vector.impl;

import heaven.main.module.modules.misc.disabler.utilities.vector.Vector;
import heaven.main.module.modules.misc.disabler.utilities.vector.impl.Vector3;

public class Vector2<T extends Number>
extends Vector<Number> {
    public Vector2(T x, T y) {
        super(x, y, 0);
    }

    public Vector3<T> toVector3() {
        return new Vector3(this.getX(), this.getY(), this.getZ());
    }
}

