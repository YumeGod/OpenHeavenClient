/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GLContext
 *  org.lwjgl.opengl.Pbuffer
 */
package com.me.theresa.fontRenderer.font.opengl.pbuffer;

import com.me.theresa.fontRenderer.font.Graphics;
import com.me.theresa.fontRenderer.font.Image;
import com.me.theresa.fontRenderer.font.SlickException;
import com.me.theresa.fontRenderer.font.log.Log;
import com.me.theresa.fontRenderer.font.opengl.pbuffer.FBOGraphics;
import com.me.theresa.fontRenderer.font.opengl.pbuffer.PBufferGraphics;
import com.me.theresa.fontRenderer.font.opengl.pbuffer.PBufferUniqueGraphics;
import java.util.HashMap;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.Pbuffer;

public class GraphicsFactory {
    private static final HashMap graphics = new HashMap();
    private static boolean pbuffer = true;
    private static boolean pbufferRT = true;
    private static boolean fbo = true;
    private static boolean init = false;

    private static void init() throws SlickException {
        init = true;
        if (fbo) {
            fbo = GLContext.getCapabilities().GL_EXT_framebuffer_object;
        }
        pbuffer = (Pbuffer.getCapabilities() & 1) != 0;
        boolean bl = pbufferRT = (Pbuffer.getCapabilities() & 2) != 0;
        if (!(fbo || pbuffer || pbufferRT)) {
            throw new SlickException("Your OpenGL card does not support offscreen buffers and hence can't handle the dynamic images required for this application.");
        }
        Log.info("Offscreen Buffers FBO=" + fbo + " PBUFFER=" + pbuffer + " PBUFFERRT=" + pbufferRT);
    }

    public static void setUseFBO(boolean useFBO) {
        fbo = useFBO;
    }

    public static boolean usingFBO() {
        return fbo;
    }

    public static boolean usingPBuffer() {
        return !fbo && pbuffer;
    }

    public static Graphics getGraphicsForImage(Image image) throws SlickException {
        Graphics g = (Graphics)graphics.get(image.getTexture());
        if (g == null) {
            g = GraphicsFactory.createGraphics(image);
            graphics.put(image.getTexture(), g);
        }
        return g;
    }

    public static void releaseGraphicsForImage(Image image) {
        Graphics g = (Graphics)graphics.remove(image.getTexture());
        if (g != null) {
            g.destroy();
        }
    }

    private static Graphics createGraphics(Image image) throws SlickException {
        GraphicsFactory.init();
        if (fbo) {
            try {
                return new FBOGraphics(image);
            }
            catch (Exception e) {
                fbo = false;
                Log.warn("FBO failed in use, falling back to PBuffer");
            }
        }
        if (pbuffer) {
            if (pbufferRT) {
                return new PBufferGraphics(image);
            }
            return new PBufferUniqueGraphics(image);
        }
        throw new SlickException("Failed to create offscreen buffer even though the card reports it's possible");
    }
}

