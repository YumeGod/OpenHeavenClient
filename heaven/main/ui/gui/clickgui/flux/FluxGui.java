/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.clickgui.flux;

import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.ClickGui;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.guimainmenu.ColorCreator;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.TranslateUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;
import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class FluxGui
extends GuiScreen {
    private static List<Module> inSetting = new CopyOnWriteArrayList<Module>();
    private static ModuleType currentCategory = ModuleType.Combat;
    private static int x;
    private static int y;
    private static int wheel;
    private boolean need2move;
    private int dragX;
    private int dragY;
    private final TranslateUtil translate = new TranslateUtil(0.0f, 0.0f);
    float finheight;
    float animheight;
    private float animationPosition;
    boolean showSetting;
    float valueSizeY;
    float valueY;
    final ModuleType[] values = ModuleType.values();
    float index = 20.0f;
    final CFontRenderer font1;
    final CFontRenderer font2;
    final CFontRenderer font3;

    public FluxGui() {
        this.font1 = Client.instance.FontLoaders.regular18;
        this.font2 = Client.instance.FontLoaders.regular15;
        this.font3 = Client.instance.FontLoaders.regular14;
        this.need2move = false;
        this.dragX = 0;
        this.dragY = 0;
        this.translate.setX(0.0f);
        this.translate.setY(0.0f);
        this.animationPosition = 150.0f;
    }

    @Override
    public void initGui() {
        super.initGui();
        if (x > width) {
            x = 130;
        }
        if (y > height) {
            y = 130;
        }
        this.need2move = false;
        if (((Boolean)ClickGui.Blur.getValue()).booleanValue() && OpenGlHelper.shadersSupported) {
            if (Minecraft.thePlayer != null) {
                if (this.mc.entityRenderer.theShaderGroup != null) {
                    this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
                }
                this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (((Boolean)ClickGui.Streamer.getValue()).booleanValue()) {
            this.drawGradientRect(0, 0, width, height, new Color(0, 0, 0, 0).getRGB(), ColorCreator.createRainbowFromOffset(-6000, 5));
        }
        this.animationPosition = AnimationUtil.moveUD(this.animationPosition, 0.0f, SimpleRender.processFPS(1000.0f, 0.008f), SimpleRender.processFPS(1000.0f, 0.007f));
        GL11.glRotatef((float)this.animationPosition, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glTranslatef((float)0.0f, (float)this.animationPosition, (float)0.0f);
        if (this.need2move) {
            x = mouseX - this.dragX;
            y = mouseY - this.dragY;
        }
        if (!Mouse.isButtonDown((int)0) && this.need2move) {
            this.need2move = false;
        }
        RenderUtil.drawBorderedRect(x - 25, y + 1, x + 492, y + 314, 1.0f, new Color(20, 20, 20).getRGB(), new Color(20, 20, 20).getRGB());
        SimpleRender.drawRect(x + 75, y + 1, x + 492, y + 314, new Color(0, 0, 0).getRGB());
        SimpleRender.drawRect(x + 75, (float)(y + 42) + this.animheight, (double)x - 25.5, (float)(y + 65) + this.animheight, this.getColor());
        Client.instance.FontLoaders.NovICON42.drawString("c", x - 5 - 13, y + 10, this.getColor());
        Client.instance.FontLoaders.regular26.drawString(Client.instance.name, x + 5, y + 12, new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB());
        float strX = x + 20 - 15;
        for (int i = 0; i < this.values.length; ++i) {
            if (this.values[i] == currentCategory) {
                this.finheight = i * 30;
            }
            Client.instance.FontLoaders.regular19.drawString(this.values[i].name(), strX - 1.0f, y + 49 + i * 30, -1);
            if (this.values[i].toString().equals("Combat")) {
                Client.instance.FontLoaders.icon24.drawString("1", strX - 15.0f, y + 50 - 1, -1);
            }
            if (this.values[i].toString().equals("Movement")) {
                Client.instance.FontLoaders.icon26.drawString("5", strX - 15.0f, y + 50 + 30 - 1, -1);
            }
            if (this.values[i].toString().equals("Render")) {
                Client.instance.FontLoaders.guiicons22.drawString("F", strX - 15.0f, y + 50 + 60, -1);
            }
            if (this.values[i].toString().equals("Player")) {
                Client.instance.FontLoaders.guiicons28.drawString("C", strX - 15.0f, y + 50 + 90 - 1, -1);
            }
            if (this.values[i].toString().equals("World")) {
                Client.instance.FontLoaders.guiicons30.drawString("E", strX - 15.0f, y + 50 + 120 - 1, -1);
            }
            if (this.values[i].toString().equals("Misc")) {
                Client.instance.FontLoaders.guiicons28.drawString("G", strX - 15.0f, y + 50 + 150 - 1, -1);
            }
            if (this.values[i].toString().equals("Globals")) {
                Client.instance.FontLoaders.guiicons28.drawString("J", strX - 15.0f, y + 50 + 180 - 1, -1);
            }
            if (!this.values[i].toString().equals("Script")) continue;
            Client.instance.FontLoaders.novoicons25.drawString("G", strX - 15.0f, y + 50 + 210, -1);
        }
        this.animheight = AnimationUtil.moveUD(this.animheight, this.finheight, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        float startX = x + 80 + 2 + 13;
        float startY = y + 9 + 2 + 28 - 30 + 30;
        float length = 375.0f;
        RenderUtil.startGlScissor((int)startX, (int)(startY - 16.0f), (int)length, 285 - (int)this.animationPosition);
        this.index = 20.0f;
        float moduleY = this.translate.getY();
        for (Module m : Client.instance.getModuleManager().getModules()) {
            if (m.getType() != currentCategory) continue;
            if (moduleY > startY + 185.0f || startY <= length - 350.0f) break;
            RenderUtil.drawRoundedRectSmooth(startX, startY + moduleY, startX + length, startY + moduleY + 31.0f, 3.0f, new Color(20, 20, 20).getRGB());
            this.font1.drawString(m.getName(), startX + 8.0f, startY + 9.0f + moduleY + 2.0f, -1);
            RenderUtil.drawRoundRect(startX + length - 25.0f - this.index, startY + moduleY + 10.0f, startX + length - 5.0f - this.index, startY + moduleY + 20.0f, 5, new Color(0, 0, 0).getRGB());
            float left = m.isEnabled() ? startX + length - 14.0f : startX + length - 24.0f;
            float right = m.isEnabled() ? startX + length - 6.0f : startX + length - 16.0f;
            RenderUtil.drawRoundedRectSmooth(left - this.index, startY + moduleY + 11.0f, right - this.index, startY + moduleY + 19.0f, 4.0f, this.getColor());
            this.showSetting = inSetting.contains(m);
            if (!m.getValues().isEmpty()) {
                Client.instance.FontLoaders.FLUXICON21.drawString(this.showSetting ? "h" : "i", startX + 8.0f + length - 27.0f, startY + 9.0f + moduleY + 3.0f, -1);
            }
            this.valueSizeY = m.getValues().size() * 20 + 15;
            this.valueY = moduleY + 48.0f;
            if (this.showSetting) {
                SimpleRender.drawRect(startX + 3.0f, startY + moduleY + 32.0f, startX + length - 3.0f, startY + moduleY + 38.0f, new Color(30, 30, 30).getRGB());
                RenderUtil.drawRoundedRectSmooth(startX + 3.0f, startY + moduleY + 32.0f, startX + length - 3.0f, startY + moduleY + 24.0f + this.valueSizeY + 8.0f, 3.0f, new Color(30, 30, 30).getRGB());
                for (Value setting : m.getValues()) {
                    Value s;
                    if (setting instanceof Mode) {
                        boolean hover;
                        s = (Mode)setting;
                        this.font2.drawString(s.getName(), startX + 10.0f, startY + this.valueY - 1.0f, -1);
                        RenderUtil.drawRoundedRectSmooth(startX + length - 85.0f, startY + this.valueY - 4.0f, startX + length - 6.0f, startY + this.valueY + 8.0f, 3.0f, new Color(10, 10, 10).getRGB());
                        float longValue = (startX + length - 6.0f - (startX + length - 85.0f)) / 2.0f;
                        this.font2.drawCenteredString(((Mode)s).getModeAsString(), startX + length - 6.0f - longValue, startY + this.valueY - 0.5f, this.getColor());
                        boolean bl = hover = (float)mouseX > startX + length - 85.0f && (float)mouseX < startX + length - 6.0f && (float)mouseY > startY + this.valueY - 4.0f && (float)mouseY < startY + this.valueY + 8.0f;
                        if (hover) {
                            RenderUtil.drawRoundRect(startX + length - 85.0f, startY + this.valueY - 4.0f, startX + length - 6.0f, startY + this.valueY + 8.0f, 3, new Color(0, 0, 0, 100).getRGB());
                        }
                    }
                    if (setting instanceof Numbers) {
                        boolean hover;
                        s = (Numbers)setting;
                        this.font2.drawString(s.getName(), startX + 10.0f, startY + this.valueY - 3.0f, -1);
                        double inc = ((Number)((Numbers)s).getIncrement()).doubleValue();
                        double max = ((Number)((Numbers)s).getMaximum()).doubleValue();
                        double min = ((Number)((Numbers)s).getMinimum()).doubleValue();
                        double valn = ((Number)s.getValue()).doubleValue();
                        float longValue = startX + length - 6.0f - (startX + length - 83.0f);
                        this.font3.drawString(String.valueOf(((Number)s.getValue()).doubleValue()), startX + length - 84.0f, startY + this.valueY - 2.0f, -1);
                        RenderUtil.drawRoundRect(startX + length - 85.0f, startY + this.valueY + 5.0f, startX + length - 6.0f, startY + this.valueY + 7.0f, 1, new Color(10, 10, 10).getRGB());
                        RenderUtil.drawRoundRect(startX + length - 85.0f, startY + this.valueY + 5.0f, (double)(startX + length - 85.0f) + (double)longValue * (valn - min) / (max - min) + 2.0, startY + this.valueY + 7.0f, 1, this.getColor());
                        boolean bl = hover = (float)mouseX > startX + length - 88.0f && (float)mouseX < startX + length - 3.0f && (float)mouseY > startY + this.valueY + 2.0f && (float)mouseY < startY + this.valueY + 11.0f;
                        if (hover) {
                            RenderUtil.drawRoundRect(startX + length - 85.0f, startY + this.valueY + 5.0f, startX + length - 6.0f, startY + this.valueY + 7.0f, 1, new Color(0, 0, 0, 100).getRGB());
                            if (Mouse.isButtonDown((int)0)) {
                                double valAbs = (float)mouseX - (startX + length - 85.0f);
                                double perc = valAbs / ((double)longValue * Math.max(Math.min(valn / max, 0.0), 1.0));
                                perc = Math.min(Math.max(0.0, perc), 1.0);
                                double valRel = (max - min) * perc;
                                double val = min + valRel;
                                val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                                s.setValue(val);
                            }
                        }
                    }
                    if (setting instanceof Option) {
                        s = (Option)setting;
                        this.font2.drawString(s.getName(), startX + 10.0f, startY + this.valueY - 3.0f, -1);
                        boolean hover = (float)mouseX > startX + length - 18.0f && (float)mouseX < startX + length - 6.0f && (float)mouseY > startY + this.valueY - 4.0f && (float)mouseY < startY + this.valueY + 8.0f;
                        RenderUtil.drawRoundedRectSmooth(startX + length - 18.0f, startY + this.valueY - 4.0f, startX + length - 6.0f, startY + this.valueY + 8.0f, 2.0f, new Color(10, 10, 10).getRGB());
                        if (((Boolean)s.getValue()).booleanValue()) {
                            Client.instance.FontLoaders.NovICON24.drawString("j", startX + length - 18.0f, startY + this.valueY - 1.0f, this.getColor());
                        }
                        if (hover) {
                            RenderUtil.drawRoundRect(startX + length - 18.0f, startY + this.valueY - 4.0f, startX + length - 6.0f, startY + this.valueY + 8.0f, 2, new Color(0, 0, 0, 100).getRGB());
                        }
                    }
                    this.valueY += 20.0f;
                }
                RenderUtil.drawGradientSidewaysV(startX + 3.0f, startY + moduleY + 24.0f + 8.0f, startX + length - 3.0f, startY + moduleY + 34.0f + 8.0f, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0).getRGB());
            }
            moduleY += this.showSetting ? 26.0f + this.valueSizeY + 15.0f : 51.0f;
        }
        RenderUtil.stopGlScissor();
        int real = Mouse.getDWheel();
        float moduleHeight = moduleY - this.translate.getY();
        if (Mouse.hasWheel() && (float)mouseX > startX - 15.0f && (float)mouseY > startY - 40.0f && (float)mouseX < startX + 395.0f && (float)mouseY < startY + 267.0f) {
            if (real > 0 && wheel < 0) {
                for (int i = 0; i < 5 && wheel < 0; wheel += 7 + Mouse.getDWheel(), ++i) {
                }
            } else {
                for (int i = 0; i < 5 && real < 0 && moduleHeight > 158.0f && (float)Math.abs(wheel) < moduleHeight - 154.0f; ++i) {
                    wheel -= 7 + Mouse.getDWheel();
                }
            }
        }
        this.translate.interpolate(0.0f, wheel, 0.15f);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        boolean hover2top;
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean bl = hover2top = mouseX > x && mouseX < x + 273 - 4 + 165 + 84 + 11 && mouseY > y && mouseY < y + 35;
        if (hover2top && mouseButton == 0) {
            this.dragX = mouseX - x;
            this.dragY = mouseY - y;
            this.need2move = true;
        } else {
            float cateY = 0.0f;
            for (ModuleType category : ModuleType.values()) {
                boolean typehover;
                float strY = (float)(y + 55) + cateY;
                boolean bl2 = typehover = mouseX > x + 5 - 30 && mouseX < x + 65 + 30 && (float)mouseY > strY - 30.0f && (float)mouseY < strY + 20.0f;
                if (typehover && mouseButton == 0) {
                    currentCategory = category;
                    wheel = 0;
                    break;
                }
                cateY += 26.0f;
            }
            float startX = x + 80 + 2 + 13;
            float startY = y + 9 + 2 + 28 - 30 + 30;
            float length = 375.0f;
            float moduleY = this.translate.getY();
            float size = 85.0f;
            for (Module m : Client.instance.getModuleManager().getModules()) {
                boolean onModuleRect;
                if (m.getType() != currentCategory) continue;
                if (moduleY > startY + 185.0f || startY <= length - 350.0f) break;
                boolean onToggleButton = (float)mouseX > startX + length - 25.0f - this.index && (float)mouseX < startX + length - 5.0f - this.index && (float)mouseY > startY + moduleY + 7.0f && (float)mouseY < startY + moduleY + 20.0f && (float)mouseY < startY + 14.0f + 140.0f + size && (float)mouseY > startY;
                boolean bl3 = onModuleRect = (float)mouseX > startX && (float)mouseX < startX + length && (float)mouseY > startY + moduleY && (float)mouseY < startY + moduleY + 28.0f && (float)mouseY < startY + 14.0f + 140.0f + size && (float)mouseY > startY;
                if (onToggleButton && mouseButton == 0) {
                    m.setEnabled(!m.isEnabled());
                }
                if (onModuleRect && mouseButton == 1) {
                    if (inSetting.contains(m)) {
                        inSetting.remove(m);
                    } else if (!m.getValues().isEmpty()) {
                        inSetting.add(m);
                    }
                }
                boolean showSetting = inSetting.contains(m);
                float valueSizeY = m.getValues().size() * 20 + 5 + 10;
                float valueY = moduleY + 35.0f + 8.0f + 5.0f;
                if (showSetting) {
                    SimpleRender.drawRect(startX + 3.0f, startY + moduleY + 24.0f, startX + length - 3.0f, startY + moduleY + 24.0f + valueSizeY, new Color(30, 30, 30).getRGB());
                    for (Value setting : m.getValues()) {
                        boolean hover;
                        Value s;
                        if (setting instanceof Mode) {
                            s = (Mode)setting;
                            boolean bl4 = hover = (float)mouseX > startX + length - 85.0f && (float)mouseX < startX + length - 6.0f && (float)mouseY > startY + valueY - 4.0f && (float)mouseY < startY + valueY + 8.0f;
                            if (hover && (mouseButton == 0 || mouseButton == 1)) {
                                List<String> options = Arrays.asList(s.getModes());
                                int indexz = options.indexOf(s.getValue());
                                indexz = mouseButton == 0 ? ++indexz : --indexz;
                                if (indexz >= options.size()) {
                                    indexz = 0;
                                } else if (indexz < 0) {
                                    indexz = options.size() - 1;
                                }
                                s.setValue(s.getModes()[indexz]);
                            }
                        }
                        if (setting instanceof Option) {
                            s = (Option)setting;
                            boolean bl5 = hover = (float)mouseX > startX + length - 18.0f && (float)mouseX < startX + length - 6.0f && (float)mouseY > startY + valueY - 4.0f && (float)mouseY < startY + valueY + 8.0f;
                            if (hover && (mouseButton == 0 || mouseButton == 2 || mouseButton == 1)) {
                                s.setValue((Boolean)s.getValue() == false);
                            }
                        }
                        valueY += 20.0f;
                    }
                }
                moduleY += showSetting ? 26.0f + valueSizeY : 51.0f;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        boolean hover2top;
        super.mouseReleased(mouseX, mouseY, state);
        float startY = y + 9 + 2 + 28;
        boolean bl = hover2top = mouseX > x + 1 && mouseX < x + 349 && mouseY > y + 1 && mouseY < y + 9 && (float)mouseY < startY + 14.0f + 140.0f + 85.0f && (float)mouseY > startY;
        if (hover2top && state == 0) {
            this.dragX = mouseX - x;
            this.dragY = mouseY - y;
            this.need2move = false;
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ClickGui.memoriseX = x;
        ClickGui.memoriseY = y;
        ClickGui.memoriseWheel = wheel;
        ClickGui.memoriseML = inSetting;
        ClickGui.memoriseCatecory = currentCategory;
    }

    public static void setX(int state) {
        x = state;
    }

    public static void setY(int state) {
        y = state;
    }

    public static void setCategory(ModuleType state) {
        currentCategory = state;
    }

    public static void setInSetting(List<Module> moduleList) {
        inSetting = moduleList;
    }

    public static void setWheel(int state) {
        wheel = state;
    }

    private int getColor() {
        return new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB();
    }
}

