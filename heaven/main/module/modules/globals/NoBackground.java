/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.globals;

import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Option;

public class NoBackground
extends Module {
    public static final Option<Boolean> inventory = new Option<Boolean>("Inventory", true);
    public static final Option<Boolean> chest = new Option<Boolean>("Chest", true);
    public static final Option<Boolean> gameMenu = new Option<Boolean>("GameMenu", true);

    public NoBackground() {
        super("NoBackground", new String[]{"nobg"}, ModuleType.Globals);
        this.addValues(inventory, chest, gameMenu);
        this.setRemoved(true);
    }
}

