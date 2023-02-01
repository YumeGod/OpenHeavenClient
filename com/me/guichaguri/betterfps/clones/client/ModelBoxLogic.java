/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.me.guichaguri.betterfps.clones.client;

import com.me.guichaguri.betterfps.transformers.cloner.CopyBoolCondition;
import com.me.guichaguri.betterfps.transformers.cloner.CopyMode;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.WorldRenderer;
import org.lwjgl.opengl.GL11;

public class ModelBoxLogic
extends ModelBox {
    @CopyMode(value=CopyMode.Mode.IGNORE)
    public ModelBoxLogic(ModelRenderer renderer, int i1, int i2, float f1, float f2, float f3, int i3, int i4, int i5, float f4) {
        super(renderer, i1, i2, f1, f2, f3, i3, i4, i5, f4);
    }

    @Override
    @CopyBoolCondition(key="fastBoxRender", value=true)
    @CopyMode(value=CopyMode.Mode.REPLACE)
    public void render(WorldRenderer renderer, float scale) {
        boolean b = GL11.glIsEnabled((int)2884);
        if (b) {
            super.render(renderer, scale);
        } else {
            GL11.glEnable((int)2884);
            super.render(renderer, scale);
            GL11.glDisable((int)2884);
        }
    }
}

