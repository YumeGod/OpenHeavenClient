/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.font.fontRenderer;

import heaven.main.management.Manager;
import heaven.main.ui.font.fontRenderer.UnicodeFontRenderer;
import heaven.main.ui.font.fontRenderer.newuse.BluelunFontRenderer;
import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontManager
implements Manager {
    private final HashMap<String, HashMap<Float, UnicodeFontRenderer>> fonts = new HashMap();
    public final UnicodeFontRenderer baloo17 = this.getFont("baloo", 17.0f, false);
    public final UnicodeFontRenderer baloo18 = this.getFont("baloo", 18.0f, false);
    public final UnicodeFontRenderer arial16 = this.getFont("Arial", 16.0f, false);
    public final UnicodeFontRenderer arial17 = this.getFont("Arial", 17.0f, false);
    public final UnicodeFontRenderer comfortaa17 = this.getFont("comfortaa", 17.0f, false);
    public final UnicodeFontRenderer comfortaa16 = this.getFont("comfortaa", 16.0f, false);
    public final BluelunFontRenderer chinese17;
    public final UnicodeFontRenderer SFUI18 = this.getFont("SF", 18.0f, false);
    public final UnicodeFontRenderer SFUI19 = this.getFont("SF", 19.0f, false);
    public int offset;

    public FontManager() {
        this.chinese17 = this.getFastChinese(17, true);
    }

    public BluelunFontRenderer getFastChinese(int size, boolean antiAlias) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/msyh.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return new BluelunFontRenderer(font, size, antiAlias);
    }

    public UnicodeFontRenderer getFont(String s, float size, boolean otf) {
        UnicodeFontRenderer UnicodeFontRenderer2 = null;
        try {
            if (this.fonts.containsKey(s) && this.fonts.get(s).containsKey(Float.valueOf(size))) {
                return this.fonts.get(s).get(Float.valueOf(size));
            }
            String s2 = otf ? ".otf" : ".ttf";
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/" + s + s2)).getInputStream();
            Font font = Font.createFont(0, is).deriveFont(size);
            UnicodeFontRenderer2 = new UnicodeFontRenderer(font.deriveFont(size), s.equals("msyh"));
            UnicodeFontRenderer2.setUnicodeFlag(true);
            UnicodeFontRenderer2.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());
            HashMap<Float, UnicodeFontRenderer> hashMap = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(s)) {
                hashMap.putAll((Map)this.fonts.get(s));
            }
            hashMap.put(Float.valueOf(size), UnicodeFontRenderer2);
            this.fonts.put(s, hashMap);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return UnicodeFontRenderer2;
    }

    @Override
    public void init() {
    }
}

