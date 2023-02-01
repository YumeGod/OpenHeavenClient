/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.util;

import java.io.InputStream;
import java.net.URL;

public interface SlickResourceLocation {
    public InputStream getResourceAsStream(String var1);

    public URL getResource(String var1);
}

