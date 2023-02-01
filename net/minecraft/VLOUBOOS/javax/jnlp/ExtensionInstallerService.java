/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.VLOUBOOS.javax.jnlp;

import java.net.URL;

public interface ExtensionInstallerService {
    public String getInstallPath();

    public String getExtensionVersion();

    public URL getExtensionLocation();

    public void hideProgressBar();

    public void hideStatusWindow();

    public void setHeading(String var1);

    public void setStatus(String var1);

    public void updateProgress(int var1);

    public void installSucceeded(boolean var1);

    public void installFailed();

    public void setJREInfo(String var1, String var2);

    public void setNativeLibraryInfo(String var1);

    public String getInstalledJRE(URL var1, String var2);
}

