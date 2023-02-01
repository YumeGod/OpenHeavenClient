/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.LWJGLException
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.Pbuffer
 *  org.lwjgl.opengl.PixelFormat
 *  org.lwjgl.opengl.RenderTexture
 */
package com.me.theresa.fontRenderer.font.opengl.pbuffer;

import com.me.theresa.fontRenderer.font.Graphics;
import com.me.theresa.fontRenderer.font.Image;
import com.me.theresa.fontRenderer.font.SlickException;
import com.me.theresa.fontRenderer.font.log.Log;
import com.me.theresa.fontRenderer.font.opengl.InternalTextureLoader;
import com.me.theresa.fontRenderer.font.opengl.SlickCallable;
import com.me.theresa.fontRenderer.font.opengl.Texture;
import com.me.theresa.fontRenderer.font.opengl.TextureImpl;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.RenderTexture;

public class PBufferGraphics
extends Graphics {
    private Pbuffer pbuffer;
    private final Image image;

    public PBufferGraphics(Image image) throws SlickException {
        super(image.getTexture().getTextureWidth(), image.getTexture().getTextureHeight());
        this.image = image;
        Log.debug("Creating pbuffer(rtt) " + image.getWidth() + "x" + image.getHeight());
        if ((Pbuffer.getCapabilities() & 1) == 0) {
            throw new SlickException("Your OpenGL card does not support PBuffers and hence can't handle the dynamic images required for this application.");
        }
        if ((Pbuffer.getCapabilities() & 2) == 0) {
            throw new SlickException("Your OpenGL card does not support Render-To-Texture and hence can't handle the dynamic images required for this application.");
        }
        this.init();
    }

    private void init() throws SlickException {
        try {
            Texture tex = InternalTextureLoader.get().createTexture(this.image.getWidth(), this.image.getHeight(), this.image.getFilter());
            RenderTexture rt = new RenderTexture(false, true, false, false, 8314, 0);
            this.pbuffer = new Pbuffer(this.screenWidth, this.screenHeight, new PixelFormat(8, 0, 0), rt, null);
            this.pbuffer.makeCurrent();
            this.initGL();
            GL.glBindTexture(3553, tex.getTextureID());
            this.pbuffer.releaseTexImage(8323);
            this.image.draw(0.0f, 0.0f);
            this.image.setTexture(tex);
            Display.makeCurrent();
        }
        catch (Exception e) {
            Log.error(e);
            throw new SlickException("Failed to create PBuffer for dynamic image. OpenGL driver failure?");
        }
    }

    @Override
    protected void disable() {
        GL.flush();
        GL.glBindTexture(3553, this.image.getTexture().getTextureID());
        this.pbuffer.bindTexImage(8323);
        try {
            Display.makeCurrent();
        }
        catch (LWJGLException e) {
            Log.error(e);
        }
        SlickCallable.leaveSafeBlock();
    }

    @Override
    protected void enable() {
        SlickCallable.enterSafeBlock();
        try {
            if (this.pbuffer.isBufferLost()) {
                this.pbuffer.destroy();
                this.init();
            }
            this.pbuffer.makeCurrent();
        }
        catch (Exception e) {
            Log.error("Failed to recreate the PBuffer");
            throw new RuntimeException(e);
        }
        GL.glBindTexture(3553, this.image.getTexture().getTextureID());
        this.pbuffer.releaseTexImage(8323);
        TextureImpl.unbind();
        this.initGL();
    }

    protected void initGL() {
        GL11.glEnable((int)3553);
        GL11.glShadeModel((int)7425);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)2896);
        GL11.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glClearDepth((double)1.0);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glViewport((int)0, (int)0, (int)this.screenWidth, (int)this.screenHeight);
        GL11.glMatrixMode((int)5888);
        GL11.glLoadIdentity();
        this.enterOrtho();
    }

    protected void enterOrtho() {
        GL11.glMatrixMode((int)5889);
        GL11.glLoadIdentity();
        GL11.glOrtho((double)0.0, (double)this.screenWidth, (double)0.0, (double)this.screenHeight, (double)1.0, (double)-1.0);
        GL11.glMatrixMode((int)5888);
    }

    @Override
    public void destroy() {
        super.destroy();
        this.pbuffer.destroy();
    }

    @Override
    public void flush() {
        super.flush();
        this.image.flushPixelData();
    }
}

