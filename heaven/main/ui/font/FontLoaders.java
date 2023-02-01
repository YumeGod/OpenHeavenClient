/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.font;

import heaven.main.ui.font.CFontRenderer;
import java.awt.Font;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontLoaders {
    public final CFontRenderer SF18 = new CFontRenderer(this.getSimpleFont(18));
    public final CFontRenderer FLUXICON21 = new CFontRenderer(this.FLUX(21));
    public final CFontRenderer NovICON10 = new CFontRenderer(this.FLUX(10));
    public final CFontRenderer NovICON12 = new CFontRenderer(this.FLUX(12));
    public final CFontRenderer NovICON24 = new CFontRenderer(this.FLUX(24));
    public final CFontRenderer NovICON42 = new CFontRenderer(this.FLUX(42));
    public final CFontRenderer Comfortaa10 = new CFontRenderer(this.getComfortaa(10));
    public final CFontRenderer Comfortaa11 = new CFontRenderer(this.getComfortaa(11));
    public final CFontRenderer Comfortaa12 = new CFontRenderer(this.getComfortaa(12));
    public final CFontRenderer Comfortaa13 = new CFontRenderer(this.getComfortaa(13));
    public final CFontRenderer Comfortaa14 = new CFontRenderer(this.getComfortaa(14));
    public final CFontRenderer Comfortaa15 = new CFontRenderer(this.getComfortaa(15));
    public final CFontRenderer Comfortaa16 = new CFontRenderer(this.getComfortaa(16));
    public final CFontRenderer Comfortaa17 = new CFontRenderer(this.getComfortaa(17));
    public final CFontRenderer Comfortaa18 = new CFontRenderer(this.getComfortaa(18));
    public final CFontRenderer Comfortaa19 = new CFontRenderer(this.getComfortaa(19));
    public final CFontRenderer Comfortaa20 = new CFontRenderer(this.getComfortaa(20));
    public final CFontRenderer Comfortaa22 = new CFontRenderer(this.getComfortaa(22));
    public final CFontRenderer Comfortaa24 = new CFontRenderer(this.getComfortaa(24));
    public final CFontRenderer Comfortaa28 = new CFontRenderer(this.getComfortaa(28));
    public final CFontRenderer Comfortaa30 = new CFontRenderer(this.getComfortaa(30));
    public final CFontRenderer Comfortaa34 = new CFontRenderer(this.getComfortaa(34));
    public final CFontRenderer Comfortaa36 = new CFontRenderer(this.getComfortaa(36));
    public final CFontRenderer Comfortaa45 = new CFontRenderer(this.getComfortaa(45));
    public final CFontRenderer bold50 = new CFontRenderer(this.getBold(50));
    public final CFontRenderer bold45 = new CFontRenderer(this.getBold(45));
    public final CFontRenderer bold30 = new CFontRenderer(this.getBold(30));
    public final CFontRenderer guiicons22 = new CFontRenderer(this.GuiICONS(22));
    public final CFontRenderer guiicons28 = new CFontRenderer(this.GuiICONS(28));
    public final CFontRenderer guiicons30 = new CFontRenderer(this.GuiICONS(30));
    public final CFontRenderer guiicons34 = new CFontRenderer(this.GuiICONS(34));
    public final CFontRenderer icon24 = new CFontRenderer(this.GUIICONS2(24));
    public final CFontRenderer icon26 = new CFontRenderer(this.GUIICONS2(26));
    public final CFontRenderer CSGO40 = new CFontRenderer(this.CSGO(40));
    public final CFontRenderer CSGO46 = new CFontRenderer(this.CSGO(46));
    public final CFontRenderer CSGO36 = new CFontRenderer(this.CSGO(36));
    public final CFontRenderer FLUXICON16 = new CFontRenderer(this.FLUX(16));
    public final CFontRenderer guiicons18 = new CFontRenderer(this.GuiICONS(18));
    public final CFontRenderer guiicons24 = new CFontRenderer(this.GuiICONS(24));
    public final CFontRenderer icon18 = new CFontRenderer(this.GUIICONS2(18));
    public final CFontRenderer icon20 = new CFontRenderer(this.GUIICONS2(20));
    public final CFontRenderer bold14 = new CFontRenderer(this.bold(14));
    public final CFontRenderer bold18 = new CFontRenderer(this.bold(18));
    public final CFontRenderer bold20 = new CFontRenderer(this.bold(20));
    public final CFontRenderer bold22 = new CFontRenderer(this.bold(22));
    public final CFontRenderer bold16 = new CFontRenderer(this.bold(16));
    public final CFontRenderer bold15 = new CFontRenderer(this.bold(15));
    public final CFontRenderer thebold20 = new CFontRenderer(this.bold(20));
    public final CFontRenderer Logo10 = new CFontRenderer(this.FLUX(10));
    public final CFontRenderer sessionInfo16 = new CFontRenderer(this.getInfoFont(16));
    public final CFontRenderer sessionInfo19 = new CFontRenderer(this.getInfoFont(19));
    public final CFontRenderer sessionInfo20 = new CFontRenderer(this.getInfoFont(20));
    public final CFontRenderer sessionInfo22 = new CFontRenderer(this.getInfoFont(22));
    public final CFontRenderer simp17 = new CFontRenderer(this.getSimpleFont(17));
    public final CFontRenderer simp16 = new CFontRenderer(this.getSimpleFont(16));
    public final CFontRenderer novoicons25 = new CFontRenderer(this.getNovoIconsFont(25));
    public final CFontRenderer novoicons24 = new CFontRenderer(this.getNovoIconsFont(24));
    public final CFontRenderer novoicons18 = new CFontRenderer(this.getNovoIconsFont(18));
    public final CFontRenderer regular36 = new CFontRenderer(this.getRegular(36));
    public final CFontRenderer regular26 = new CFontRenderer(this.getRegular(26));
    public final CFontRenderer regular24 = new CFontRenderer(this.getRegular(24));
    public final CFontRenderer regular20 = new CFontRenderer(this.getRegular(20));
    public final CFontRenderer regular19 = new CFontRenderer(this.getRegular(19));
    public final CFontRenderer regular18 = new CFontRenderer(this.getRegular(18));
    public final CFontRenderer regular17 = new CFontRenderer(this.getRegular(17));
    public final CFontRenderer regular16 = new CFontRenderer(this.getRegular(16));
    public final CFontRenderer regular15 = new CFontRenderer(this.getRegular(15));
    public final CFontRenderer regular14 = new CFontRenderer(this.getRegular(14));
    public final CFontRenderer regular13 = new CFontRenderer(this.getRegular(13));
    public final CFontRenderer regular12 = new CFontRenderer(this.getRegular(12));
    public final CFontRenderer regular10 = new CFontRenderer(this.getRegular(10));

    private Font getRegular(int size) {
        Font font;
        try {
            InputStream is2 = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/regular.ttf")).getInputStream();
            font = Font.createFont(0, is2);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex2) {
            font = new Font("default", 0, size);
        }
        return font;
    }

    private Font getNovoIconsFont(int size) {
        Font font;
        try {
            InputStream is2 = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/stylesicons.ttf")).getInputStream();
            font = Font.createFont(0, is2);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex2) {
            font = new Font("default", 0, size);
        }
        return font;
    }

    private Font getInfoFont(int size) {
        Font font;
        try {
            InputStream is2 = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/SessionInfo.ttf")).getInputStream();
            font = Font.createFont(0, is2);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex2) {
            font = new Font("default", 0, size);
        }
        return font;
    }

    private Font getSFUIFont(int size) {
        Font font;
        try {
            InputStream is2 = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/SF.ttf")).getInputStream();
            font = Font.createFont(0, is2);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex2) {
            font = new Font("default", 0, size);
        }
        return font;
    }

    private Font getSimpleFont(int size) {
        Font font;
        try {
            InputStream is2 = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/SF.ttf")).getInputStream();
            font = Font.createFont(0, is2);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex2) {
            font = new Font("default", 0, size);
        }
        return font;
    }

    public Font CSGO(int size) {
        Font font;
        try {
            InputStream is2 = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/CSGO.ttf")).getInputStream();
            font = Font.createFont(0, is2);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex2) {
            font = new Font("default", 0, size);
        }
        return font;
    }

    public Font getBold(int size) {
        Font font;
        try {
            InputStream is2 = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/ArialBold.ttf")).getInputStream();
            font = Font.createFont(0, is2);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex2) {
            font = new Font("default", 0, size);
        }
        return font;
    }

    public Font bold(int size) {
        Font font;
        try {
            InputStream is2 = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/Lato-Bold.ttf")).getInputStream();
            font = Font.createFont(0, is2);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex2) {
            font = new Font("default", 0, size);
        }
        return font;
    }

    private Font GUIICONS2(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/GuiICONS2.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            font = new Font("default", 0, size);
        }
        return font;
    }

    private Font GuiICONS(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/GuiICONS.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            font = new Font("default", 0, size);
        }
        return font;
    }

    private Font FLUX(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/fluxicon.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            font = new Font("default", 0, size);
        }
        return font;
    }

    private Font getComfortaa(int size) {
        Font font;
        try {
            InputStream is2 = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/Arial.ttf")).getInputStream();
            font = Font.createFont(0, is2);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex2) {
            font = new Font("default", 0, size);
        }
        return font;
    }
}

