/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;

public class EnchantEffect
extends Module {
    public static float hue;
    public static final Option<Boolean> HUDColor;
    public static final Option<Boolean> Rainbow;
    public static final Option<Boolean> fade;
    public final Numbers<Double> RainbowSpeed = new Numbers<Double>("RainbowSpeed", 6.0, 1.0, 20.0, 1.0, Rainbow::getValue, () -> (Boolean)HUDColor.get() == false);
    public static final Numbers<Double> r;
    public static final Numbers<Double> g;
    public static final Numbers<Double> b;

    public EnchantEffect() {
        super("EnchantEffect", ModuleType.Render);
        this.addValues(HUDColor, r, g, b, fade, Rainbow, this.RainbowSpeed);
    }

    @EventHandler
    public void Render2d(EventRender2D e) {
        if (((Boolean)HUDColor.get()).booleanValue()) {
            return;
        }
        if ((hue += ((Double)this.RainbowSpeed.getValue()).floatValue() / 5.0f) > 255.0f) {
            hue = 0.0f;
        }
    }

    public static Color getEnchantColor() {
        if (((Boolean)HUDColor.get()).booleanValue()) {
            if (((Boolean)HUD.rainbow.get()).booleanValue()) {
                return HUD.rainbowToEffect();
            }
            return (Boolean)HUD.Breathinglamp.get() != false ? HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 70, ((Double)HUD.ez.get()).intValue()) : new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue());
        }
        if (((Boolean)Rainbow.getValue()).booleanValue()) {
            return Color.getHSBColor(hue / 255.0f, 0.75f, 0.9f);
        }
        return (Boolean)fade.get() != false ? HUD.fade(new Color(((Double)r.getValue()).intValue(), ((Double)g.getValue()).intValue(), ((Double)b.getValue()).intValue()), 70, ((Double)HUD.ez.get()).intValue()) : new Color(((Double)r.getValue()).intValue(), ((Double)g.getValue()).intValue(), ((Double)b.getValue()).intValue());
    }

    static {
        HUDColor = new Option<Boolean>("HUDColor", true);
        Rainbow = new Option<Boolean>("Rainbow", false, () -> (Boolean)HUDColor.get() == false);
        fade = new Option<Boolean>("Fade", false, () -> (Boolean)Rainbow.get() == false, () -> (Boolean)HUDColor.get() == false);
        r = new Numbers<Double>("Red", 120.0, 0.0, 255.0, 5.0, () -> (Boolean)Rainbow.getValue() == false, () -> (Boolean)HUDColor.get() == false);
        g = new Numbers<Double>("Green", 120.0, 0.0, 255.0, 5.0, () -> (Boolean)Rainbow.getValue() == false, () -> (Boolean)HUDColor.get() == false);
        b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0, () -> (Boolean)Rainbow.getValue() == false, () -> (Boolean)HUDColor.get() == false);
    }
}

