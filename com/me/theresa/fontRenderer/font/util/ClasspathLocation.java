/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.util;

import com.me.theresa.fontRenderer.font.util.ResourceLoader;
import com.me.theresa.fontRenderer.font.util.SlickResourceLocation;
import java.io.InputStream;
import java.net.URL;

public class ClasspathLocation
implements SlickResourceLocation {
    @Override
    public URL getResource(String ref) {
        String cpRef = ref.replace('\\', '/');
        return ResourceLoader.class.getClassLoader().getResource(cpRef);
    }

    @Override
    public InputStream getResourceAsStream(String ref) {
        String cpRef = ref.replace('\\', '/');
        return ResourceLoader.class.getClassLoader().getResourceAsStream(cpRef);
    }
}

