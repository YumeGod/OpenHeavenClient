/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.opengl;

import com.me.theresa.fontRenderer.font.log.Log;
import com.me.theresa.fontRenderer.font.opengl.CompositeImageData;
import com.me.theresa.fontRenderer.font.opengl.ImageIOImageData;
import com.me.theresa.fontRenderer.font.opengl.LoadableImageData;
import com.me.theresa.fontRenderer.font.opengl.PNGImageData;
import com.me.theresa.fontRenderer.font.opengl.TGAImageData;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class ImageDataFactory {
    static boolean usePngLoader = true;
    private static boolean pngLoaderPropertyChecked = false;
    private static final String PNG_LOADER = "org.newdawn.slick.pngloader";

    private static void checkProperty() {
        if (!pngLoaderPropertyChecked) {
            pngLoaderPropertyChecked = true;
            try {
                AccessController.doPrivileged(new PrivilegedAction(){

                    public Object run() {
                        String val = System.getProperty(ImageDataFactory.PNG_LOADER);
                        if ("false".equalsIgnoreCase(val)) {
                            usePngLoader = false;
                        }
                        Log.info("Use Java PNG Loader = " + usePngLoader);
                        return null;
                    }
                });
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }

    public static LoadableImageData getImageDataFor(String ref) {
        ImageDataFactory.checkProperty();
        ref = ref.toLowerCase();
        if (ref.endsWith(".tga")) {
            return new TGAImageData();
        }
        if (ref.endsWith(".png")) {
            CompositeImageData data = new CompositeImageData();
            if (usePngLoader) {
                data.add(new PNGImageData());
            }
            data.add(new ImageIOImageData());
            return data;
        }
        return new ImageIOImageData();
    }
}

