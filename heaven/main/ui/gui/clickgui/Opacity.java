/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.clickgui;

import heaven.main.ui.gui.clickgui.AnimationUtil;

public class Opacity {
    private float opacity;
    private final float opacity2;
    private long lastMS;

    public Opacity(int opacity) {
        this.opacity = opacity;
        this.opacity2 = 120.0f;
        this.lastMS = System.currentTimeMillis();
    }

    public void interpolate(float targetOpacity) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.opacity = AnimationUtil.calculateCompensation(targetOpacity, this.opacity, delta, 20);
    }

    public float getOpacity() {
        return (int)this.opacity;
    }

    public float shadowAnim() {
        return (int)this.opacity2;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
}

