/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.util;

import java.io.IOException;

public interface DeferredResource {
    public void load() throws IOException;

    public String getDescription();
}

