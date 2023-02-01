/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package heaven.main.module.modules.render.CompassUtil;

import com.google.common.collect.Lists;
import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.RenderUtil;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class CompassUtil {
    public float innerWidth;
    public float outerWidth;
    public boolean shadow;
    public float scale;
    public int accuracy;
    public static final List<Degree> degrees;
    static final CFontRenderer font;

    public CompassUtil(float i, float o, float s, int a, boolean sh) {
        this.innerWidth = i;
        this.outerWidth = o;
        this.scale = s;
        this.accuracy = a;
        this.shadow = sh;
        degrees.add(new Degree("N", 1));
        degrees.add(new Degree("195", 2));
        degrees.add(new Degree("210", 2));
        degrees.add(new Degree("NE", 3));
        degrees.add(new Degree("240", 2));
        degrees.add(new Degree("255", 2));
        degrees.add(new Degree("E", 1));
        degrees.add(new Degree("285", 2));
        degrees.add(new Degree("300", 2));
        degrees.add(new Degree("SE", 3));
        degrees.add(new Degree("330", 2));
        degrees.add(new Degree("345", 2));
        degrees.add(new Degree("S", 1));
        degrees.add(new Degree("15", 2));
        degrees.add(new Degree("30", 2));
        degrees.add(new Degree("SW", 3));
        degrees.add(new Degree("60", 2));
        degrees.add(new Degree("75", 2));
        degrees.add(new Degree("W", 1));
        degrees.add(new Degree("105", 2));
        degrees.add(new Degree("120", 2));
        degrees.add(new Degree("NW", 3));
        degrees.add(new Degree("150", 2));
        degrees.add(new Degree("165", 2));
    }

    public void draw(ScaledResolution sr) {
        float completeLocation;
        float location;
        this.preRender();
        float center = (float)sr.getScaledWidth() / 2.0f;
        int count = 0;
        Minecraft.getMinecraft();
        float yaaahhrewindTime = Minecraft.thePlayer.rotationYaw % 360.0f * 2.0f + 1080.0f;
        RenderUtil.startGlScissor(sr.getScaledWidth() / 2 - 100, 25, 200, 25);
        for (Degree d : degrees) {
            location = center + (float)(count * 30) - yaaahhrewindTime;
            completeLocation = location - (float)(font.getStringWidth(d.text) / 2);
            int opacity = this.opacity(sr, completeLocation);
            if (d.type == 1 && opacity != 0xFFFFFF) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                font.drawString(d.text, (double)completeLocation, 25.0, this.opacity(sr, completeLocation));
            }
            if (d.type == 2 && opacity != 0xFFFFFF) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                RenderUtil.drawRect((double)(location - 0.5f), 29.0, (double)(location + 0.5f), 34.0, this.opacity(sr, completeLocation));
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                font.drawString(d.text, completeLocation, 37.5f, this.opacity(sr, completeLocation));
            }
            if (d.type == 3 && opacity != 0xFFFFFF) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                font.drawString(d.text, completeLocation, 25.0f + (float)font.getHeight() / 2.0f - (float)font.getHeight() / 2.0f, this.opacity(sr, completeLocation));
            }
            ++count;
        }
        for (Degree d : degrees) {
            location = center + (float)(count * 30) - yaaahhrewindTime;
            completeLocation = location - (float)(font.getStringWidth(d.text) / 2);
            if (d.type == 1) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                font.drawString(d.text, (double)completeLocation, 25.0, this.opacity(sr, completeLocation));
            }
            if (d.type == 2) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                RenderUtil.drawRect((double)(location - 0.5f), 29.0, (double)(location + 0.5f), 34.0, this.opacity(sr, completeLocation));
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                font.drawString(d.text, completeLocation, 37.5f, this.opacity(sr, completeLocation));
            }
            if (d.type == 3) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                font.drawString(d.text, completeLocation, 25.0f + (float)font.getHeight() / 2.0f - (float)(font.getHeight() / 2), this.opacity(sr, completeLocation));
            }
            ++count;
        }
        for (Degree d : degrees) {
            location = center + (float)(count * 30) - yaaahhrewindTime;
            completeLocation = location - (float)(font.getStringWidth(d.text) / 2);
            if (d.type == 1) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                font.drawString(d.text, (double)completeLocation, 25.0, this.opacity(sr, completeLocation));
            }
            if (d.type == 2) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                RenderUtil.drawRect((double)(location - 0.5f), 29.0, (double)(location + 0.5f), 34.0, this.opacity(sr, completeLocation));
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                font.drawString(d.text, completeLocation, 37.5f, this.opacity(sr, completeLocation));
            }
            if (d.type == 3) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                font.drawString(d.text, completeLocation, 25.0f + (float)font.getHeight() / 2.0f - (float)font.getHeight() / 2.0f, this.opacity(sr, completeLocation));
            }
            ++count;
        }
        RenderUtil.stopGlScissor();
    }

    public void preRender() {
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
    }

    public int opacity(ScaledResolution sr, float offset) {
        float offs = 255.0f - Math.abs((float)sr.getScaledWidth() / 2.0f - offset) * 1.8f;
        Color c = new Color(255, 255, 255, (int)Math.min(Math.max(0.0f, offs), 255.0f));
        return c.getRGB();
    }

    static {
        font = Client.instance.FontLoaders.Comfortaa24;
        degrees = Lists.newArrayList();
    }

    public class Degree {
        public final String text;
        public final int type;

        public Degree(String s, int t) {
            this.text = s;
            this.type = t;
        }
    }
}

