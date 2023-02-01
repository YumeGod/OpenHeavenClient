/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventEntityBorderSize;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Numbers;

public class HitBox
extends Module {
    public final Numbers<Double> size = new Numbers<Double>("Size", 0.3, 0.1, 1.0, 0.1);

    public HitBox() {
        super("HitBoxes", ModuleType.Combat);
        this.addValues(this.size);
    }

    @EventHandler
    public void onUpdate(EventEntityBorderSize event) {
        event.setBorderSize(((Double)this.size.getValue()).floatValue());
    }
}

