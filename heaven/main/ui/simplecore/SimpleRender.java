/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.simplecore;

import heaven.main.module.modules.render.HUD;
import heaven.main.utils.math.MathUtil;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public enum SimpleRender {
    INSTANCE;

    public static Minecraft mc;
    public static double delta;
    private static final int colorDelay = 11;
    private static final int colorLength = 110;

    public static int width() {
        return ScaledResolution.getScaledWidth();
    }

    public static int height() {
        return ScaledResolution.getScaledHeight();
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static float processFPS(float defF, float defV) {
        float f;
        Minecraft.getMinecraft();
        if (Minecraft.getDebugFPS() == 0) {
            f = 1.0f;
        } else {
            Minecraft.getMinecraft();
            f = (float)Minecraft.getDebugFPS() / defF;
        }
        return defV / f;
    }

    public static String abcdefg() {
        String[] abc = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String[] ABC = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String[] aBc = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        try {
            int var0 = (int)MathUtil.randomDouble(0.0, aBc.length - 1);
            return aBc[var0] + var0 + abc[abc.length - 1] + ABC[ABC.length - 1] + aBc[abc.length] + abc[ABC.length - 1];
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double getEntityRenderX(Entity entity) {
        return entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)Minecraft.getMinecraft().timer.renderPartialTicks - RenderManager.renderPosX;
    }

    public static double getEntityRenderY(Entity entity) {
        return entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)Minecraft.getMinecraft().timer.renderPartialTicks - RenderManager.renderPosY;
    }

    public static double getEntityRenderZ(Entity entity) {
        return entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)Minecraft.getMinecraft().timer.renderPartialTicks - RenderManager.renderPosZ;
    }

    public static void drawCircle(float x, float y, float r, float lineWidth, boolean isFull, int color) {
        SimpleRender.drawCircle(x, y, r, 10, lineWidth, 360, isFull, color);
    }

    public static void drawCircle(float cx, float cy, double r, int segments, float lineWidth, int part, boolean isFull, int c) {
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        r *= 2.0;
        cx *= 2.0f;
        cy *= 2.0f;
        float f2 = (float)(c >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(c >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(c >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(c & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)lineWidth);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)f3, (float)f4, (float)f5, (float)f2);
        GL11.glBegin((int)3);
        for (int i = segments - part; i <= segments; ++i) {
            double x = Math.sin((double)i * Math.PI / 180.0) * r;
            double y = Math.cos((double)i * Math.PI / 180.0) * r;
            GL11.glVertex2d((double)((double)cx + x), (double)((double)cy + y));
            if (!isFull) continue;
            GL11.glVertex2d((double)cx, (double)cy);
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public static Color getBlendColor(double current, double max) {
        long base = Math.round(max / 5.0);
        if (current >= (double)(base * 5L)) {
            return new Color(15, 255, 15);
        }
        if (current >= (double)(base << 2)) {
            return new Color(166, 255, 0);
        }
        if (current >= (double)(base * 3L)) {
            return new Color(255, 191, 0);
        }
        if (current >= (double)(base << 1)) {
            return new Color(255, 89, 0);
        }
        return new Color(255, 0, 0);
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f4 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f5 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f6 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBorderedRect(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        SimpleRender.drawRect(left - (borderIncludedInBounds ? 0.0 : borderWidth), top - (borderIncludedInBounds ? 0.0 : borderWidth), right + (borderIncludedInBounds ? 0.0 : borderWidth), bottom + (borderIncludedInBounds ? 0.0 : borderWidth), borderColor);
        SimpleRender.drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0), top + (borderIncludedInBounds ? borderWidth : 0.0), right - (borderIncludedInBounds ? borderWidth : 0.0), bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static int getArrayRainbow(int counter, int alpha) {
        double rainbowState = Math.ceil(System.currentTimeMillis() - (long)counter * 110L) / 11.0;
        Color color = Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), ((Double)HUD.g.get()).intValue(), ((Double)HUD.b.get()).intValue());
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }

    static {
        mc = Minecraft.getMinecraft();
    }
}

