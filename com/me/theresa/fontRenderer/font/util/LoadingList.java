/*
 * Decompiled with CFR 0.152.
 */
package com.me.theresa.fontRenderer.font.util;

import com.me.theresa.fontRenderer.font.log.Log;
import com.me.theresa.fontRenderer.font.opengl.InternalTextureLoader;
import com.me.theresa.fontRenderer.font.util.DeferredResource;
import java.util.ArrayList;

public class LoadingList {
    private static LoadingList single = new LoadingList();
    private final ArrayList deferred = new ArrayList();
    private int total;

    public static LoadingList get() {
        return single;
    }

    public static void setDeferredLoading(boolean loading) {
        single = new LoadingList();
        InternalTextureLoader.get().setDeferredLoading(loading);
    }

    public static boolean isDeferredLoading() {
        return InternalTextureLoader.get().isDeferredLoading();
    }

    private LoadingList() {
    }

    public void add(DeferredResource resource) {
        ++this.total;
        this.deferred.add(resource);
    }

    public void remove(DeferredResource resource) {
        Log.info("Early loading of deferred resource due to req: " + resource.getDescription());
        --this.total;
        this.deferred.remove(resource);
    }

    public int getTotalResources() {
        return this.total;
    }

    public int getRemainingResources() {
        return this.deferred.size();
    }

    public DeferredResource getNext() {
        if (this.deferred.size() == 0) {
            return null;
        }
        return (DeferredResource)this.deferred.remove(0);
    }
}

