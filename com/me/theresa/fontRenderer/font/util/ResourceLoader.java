/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.util;

import com.me.theresa.fontRenderer.font.util.ClasspathLocation;
import com.me.theresa.fontRenderer.font.util.FileSystemLocation;
import com.me.theresa.fontRenderer.font.util.SlickResourceLocation;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ResourceLoader {
    private static final ArrayList locations = new ArrayList();

    public static InputStream getResourceAsStream(String ref) {
        SlickResourceLocation location;
        InputStream in = null;
        for (int i = 0; i < locations.size() && (in = (location = (SlickResourceLocation)locations.get(i)).getResourceAsStream(ref)) == null; ++i) {
        }
        if (in == null) {
            throw new RuntimeException("Resource not found: " + ref);
        }
        return new BufferedInputStream(in);
    }

    public static boolean resourceExists(String ref) {
        URL url = null;
        for (int i = 0; i < locations.size(); ++i) {
            SlickResourceLocation location = (SlickResourceLocation)locations.get(i);
            url = location.getResource(ref);
            if (url == null) continue;
            return true;
        }
        return false;
    }

    public static URL getResource(String ref) {
        SlickResourceLocation location;
        URL url = null;
        for (int i = 0; i < locations.size() && (url = (location = (SlickResourceLocation)locations.get(i)).getResource(ref)) == null; ++i) {
        }
        if (url == null) {
            throw new RuntimeException("Resource not found: " + ref);
        }
        return url;
    }

    static {
        locations.add(new ClasspathLocation());
        locations.add(new FileSystemLocation(new File(".")));
    }
}

