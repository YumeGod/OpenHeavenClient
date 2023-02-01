/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.font.fontRenderer;

import com.me.theresa.fontRenderer.font.SlickException;
import com.me.theresa.fontRenderer.font.UnicodeFont;
import com.me.theresa.fontRenderer.font.effect.ColorEffect;
import heaven.main.ui.gui.clickgui.RenderUtil;
import java.awt.Color;
import java.awt.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class UnicodeFontRenderer
extends FontRenderer {
    public final UnicodeFont unicodeFont;

    public UnicodeFontRenderer(Font fontIn, boolean useChinese) {
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        ResourceLocation resourceLocation = new ResourceLocation("client/bit.png");
        Minecraft.getMinecraft();
        super(gameSettings, resourceLocation, Minecraft.getTextureManager(), true);
        this.unicodeFont = new UnicodeFont(fontIn);
        this.unicodeFont.getEffects().add(new ColorEffect(Color.WHITE));
        if (useChinese) {
            this.unicodeFont.addGlyphs(0, 65535);
        } else {
            this.unicodeFont.addAsciiGlyphs();
        }
        try {
            this.unicodeFont.loadGlyphs();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
        this.FONT_HEIGHT = this.unicodeFont.getHeight("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789") / 2;
    }

    public void drawStringDirectly(String string, float x, float y, int color) {
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean lighting = GL11.glIsEnabled((int)2896);
        boolean texture = GL11.glIsEnabled((int)3553);
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        if (lighting) {
            GL11.glDisable((int)2896);
        }
        if (texture) {
            GL11.glDisable((int)3553);
        }
        this.unicodeFont.drawString(x * 2.0f, y * 2.0f, string, new com.me.theresa.fontRenderer.font.Color(color));
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (lighting) {
            GL11.glEnable((int)2896);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        RenderUtil.resetColor();
        GL11.glPopMatrix();
        GlStateManager.bindTexture(0);
    }

    @Override
    public int drawString(String text, int x, int y, int color) {
        return this.drawString(text, (float)x, (float)y, color, new Color(color).getAlpha());
    }

    public int drawString(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, new Color(color).getAlpha());
    }

    public int drawString(String text, float x, float y, int color, int alpha) {
        text = "\ufffd\ufffdr" + text;
        float len = -1.0f;
        for (String str : text.split("\ufffd\ufffd")) {
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                }
            }
            Color col = new Color(color);
            str = str.substring(1);
            this.drawStringDirectly(str, x + len, y, new Color(col.getRed(), col.getGreen(), col.getBlue(), alpha).getRGB());
            len += (float)(this.getStringWidth(str) + 1);
        }
        return (int)len;
    }

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        return this.drawStringWithShadow(text, x, y, color, new Color(color).getAlpha());
    }

    public int drawStringWithShadow(String text, int x, int y, int color) {
        return this.drawStringWithShadow(text, x, y, color, new Color(color).getAlpha());
    }

    public int drawStringWithShadow(String text, float x, float y, int color, int alpha) {
        text = "\ufffd\ufffdr" + text;
        float len = -1.0f;
        for (String str : text.split("\ufffd\ufffd")) {
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                }
            }
            Color col = new Color(color);
            str = str.substring(1);
            this.drawStringDirectly(str, x + len + 0.5f, y + 0.5f, new Color(0, 0, 0, 80).getRGB());
            this.drawStringDirectly(str, x + len, y, new Color(col.getRed(), col.getGreen(), col.getBlue(), alpha).getRGB());
            len += (float)(this.getStringWidth(str) + 1);
        }
        return (int)len;
    }

    public void drawStringOther(String string, double x, double y, int color) {
        if (string == null) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean lighting = GL11.glIsEnabled((int)2896);
        boolean texture = GL11.glIsEnabled((int)3553);
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        if (lighting) {
            GL11.glDisable((int)2896);
        }
        if (texture) {
            GL11.glDisable((int)3553);
        }
        this.unicodeFont.drawString((float)(x * 2.0), (float)(y * 2.0), string, new com.me.theresa.fontRenderer.font.Color(color));
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (lighting) {
            GL11.glEnable((int)2896);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glPopMatrix();
        GlStateManager.bindTexture(0);
    }

    @Override
    public int getCharWidth(char c) {
        return this.getStringWidth(Character.toString(c));
    }

    @Override
    public int getStringWidth(String string) {
        return this.unicodeFont.getWidth(string) / 2;
    }

    @Override
    public void drawCenteredString(String text, float x, float y, int color) {
        this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    @Override
    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        this.drawStringWithShadow(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public void drawCenteredString(String text, int x, int y, int color) {
        this.drawString(text, (float)x - (float)(this.getStringWidth(text) / 2), (float)y, color);
    }

    public int getStringHeight() {
        return this.getHeight();
    }

    @Override
    public int getHeight() {
        return (this.FONT_HEIGHT - 8) / 2;
    }

    public int HUDDrawString(String name, float x, float y, int color) {
        return this.drawStringWithShadow(name, x, y, color, new Color(color).getAlpha());
    }
}

