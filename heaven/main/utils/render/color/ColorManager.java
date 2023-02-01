/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.render.color;

import heaven.main.module.modules.render.HUD;
import heaven.main.utils.render.color.ColorObject;
import java.awt.Color;

public class ColorManager {
    public static final ColorObject eVis = new ColorObject(255, 0, 0, 255);
    public static final ColorObject eInvis = new ColorObject(255, 255, 0, 255);
    public static final ColorObject hudColor = new ColorObject(220, 1, 5, 255);

    public static int HUDColor() {
        return new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB();
    }

    public static Color fade(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static ColorObject getEnemyVisible() {
        return eVis;
    }

    public static ColorObject getEnemyInvisible() {
        return eInvis;
    }

    public static int astolfoRainbow(int delay, int offset, int index) {
        double d;
        double rainbowDelay = Math.ceil(System.currentTimeMillis() + (long)(delay * index)) / (double)offset;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(rainbowDelay / 360.0)) : (float)((rainbowDelay %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }
}

