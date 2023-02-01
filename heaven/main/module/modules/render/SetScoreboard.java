/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Numbers;
import heaven.main.value.Option;

public class SetScoreboard
extends Module {
    public final Option<Boolean> hideboard = new Option<Boolean>("HideBoard", false);
    public static final Option<Boolean> fastbord = new Option<Boolean>("FastBord", false);
    public final Option<Boolean> Norednumber = new Option<Boolean>("NoRedNumber", false);
    public static final Option<Boolean> noServername = new Option<Boolean>("NoServerName", false);
    public final Option<Boolean> noanyfont = new Option<Boolean>("NoAnyFont", false);
    public static final Numbers<Double> X = new Numbers<Double>("X", 4.5, 0.0, 1000.0, 1.0);
    public static final Numbers<Double> Y = new Numbers<Double>("Y", 4.5, -300.0, 300.0, 1.0);

    public SetScoreboard() {
        super("Scoreboard", new String[]{"SetScoreboard"}, ModuleType.Render);
        this.addValues(X, Y, this.hideboard, fastbord, this.Norednumber, noServername, this.noanyfont);
    }
}

