/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.log;

public interface LogSystem {
    public void error(String var1, Throwable var2);

    public void error(Throwable var1);

    public void error(String var1);

    public void warn(String var1);

    public void warn(String var1, Throwable var2);

    public void info(String var1);

    public void debug(String var1);
}

