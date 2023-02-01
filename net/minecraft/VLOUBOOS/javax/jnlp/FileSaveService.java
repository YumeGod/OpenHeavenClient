/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.VLOUBOOS.javax.jnlp;

import java.io.InputStream;
import net.minecraft.VLOUBOOS.javax.jnlp.FileContents;

public interface FileSaveService {
    public FileContents saveFileDialog(String var1, String[] var2, InputStream var3, String var4);

    public FileContents saveAsFileDialog(String var1, String[] var2, FileContents var3);
}

