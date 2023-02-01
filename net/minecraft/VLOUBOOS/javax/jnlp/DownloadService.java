/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.VLOUBOOS.javax.jnlp;

import java.net.URL;
import net.minecraft.VLOUBOOS.javax.jnlp.DownloadServiceListener;

public interface DownloadService {
    public boolean isResourceCached(URL var1, String var2);

    public boolean isPartCached(String var1);

    public boolean isPartCached(String[] var1);

    public boolean isExtensionPartCached(URL var1, String var2, String var3);

    public boolean isExtensionPartCached(URL var1, String var2, String[] var3);

    public void loadResource(URL var1, String var2, DownloadServiceListener var3);

    public void loadPart(String var1, DownloadServiceListener var2);

    public void loadPart(String[] var1, DownloadServiceListener var2);

    public void loadExtensionPart(URL var1, String var2, String var3, DownloadServiceListener var4);

    public void loadExtensionPart(URL var1, String var2, String[] var3, DownloadServiceListener var4);

    public void removeResource(URL var1, String var2);

    public void removePart(String var1);

    public void removePart(String[] var1);

    public void removeExtensionPart(URL var1, String var2, String var3);

    public void removeExtensionPart(URL var1, String var2, String[] var3);

    public DownloadServiceListener getDefaultProgressWindow();
}

