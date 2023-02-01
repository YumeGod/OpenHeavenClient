/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.opengl.renderer;

import com.me.theresa.fontRenderer.font.opengl.renderer.LineStripRenderer;
import com.me.theresa.fontRenderer.font.opengl.renderer.Renderer;
import com.me.theresa.fontRenderer.font.opengl.renderer.SGL;

public class DefaultLineStripRenderer
implements LineStripRenderer {
    private final SGL GL = Renderer.get();

    @Override
    public void end() {
        this.GL.glEnd();
    }

    @Override
    public void setAntiAlias(boolean antialias) {
        if (antialias) {
            this.GL.glEnable(2848);
        } else {
            this.GL.glDisable(2848);
        }
    }

    @Override
    public void setWidth(float width) {
        this.GL.glLineWidth(width);
    }

    @Override
    public void start() {
        this.GL.glBegin(3);
    }

    @Override
    public void vertex(float x, float y) {
        this.GL.glVertex2f(x, y);
    }

    @Override
    public void color(float r, float g, float b, float a) {
        this.GL.glColor4f(r, g, b, a);
    }

    @Override
    public void setLineCaps(boolean caps) {
    }

    @Override
    public boolean applyGLLineFixes() {
        return true;
    }
}

