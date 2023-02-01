/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.render.color;

import heaven.main.utils.render.color.Colors;

public class ColorObject {
    public final int red;
    public final int green;
    public final int blue;
    public final int alpha;

    public ColorObject(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public int getColorInt() {
        return Colors.getColor(this.red, this.green, this.blue, this.alpha);
    }
}

