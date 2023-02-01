/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.render.color;

public enum Colors {
    BLACK(-16711423),
    WHITE(-65794),
    RED(-65536),
    ORANGE(-29696);

    public final int c;

    private Colors(int co) {
        this.c = co;
    }

    public static int getColor(int brightness) {
        return Colors.getColor(brightness, brightness, brightness, 255);
    }

    public static int getColor(int brightness, int alpha) {
        return Colors.getColor(brightness, brightness, brightness, alpha);
    }

    public static int getColor(int red, int green, int blue) {
        return Colors.getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }
}

