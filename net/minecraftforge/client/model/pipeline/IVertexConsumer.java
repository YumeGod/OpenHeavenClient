/*
 * Decompiled with CFR 0.152.
 */
package net.minecraftforge.client.model.pipeline;

import net.minecraft.client.renderer.vertex.VertexFormat;

public interface IVertexConsumer {
    public VertexFormat getVertexFormat();

    public void put(int var1, float ... var2);
}

