/*
 * Decompiled with CFR 0.152.
 */
package javax.jnlp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileContents {
    public String getName() throws IOException;

    public InputStream getInputStream() throws IOException;

    public OutputStream getOutputStream(boolean var1) throws IOException;

    public long getLength() throws IOException;

    public boolean canRead() throws IOException;
}

