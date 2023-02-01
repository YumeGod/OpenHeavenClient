/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.opengl;

import java.io.IOException;
import java.util.ArrayList;

public class CompositeIOException
extends IOException {
    private final ArrayList exceptions = new ArrayList();

    public void addException(Exception e) {
        this.exceptions.add(e);
    }

    @Override
    public String getMessage() {
        StringBuilder msg = new StringBuilder("Composite Exception: \n");
        for (int i = 0; i < this.exceptions.size(); ++i) {
            msg.append("\t").append(((IOException)this.exceptions.get(i)).getMessage()).append("\n");
        }
        return msg.toString();
    }
}

