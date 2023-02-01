/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render.ParticlesUtils;

import heaven.main.module.modules.render.ParticlesUtils.Location;

public class Particles {
    public int ticks;
    public final Location location;
    public final String text;

    public Particles(Location location, String text) {
        this.location = location;
        this.text = text;
        this.ticks = 0;
    }
}

