/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.clickgui.fakeexhi;

import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.ClickGui;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.guimainmenu.ColorCreator;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class Clickgui
extends GuiScreen {
    public static ModuleType currentModuleType;
    public static Module currentModule;
    public static float startX;
    public static float startY;
    public int moduleStart = 0;
    public int valueStart = 0;
    boolean previousmouse = true;
    float x;
    float mY;
    boolean a = false;
    String inmode = null;
    String nowinmode = null;
    String inmodule = null;
    boolean mouse;
    public float moveX = 0.0f;
    public float moveY = 0.0f;
    private static final CFontRenderer titlefont;
    private static final CFontRenderer efont;

    public static int getAstolfoRainbow(int v1) {
        double d;
        double length = 1.6;
        double delay = Math.ceil((System.currentTimeMillis() + (long)((double)v1 * 1.6)) / 5L);
        float rainbow = (double)((float)(d / 360.0)) < 0.5 ? -((float)(delay / 360.0)) : (float)((delay %= 360.0) / 360.0);
        return Color.getHSBColor(rainbow, 0.6f, 1.0f).getRGB();
    }

    @Override
    public void initGui() {
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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (Clickgui.isHovered(startX, startY - 25.0f, startX + 400.0f, startY + 25.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = (float)mouseX - startX;
                this.moveY = (float)mouseY - startY;
            } else {
                startX = (float)mouseX - this.moveX;
                startY = (float)mouseY - this.moveY;
            }
            this.previousmouse = true;
        } else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        if (((Boolean)ClickGui.Streamer.getValue()).booleanValue()) {
            this.drawGradientRect(0, 0, width, height, new Color(0, 0, 0, 0).getRGB(), ColorCreator.createRainbowFromOffset(-6000, 5));
        }
        SimpleRender.drawBorderedRect((double)startX + 0.5, (double)startY + 0.5, (double)(startX + 450.0f) - 0.5, (double)(startY + 300.0f) - 0.5, 0.5, new Color(40, 40, 40).getRGB(), new Color(60, 60, 60).getRGB(), true);
        SimpleRender.drawBorderedRect(startX + 2.0f, startY + 2.0f, startX + 450.0f - 2.0f, startY + 300.0f - 2.0f, 0.5, new Color(22, 22, 22).getRGB(), new Color(60, 60, 60).getRGB(), true);
        SimpleRender.drawBorderedRect(startX + 2.0f, startY + 2.0f, startX + 50.0f - 2.0f, startY + 300.0f - 2.0f, 0.5, new Color(15, 15, 15).getRGB(), new Color(60, 60, 60).getRGB(), true);
        RenderUtil.drawGradientSideways(startX + 3.0f, startY + 3.0f, (double)(startX + 450.0f) - 2.5, startY + 4.0f, Clickgui.getAstolfoRainbow((int)(startX * 20.0f)), Clickgui.getAstolfoRainbow((int)(startX * 60.0f)));
        SimpleRender.drawBorderedRect(startX + 190.0f, startY + 20.0f, startX + 440.0f, startY + 280.0f - 2.0f + 11.0f, 0.5, new Color(15, 15, 15).getRGB(), new Color(60, 60, 60).getRGB(), true);
        RenderUtil.drawRect(startX + 195.0f, startY + 20.0f, startX + 222.0f, startY + 25.0f, new Color(15, 15, 15).getRGB());
        efont.drawString("Values", startX + 197.0f, startY + 20.0f, -1);
        Client.instance.FontLoaders.Comfortaa20.drawString("B", startX + 6.0f, startY + 7.0f, Clickgui.HUDColor());
        Client.instance.FontLoaders.Comfortaa20.drawString("luelun", startX + 6.0f + (float)Client.instance.FontLoaders.Comfortaa20.getStringWidth("S"), startY + 7.0f, -1);
        for (int i = 0; i < ModuleType.values().length; ++i) {
            if (ModuleType.values()[i] == currentModuleType) {
                if (ModuleType.values()[i].name().equals("Combat")) {
                    SimpleRender.drawBorderedRect(startX + 2.0f, startY + 20.0f, startX + 50.0f - 2.0f, startY + 65.0f - 2.0f, 0.5, new Color(22, 22, 22).getRGB(), new Color(50, 50, 50).getRGB(), true);
                    Gui.drawRect(startX + 47.0f, (double)(startY + 20.0f + 1.0f) - 0.5, startX + 50.0f, (double)(startY + 65.0f) - 2.5, new Color(22, 22, 22).getRGB());
                }
                if (ModuleType.values()[i].name().equals("Movement")) {
                    SimpleRender.drawBorderedRect(startX + 2.0f, startY + 62.0f, startX + 50.0f - 2.0f, startY + 103.0f, 0.5, new Color(22, 22, 22).getRGB(), new Color(50, 50, 50).getRGB(), true);
                    Gui.drawRect(startX + 47.0f, (double)(startY + 62.0f) + 0.5, startX + 50.0f, (double)(startY + 103.0f) - 0.5, new Color(22, 22, 22).getRGB());
                }
                if (ModuleType.values()[i].name().equals("Render")) {
                    SimpleRender.drawBorderedRect(startX + 2.0f, startY + 103.0f, startX + 50.0f - 2.0f, startY + 143.0f, 0.5, new Color(22, 22, 22).getRGB(), new Color(50, 50, 50).getRGB(), true);
                    Gui.drawRect(startX + 47.0f, (double)(startY + 103.0f) + 0.5, startX + 50.0f, (double)(startY + 143.0f) - 0.5, new Color(22, 22, 22).getRGB());
                }
                if (ModuleType.values()[i].name().equals("Player")) {
                    SimpleRender.drawBorderedRect(startX + 2.0f, startY + 143.0f, startX + 50.0f - 2.0f, startY + 183.0f - 2.0f, 0.5, new Color(22, 22, 22).getRGB(), new Color(50, 50, 50).getRGB(), true);
                    Gui.drawRect(startX + 47.0f, (double)(startY + 143.0f) + 0.5, startX + 50.0f, (double)(startY + 181.0f) - 0.5, new Color(22, 22, 22).getRGB());
                }
                if (ModuleType.values()[i].name().equals("World")) {
                    SimpleRender.drawBorderedRect(startX + 2.0f, startY + 183.0f, startX + 50.0f - 2.0f, startY + 223.0f - 2.0f, 0.5, new Color(22, 22, 22).getRGB(), new Color(50, 50, 50).getRGB(), true);
                    Gui.drawRect(startX + 47.0f, (double)(startY + 183.0f) + 0.5, startX + 50.0f, (double)(startY + 221.0f) - 0.5, new Color(22, 22, 22).getRGB());
                }
                if (ModuleType.values()[i].name().equals("Misc")) {
                    SimpleRender.drawBorderedRect(startX + 2.0f, startY + 223.0f, startX + 50.0f - 2.0f, startY + 263.0f - 2.0f, 0.5, new Color(22, 22, 22).getRGB(), new Color(50, 50, 50).getRGB(), true);
                    Gui.drawRect(startX + 47.0f, (double)(startY + 223.0f) + 0.5, startX + 50.0f, (double)(startY + 261.0f) - 0.5, new Color(22, 22, 22).getRGB());
                }
            }
            if (ModuleType.values()[i].name().equals("Combat")) {
                Client.instance.FontLoaders.CSGO46.drawString("J", startX + 15.0f, startY + 33.0f, ModuleType.values()[i] == currentModuleType ? -1 : new Color(200, 200, 200).getRGB());
            }
            if (ModuleType.values()[i].name().equals("Movement")) {
                Client.instance.FontLoaders.CSGO40.drawString("E", startX + 15.0f, startY + 73.0f, ModuleType.values()[i] == currentModuleType ? -1 : new Color(200, 200, 200).getRGB());
            }
            if (ModuleType.values()[i].name().equals("Render")) {
                Client.instance.FontLoaders.CSGO40.drawString("C", startX + 15.0f, startY + 115.0f, ModuleType.values()[i] == currentModuleType ? -1 : new Color(200, 200, 200).getRGB());
            }
            if (ModuleType.values()[i].name().equals("Player")) {
                Client.instance.FontLoaders.CSGO36.drawString("F", startX + 15.0f, startY + 155.0f, ModuleType.values()[i] == currentModuleType ? -1 : new Color(200, 200, 200).getRGB());
            }
            if (ModuleType.values()[i].name().equals("World")) {
                Client.instance.FontLoaders.CSGO36.drawString("I", startX + 15.0f, startY + 195.0f, ModuleType.values()[i] == currentModuleType ? -1 : new Color(200, 200, 200).getRGB());
            }
            if (ModuleType.values()[i].name().equals("Misc")) {
                Client.instance.FontLoaders.CSGO36.drawString("J", startX + 15.0f, startY + 235.0f, ModuleType.values()[i] == currentModuleType ? -1 : new Color(200, 200, 200).getRGB());
            }
            try {
                if (!Clickgui.isCategoryHovered(startX, startY + 15.0f + (float)(titlefont.getStringHeight() + 3) + (float)(i * 40), startX + 45.0f, startY + 45.0f + (float)(titlefont.getStringHeight() + 3) + (float)(i * 40), mouseX, mouseY) || !Mouse.isButtonDown((int)0)) continue;
                currentModuleType = ModuleType.values()[i];
                currentModule = !Client.instance.getModuleManager().getModulesInType(currentModuleType).isEmpty() ? Client.instance.getModuleManager().getModulesInType(currentModuleType).get(0) : null;
                this.moduleStart = 0;
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        int m = Mouse.getDWheel();
        if (Clickgui.isCategoryHovered(startX + 60.0f, startY + (float)(titlefont.getStringHeight() + 3), startX + 200.0f, startY + (float)(titlefont.getStringHeight() + 3) + 320.0f, mouseX, mouseY)) {
            if (m < 0 && this.moduleStart < Client.instance.getModuleManager().getModulesInType(currentModuleType).size() - 1) {
                ++this.moduleStart;
            }
            if (m > 0 && this.moduleStart > 0) {
                --this.moduleStart;
            }
        }
        List<Value> values1 = currentModule.getValues();
        if (Clickgui.isCategoryHovered(startX + 200.0f, startY + (float)(titlefont.getStringHeight() + 3), startX + 420.0f, startY + (float)(titlefont.getStringHeight() + 3) + 320.0f, mouseX, mouseY)) {
            if (m < 0 && this.valueStart < currentModule.getValues().size() - 1) {
                ++this.valueStart;
            }
            if (m > 0 && this.valueStart > 0) {
                --this.valueStart;
            }
        }
        efont.drawString(currentModule == null ? currentModuleType.toString() : currentModuleType.toString() + "-" + currentModule.getName(), startX + 420.0f - (float)Minecraft.fontRendererObj.getStringWidth(currentModule == null ? currentModuleType.toString() : currentModuleType.toString() + "-" + currentModule.getName()), startY + 10.0f, new Color(248, 248, 248).getRGB());
        if (currentModule != null) {
            int j;
            this.mY = startY + 5.0f;
            for (j = 0; j < Client.instance.getModuleManager().getModulesInType(currentModuleType).size(); ++j) {
                Module module = Client.instance.getModuleManager().getModulesInType(currentModuleType).get(j);
                if (this.mY > startY + 290.0f) break;
                if (j < this.moduleStart) continue;
                SimpleRender.drawBorderedRect(startX + 65.0f, this.mY + 2.0f, startX + 175.0f, this.mY + 20.0f, 0.5, new Color(22, 22, 22).getRGB(), new Color(60, 60, 60).getRGB(), true);
                if (!module.isEnabled()) {
                    SimpleRender.drawBorderedRect(startX + 161.0f, this.mY + 4.0f + 5.0f, startX + 165.0f, this.mY + 13.0f, 0.5, new Color(22, 22, 22).getRGB(), new Color(60, 60, 60).getRGB(), true);
                } else {
                    SimpleRender.drawBorderedRect(startX + 161.0f, this.mY + 4.0f + 5.0f, startX + 165.0f, this.mY + 13.0f, 0.5, Clickgui.HUDColor(), new Color(60, 60, 60).getRGB(), true);
                }
                efont.drawString(module.getName(), startX + 75.0f, this.mY + 9.0f, module.isEnabled() ? Clickgui.HUDColor() : new Color(200, 200, 200).getRGB());
                this.inmodule = module.getName();
                if (Clickgui.isSettingsButtonHovered(startX + 90.0f, this.mY, startX + 100.0f + (float)efont.getStringWidth(module.getName()), this.mY + 8.0f + (float)efont.getHeight(), mouseX, mouseY)) {
                    if (!this.previousmouse && Mouse.isButtonDown((int)0)) {
                        module.setEnabled(!module.isEnabled());
                        this.previousmouse = true;
                    }
                    if (!this.previousmouse && Mouse.isButtonDown((int)1)) {
                        this.previousmouse = true;
                    }
                }
                if (!Mouse.isButtonDown((int)0)) {
                    this.previousmouse = false;
                }
                if (Clickgui.isSettingsButtonHovered(startX + 90.0f, this.mY, startX + 100.0f + (float)efont.getStringWidth(module.getName()), this.mY + 8.0f + (float)efont.getHeight(), mouseX, mouseY) && Mouse.isButtonDown((int)1)) {
                    currentModule = module;
                    this.valueStart = 0;
                }
                this.mY += 22.0f;
            }
            this.mY = startY + 30.0f;
            for (j = 0; j < currentModule.getValues().size() && this.mY <= startY + 277.0f; ++j) {
                if (j < this.valueStart) continue;
                if (this.mY > startY + 277.0f) break;
                Value value = values1.get(j);
                if (((Boolean)ClickGui.Visitable.getValue()).booleanValue() && !value.isVisitable()) continue;
                if (value instanceof Numbers) {
                    this.x = startX + 205.0f;
                    double render = 68.0f * (((Number)value.getValue()).floatValue() - ((Number)((Numbers)value).getMinimum()).floatValue()) / (((Number)((Numbers)value).getMaximum()).floatValue() - ((Number)((Numbers)value).getMinimum()).floatValue());
                    SimpleRender.drawBorderedRect(this.x - 6.0f - 1.0f, this.mY + 2.0f + 5.0f - 1.0f, this.x + 75.0f, this.mY + 5.0f + 5.0f + 1.0f, 0.5, new Color(22, 22, 22).getRGB(), new Color(15, 15, 15).getRGB(), true);
                    RenderUtil.drawRect((double)(this.x - 6.0f), (double)(this.mY + 2.0f + 5.0f), (double)this.x + render + 6.5, (double)(this.mY + 5.0f + 5.0f), Clickgui.HUDColor());
                    efont.drawString(value.getName(), startX + 200.0f, this.mY, -1);
                    efont.drawString(String.valueOf(value.getValue()), startX + 290.0f, this.mY + 6.0f, -1);
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousmouse = false;
                    }
                    if (Clickgui.isButtonHovered(this.x, this.mY + 3.0f, this.x + 100.0f, this.mY + 12.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
                        if (!this.previousmouse && Mouse.isButtonDown((int)0)) {
                            render = ((Number)((Numbers)value).getMinimum()).doubleValue();
                            double max = ((Number)((Numbers)value).getMaximum()).doubleValue();
                            double inc = ((Number)((Numbers)value).getIncrement()).doubleValue();
                            double valAbs = (float)mouseX - (this.x + 1.0f);
                            double perc = valAbs / 68.0;
                            perc = Math.min(Math.max(0.0, perc), 1.0);
                            double valRel = (max - render) * perc;
                            double val = render + valRel;
                            val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                            value.setValue(val);
                        }
                        if (!Mouse.isButtonDown((int)0)) {
                            this.previousmouse = false;
                        }
                    }
                    this.mY += 17.0f;
                }
                if (value instanceof Option) {
                    this.x = startX + 120.0f;
                    efont.drawString(value.getName(), startX + 200.0f, this.mY + 3.0f, -1);
                    if (((Boolean)value.getValue()).booleanValue()) {
                        SimpleRender.drawBorderedRect(this.x + 182.0f, (int)this.mY + 2, this.x + 188.0f, this.mY + 8.0f, 0.5, Clickgui.HUDColor(), new Color(60, 60, 60).getRGB(), true);
                    } else {
                        SimpleRender.drawBorderedRect(this.x + 182.0f, (int)this.mY + 2, this.x + 188.0f, this.mY + 8.0f, 0.5, new Color(22, 22, 22).getRGB(), new Color(60, 60, 60).getRGB(), true);
                    }
                    if (Clickgui.isCheckBoxHovered(this.x + 80.0f, this.mY, this.x + 216.0f, this.mY + 9.0f, mouseX, mouseY)) {
                        if (!this.previousmouse && Mouse.isButtonDown((int)0)) {
                            this.previousmouse = true;
                            this.mouse = true;
                        }
                        if (this.mouse) {
                            value.setValue((Boolean)value.getValue() == false);
                            this.mouse = false;
                        }
                    }
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousmouse = false;
                    }
                    this.mY += 18.0f;
                }
                if (!(value instanceof Mode)) continue;
                this.x = startX + 300.0f;
                efont.drawString(value.getName(), startX + 200.0f, this.mY + 1.0f, -1);
                if (Clickgui.isStringHovered(this.x - 15.0f, this.mY, this.x + 100.0f - 25.0f, this.mY + 15.0f, mouseX, mouseY)) {
                    SimpleRender.drawBorderedRect(this.x - 5.0f, this.mY - 3.0f, this.x + 80.0f, this.mY + 10.0f, 0.5, new Color(40, 40, 40).getRGB(), new Color(60, 60, 60).getRGB(), true);
                } else {
                    SimpleRender.drawBorderedRect(this.x - 5.0f, this.mY - 3.0f, this.x + 80.0f, this.mY + 10.0f, 0.5, new Color(40, 40, 40).getRGB(), new Color(0, 0, 0).getRGB(), true);
                }
                efont.drawString(((Mode)value).getModeAsString(), this.x, this.mY + 1.0f, -1);
                this.nowinmode = this.inmodule + value.getName();
                if (Clickgui.isStringHovered(this.x - 15.0f, this.mY, this.x + 75.0f, this.mY + 15.0f, mouseX, mouseY)) {
                    if (Mouse.isButtonDown((int)0) && !this.previousmouse) {
                        if (this.inmode.equals(this.inmodule + value.getName())) {
                            this.inmode = "heshang";
                        } else {
                            this.a = true;
                            this.inmode = this.nowinmode;
                        }
                        this.previousmouse = true;
                    }
                    if (Mouse.isButtonDown((int)0) && !this.previousmouse) {
                        this.previousmouse = true;
                    }
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousmouse = false;
                    }
                }
                if (this.a) {
                    if (this.inmode.equals(this.inmodule + value.getName())) {
                        Minecraft.fontRendererObj.drawString(">", (int)this.x + 70, (int)this.mY - 1, -1);
                        SimpleRender.drawBorderedRect((int)this.x + 81, (int)this.mY - 3, (int)this.x + 70 + 60, (int)this.mY - 1 + 10 * ((Mode)value).getModes().length, 0.5, new Color(22, 22, 22).getRGB(), new Color(60, 60, 60).getRGB(), true);
                        int next = ((Mode)value).getModes().length - 1;
                        if (next == 0) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                        }
                        if (next == 1) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                        }
                        if (next == 2) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                        }
                        if (next == 3) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                        }
                        if (next == 4) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                        }
                        if (next == 5) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                        }
                        if (next == 6) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                        }
                        if (next == 7) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                        }
                        if (next == 8) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                        }
                        if (next == 9) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                        }
                        if (next == 10) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                        }
                        if (next == 11) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[11], startX + 384.0f, this.mY + 1.0f + 110.0f, Clickgui.ModeColor());
                        }
                        if (next == 12) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[11], startX + 384.0f, this.mY + 1.0f + 110.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[12], startX + 384.0f, this.mY + 1.0f + 120.0f, Clickgui.ModeColor());
                        }
                        if (next == 13) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[11], startX + 384.0f, this.mY + 1.0f + 110.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[11], startX + 384.0f, this.mY + 1.0f + 110.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[12], startX + 384.0f, this.mY + 1.0f + 120.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[13], startX + 384.0f, this.mY + 1.0f + 130.0f, Clickgui.ModeColor());
                        }
                        if (next == 14) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[11], startX + 384.0f, this.mY + 1.0f + 110.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[12], startX + 384.0f, this.mY + 1.0f + 120.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[13], startX + 384.0f, this.mY + 1.0f + 130.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[14], startX + 384.0f, this.mY + 1.0f + 140.0f, Clickgui.ModeColor());
                        }
                        if (next == 15) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[11], startX + 384.0f, this.mY + 1.0f + 110.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[12], startX + 384.0f, this.mY + 1.0f + 120.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[13], startX + 384.0f, this.mY + 1.0f + 130.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[14], startX + 384.0f, this.mY + 1.0f + 140.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[15], startX + 384.0f, this.mY + 1.0f + 150.0f, Clickgui.ModeColor());
                        }
                        if (next == 16) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[11], startX + 384.0f, this.mY + 1.0f + 110.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[12], startX + 384.0f, this.mY + 1.0f + 120.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[13], startX + 384.0f, this.mY + 1.0f + 130.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[14], startX + 384.0f, this.mY + 1.0f + 140.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[15], startX + 384.0f, this.mY + 1.0f + 150.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[16], startX + 384.0f, this.mY + 1.0f + 160.0f, Clickgui.ModeColor());
                        }
                        if (next == 17) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[11], startX + 384.0f, this.mY + 1.0f + 110.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[12], startX + 384.0f, this.mY + 1.0f + 120.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[13], startX + 384.0f, this.mY + 1.0f + 130.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[14], startX + 384.0f, this.mY + 1.0f + 140.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[15], startX + 384.0f, this.mY + 1.0f + 150.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[16], startX + 384.0f, this.mY + 1.0f + 160.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[17], startX + 384.0f, this.mY + 1.0f + 170.0f, Clickgui.ModeColor());
                        }
                        if (next == 18) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[11], startX + 384.0f, this.mY + 1.0f + 110.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[12], startX + 384.0f, this.mY + 1.0f + 120.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[13], startX + 384.0f, this.mY + 1.0f + 130.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[14], startX + 384.0f, this.mY + 1.0f + 140.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[15], startX + 384.0f, this.mY + 1.0f + 150.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[16], startX + 384.0f, this.mY + 1.0f + 160.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[17], startX + 384.0f, this.mY + 1.0f + 170.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[18], startX + 384.0f, this.mY + 1.0f + 180.0f, Clickgui.ModeColor());
                        }
                        if (next == 19) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[11], startX + 384.0f, this.mY + 1.0f + 110.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[12], startX + 384.0f, this.mY + 1.0f + 120.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[13], startX + 384.0f, this.mY + 1.0f + 130.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[14], startX + 384.0f, this.mY + 1.0f + 140.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[15], startX + 384.0f, this.mY + 1.0f + 150.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[16], startX + 384.0f, this.mY + 1.0f + 160.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[17], startX + 384.0f, this.mY + 1.0f + 170.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[18], startX + 384.0f, this.mY + 1.0f + 180.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[19], startX + 384.0f, this.mY + 1.0f + 190.0f, Clickgui.ModeColor());
                        }
                        if (next == 20) {
                            efont.drawStringWithShadow(((Mode)value).getModes()[0], startX + 384.0f, this.mY + 1.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[1], startX + 384.0f, this.mY + 1.0f + 10.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[2], startX + 384.0f, this.mY + 1.0f + 20.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[3], startX + 384.0f, this.mY + 1.0f + 30.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[4], startX + 384.0f, this.mY + 1.0f + 40.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[5], startX + 384.0f, this.mY + 1.0f + 50.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[6], startX + 384.0f, this.mY + 1.0f + 60.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[7], startX + 384.0f, this.mY + 1.0f + 70.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[8], startX + 384.0f, this.mY + 1.0f + 80.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[9], startX + 384.0f, this.mY + 1.0f + 90.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[10], startX + 384.0f, this.mY + 1.0f + 100.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[11], startX + 384.0f, this.mY + 1.0f + 110.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[12], startX + 384.0f, this.mY + 1.0f + 120.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[13], startX + 384.0f, this.mY + 1.0f + 130.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[14], startX + 384.0f, this.mY + 1.0f + 140.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[15], startX + 384.0f, this.mY + 1.0f + 150.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[16], startX + 384.0f, this.mY + 1.0f + 160.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[17], startX + 384.0f, this.mY + 1.0f + 170.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[18], startX + 384.0f, this.mY + 1.0f + 180.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[19], startX + 384.0f, this.mY + 1.0f + 190.0f, Clickgui.ModeColor());
                            efont.drawStringWithShadow(((Mode)value).getModes()[20], startX + 384.0f, this.mY + 1.0f + 200.0f, Clickgui.ModeColor());
                        }
                        if (Mouse.isButtonDown((int)0)) {
                            if (next >= 0 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY, (int)this.x + 70 + 60, (int)this.mY + 5, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[0]);
                                this.a = false;
                            }
                            if (next >= 1 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 10, (int)this.x + 70 + 60, (int)this.mY + 5 + 10, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[1]);
                                this.a = false;
                            }
                            if (next >= 2 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 20, (int)this.x + 70 + 60, (int)this.mY + 5 + 20, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[2]);
                                this.a = false;
                            }
                            if (next >= 3 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 30, (int)this.x + 70 + 60, (int)this.mY + 5 + 30, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[3]);
                                this.a = false;
                            }
                            if (next >= 4 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 40, (int)this.x + 70 + 60, (int)this.mY + 5 + 40, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[4]);
                                this.a = false;
                            }
                            if (next >= 5 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 50, (int)this.x + 70 + 60, (int)this.mY + 5 + 50, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[5]);
                                this.a = false;
                            }
                            if (next >= 6 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 60, (int)this.x + 70 + 60, (int)this.mY + 5 + 60, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[6]);
                                this.a = false;
                            }
                            if (next >= 7 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 70, (int)this.x + 70 + 60, (int)this.mY + 5 + 70, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[7]);
                                this.a = false;
                            }
                            if (next >= 8 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 80, (int)this.x + 70 + 60, (int)this.mY + 5 + 80, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[8]);
                                this.a = false;
                            }
                            if (next >= 9 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 90, (int)this.x + 70 + 60, (int)this.mY + 5 + 90, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[9]);
                                this.a = false;
                            }
                            if (next >= 10 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 100, (int)this.x + 70 + 60, (int)this.mY + 5 + 100, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[10]);
                                this.a = false;
                            }
                            if (next >= 11 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 110, (int)this.x + 70 + 60, (int)this.mY + 5 + 110, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[11]);
                                this.a = false;
                            }
                            if (next >= 12 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 120, (int)this.x + 70 + 60, (int)this.mY + 5 + 120, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[12]);
                                this.a = false;
                            }
                            if (next >= 13 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 130, (int)this.x + 70 + 60, (int)this.mY + 5 + 130, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[13]);
                                this.a = false;
                            }
                            if (next >= 14 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 140, (int)this.x + 70 + 60, (int)this.mY + 5 + 140, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[14]);
                                this.a = false;
                            }
                            if (next >= 15 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 150, (int)this.x + 70 + 60, (int)this.mY + 5 + 150, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[15]);
                                this.a = false;
                            }
                            if (next >= 16 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 160, (int)this.x + 70 + 60, (int)this.mY + 5 + 160, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[16]);
                                this.a = false;
                            }
                            if (next >= 17 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 170, (int)this.x + 70 + 60, (int)this.mY + 5 + 170, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[17]);
                                this.a = false;
                            }
                            if (next >= 18 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 180, (int)this.x + 70 + 60, (int)this.mY + 5 + 180, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[18]);
                                this.a = false;
                            }
                            if (next >= 19 && Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 190, (int)this.x + 70 + 60, (int)this.mY + 5 + 190, mouseX, mouseY)) {
                                value.setValue(((Mode)value).getModes()[19]);
                                this.a = false;
                            }
                            if (next >= 20) {
                                if (Clickgui.isStringHovered((int)this.x + 81, (int)this.mY + 200, (int)this.x + 70 + 60, (int)this.mY + 5 + 200, mouseX, mouseY)) {
                                    value.setValue(((Mode)value).getModes()[20]);
                                    this.a = false;
                                }
                                this.previousmouse = true;
                            }
                        }
                    } else {
                        Minecraft.fontRendererObj.drawString("V", (int)this.x + 70, (int)this.mY - 1, -1);
                    }
                } else {
                    this.inmode = "START";
                }
                this.mY += 17.0f;
            }
        }
    }

    private static int ModeColor() {
        return new Color(225, 225, 225).getRGB();
    }

    private static int HUDColor() {
        return new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public static boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public static boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public static boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public static boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public static boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseX <= x2 && (float)mouseY >= y && (float)mouseY <= y2;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    static {
        titlefont = Client.instance.FontLoaders.Comfortaa20;
        efont = Client.instance.FontLoaders.Comfortaa16;
        currentModuleType = ModuleType.Combat;
        currentModule = !Client.instance.getModuleManager().getModulesInType(currentModuleType).isEmpty() ? Client.instance.getModuleManager().getModulesInType(currentModuleType).get(0) : null;
        startX = 100.0f;
        startY = 100.0f;
    }
}

