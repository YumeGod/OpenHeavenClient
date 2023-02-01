/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.WingRenderer.RenderWings;
import heaven.main.value.Numbers;
import heaven.main.value.Option;

public class Wings
extends Module {
    public static final Option<Boolean> rainbow = new Option<Boolean>("Rainbow", false);
    public static final Numbers<Double> red = new Numbers<Double>("Red", 255.0, 0.0, 255.0, 1.0, () -> (Boolean)rainbow.getValue() == false);
    public static final Numbers<Double> blue = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 1.0, () -> (Boolean)rainbow.getValue() == false);
    public static final Numbers<Double> green = new Numbers<Double>("Green", 255.0, 0.0, 255.0, 1.0, () -> (Boolean)rainbow.getValue() == false);
    public static final Numbers<Double> Size = new Numbers<Double>("Size", 0.0625, 0.0045, 0.1, 1.0E-4);
    public static final Numbers<Double> rotateSpeed = new Numbers<Double>("RotateSpeed", 2.0, 0.0, 5.0, 1.0);

    public Wings() {
        super("Wings", new String[]{"Wings"}, ModuleType.Render);
        this.addValues(red, blue, green, Size, rotateSpeed, rainbow);
    }

    @EventHandler
    public void onRenderPlayer(EventRender3D event) {
        RenderWings renderWings = new RenderWings();
        renderWings.renderWings(1.0f);
    }
}

