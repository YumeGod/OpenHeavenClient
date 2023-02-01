/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.opengl;

import com.me.theresa.fontRenderer.font.log.Log;
import com.me.theresa.fontRenderer.font.opengl.CompositeIOException;
import com.me.theresa.fontRenderer.font.opengl.LoadableImageData;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class CompositeImageData
implements LoadableImageData {
    private final ArrayList sources = new ArrayList();
    private LoadableImageData picked;

    public void add(LoadableImageData data) {
        this.sources.add(data);
    }

    @Override
    public ByteBuffer loadImage(InputStream fis) throws IOException {
        return this.loadImage(fis, false, null);
    }

    @Override
    public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
        return this.loadImage(fis, flipped, false, transparent);
    }

    @Override
    public ByteBuffer loadImage(InputStream is, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
        CompositeIOException exception = new CompositeIOException();
        ByteBuffer buffer = null;
        BufferedInputStream in = new BufferedInputStream(is, is.available());
        in.mark(is.available());
        for (int i = 0; i < this.sources.size(); ++i) {
            in.reset();
            try {
                LoadableImageData data = (LoadableImageData)this.sources.get(i);
                buffer = data.loadImage(in, flipped, forceAlpha, transparent);
                this.picked = data;
                break;
            }
            catch (Exception e) {
                Log.warn(this.sources.get(i).getClass() + " failed to read the data", e);
                exception.addException(e);
                continue;
            }
        }
        if (this.picked == null) {
            throw exception;
        }
        return buffer;
    }

    private void checkPicked() {
        if (this.picked == null) {
            throw new RuntimeException("Attempt to make use of uninitialised or invalid composite image data");
        }
    }

    @Override
    public int getDepth() {
        this.checkPicked();
        return this.picked.getDepth();
    }

    @Override
    public int getHeight() {
        this.checkPicked();
        return this.picked.getHeight();
    }

    @Override
    public ByteBuffer getImageBufferData() {
        this.checkPicked();
        return this.picked.getImageBufferData();
    }

    @Override
    public int getTexHeight() {
        this.checkPicked();
        return this.picked.getTexHeight();
    }

    @Override
    public int getTexWidth() {
        this.checkPicked();
        return this.picked.getTexWidth();
    }

    @Override
    public int getWidth() {
        this.checkPicked();
        return this.picked.getWidth();
    }

    @Override
    public void configureEdging(boolean edging) {
        for (int i = 0; i < this.sources.size(); ++i) {
            ((LoadableImageData)this.sources.get(i)).configureEdging(edging);
        }
    }
}

