/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Numbers;

public class EntityHurtColor
extends Module {
    public static final Numbers<Double> r = new Numbers<Double>("Red", 120.0, 0.0, 255.0, 5.0);
    public static final Numbers<Double> g = new Numbers<Double>("Green", 120.0, 0.0, 255.0, 5.0);
    public static final Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0);
    public static final Numbers<Double> alpha = new Numbers<Double>("Alpha", 255.0, 0.0, 255.0, 5.0);

    public EntityHurtColor() {
        super("EntityHurtColor", ModuleType.Render);
        this.addValues(r, g, b, alpha);
    }
}

