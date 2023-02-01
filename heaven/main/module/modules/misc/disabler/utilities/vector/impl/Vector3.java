/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc.disabler.utilities.vector.impl;

import heaven.main.module.modules.misc.disabler.utilities.vector.Vector;
import heaven.main.module.modules.misc.disabler.utilities.vector.impl.Vector2;

public class Vector3<T extends Number>
extends Vector<Number> {
    public Vector3(T x, T y, T z) {
        super(x, y, z);
    }

    public Vector2<T> toVector2() {
        return new Vector2(this.getX(), this.getY());
    }
}

