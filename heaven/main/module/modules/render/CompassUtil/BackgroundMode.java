/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render.CompassUtil;

import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class BackgroundMode {
    private final CFontRenderer fr;
    public boolean enabled = true;
    public final int details = 2;
    public int offX;
    public int offY;
    public final int width = 150;
    public final int height = 20;
    public final int cwidth = 500;
    public final boolean background = true;
    public boolean chroma;
    public final boolean shadow = true;
    public int tintMarker;
    public int tintDirection;
    private int offsetAll;
    private int centerX;
    private int colorMarker;
    private int colorDirection;

    public BackgroundMode() {
        this.fr = Client.instance.FontLoaders.Comfortaa16;
    }

    public void drawCompass(int screenWidth) {
        int direction = BackgroundMode.normalize((int)Minecraft.thePlayer.rotationYaw);
        this.offsetAll = 500 * direction / 360;
        this.centerX = screenWidth / 2 + this.offX;
        Gui.drawRect(this.centerX - 75, this.offY, this.centerX + 75, this.offY + 20, -1442840576);
        if (!this.chroma) {
            this.colorMarker = this.tintMarker != 0 ? Color.HSBtoRGB((float)this.tintMarker / 100.0f, 1.0f, 1.0f) : -1;
            this.colorDirection = this.tintDirection != 0 ? Color.HSBtoRGB((float)this.tintDirection / 100.0f, 1.0f, 1.0f) : -1;
        } else {
            this.colorDirection = this.colorMarker = Color.HSBtoRGB((float)(System.currentTimeMillis() % 3000L) / 3000.0f, 1.0f, 1.0f);
        }
        this.renderMarker();
        this.drawDirection("S", 0, 1.5);
        this.drawDirection("W", 90, 1.5);
        this.drawDirection("N", 180, 1.5);
        this.drawDirection("E", 270, 1.5);
        this.drawDirection("SW", 45, 1.0);
        this.drawDirection("NW", 135, 1.0);
        this.drawDirection("NE", 225, 1.0);
        this.drawDirection("SE", 315, 1.0);
        this.drawDirection("15", 15, 0.75);
        this.drawDirection("30", 30, 0.75);
        this.drawDirection("60", 60, 0.75);
        this.drawDirection("75", 75, 0.75);
        this.drawDirection("105", 105, 0.75);
        this.drawDirection("120", 120, 0.75);
        this.drawDirection("150", 150, 0.75);
        this.drawDirection("165", 165, 0.75);
        this.drawDirection("195", 195, 0.75);
        this.drawDirection("210", 210, 0.75);
        this.drawDirection("240", 240, 0.75);
        this.drawDirection("255", 255, 0.75);
        this.drawDirection("285", 285, 0.75);
        this.drawDirection("300", 300, 0.75);
        this.drawDirection("330", 330, 0.75);
        this.drawDirection("345", 345, 0.75);
    }

    private void renderMarker() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color((float)(this.colorMarker >> 16 & 0xFF) / 255.0f, (float)(this.colorMarker >> 8 & 0xFF) / 255.0f, (float)(this.colorMarker & 0xFF) / 255.0f, 1.0f);
        worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(this.centerX, this.offY + 3, 0.0).endVertex();
        worldrenderer.pos(this.centerX + 3, this.offY, 0.0).endVertex();
        worldrenderer.pos(this.centerX - 3, this.offY, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    private void drawDirection(String dir, int degrees, double scale) {
        double d;
        int offset = 500 * degrees / 360 - this.offsetAll;
        if (offset > 250) {
            offset -= 500;
        }
        if (offset < -250) {
            offset += 500;
        }
        double opacity = 1.0 - (double)Math.abs(offset) / 75.0;
        if (d > 0.1) {
            int defcolor = this.colorDirection & 0xFFFFFF;
            int color = defcolor | (int)(opacity * 255.0) << 24;
            int posX = this.centerX + offset - (int)((double)this.fr.getStringWidth(dir) * scale / 2.0);
            int posY = this.offY + 10 - (int)(0.0 * scale / 2.0);
            GL11.glEnable((int)3042);
            GL11.glPushMatrix();
            GL11.glTranslated((double)((double)(-posX) * (scale - 1.0)), (double)((double)(-posY) * (scale - 1.0)), (double)0.0);
            GL11.glScaled((double)scale, (double)scale, (double)1.0);
            this.fr.drawStringWithShadow(dir, posX, posY, color);
            Gui.drawRect(0, 0, 0, 0, 0);
            GL11.glPopMatrix();
            GL11.glDisable((int)3042);
        }
    }

    private static int normalize(int direction) {
        while ((direction %= 360) < 0) {
            direction += 360;
        }
        return direction;
    }
}

