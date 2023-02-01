/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.utils.color.ColorUtils
 */
package heaven.main.module.modules.render;

import com.utils.color.ColorUtils;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.value.Numbers;
import net.minecraft.client.gui.ScaledResolution;

public class RainbowRect
extends Module {
    private final Numbers<Double> width = new Numbers<Double>("Width", 2.0, 0.0, 100.0, 0.1);
    private int prevRainbowTicks;

    public RainbowRect() {
        super("RainbowRect", ModuleType.Render);
        this.addValues(this.width);
    }

    @EventHandler
    public void on2D(EventRender2D e) {
        int color;
        int i;
        double width = (Double)this.width.getValue();
        int offset = 5;
        int rainbowTicks = this.prevRainbowTicks;
        ScaledResolution sr = new ScaledResolution(mc);
        for (i = 0; i < sr.getScaledWidth(); ++i) {
            if ((double)i > width - 1.0) {
                color = ColorUtils.rainbowInt((int)rainbowTicks);
                RenderUtil.drawRect((double)i, 0.0, (double)(i + 1), width, color);
            }
            rainbowTicks += offset;
        }
        for (i = 0; i < sr.getScaledHeight(); ++i) {
            color = ColorUtils.rainbowInt((int)rainbowTicks);
            RenderUtil.drawRect((double)sr.getScaledWidth() - width, (double)i, (double)sr.getScaledWidth(), (double)(i + 1), color);
            rainbowTicks += offset;
        }
        for (i = sr.getScaledWidth(); i > 0; --i) {
            color = ColorUtils.rainbowInt((int)rainbowTicks);
            RenderUtil.drawRect((double)i, (double)sr.getScaledHeight() - width, (double)(i - 1), (double)sr.getScaledHeight(), color);
            rainbowTicks += offset;
        }
        for (i = sr.getScaledHeight(); i > 0; --i) {
            color = ColorUtils.rainbowInt((int)rainbowTicks);
            RenderUtil.drawRect(0.0, (double)i, width, (double)(i - 1), color);
            rainbowTicks += offset;
        }
        this.prevRainbowTicks = Math.min(rainbowTicks, 100);
    }
}

