/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.guimainmenu;

import java.awt.Color;
import java.util.ArrayList;

public class ColorCreator {
    private static final ArrayList<Color> loggedColors = new ArrayList();

    public static int create(int r, int g, int b) {
        for (Color color : loggedColors) {
            if (color.getRed() != r || color.getGreen() != g || color.getBlue() != b || color.getAlpha() != 255) continue;
            return color.getRGB();
        }
        Color color = new Color(r, g, b);
        loggedColors.add(color);
        return color.getRGB();
    }

    public static int create(int r, int g, int b, int a) {
        for (Color color : loggedColors) {
            if (color.getRed() != r || color.getGreen() != g || color.getBlue() != b || color.getAlpha() != a) continue;
            return color.getRGB();
        }
        Color color = new Color(r, g, b, a);
        loggedColors.add(color);
        return color.getRGB();
    }

    public static int createRainbowFromOffset(int speed, int offset) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor(hue / (float)speed, 0.6f, 1.0f).getRGB();
    }
}

