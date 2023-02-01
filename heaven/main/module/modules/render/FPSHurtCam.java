/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.module.modules.render.WingRenderer.ColorUtils;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class FPSHurtCam
extends Module {
    private static final Mode<String> colormode = new Mode("Color", new String[]{"HealthColor", "CustomColor"}, "DistanceHealth");
    private static final Option<Boolean> rainbow = new Option<Boolean>("Rainbow", false);
    private static final Option<Boolean> HUDColor = new Option<Boolean>("HUDColor", true);
    private static final Numbers<Double> r = new Numbers<Double>("Red", 120.0, 0.0, 255.0, 5.0);
    private static final Numbers<Double> g = new Numbers<Double>("Green", 120.0, 0.0, 255.0, 5.0);
    private static final Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0);
    private static int n;
    private static int n2;
    private static int n3;

    public FPSHurtCam() {
        super("FPSHurtCam", ModuleType.Render);
        this.addValues(colormode, r, g, b, HUDColor, rainbow);
    }

    @EventHandler
    private void onRender3D(EventRender2D event) {
        ScaledResolution sr = new ScaledResolution(mc);
        double left = 0.0;
        double top = 0.0;
        double right = sr.getScaledWidth();
        double bottom = 30.0;
        if (colormode.isCurrentMode("HealthColor")) {
            n = 255;
            n2 = 15;
            n3 = 15;
        } else if (!((Boolean)rainbow.getValue()).booleanValue()) {
            if (((Boolean)HUDColor.getValue()).booleanValue()) {
                n = ((Double)HUD.r.getValue()).intValue();
                n2 = ((Double)HUD.g.getValue()).intValue();
                n3 = ((Double)HUD.b.getValue()).intValue();
            } else {
                n = ((Double)r.getValue()).intValue();
                n2 = ((Double)g.getValue()).intValue();
                n3 = ((Double)b.getValue()).intValue();
            }
        } else {
            n = ColorUtils.rainbow(1L, 1.0f).getRed() / 255;
            n2 = ColorUtils.rainbow(1L, 1.0f).getGreen() / 255;
            n3 = ColorUtils.rainbow(1L, 1.0f).getBlue() / 255;
        }
        FPSHurtCam.drawVerticalGradientSideways(0.0, 0.0, right, 30.0, new Color(n, n2, n3, Minecraft.thePlayer.hurtTime * 20).getRGB(), 0);
        double left2 = 0.0;
        double top2 = sr.getScaledHeight() - 30;
        double right2 = sr.getScaledWidth();
        double bottom2 = sr.getScaledHeight();
        FPSHurtCam.drawVerticalGradientSideways(0.0, top2, right2, bottom2, 0, new Color(n, n2, n3, Minecraft.thePlayer.hurtTime * 20).getRGB());
        double left3 = 0.0;
        double top3 = 0.0;
        double right3 = 30.0;
        this.drawHorizontalGradientSideways(0.0, 0.0, 30.0, bottom2, new Color(n, n2, n3, Minecraft.thePlayer.hurtTime * 20).getRGB(), 0);
        double left4 = sr.getScaledWidth() - 30;
        double top4 = 0.0;
        double right4 = sr.getScaledWidth();
        this.drawHorizontalGradientSideways(left4, 0.0, right4, bottom2, 0, new Color(n, n2, n3, Minecraft.thePlayer.hurtTime * 20).getRGB());
    }

    public static void drawVerticalGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(col2 & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer world = tessellator.getWorldRenderer();
        world.begin(7, DefaultVertexFormats.POSITION_COLOR);
        world.pos(right, top, 0.0).color(f2, f3, f4, f).endVertex();
        world.pos(left, top, 0.0).color(f2, f3, f4, f).endVertex();
        world.pos(left, bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        world.pos(right, bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void drawHorizontalGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(col2 & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer world = tessellator.getWorldRenderer();
        world.begin(7, DefaultVertexFormats.POSITION_COLOR);
        world.pos(left, top, 0.0).color(f2, f3, f4, f).endVertex();
        world.pos(left, bottom, 0.0).color(f2, f3, f4, f).endVertex();
        world.pos(right, bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        world.pos(right, top, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}

