/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.opengl.renderer;

import com.me.theresa.fontRenderer.font.opengl.renderer.DefaultLineStripRenderer;
import com.me.theresa.fontRenderer.font.opengl.renderer.ImmediateModeOGLRenderer;
import com.me.theresa.fontRenderer.font.opengl.renderer.LineStripRenderer;
import com.me.theresa.fontRenderer.font.opengl.renderer.QuadBasedLineStripRenderer;
import com.me.theresa.fontRenderer.font.opengl.renderer.SGL;
import com.me.theresa.fontRenderer.font.opengl.renderer.VAOGLRenderer;

public class Renderer {
    public static final int IMMEDIATE_RENDERER = 1;
    public static final int VERTEX_ARRAY_RENDERER = 2;
    public static final int DEFAULT_LINE_STRIP_RENDERER = 3;
    public static final int QUAD_BASED_LINE_STRIP_RENDERER = 4;
    private static SGL renderer = new ImmediateModeOGLRenderer();
    private static LineStripRenderer lineStripRenderer = new DefaultLineStripRenderer();

    public static void setRenderer(int type) {
        switch (type) {
            case 1: {
                renderer = new ImmediateModeOGLRenderer();
                return;
            }
            case 2: {
                renderer = new VAOGLRenderer();
                return;
            }
        }
        throw new RuntimeException("Unknown renderer type: " + type);
    }

    public static void setLineStripRenderer(int type) {
        switch (type) {
            case 3: {
                lineStripRenderer = new DefaultLineStripRenderer();
                return;
            }
            case 4: {
                lineStripRenderer = new QuadBasedLineStripRenderer();
                return;
            }
        }
        throw new RuntimeException("Unknown line strip renderer type: " + type);
    }

    public static void setLineStripRenderer(LineStripRenderer renderer) {
        lineStripRenderer = renderer;
    }

    public static void setRenderer(SGL r) {
        renderer = r;
    }

    public static SGL get() {
        return renderer;
    }

    public static LineStripRenderer getLineStripRenderer() {
        return lineStripRenderer;
    }
}

