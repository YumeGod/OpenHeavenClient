/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

public class DynamicTexture
extends AbstractTexture {
    private final int[] dynamicTextureData;
    private final int width;
    private final int height;

    public DynamicTexture(BufferedImage bufferedImage) {
        this(bufferedImage.getWidth(), bufferedImage.getHeight());
        if (this.dynamicTextureData != null) {
            bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.dynamicTextureData, 0, bufferedImage.getWidth());
            this.updateDynamicTexture();
        }
    }

    public DynamicTexture(int textureWidth, int textureHeight) {
        this.width = textureWidth;
        this.height = textureHeight;
        this.dynamicTextureData = new int[textureWidth * textureHeight];
        TextureUtil.allocateTexture(this.getGlTextureId(), textureWidth, textureHeight);
    }

    @Override
    public void loadTexture(IResourceManager resourceManager) {
    }

    public void updateDynamicTexture() {
        TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
    }

    public int[] getTextureData() {
        return this.dynamicTextureData;
    }
}

