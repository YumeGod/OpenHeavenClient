/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
package heaven.main.ui.gui.clickgui;

import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.clickgui.Opacity;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class CSGOClickGui
extends GuiScreen {
    private static ModuleType currentModuleType = ModuleType.Combat;
    private static Module currentModule = !Client.instance.getModuleManager().getModulesInType(currentModuleType).isEmpty() ? Client.instance.getModuleManager().getModulesInType(currentModuleType).get(0) : null;
    private static float startX = 100.0f;
    private static float startY = 100.0f;
    public int moduleStart;
    public int valueStart;
    boolean previousmouse = true;
    boolean mouse;
    public final Opacity opacity = new Opacity(0);
    public static final int opacityx = 255;
    public float moveX;
    public float moveY;
    boolean a = false;
    String inmode = null;
    String nowinmode = null;
    final String inmodule;
    int i1;
    float x;

    public CSGOClickGui() {
        this.inmodule = null;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int i;
        Gui.drawRect(0.0, 0.0, (double)Display.getWidth(), (double)Display.getHeight(), new Color(25, 25, 25, (int)this.opacity.shadowAnim()).getRGB());
        if (CSGOClickGui.isHovered(startX, startY - 25.0f, startX + 400.0f, startY + 25.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
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
        this.opacity.interpolate(255.0f);
        int morex = 65;
        Gui.drawRect(startX + 50.0f, startY - 5.0f, startX + 440.0f + (float)morex, startY - 35.0f, new Color(18, 17, 17, (int)this.opacity.getOpacity()).getRGB());
        Gui.drawRect(startX + 50.0f, startY - 5.0f, startX + 440.0f + (float)morex, startY + 220.0f + 20.0f, new Color(22, 22, 22, (int)this.opacity.getOpacity()).getRGB());
        Gui.drawRect(startX + 52.0f, (double)startY - 31.5, (double)(startX + 438.0f) + (double)morex, startY - 32.0f, new Color(255, 15, 15, (int)this.opacity.getOpacity()).getRGB());
        Gui.drawRect(startX + 64.0f, startY, startX + 145.0f + (float)morex, startY + 210.0f + 10.0f, new Color(0, 0, 0, (int)this.opacity.getOpacity()).getRGB());
        Gui.drawRect(startX + 150.0f, startY, startX + 420.0f + (float)morex, startY + 210.0f + 10.0f, new Color(0, 0, 0, (int)this.opacity.getOpacity()).getRGB());
        for (i = 0; i < ModuleType.values().length; ++i) {
            ModuleType[] iterator = ModuleType.values();
            int mousedown = 20;
            if (iterator[i] != currentModuleType) {
                if (iterator[i].toString().equals("Combat")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("Combot", startX + 75.0f, startY - 22.0f, new Color(100, 100, 100).getRGB());
                }
                if (iterator[i].toString().equals("Movement")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("Movement", startX + 145.0f, startY - 22.0f, new Color(100, 100, 100).getRGB());
                }
                if (iterator[i].toString().equals("Render")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("Render", startX + 215.0f + 20.0f, startY - 22.0f, new Color(100, 100, 100).getRGB());
                }
                if (iterator[i].toString().equals("Player")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("Player", startX + 285.0f + 20.0f, startY - 22.0f, new Color(100, 100, 100).getRGB());
                }
                if (iterator[i].toString().equals("World")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("World", startX + 355.0f + 20.0f, startY - 22.0f, new Color(100, 100, 100).getRGB());
                }
                if (iterator[i].toString().equals("Misc")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("Misc", startX + 425.0f + 20.0f, startY - 22.0f, new Color(100, 100, 100).getRGB());
                }
            } else {
                if (iterator[i].toString().equals("Combat")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("Combot", startX + 75.0f, startY - 22.0f, new Color(235, 235, 235).getRGB());
                }
                if (iterator[i].toString().equals("Movement")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("Movement", startX + 145.0f, startY - 22.0f, new Color(235, 235, 235).getRGB());
                }
                if (iterator[i].toString().equals("Render")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("Render", startX + 215.0f + 20.0f, startY - 22.0f, new Color(235, 235, 235).getRGB());
                }
                if (iterator[i].toString().equals("Player")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("Player", startX + 285.0f + 20.0f, startY - 22.0f, new Color(235, 235, 235).getRGB());
                }
                if (iterator[i].toString().equals("World")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("World", startX + 355.0f + 20.0f, startY - 22.0f, new Color(235, 235, 235).getRGB());
                }
                if (iterator[i].toString().equals("Misc")) {
                    Client.instance.FontLoaders.Comfortaa22.drawString("Misc", startX + 425.0f + 20.0f, startY - 22.0f, new Color(235, 235, 235).getRGB());
                }
            }
            try {
                if (!CSGOClickGui.isCategoryHovered(startX + 75.0f + (float)(i * 70), startY - 30.0f, startX + 105.0f + 20.0f + (float)(i * 70), startY - 15.0f, mouseX, mouseY) || !Mouse.isButtonDown((int)0)) continue;
                currentModuleType = iterator[i];
                currentModule = !Client.instance.getModuleManager().getModulesInType(currentModuleType).isEmpty() ? Client.instance.getModuleManager().getModulesInType(currentModuleType).get(0) : null;
                this.moduleStart = 0;
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        i = Mouse.getDWheel();
        if (CSGOClickGui.isCategoryHovered(startX + 60.0f, startY, startX + 200.0f, startY + 260.0f, mouseX, mouseY)) {
            if (i < 0 && this.moduleStart < Client.instance.getModuleManager().getModulesInType(currentModuleType).size() - 1) {
                ++this.moduleStart;
            }
            if (i > 0 && this.moduleStart > 0) {
                --this.moduleStart;
            }
        }
        if (CSGOClickGui.isCategoryHovered(startX + 200.0f, startY, startX + 420.0f, startY + 260.0f, mouseX, mouseY)) {
            if (i < 0 && this.valueStart < currentModule.getValues().size() - 1) {
                ++this.valueStart;
            }
            if (i > 0 && this.valueStart > 0) {
                --this.valueStart;
            }
        }
        Client.instance.FontLoaders.Comfortaa15.drawString(currentModule == null ? currentModuleType.toString() : currentModule.getName() + "_Settings", startX + 150.0f, startY + 9.0f, new Color(248, 248, 248).getRGB());
        if (currentModule != null) {
            float var241 = startY + 22.0f;
            for (i = 0; i < Client.instance.getModuleManager().getModulesInType(currentModuleType).size(); ++i) {
                Module value = Client.instance.getModuleManager().getModulesInType(currentModuleType).get(i);
                if (var241 > startY + 210.0f) break;
                if (i < this.moduleStart) continue;
                if (!value.isEnabled()) {
                    Gui.drawRect(startX + 128.0f + 20.0f, startY + 8.0f + 212.0f, startX + 140.0f + 5.0f, var241 + 12.0f - 4.0f - 30.0f, new Color(22, 22, 22, (int)this.opacity.getOpacity()).getRGB());
                    Client.instance.FontLoaders.Comfortaa18.drawString(value.getName(), (double)(startX + 70.0f), (double)var241 + 8.0, new Color(100, 100, 100, (int)this.opacity.getOpacity()).getRGB());
                } else {
                    Gui.drawRect(startX + 128.0f + 20.0f, startY + 8.0f + 212.0f, startX + 140.0f + 5.0f, var241 + 12.0f - 4.0f - 30.0f, new Color(22, 22, 22, (int)this.opacity.getOpacity()).getRGB());
                    Client.instance.FontLoaders.Comfortaa22.drawString("|", startX + 65.0f, var241 + 2.0f + 4.0f, new Color(225, 15, 15, (int)this.opacity.getOpacity()).getRGB());
                    Client.instance.FontLoaders.Comfortaa18.drawString(value.getName(), startX + 70.0f, var241 + 8.0f, new Color(248, 248, 248, (int)this.opacity.getOpacity()).getRGB());
                }
                if (CSGOClickGui.isSettingsButtonHovered(startX + 70.0f, var241 - 10.0f, startX + 100.0f + (float)Client.instance.FontLoaders.Comfortaa20.getStringWidth(value.getName()), var241 + 8.0f + 10.0f, mouseX, mouseY)) {
                    if (!this.previousmouse && Mouse.isButtonDown((int)0)) {
                        value.setEnabled(!value.isEnabled());
                        this.previousmouse = true;
                    }
                    if (!this.previousmouse && Mouse.isButtonDown((int)1)) {
                        this.previousmouse = true;
                    }
                }
                if (!Mouse.isButtonDown((int)0)) {
                    this.previousmouse = false;
                }
                if (CSGOClickGui.isSettingsButtonHovered(startX + 65.0f, var241, startX + 100.0f + (float)Client.instance.FontLoaders.Comfortaa20.getStringWidth(value.getName()), var241 + 8.0f + 0.0f, mouseX, mouseY) && Mouse.isButtonDown((int)1)) {
                    currentModule = value;
                    this.valueStart = 0;
                }
                var241 += 25.0f;
            }
            var241 = startY + 23.0f;
            for (i = 0; i < currentModule.getValues().size() && var241 <= startY + 200.0f; ++i) {
                if (i < this.valueStart) continue;
                Value var25 = currentModule.getValues().get(i);
                if (var25 instanceof Numbers) {
                    this.x = startX + 300.0f;
                    double current = 68.0f * (((Number)var25.getValue()).floatValue() - ((Number)((Numbers)var25).getMinimum()).floatValue()) / (((Number)((Numbers)var25).getMaximum()).floatValue() - ((Number)((Numbers)var25).getMinimum()).floatValue());
                    Gui.drawRect(this.x - 6.0f, var241 + 2.0f, (float)((double)this.x + 75.0), var241 + 3.0f, new Color(50, 50, 50, (int)this.opacity.getOpacity()).getRGB());
                    Gui.drawRect(this.x - 6.0f, var241 + 2.0f, (float)((double)this.x + current + 6.5), var241 + 3.0f, new Color(225, 15, 15, (int)this.opacity.getOpacity()).getRGB());
                    RenderUtil.drawRoundRect((float)((double)this.x + current + 2.0), var241, (float)((double)this.x + current + 7.0), var241 + 5.0f, new Color(225, 15, 15, (int)this.opacity.getOpacity()).getRGB());
                    Client.instance.FontLoaders.Comfortaa18.drawString(var25.getName() + ": " + var25.getValue(), (double)(startX + 160.0f), (double)var241, -1);
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousmouse = false;
                    }
                    if (CSGOClickGui.isButtonHovered(this.x, var241 - 2.0f, this.x + 100.0f, var241 + 7.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
                        if (!this.previousmouse && Mouse.isButtonDown((int)0)) {
                            current = ((Number)((Numbers)var25).getMinimum()).doubleValue();
                            double max = ((Number)((Numbers)var25).getMaximum()).doubleValue();
                            double inc = ((Number)((Numbers)var25).getIncrement()).doubleValue();
                            double valAbs = (double)mouseX - ((double)this.x + 1.0);
                            double perc = valAbs / 68.0;
                            perc = Math.min(Math.max(0.0, perc), 1.0);
                            double valRel = (max - current) * perc;
                            double val = current + valRel;
                            val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                            var25.setValue(val);
                        }
                        if (!Mouse.isButtonDown((int)0)) {
                            this.previousmouse = false;
                        }
                    }
                    var241 += 17.0f;
                }
                if (var25 instanceof Option) {
                    this.x = startX + 237.0f;
                    Client.instance.FontLoaders.Comfortaa18.drawString(var25.getName(), (double)(startX + 160.0f), (double)var241, -1);
                    Gui.drawRect(this.x + 56.0f, var241, this.x + 76.0f, var241 + 1.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    Gui.drawRect(this.x + 56.0f, var241 + 8.0f, this.x + 76.0f, var241 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    Gui.drawRect(this.x + 56.0f, var241, this.x + 57.0f, var241 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    Gui.drawRect(this.x + 77.0f, var241, this.x + 76.0f, var241 + 9.0f, new Color(255, 255, 255, (int)this.opacity.getOpacity()).getRGB());
                    if (((Boolean)var25.getValue()).booleanValue()) {
                        Gui.drawRect(this.x + 67.0f, var241 + 2.0f, this.x + 75.0f, var241 + 7.0f, new Color(225, 15, 15, (int)this.opacity.getOpacity()).getRGB());
                    } else {
                        Gui.drawRect(this.x + 58.0f, var241 + 2.0f, this.x + 65.0f, var241 + 7.0f, new Color(150, 150, 150, (int)this.opacity.getOpacity()).getRGB());
                    }
                    if (CSGOClickGui.isCheckBoxHovered(this.x + 56.0f, var241, this.x + 76.0f, var241 + 9.0f, mouseX, mouseY)) {
                        if (!this.previousmouse && Mouse.isButtonDown((int)0)) {
                            this.previousmouse = true;
                            this.mouse = true;
                        }
                        if (this.mouse) {
                            var25.setValue((Boolean)var25.getValue() == false);
                            this.mouse = false;
                        }
                    }
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousmouse = false;
                    }
                    var241 += 17.0f;
                }
                if (!(var25 instanceof Mode)) continue;
                this.x = startX + 300.0f;
                Client.instance.FontLoaders.Comfortaa18.drawString(var25.getName(), (double)(startX + 160.0f), (double)var241, -1);
                Gui.drawRect(this.x + 7.0f, var241 - 5.0f, this.x + 75.0f, var241 + 10.0f, new Color(225, 15, 15, (int)this.opacity.getOpacity()).getRGB());
                Client.instance.FontLoaders.Comfortaa18.drawString(((Mode)var25).getModeAsString(), (double)(this.x + 40.0f - (float)(Client.instance.FontLoaders.Comfortaa18.getStringWidth(((Mode)var25).getModeAsString()) / 2)), (double)var241, -1);
                this.nowinmode = this.inmodule + var25.getName();
                if (CSGOClickGui.isStringHovered(this.x, var241 - 5.0f, this.x + 100.0f, var241 + 15.0f, mouseX, mouseY)) {
                    if (Mouse.isButtonDown((int)0) && !this.previousmouse) {
                        if (this.inmode.equals(this.inmodule + var25.getName())) {
                            this.inmode = "heshang";
                        } else {
                            this.a = true;
                            this.inmode = this.nowinmode;
                        }
                        this.previousmouse = true;
                    }
                    if (Mouse.isButtonDown((int)0) && !this.previousmouse) {
                        this.i1 = 0;
                        this.previousmouse = true;
                    }
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousmouse = false;
                    }
                }
                if (this.a) {
                    if (this.inmode.equals(this.inmodule + var25.getName())) {
                        Client.instance.FontLoaders.Comfortaa28.drawString(">", (int)this.x + 65, (int)var241 - 1 - 1, -1);
                        RenderUtil.drawBorderedRect((int)this.x + 81, (int)var241 - 3, (int)this.x + 130, (int)var241 - 1 + 10 * ((Mode)var25).getModes().length, 0.5, new Color(22, 22, 22).getRGB(), new Color(225, 15, 15).getRGB(), true);
                        int next = ((Mode)var25).getModes().length - 1;
                        if (next == 0) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (next == 1) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[1], startX + 384.0f, var241 + 1.0f + 10.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (next == 2) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[1], startX + 384.0f, var241 + 1.0f + 10.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[2], startX + 384.0f, var241 + 1.0f + 20.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (next == 3) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[1], startX + 384.0f, var241 + 1.0f + 10.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[2], startX + 384.0f, var241 + 1.0f + 20.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[3], startX + 384.0f, var241 + 1.0f + 30.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (next == 4) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[1], startX + 384.0f, var241 + 1.0f + 10.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[2], startX + 384.0f, var241 + 1.0f + 20.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[3], startX + 384.0f, var241 + 1.0f + 30.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[4], startX + 384.0f, var241 + 1.0f + 40.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (next == 5) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[1], startX + 384.0f, var241 + 1.0f + 10.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[2], startX + 384.0f, var241 + 1.0f + 20.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[3], startX + 384.0f, var241 + 1.0f + 30.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[4], startX + 384.0f, var241 + 1.0f + 40.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[5], startX + 384.0f, var241 + 1.0f + 50.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (next == 6) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[1], startX + 384.0f, var241 + 1.0f + 10.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[2], startX + 384.0f, var241 + 1.0f + 20.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[3], startX + 384.0f, var241 + 1.0f + 30.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[4], startX + 384.0f, var241 + 1.0f + 40.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[5], startX + 384.0f, var241 + 1.0f + 50.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[6], startX + 384.0f, var241 + 1.0f + 60.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (next == 7) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[1], startX + 384.0f, var241 + 1.0f + 10.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[2], startX + 384.0f, var241 + 1.0f + 20.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[3], startX + 384.0f, var241 + 1.0f + 30.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[4], startX + 384.0f, var241 + 1.0f + 40.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[5], startX + 384.0f, var241 + 1.0f + 50.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[6], startX + 384.0f, var241 + 1.0f + 60.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[7], startX + 384.0f, var241 + 1.0f + 70.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (next == 8) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[1], startX + 384.0f, var241 + 1.0f + 10.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[2], startX + 384.0f, var241 + 1.0f + 20.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[3], startX + 384.0f, var241 + 1.0f + 30.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[4], startX + 384.0f, var241 + 1.0f + 40.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[5], startX + 384.0f, var241 + 1.0f + 50.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[6], startX + 384.0f, var241 + 1.0f + 60.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[7], startX + 384.0f, var241 + 1.0f + 70.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[8], startX + 384.0f, var241 + 1.0f + 80.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (next == 9) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[1], startX + 384.0f, var241 + 1.0f + 10.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[2], startX + 384.0f, var241 + 1.0f + 20.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[3], startX + 384.0f, var241 + 1.0f + 30.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[4], startX + 384.0f, var241 + 1.0f + 40.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[5], startX + 384.0f, var241 + 1.0f + 50.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[6], startX + 384.0f, var241 + 1.0f + 60.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[7], startX + 384.0f, var241 + 1.0f + 70.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[8], startX + 384.0f, var241 + 1.0f + 80.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[9], startX + 384.0f, var241 + 1.0f + 90.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (next == 10) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[1], startX + 384.0f, var241 + 1.0f + 10.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[2], startX + 384.0f, var241 + 1.0f + 20.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[3], startX + 384.0f, var241 + 1.0f + 30.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[4], startX + 384.0f, var241 + 1.0f + 40.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[5], startX + 384.0f, var241 + 1.0f + 50.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[6], startX + 384.0f, var241 + 1.0f + 60.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[7], startX + 384.0f, var241 + 1.0f + 70.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[8], startX + 384.0f, var241 + 1.0f + 80.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[9], startX + 384.0f, var241 + 1.0f + 90.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[10], startX + 384.0f, var241 + 1.0f + 100.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (next == 11) {
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[0], startX + 384.0f, var241 + 1.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[1], startX + 384.0f, var241 + 1.0f + 10.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[2], startX + 384.0f, var241 + 1.0f + 20.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[3], startX + 384.0f, var241 + 1.0f + 30.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[4], startX + 384.0f, var241 + 1.0f + 40.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[5], startX + 384.0f, var241 + 1.0f + 50.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[6], startX + 384.0f, var241 + 1.0f + 60.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[7], startX + 384.0f, var241 + 1.0f + 70.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[8], startX + 384.0f, var241 + 1.0f + 80.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[9], startX + 384.0f, var241 + 1.0f + 90.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[10], startX + 384.0f, var241 + 1.0f + 100.0f, new Color(225, 225, 225).getRGB());
                            Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(((Mode)var25).getModes()[11], startX + 384.0f, var241 + 1.0f + 110.0f, new Color(225, 225, 225).getRGB());
                        }
                        if (Mouse.isButtonDown((int)0)) {
                            if (next >= 0 && CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241, (int)this.x + 70 + 60, (int)var241 + 5, mouseX, mouseY)) {
                                var25.setValue(((Mode)var25).getModes()[0]);
                                this.a = false;
                            }
                            if (next >= 1 && CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241 + 10, (int)this.x + 70 + 60, (int)var241 + 5 + 10, mouseX, mouseY)) {
                                var25.setValue(((Mode)var25).getModes()[1]);
                                this.a = false;
                            }
                            if (next >= 2 && CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241 + 20, (int)this.x + 70 + 60, (int)var241 + 5 + 20, mouseX, mouseY)) {
                                var25.setValue(((Mode)var25).getModes()[2]);
                                this.a = false;
                            }
                            if (next >= 3 && CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241 + 30, (int)this.x + 70 + 60, (int)var241 + 5 + 30, mouseX, mouseY)) {
                                var25.setValue(((Mode)var25).getModes()[3]);
                                this.a = false;
                            }
                            if (next >= 4 && CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241 + 40, (int)this.x + 70 + 60, (int)var241 + 5 + 40, mouseX, mouseY)) {
                                var25.setValue(((Mode)var25).getModes()[4]);
                                this.a = false;
                            }
                            if (next >= 5 && CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241 + 50, (int)this.x + 70 + 60, (int)var241 + 5 + 50, mouseX, mouseY)) {
                                var25.setValue(((Mode)var25).getModes()[5]);
                                this.a = false;
                            }
                            if (next >= 6 && CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241 + 60, (int)this.x + 70 + 60, (int)var241 + 5 + 60, mouseX, mouseY)) {
                                var25.setValue(((Mode)var25).getModes()[6]);
                                this.a = false;
                            }
                            if (next >= 7 && CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241 + 70, (int)this.x + 70 + 60, (int)var241 + 5 + 70, mouseX, mouseY)) {
                                var25.setValue(((Mode)var25).getModes()[7]);
                                this.a = false;
                            }
                            if (next >= 8 && CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241 + 80, (int)this.x + 70 + 60, (int)var241 + 5 + 80, mouseX, mouseY)) {
                                var25.setValue(((Mode)var25).getModes()[8]);
                                this.a = false;
                            }
                            if (next >= 9 && CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241 + 90, (int)this.x + 70 + 60, (int)var241 + 5 + 90, mouseX, mouseY)) {
                                var25.setValue(((Mode)var25).getModes()[9]);
                                this.a = false;
                            }
                            if (next >= 10 && CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241 + 100, (int)this.x + 70 + 60, (int)var241 + 5 + 100, mouseX, mouseY)) {
                                var25.setValue(((Mode)var25).getModes()[10]);
                                this.a = false;
                            }
                            if (next >= 11) {
                                if (CSGOClickGui.isStringHovered((int)this.x + 81, (int)var241 + 110, (int)this.x + 70 + 60, (int)var241 + 5 + 110, mouseX, mouseY)) {
                                    var25.setValue(((Mode)var25).getModes()[11]);
                                    this.a = false;
                                }
                                this.previousmouse = true;
                            }
                        }
                    } else {
                        Client.instance.FontLoaders.Comfortaa20.drawString("V", (int)this.x + 65, (int)var241 - 1, -1);
                    }
                } else {
                    this.inmode = "START";
                }
                var241 += 23.0f;
            }
        }
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
    public void onGuiClosed() {
        this.opacity.setOpacity(0.0f);
    }
}

