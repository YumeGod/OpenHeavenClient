/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.clones.client;

import com.me.guichaguri.betterfps.transformers.cloner.CopyBoolCondition;
import com.me.guichaguri.betterfps.transformers.cloner.CopyMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManager;

public class EntityRenderLogic
extends EntityRenderer {
    @CopyMode(value=CopyMode.Mode.IGNORE)
    public EntityRenderLogic(Minecraft mcIn, IResourceManager manager) {
        super(mcIn, manager);
    }

    @Override
    @CopyBoolCondition(key="fog", value=false)
    @CopyMode(value=CopyMode.Mode.REPLACE)
    public void setupFog(int p1, float partialTicks) {
        if (p1 != -1) {
            return;
        }
        super.setupFog(p1, partialTicks);
    }
}

