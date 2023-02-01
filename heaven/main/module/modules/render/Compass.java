/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.CompassUtil.BackgroundMode;
import heaven.main.module.modules.render.CompassUtil.CompassUtil;
import heaven.main.value.Mode;
import net.minecraft.client.gui.ScaledResolution;

public class Compass
extends Module {
    private final CompassUtil compass;
    private final BackgroundMode compass2 = new BackgroundMode();
    private final String[] modes = new String[]{"Jello", "Shadow"};
    private final Mode<String> mode = new Mode("Mode", this.modes, this.modes[0]);

    public Compass() {
        super("Compass", ModuleType.Render);
        this.compass = new CompassUtil(325.0f, 325.0f, 1.0f, 2, true);
        this.addValues(this.mode);
    }

    @EventHandler
    private void renderHud(EventRender2D event) {
        if (this.mode.isCurrentMode("Jello")) {
            this.compass.draw(new ScaledResolution(mc));
        }
        if (this.mode.isCurrentMode("Shadow")) {
            this.compass2.drawCompass(ScaledResolution.getScaledWidth());
        }
    }
}

