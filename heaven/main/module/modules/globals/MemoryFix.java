/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.globals;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.chat.Helper;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;

public class MemoryFix
extends Module {
    public static final Mode<String> mode = new Mode("ClearMode", new String[]{"Simple", "Advanced"}, "Simple");
    public static final Option<Boolean> disableMinecraftGC = new Option<Boolean>("DisableMinecraftGC", true);
    public static final Option<Boolean> onlyUseSystemGC = new Option<Boolean>("OnlyUseSystemGC", true, () -> mode.is("Advanced"));
    private final Numbers<Double> delay = new Numbers<Double>("Delay", 120.0, 10.0, 600.0, 5.0, () -> (Boolean)onlyUseSystemGC.get() == false, () -> mode.is("Advanced"));
    private final Numbers<Double> limit = new Numbers<Double>("Limit", 80.0, 20.0, 95.0, 1.0, () -> (Boolean)onlyUseSystemGC.get() == false, () -> mode.is("Advanced"));
    private final Option<Boolean> debug = new Option<Boolean>("DebugMessage", true, () -> (Boolean)onlyUseSystemGC.get() == false, () -> mode.is("Advanced"));
    private final TimerUtil timer = new TimerUtil();

    public MemoryFix() {
        super("MemoryFixer", ModuleType.Globals);
        this.addValues(mode, this.delay, this.limit, disableMinecraftGC, onlyUseSystemGC, this.debug);
        this.setRemoved(true);
        this.setEnabled(true);
    }

    @EventHandler
    public void onTick(EventTick e) {
        if (mode.is("Advanced") && !((Boolean)onlyUseSystemGC.get()).booleanValue()) {
            long maxmem = Runtime.getRuntime().maxMemory();
            long totalmem = Runtime.getRuntime().totalMemory();
            long freemem = Runtime.getRuntime().freeMemory();
            long usemem = totalmem - freemem;
            float pct = usemem * 100L / maxmem;
            if (this.timer.hasReached((Double)this.delay.getValue() * 1000.0) && (Double)this.limit.getValue() <= (double)pct) {
                if (((Boolean)this.debug.getValue()).booleanValue()) {
                    Helper.sendMessage("MemoryCleaned!");
                }
                Runtime.getRuntime().gc();
                this.timer.reset();
            }
        }
    }
}

