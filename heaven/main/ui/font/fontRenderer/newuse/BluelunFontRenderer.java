/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.font.fontRenderer.newuse;

import heaven.main.ui.font.fontRenderer.StringCache;
import heaven.main.ui.font.fontRenderer.newuse.FontRendererCallback;
import heaven.main.ui.font.fontRenderer.newuse.IBFFontRenderer;
import heaven.main.ui.simplecore.SimpleRender;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;
import net.optifine.render.GlBlendState;
import net.optifine.util.FontUtils;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

public class BluelunFontRenderer
implements IResourceManagerReloadListener,
IBFFontRenderer {
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
    private final int[] charWidth = new int[256];
    public final int FONT_HEIGHT = 8;
    public final Random fontRandom = new Random();
    private final byte[] glyphWidth = new byte[65536];
    public final int[] colorCode = new int[32];
    private ResourceLocation locationFontTexture;
    private final TextureManager renderEngine;
    private float posX;
    private float posY;
    private boolean unicodeFlag;
    private boolean bidiFlag;
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private int textColor;
    private boolean randomStyle;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;
    public final GameSettings gameSettings;
    public final ResourceLocation locationFontTextureBase;
    public float offsetBold = 1.0f;
    private final float[] charWidthFloat = new float[256];
    private boolean blend;
    private final GlBlendState oldBlendState = new GlBlendState();
    private StringCache stringCache;

    public BluelunFontRenderer(Font font, int size, boolean antiAlias) {
        ResourceLocation res = new ResourceLocation("textures/font/ascii.png");
        this.gameSettings = Minecraft.getMinecraft().gameSettings;
        this.locationFontTextureBase = res;
        this.locationFontTexture = res;
        Minecraft.getMinecraft();
        this.renderEngine = Minecraft.renderEngine;
        this.unicodeFlag = false;
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (this.gameSettings.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
        }
        if (res.getResourcePath().equalsIgnoreCase("textures/font/ascii.png") && this.stringCache == null) {
            this.stringCache = new StringCache(this.colorCode);
            this.stringCache.setDefaultFont(font, size, antiAlias);
        }
        this.readGlyphSizes();
    }

    public BluelunFontRenderer(int size, boolean antiAlias) {
        ResourceLocation res = new ResourceLocation("textures/font/ascii.png");
        this.gameSettings = Minecraft.getMinecraft().gameSettings;
        this.locationFontTextureBase = res;
        this.locationFontTexture = res;
        Minecraft.getMinecraft();
        this.renderEngine = Minecraft.renderEngine;
        this.unicodeFlag = false;
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (this.gameSettings.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
        }
        if (res.getResourcePath().equalsIgnoreCase("textures/font/ascii.png") && this.stringCache == null) {
            this.stringCache = new StringCache(this.colorCode);
            this.stringCache.setDefaultFont("Lucida Sans Regular", size, antiAlias);
        }
        this.readGlyphSizes();
    }

    public BluelunFontRenderer(String name, int size, boolean antiAlias) {
        ResourceLocation res = new ResourceLocation("textures/font/ascii.png");
        this.gameSettings = Minecraft.getMinecraft().gameSettings;
        this.locationFontTextureBase = res;
        this.locationFontTexture = res;
        Minecraft.getMinecraft();
        this.renderEngine = Minecraft.renderEngine;
        this.unicodeFlag = false;
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (this.gameSettings.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
        }
        if (res.getResourcePath().equalsIgnoreCase("textures/font/ascii.png") && this.stringCache == null) {
            this.stringCache = new StringCache(this.colorCode);
            this.stringCache.setDefaultFont(name, size, antiAlias);
        }
        this.readGlyphSizes();
    }

    public BluelunFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode, String name, int size, boolean antiAlias) {
        this.gameSettings = gameSettingsIn;
        this.locationFontTextureBase = location;
        this.locationFontTexture = location;
        this.renderEngine = textureManagerIn;
        this.unicodeFlag = unicode;
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        this.bindTexture(this.locationFontTexture);
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (gameSettingsIn.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
        }
        if (location.getResourcePath().equalsIgnoreCase("textures/font/ascii.png") && this.stringCache == null) {
            this.stringCache = new StringCache(this.colorCode);
            this.stringCache.setDefaultFont(name, size, antiAlias);
        }
        this.readGlyphSizes();
    }

    public BluelunFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        this.gameSettings = gameSettingsIn;
        this.locationFontTextureBase = location;
        this.locationFontTexture = location;
        this.renderEngine = textureManagerIn;
        this.unicodeFlag = unicode;
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        this.bindTexture(this.locationFontTexture);
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (gameSettingsIn.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
        }
        if (location.getResourcePath().equalsIgnoreCase("textures/font/ascii.png") && this.stringCache == null) {
            this.stringCache = new StringCache(this.colorCode);
            this.stringCache.setDefaultFont("Lucida Sans Regular", 18, false);
        }
        this.readGlyphSizes();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.locationFontTexture = FontUtils.getHdFontLocation(this.locationFontTextureBase);
        Arrays.fill(unicodePageLocations, null);
        this.readFontTexture();
        this.readGlyphSizes();
    }

    private void readFontTexture() {
        BufferedImage bufferedimage;
        try {
            bufferedimage = TextureUtil.readBufferedImage(this.getResourceInputStream(this.locationFontTexture));
        }
        catch (IOException ioexception1) {
            throw new RuntimeException(ioexception1);
        }
        Properties properties = FontUtils.readFontProperties(this.locationFontTexture);
        this.blend = FontUtils.readBoolean(properties, "blend", false);
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int k = i / 16;
        int l = j / 16;
        float f = (float)i / 128.0f;
        float f1 = Config.limit(f, 1.0f, 2.0f);
        this.offsetBold = 1.0f / f1;
        float f2 = FontUtils.readFloat(properties, "offsetBold", -1.0f);
        if (f2 >= 0.0f) {
            this.offsetBold = f2;
        }
        int[] aint = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
        for (int i1 = 0; i1 < 256; ++i1) {
            int j1 = i1 % 16;
            int k1 = i1 / 16;
            int l1 = 0;
            for (l1 = k - 1; l1 >= 0; --l1) {
                int i2 = j1 * k + l1;
                boolean flag = true;
                for (int j2 = 0; j2 < l && flag; ++j2) {
                    int k2 = (k1 * l + j2) * i;
                    int l2 = aint[i2 + k2];
                    int i3 = l2 >> 24 & 0xFF;
                    if (i3 <= 16) continue;
                    flag = false;
                    break;
                }
                if (!flag) break;
            }
            if (i1 == 65) {
                // empty if block
            }
            if (i1 == 32) {
                l1 = k <= 8 ? (int)(2.0f * f) : (int)(1.5f * f);
            }
            this.charWidthFloat[i1] = (float)(l1 + 1) / f + 1.0f;
        }
        FontUtils.readCustomCharWidths(properties, this.charWidthFloat);
        for (int j3 = 0; j3 < this.charWidth.length; ++j3) {
            this.charWidth[j3] = Math.round(this.charWidthFloat[j3]);
        }
    }

    private void readGlyphSizes() {
        InputStream inputstream = null;
        try {
            inputstream = this.getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
            inputstream.read(this.glyphWidth);
        }
        catch (IOException ioexception) {
            try {
                throw new RuntimeException(ioexception);
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(inputstream);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((InputStream)inputstream);
    }

    private float renderChar(char ch, boolean italic) {
        if (ch != ' ' && ch != '\u00a0') {
            int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(ch);
            return i != -1 && !this.unicodeFlag ? this.renderDefaultChar(i, italic) : this.renderUnicodeChar(ch, italic);
        }
        return !this.unicodeFlag ? this.charWidthFloat[ch] : 4.0f;
    }

    private float renderDefaultChar(int ch, boolean italic) {
        int i = ch % 16 << 3;
        int j = ch / 16 << 3;
        boolean k = italic;
        this.bindTexture(this.locationFontTexture);
        float f = this.charWidthFloat[ch];
        float f1 = 7.99f;
        GL11.glBegin((int)5);
        GL11.glTexCoord2f((float)((float)i / 128.0f), (float)((float)j / 128.0f));
        GL11.glVertex3f((float)(this.posX + (float)k), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)((float)i / 128.0f), (float)(((float)j + 7.99f) / 128.0f));
        GL11.glVertex3f((float)(this.posX - (float)k), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glTexCoord2f((float)(((float)i + f1 - 1.0f) / 128.0f), (float)((float)j / 128.0f));
        GL11.glVertex3f((float)(this.posX + f1 - 1.0f + (float)k), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)(((float)i + f1 - 1.0f) / 128.0f), (float)(((float)j + 7.99f) / 128.0f));
        GL11.glVertex3f((float)(this.posX + f1 - 1.0f - (float)k), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glEnd();
        return f;
    }

    private ResourceLocation getUnicodePageLocation(int page) {
        if (unicodePageLocations[page] == null) {
            BluelunFontRenderer.unicodePageLocations[page] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", page));
            BluelunFontRenderer.unicodePageLocations[page] = FontUtils.getHdFontLocation(unicodePageLocations[page]);
        }
        return unicodePageLocations[page];
    }

    private void loadGlyphTexture(int page) {
        this.bindTexture(this.getUnicodePageLocation(page));
    }

    private float renderUnicodeChar(char ch, boolean italic) {
        if (this.glyphWidth[ch] == 0) {
            return 0.0f;
        }
        int i = ch / 256;
        this.loadGlyphTexture(i);
        int j = this.glyphWidth[ch] >>> 4;
        int k = this.glyphWidth[ch] & 0xF;
        float f = j;
        float f1 = k + 1;
        float f2 = (float)(ch % 16 << 4) + f;
        float f3 = (ch & 0xFF) / 16 << 4;
        float f4 = f1 - f - 0.02f;
        float f5 = italic ? 1.0f : 0.0f;
        GL11.glBegin((int)5);
        GL11.glTexCoord2f((float)(f2 / 256.0f), (float)(f3 / 256.0f));
        GL11.glVertex3f((float)(this.posX + f5), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)(f2 / 256.0f), (float)((f3 + 15.98f) / 256.0f));
        GL11.glVertex3f((float)(this.posX - f5), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glTexCoord2f((float)((f2 + f4) / 256.0f), (float)(f3 / 256.0f));
        GL11.glVertex3f((float)(this.posX + f4 / 2.0f + f5), (float)this.posY, (float)0.0f);
        GL11.glTexCoord2f((float)((f2 + f4) / 256.0f), (float)((f3 + 15.98f) / 256.0f));
        GL11.glVertex3f((float)(this.posX + f4 / 2.0f - f5), (float)(this.posY + 7.99f), (float)0.0f);
        GL11.glEnd();
        return (f1 - f) / 2.0f + 1.0f;
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, true);
    }

    public int drawString(String text, float x, int y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    public int drawString(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, false);
        return this.getStringWidth(text);
    }

    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        int i;
        this.enableAlpha();
        GlStateManager.disableBlend();
        this.resetStyles();
        if (dropShadow) {
            i = this.renderString(text, x + 1.0f, y + 1.0f, color, true);
            i = Math.max(i, this.renderString(text, x, y, color, false));
        } else {
            i = this.renderString(text, x, y, color, false);
        }
        return i;
    }

    private String bidiReorder(String text) {
        return FontRendererCallback.bidiReorder(this, text);
    }

    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    private void renderStringAtPos(String text, boolean shadow) {
        for (int i = 0; i < text.length(); ++i) {
            boolean flag;
            char c0 = text.charAt(i);
            if (c0 == '\u00a7' && i + 1 < text.length()) {
                int l = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (l < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (l < 0 || l > 15) {
                        l = 15;
                    }
                    if (shadow) {
                        l += 16;
                    }
                    int i1 = this.colorCode[l];
                    if (Config.isCustomColors()) {
                        i1 = CustomColors.getTextColor(l, i1);
                    }
                    this.textColor = i1;
                    this.setColor((float)(i1 >> 16) / 255.0f, (float)(i1 >> 8 & 0xFF) / 255.0f, (float)(i1 & 0xFF) / 255.0f, this.alpha);
                } else if (l == 16) {
                    this.randomStyle = true;
                } else if (l == 17) {
                    this.boldStyle = true;
                } else if (l == 18) {
                    this.strikethroughStyle = true;
                } else if (l == 19) {
                    this.underlineStyle = true;
                } else if (l == 20) {
                    this.italicStyle = true;
                } else if (l == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.setColor(this.red, this.blue, this.green, this.alpha);
                }
                ++i;
                continue;
            }
            int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);
            if (this.randomStyle && j != -1) {
                char c1;
                int k = this.getCharWidth(c0);
                while (k != this.getCharWidth(c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length())))) {
                }
                c0 = c1;
            }
            float f1 = j != -1 && !this.unicodeFlag ? this.offsetBold : 0.5f;
            boolean bl = flag = (c0 == '\u0000' || j == -1 || this.unicodeFlag) && shadow;
            if (flag) {
                this.posX -= f1;
                this.posY -= f1;
            }
            float f = this.renderChar(c0, this.italicStyle);
            if (flag) {
                this.posX += f1;
                this.posY += f1;
            }
            if (this.boldStyle) {
                this.posX += f1;
                if (flag) {
                    this.posX -= f1;
                    this.posY -= f1;
                }
                this.renderChar(c0, this.italicStyle);
                this.posX -= f1;
                if (flag) {
                    this.posX += f1;
                    this.posY += f1;
                }
                f += f1;
            }
            this.doDraw(f);
        }
    }

    protected void doDraw(float p_doDraw_1_) {
        if (this.strikethroughStyle) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos(this.posX, this.posY + 4.0f, 0.0).endVertex();
            worldrenderer.pos(this.posX + p_doDraw_1_, this.posY + 4.0f, 0.0).endVertex();
            worldrenderer.pos(this.posX + p_doDraw_1_, this.posY + 4.0f - 1.0f, 0.0).endVertex();
            worldrenderer.pos(this.posX, this.posY + 4.0f - 1.0f, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }
        if (this.underlineStyle) {
            Tessellator tessellator1 = Tessellator.getInstance();
            WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
            GlStateManager.disableTexture2D();
            worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
            int i = this.underlineStyle ? -1 : 0;
            worldrenderer1.pos(this.posX + (float)i, this.posY + 8.0f, 0.0).endVertex();
            worldrenderer1.pos(this.posX + p_doDraw_1_, this.posY + 8.0f, 0.0).endVertex();
            worldrenderer1.pos(this.posX + p_doDraw_1_, this.posY + 8.0f - 1.0f, 0.0).endVertex();
            worldrenderer1.pos(this.posX + (float)i, this.posY + 8.0f - 1.0f, 0.0).endVertex();
            tessellator1.draw();
            GlStateManager.enableTexture2D();
        }
        this.posX += p_doDraw_1_;
    }

    private int renderStringAligned(String text, int x, int y, int width, int color, boolean dropShadow) {
        if (this.bidiFlag) {
            int i = this.getStringWidth(this.bidiReorder(text));
            x = x + width - i;
        }
        return this.renderString(text, x, y, color, dropShadow);
    }

    private int renderString(String text, float x, float y, int color, boolean dropShadow) {
        if (text == null) {
            return 0;
        }
        if (this.bidiFlag) {
            text = this.bidiReorder(text);
        }
        if ((color & 0xFC000000) == 0) {
            color |= 0xFF000000;
        }
        if (dropShadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }
        this.red = (float)(color >> 16 & 0xFF) / 255.0f;
        this.blue = (float)(color >> 8 & 0xFF) / 255.0f;
        this.green = (float)(color & 0xFF) / 255.0f;
        this.alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        this.setColor(this.red, this.blue, this.green, this.alpha);
        this.posX = x;
        this.posY = y;
        this.stringCache.renderString(text, x, y, color, dropShadow);
        return (int)this.posX;
    }

    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        return this.stringCache.getStringWidth(text);
    }

    public int getCharWidth(char character) {
        return Math.round(this.getCharWidthFloat(character));
    }

    private float getCharWidthFloat(char p_getCharWidthFloat_1_) {
        if (p_getCharWidthFloat_1_ == '\u00a7') {
            return -1.0f;
        }
        if (p_getCharWidthFloat_1_ != ' ' && p_getCharWidthFloat_1_ != '\u00a0') {
            int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(p_getCharWidthFloat_1_);
            if (p_getCharWidthFloat_1_ > '\u0000' && i != -1 && !this.unicodeFlag) {
                return this.charWidthFloat[i];
            }
            if (this.glyphWidth[p_getCharWidthFloat_1_] != 0) {
                int j = this.glyphWidth[p_getCharWidthFloat_1_] >>> 4;
                int k = this.glyphWidth[p_getCharWidthFloat_1_] & 0xF;
                if (k > 7) {
                    k = 15;
                    j = 0;
                }
                return (++k - j) / 2 + 1;
            }
            return 0.0f;
        }
        return this.charWidthFloat[32];
    }

    public String trimStringToWidth(String text, int width) {
        return this.stringCache.trimStringToWidth(text, width, false);
    }

    public String trimStringToWidth(String text, int width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0f;
        int i = reverse ? text.length() - 1 : 0;
        int j = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;
        for (int k = i; k >= 0 && k < text.length() && f < (float)width; k += j) {
            char c0 = text.charAt(k);
            float f1 = this.getCharWidthFloat(c0);
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (f1 < 0.0f) {
                flag = true;
            } else {
                f += f1;
                if (flag1) {
                    f += 1.0f;
                }
            }
            if (f > (float)width) break;
            if (reverse) {
                stringbuilder.insert(0, c0);
                continue;
            }
            stringbuilder.append(c0);
        }
        return stringbuilder.toString();
    }

    private String trimStringNewline(String text) {
        while (text != null && !text.isEmpty() && text.charAt(text.length() - 1) == '\n') {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }

    public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
        if (this.blend) {
            GlStateManager.getBlendState(this.oldBlendState);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
        }
        this.resetStyles();
        this.textColor = textColor;
        str = this.trimStringNewline(str);
        this.renderSplitString(str, x, y, wrapWidth, false);
        if (this.blend) {
            GlStateManager.setBlendState(this.oldBlendState);
        }
    }

    private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
        for (String s : this.listFormattedStringToWidth(str, wrapWidth)) {
            this.renderStringAligned(s, x, y, wrapWidth, this.textColor, addShadow);
            y += 8;
        }
    }

    public int splitStringWidth(String str, int maxLength) {
        return 8 * this.listFormattedStringToWidth(str, maxLength).size();
    }

    public void setUnicodeFlag(boolean unicodeFlagIn) {
        this.unicodeFlag = unicodeFlagIn;
    }

    public boolean getUnicodeFlag() {
        return this.unicodeFlag;
    }

    public void setBidiFlag(boolean bidiFlagIn) {
        this.bidiFlag = bidiFlagIn;
    }

    public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
        return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
    }

    String wrapFormattedStringToWidth(String str, int wrapWidth) {
        if (str.length() <= 1) {
            return str;
        }
        int i = this.sizeStringToWidth(str, wrapWidth);
        if (str.length() <= i) {
            return str;
        }
        String s = str.substring(0, i);
        char c0 = str.charAt(i);
        boolean flag = c0 == ' ' || c0 == '\n';
        String s1 = BluelunFontRenderer.getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
        return s + "\n" + this.wrapFormattedStringToWidth(s1, wrapWidth);
    }

    private int sizeStringToWidth(String str, int wrapWidth) {
        int j;
        int i = str.length();
        float f = 0.0f;
        int k = -1;
        boolean flag = false;
        for (j = 0; j < i; ++j) {
            char c0 = str.charAt(j);
            switch (c0) {
                case '\n': {
                    --j;
                    break;
                }
                case ' ': {
                    k = j;
                }
                default: {
                    f += (float)this.getCharWidth(c0);
                    if (!flag) break;
                    f += 1.0f;
                    break;
                }
                case '\u00a7': {
                    char c1;
                    if (j >= i - 1) break;
                    if ((c1 = str.charAt(++j)) != 'l' && c1 != 'L') {
                        if (c1 != 'r' && c1 != 'R' && !BluelunFontRenderer.isFormatColor(c1)) break;
                        flag = false;
                        break;
                    }
                    flag = true;
                }
            }
            if (c0 == '\n') {
                k = ++j;
                break;
            }
            if (Math.round(f) > wrapWidth) break;
        }
        return j != i && k != -1 && k < j ? k : j;
    }

    private static boolean isFormatColor(char colorChar) {
        return colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F';
    }

    private static boolean isFormatSpecial(char formatChar) {
        return formatChar >= 'k' && formatChar <= 'o' || formatChar >= 'K' && formatChar <= 'O' || formatChar == 'r' || formatChar == 'R';
    }

    public static String getFormatFromString(String text) {
        StringBuilder s = new StringBuilder();
        int i = -1;
        int j = text.length();
        while ((i = text.indexOf(167, i + 1)) != -1) {
            if (i >= j - 1) continue;
            char c0 = text.charAt(i + 1);
            if (BluelunFontRenderer.isFormatColor(c0)) {
                s = new StringBuilder("\u00a7" + c0);
                continue;
            }
            if (!BluelunFontRenderer.isFormatSpecial(c0)) continue;
            s.append("\u00a7").append(c0);
        }
        return s.toString();
    }

    public boolean getBidiFlag() {
        return this.bidiFlag;
    }

    public int getColorCode(char character) {
        int i = "0123456789abcdef".indexOf(character);
        if (i >= 0 && i < this.colorCode.length) {
            int j = this.colorCode[i];
            if (Config.isCustomColors()) {
                j = CustomColors.getTextColor(i, j);
            }
            return j;
        }
        return 0xFFFFFF;
    }

    protected void setColor(float p_setColor_1_, float p_setColor_2_, float p_setColor_3_, float p_setColor_4_) {
        GlStateManager.color(p_setColor_1_, p_setColor_2_, p_setColor_3_, p_setColor_4_);
    }

    protected void enableAlpha() {
        GlStateManager.enableAlpha();
    }

    protected void bindTexture(ResourceLocation p_bindTexture_1_) {
        this.renderEngine.bindTexture(p_bindTexture_1_);
    }

    protected InputStream getResourceInputStream(ResourceLocation p_getResourceInputStream_1_) throws IOException {
        return Minecraft.getMinecraft().getResourceManager().getResource(p_getResourceInputStream_1_).getInputStream();
    }

    @Override
    public StringCache getStringCache() {
        return this.stringCache;
    }

    @Override
    public void setStringCache(StringCache value) {
        this.stringCache = value;
    }

    @Override
    public boolean isDropShadowEnabled() {
        return false;
    }

    @Override
    public void setDropShadowEnabled(boolean value) {
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean value) {
    }

    public float getHeight() {
        return 8.0f;
    }

    public void drawCenteredString(String s, int x, float y, int textColor) {
        this.drawString(s, (float)x - (float)this.getStringWidth(s) / 2.0f, y, textColor);
    }

    public void drawCenteredString(String s, float x, float y, int textColor) {
        this.drawString(s, x - (float)this.getStringWidth(s) / 2.0f, y, textColor);
    }

    public int drawStringWithColorNoShadow(String text, float x, float y, int color) {
        String[] array;
        text = "\u00a7r" + text;
        float len = -1.0f;
        for (String str : array = text.split("\u00a7")) {
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
                    break;
                }
                case 'r': {
                    color = new Color(255, 255, 255).getRGB();
                }
            }
            Color col = new Color(color);
            str = str.substring(1);
            this.drawString(str, x + len, y, SimpleRender.reAlpha(col.getRGB(), (float)new Color(color).getAlpha() / 255.0f));
            len += (float)(this.getStringWidth(str) + 1);
        }
        return (int)len;
    }
}

