/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.text.ArabicShaping
 *  com.ibm.icu.text.ArabicShapingException
 *  com.ibm.icu.text.Bidi
 */
package heaven.main.ui.font.fontRenderer.newuse;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import heaven.main.ui.font.fontRenderer.StringCache;
import heaven.main.ui.font.fontRenderer.newuse.IBFFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class FontRendererCallback {
    public static final boolean betterFontsEnabled = true;

    public static void constructor(IBFFontRenderer font, ResourceLocation location) {
        if (((FontRenderer)((Object)font)).getClass() != FontRenderer.class) {
            return;
        }
        if (location.getResourcePath().equalsIgnoreCase("textures/font/ascii.png") && font.getStringCache() == null) {
            font.setDropShadowEnabled(true);
            int[] colorCode = ((FontRenderer)((Object)font)).colorCode;
            font.setStringCache(new StringCache(colorCode));
            font.getStringCache().setDefaultFont("Lucida Sans Regular", 18, false);
        }
    }

    public static String bidiReorder(IBFFontRenderer font, String text) {
        if (font.getStringCache() != null) {
            return text;
        }
        try {
            Bidi bidi = new Bidi(new ArabicShaping(8).shape(text), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        }
        catch (ArabicShapingException var3) {
            return text;
        }
    }
}

