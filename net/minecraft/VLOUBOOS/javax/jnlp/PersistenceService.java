/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.VLOUBOOS.javax.jnlp;

import java.net.URL;
import net.minecraft.VLOUBOOS.javax.jnlp.FileContents;

public interface PersistenceService {
    public static final int CACHED = 0;
    public static final int TEMPORARY = 1;
    public static final int DIRTY = 2;

    public long create(URL var1, long var2);

    public FileContents get(URL var1);

    public void delete(URL var1);

    public String[] getNames(URL var1);

    public int getTag(URL var1);

    public void setTag(URL var1, int var2);
}

