/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render.WingRenderer;

import java.awt.Color;
import net.minecraft.client.Minecraft;

public class ColorUtils {
    public static Color rainbow(long offset, float fade) {
        float hue = (float)(System.nanoTime() + offset) / 1.0E10f % 1.0f;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        Color c = new Color((int)color);
        return new Color((float)c.getRed() / 255.0f * fade, (float)c.getGreen() / 255.0f * fade, (float)c.getBlue() / 255.0f * fade, (float)c.getAlpha() / 255.0f);
    }

    public static int[] getFractionIndices(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions.length == colors.length) {
            int[] indices = ColorUtils.getFractionIndices(fractions, progress);
            float[] range = new float[]{fractions[indices[0]], fractions[indices[1]]};
            Color[] colorRange = new Color[]{colors[indices[0]], colors[indices[1]]};
            float max = range[1] - range[0];
            float value = progress - range[0];
            float weight = value / max;
            Color color = ColorUtils.blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float)ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);
        return color3;
    }

    public static Color getHealthColorint(float health, float maxHealth) {
        float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
        Color[] colors = new Color[]{new Color(108, 0, 0), new Color(255, 51, 0), Color.GREEN};
        float progress = health / maxHealth;
        return ColorUtils.blendColors(fractions, colors, progress).brighter();
    }

    public static int getHealthColor(float health, float maxHealth) {
        float percentage = health / maxHealth;
        if (percentage >= 0.75f) {
            return new Color(100, 200, 100).getRGB();
        }
        if ((double)percentage < 0.75 && (double)percentage >= 0.25) {
            return new Color(200, 200, 100).getRGB();
        }
        return new Color(200, 75, 75).getRGB();
    }

    public Color getAstolfoRainbow(int v1) {
        double d = 0.0;
        double delay = Math.ceil((double)(System.currentTimeMillis() + (long)v1 * 70L) / 5.0);
        float rainbow = (double)((float)(d / 420.0)) < 0.5 ? -((float)(delay / 420.0)) : (float)(delay % 420.0 / 420.0);
        return Color.getHSBColor(rainbow, 0.5f, 1.0f);
    }

    public static Color getRainbow(float second, float sat, float bright) {
        float hue = (float)(System.currentTimeMillis() % (long)((int)(second * 1000.0f))) / (second * 1000.0f);
        return new Color(Color.HSBtoRGB(hue, sat, bright));
    }

    public static Color getBlendColor(double current, double max) {
        long base = Math.round(max / 5.0);
        if (current >= (double)(base * 5L)) {
            return new Color(15, 255, 15);
        }
        if (current >= (double)(base << 2)) {
            return new Color(166, 255, 0);
        }
        if (current >= (double)(base * 3L)) {
            return new Color(255, 191, 0);
        }
        if (current >= (double)(base << 1)) {
            return new Color(255, 89, 0);
        }
        return new Color(255, 0, 0);
    }

    public static Color fastrainbow() {
        boolean rainbowTick = false;
        return new Color(Color.HSBtoRGB((float)((double)Minecraft.thePlayer.ticksExisted / 50.0 + Math.sin((double)rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
    }
}

