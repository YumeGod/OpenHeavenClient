/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.globals;

import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Numbers;

public class LowFire
extends Module {
    public static final Numbers<Number> y = new Numbers<Double>("Y", -0.1, -2.0, 2.0, 0.05);

    public LowFire() {
        super("LowFire", ModuleType.Globals);
        this.addValues(y);
        this.setRemoved(true);
    }
}

