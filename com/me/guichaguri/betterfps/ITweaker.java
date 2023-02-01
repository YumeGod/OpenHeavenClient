/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps;

import java.io.File;
import java.util.List;

public interface ITweaker {
    public void acceptOptions(List<String> var1, File var2, File var3, String var4);

    public String getLaunchTarget();

    public String[] getLaunchArguments();
}

