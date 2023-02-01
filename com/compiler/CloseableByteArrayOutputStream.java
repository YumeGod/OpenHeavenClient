/*
 * Decompiled with CFR 0.152.
 */
package com.compiler;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

public class CloseableByteArrayOutputStream
extends ByteArrayOutputStream {
    private final CompletableFuture<?> closeFuture = new CompletableFuture();

    @Override
    public void close() {
        this.closeFuture.complete(null);
    }

    public CompletableFuture<?> closeFuture() {
        return this.closeFuture;
    }
}

