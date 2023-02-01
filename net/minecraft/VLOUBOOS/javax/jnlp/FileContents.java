/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.VLOUBOOS.javax.jnlp;

import java.io.InputStream;
import java.io.OutputStream;
import net.minecraft.VLOUBOOS.javax.jnlp.JNLPRandomAccessFile;

public interface FileContents {
    public String getName();

    public InputStream getInputStream();

    public OutputStream getOutputStream(boolean var1);

    public long getLength();

    public boolean canRead();

    public boolean canWrite();

    public JNLPRandomAccessFile getRandomAccessFile(String var1);

    public long getMaxLength();

    public long setMaxLength(long var1);
}

