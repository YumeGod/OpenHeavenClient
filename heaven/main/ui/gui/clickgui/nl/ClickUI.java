/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.clickgui.nl;

import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.ClickGui;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.utils.TranslateUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ClickUI
extends GuiScreen {
    private static ModuleType currentCategory = ModuleType.Combat;
    private static float startX = ClickGui.startX;
    private static float startY = ClickGui.startY;
    private float moveX;
    private float moveY;
    private boolean previousMouse = true;
    private float currentCateRectY = 22.0f;
    private float endCateY;
    private final TranslateUtil translate = new TranslateUtil(0.0f, 0.0f);
    private static int wheel = ClickGui.tempWheel;
    boolean useLeft;

    @Override
    public void initGui() {
        super.initGui();
        currentCategory = ClickGui.currentModuleType;
        this.translate.setX(0.0f);
        this.translate.setY(0.0f);
    }

    public float processFPS(float fps, float defF, float defV) {
        return defV / (fps / defF);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float rightY;
        int FPS;
        int n;
        ClickUI clickUI = this;
        if (clickUI.mc.getDebugFPS() == 0) {
            n = 1;
        } else {
            ClickUI clickUI2 = this;
            n = FPS = clickUI2.mc.getDebugFPS();
        }
        if (RenderUtil.isHovered(startX, startY, startX + 520.0f, startY + 50.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
            if (this.moveX == 0.0f && this.moveY == 0.0f) {
                this.moveX = (float)mouseX - startX;
                this.moveY = (float)mouseY - startY;
            } else {
                startX = (float)mouseX - this.moveX;
                startY = (float)mouseY - this.moveY;
            }
            this.previousMouse = true;
        } else if (this.moveX != 0.0f || this.moveY != 0.0f) {
            this.moveX = 0.0f;
            this.moveY = 0.0f;
        }
        double sizes = 1.0;
        GL11.glScaled((double)sizes, (double)sizes, (double)sizes);
        RenderUtil.drawRect(startX + 10.0f, startY, startX + 520.0f, startY + 460.0f - 180.0f + 100.0f, new Color(0, 0, 0, 200).getRGB());
        RenderUtil.drawRect(startX + 10.0f, startY, startX + 520.0f, startY + 460.0f - 180.0f + 100.0f, new Color(1, 20, 40, 180).getRGB());
        RenderUtil.drawRect(startX + 120.0f, startY + 40.0f, startX + 520.0f, startY + 460.0f - 180.0f + 100.0f, new Color(8, 8, 13).getRGB());
        RenderUtil.drawRect(startX + 120.0f, startY, startX + 520.0f, startY + 38.0f, new Color(8, 8, 13).getRGB());
        RenderUtil.drawRect(startX + 120.0f, startY + 38.0f, startX + 520.0f, startY + 40.0f, new Color(5, 26, 38).getRGB());
        RenderUtil.drawRect(startX + 118.0f, startY, startX + 120.0f, startY + 460.0f - 180.0f + 100.0f, new Color(5, 26, 38).getRGB());
        Client.instance.FontLoaders.bold30.drawString(Client.instance.name.toUpperCase(), startX + 65.0f - (float)(Client.instance.FontLoaders.bold30.getStringWidth(Client.instance.name.toUpperCase()) / 2) - 0.5f, startY + 17.0f - 0.5f, Color.CYAN.getRGB());
        Client.instance.FontLoaders.bold30.drawString(Client.instance.name.toUpperCase(), startX + 65.0f - (float)(Client.instance.FontLoaders.bold30.getStringWidth(Client.instance.name.toUpperCase()) / 2), startY + 17.0f, new Color(255, 255, 255).getRGB());
        RenderUtil.startGlScissor((int)startX, (int)startY + 40 + 15, 600, 324);
        int cateY = 0;
        String oldPref = "";
        this.currentCateRectY = AnimationUtil.moveUD(this.currentCateRectY, this.endCateY, this.processFPS(FPS, 1000.0f, 0.01f), this.processFPS(FPS, 1000.0f, 0.008f));
        RenderUtil.drawFastRoundedRect((int)(startX + 8.0f + 10.0f), startY + 18.0f + this.currentCateRectY - 2.0f, (int)(startX + 110.0f), startY + 36.0f + this.currentCateRectY - 3.0f, 4.0f, new Color(8, 50, 74).getRGB());
        for (int i = 0; i < ModuleType.values().length; ++i) {
            ModuleType category = ModuleType.values()[i];
            cateY += 20;
            if (!category.name().split("_")[0].equals(oldPref)) {
                cateY = (int)((float)cateY + 5.5f);
                oldPref = category.name().split("_")[0];
            }
            if (category == currentCategory) {
                this.endCateY = cateY;
            }
            RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
            Client.instance.FontLoaders.bold18.drawString(category.name().split("_")[0], startX + 9.0f + 60.0f - (float)(Client.instance.FontLoaders.Comfortaa30.getStringWidth(Client.instance.name) / 2), startY + 23.0f + (float)cateY, new Color(255, 255, 255).getRGB());
            float strX = startX + 70.0f - (float)(Client.instance.FontLoaders.Comfortaa30.getStringWidth(Client.instance.name) / 2);
            float y = startY + 21.0f + (float)cateY;
            int Blue = new Color(3, 168, 245).getRGB();
            if (category.name().equals("Combat")) {
                Client.instance.FontLoaders.icon24.drawString("1", strX - 15.0f, y, Blue);
            }
            if (category.name().equals("Movement")) {
                Client.instance.FontLoaders.icon26.drawString("5", strX - 15.0f, y, Blue);
            }
            if (category.name().equals("Render")) {
                Client.instance.FontLoaders.guiicons22.drawString("F", strX - 15.0f, y, Blue);
            }
            if (category.name().equals("Player")) {
                Client.instance.FontLoaders.guiicons28.drawString("C", strX - 15.0f, y, Blue);
            }
            if (category.name().equals("World")) {
                Client.instance.FontLoaders.guiicons30.drawString("E", strX - 15.0f, y, Blue);
            }
            if (category.name().equals("Misc")) {
                Client.instance.FontLoaders.guiicons28.drawString("G", strX - 15.0f, y, Blue);
            }
            if (category.name().equals("Globals")) {
                Client.instance.FontLoaders.guiicons28.drawString("J", strX - 15.0f, y, -1);
            }
            if (category.name().equals("Script")) {
                Client.instance.FontLoaders.novoicons25.drawString("G", strX - 15.0f, y, -1);
            }
            if (!RenderUtil.isHovered(startX + 8.0f, startY + 18.0f + (float)cateY, startX + 110.0f, startY + 36.0f + (float)cateY, mouseX, mouseY) || !Mouse.isButtonDown((int)0) || this.previousMouse) continue;
            currentCategory = category;
            wheel = 0;
            this.previousMouse = true;
        }
        float leftY = rightY = this.translate.getY();
        int size = 0;
        for (Module m : Client.instance.getModuleManager().getModules()) {
            int v;
            String msg;
            Numbers s;
            int valueY;
            boolean listY;
            if (m.getType() != currentCategory) continue;
            for (Value v2 : m.getValues()) {
                if (((Boolean)ClickGui.Visitable.getValue()).booleanValue() && !v2.isVisitable()) continue;
                ++size;
            }
            float preY = (size + 1) * 20;
            size = 0;
            boolean bl = this.useLeft = !(leftY + preY > rightY + preY);
            if (leftY + preY > rightY + preY) {
                listY = false;
                for (Value v3 : m.getValues()) {
                    if (((Boolean)ClickGui.Visitable.getValue()).booleanValue() && !v3.isVisitable()) continue;
                    size += v3.isDownopen() ? v3.listModes().size() + 1 : 1;
                }
                if (rightY > startY + 185.0f + 100.0f) break;
                RenderUtil.drawFastRoundedRect((int)(startX + 325.0f), startY + 50.0f + rightY, (int)(startX + 510.0f), startY + 62.0f + rightY + (float)((size + 1) * 20) + (float)listY, 3.0f, new Color(3, 13, 26).getRGB());
                RenderUtil.drawRect(startX + 328.0f, startY + 50.0f + rightY + 14.0f, startX + 507.0f, startY + 52.0f + rightY + 14.0f, new Color(5, 26, 38).getRGB());
                Client.instance.FontLoaders.regular19.drawString(m.getName(), startX + 329.0f, startY + 51.0f + rightY + 2.0f, -1);
                RenderUtil.drawRect(startX + 484.0f, startY + 58.0f + rightY + 13.0f - 18.0f, startX + 501.0f, startY + 64.0f + rightY + 15.0f - 18.0f, new Color(3, 23, 46).getRGB());
                RenderUtil.drawCircle(startX + 485.0f, startY + 61.0f + rightY + 14.0f - 18.0f, 4.0, new Color(3, 23, 46));
                RenderUtil.drawCircle(startX + 500.0f, startY + 61.0f + rightY + 14.0f - 18.0f, 4.0, new Color(3, 23, 46));
                RenderUtil.drawRect(startX + 485.0f, startY + 58.0f + rightY + 14.0f - 18.0f, startX + 500.0f, startY + 64.0f + rightY + 14.0f - 18.0f, m.isEnabled() ? new Color(0, 102, 148).getRGB() : new Color(3, 6, 14).getRGB());
                RenderUtil.drawCircle(startX + 485.0f, startY + 61.0f + rightY + 14.0f - 18.0f, 3.0, m.isEnabled() ? new Color(0, 102, 148) : new Color(3, 6, 14));
                RenderUtil.drawCircle(startX + 500.0f, startY + 61.0f + rightY + 14.0f - 18.0f, 3.0, m.isEnabled() ? new Color(0, 102, 148) : new Color(3, 6, 14));
                RenderUtil.drawCircle(startX + 487.0f + (float)(m.isEnabled() ? 11 : 0), startY + 61.0f + rightY + 14.0f - 18.0f, 5.0, m.isEnabled() ? new Color(3, 168, 245) : new Color(120, 139, 151));
                if (RenderUtil.isHovered(startX + 485.0f, startY + 58.0f + rightY + 14.0f - 18.0f, startX + 500.0f, startY + 64.0f + rightY + 14.0f - 18.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.previousMouse) {
                    m.setEnabled(!m.isEnabled());
                    this.previousMouse = true;
                }
                valueY = 5;
                for (Value value : m.getValues()) {
                    if (((Boolean)ClickGui.Visitable.getValue()).booleanValue() && !value.isVisitable()) continue;
                    if (value instanceof Option) {
                        Client.instance.FontLoaders.regular16.drawString(value.getName(), startX + 328.0f, startY + 55.0f + rightY + 14.0f + (float)valueY, new Color(156, 178, 191).getRGB());
                        RenderUtil.drawRect(startX + 484.0f, startY + 58.0f + rightY + 11.0f + (float)valueY, startX + 501.0f, startY + 64.0f + rightY + 13.0f + (float)valueY, new Color(3, 23, 46).getRGB());
                        RenderUtil.drawCircle(startX + 485.0f, startY + 61.0f + rightY + 12.0f + (float)valueY, 4.0, new Color(3, 23, 46));
                        RenderUtil.drawCircle(startX + 500.0f, startY + 61.0f + rightY + 12.0f + (float)valueY, 4.0, new Color(3, 23, 46));
                        RenderUtil.drawRect(startX + 485.0f, startY + 58.0f + rightY + 12.0f + (float)valueY, startX + 500.0f, startY + 64.0f + rightY + 12.0f + (float)valueY, (Boolean)value.getValue() != false ? new Color(0, 102, 148).getRGB() : new Color(3, 6, 14).getRGB());
                        RenderUtil.drawCircle(startX + 485.0f, startY + 61.0f + rightY + 12.0f + (float)valueY, 3.0, (Boolean)value.getValue() != false ? new Color(0, 102, 148) : new Color(3, 6, 14));
                        RenderUtil.drawCircle(startX + 500.0f, startY + 61.0f + rightY + 12.0f + (float)valueY, 3.0, (Boolean)value.getValue() != false ? new Color(0, 102, 148) : new Color(3, 6, 14));
                        RenderUtil.drawCircle(startX + 487.0f + (float)((Boolean)value.getValue() != false ? 11 : 0), startY + 61.0f + rightY + 12.0f + (float)valueY, 5.0, (Boolean)value.getValue() != false ? new Color(3, 168, 245) : new Color(120, 139, 151));
                        if (RenderUtil.isHovered(startX + 485.0f, startY + 58.0f + rightY + 10.0f + (float)valueY, startX + 500.0f, startY + 64.0f + rightY + 14.0f + (float)valueY, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.previousMouse) {
                            value.setValue((Boolean)value.getValue() == false);
                            Minecraft.thePlayer.playSound("random.click", 1.0f, 1.0f);
                            this.previousMouse = true;
                        }
                        valueY += 20;
                    }
                    if (value instanceof Numbers) {
                        Client.instance.FontLoaders.regular18.drawString(value.getName(), startX + 328.0f, startY + 55.0f + rightY + 14.0f + (float)valueY, new Color(156, 178, 191).getRGB());
                        RenderUtil.drawRect(startX + 415.0f, startY + 54.0f + rightY + 19.0f + (float)valueY, startX + 480.0f, startY + 54.0f + rightY + 21.0f + (float)valueY, new Color(3, 23, 46).getRGB());
                        s = (Numbers)value;
                        double state = (Double)value.getValue();
                        double min = ((Number)s.getMinimum()).doubleValue();
                        double max = ((Number)s.getMaximum()).doubleValue();
                        double render = 68.0 * ((state - min) / (max - min));
                        RenderUtil.drawRect((double)(startX + 415.0f), (double)(startY + 54.0f + rightY + 19.0f + (float)valueY), (double)(startX + 415.0f) + render, (double)(startY + 54.0f + rightY + 21.0f + (float)valueY), new Color(0, 102, 148).getRGB());
                        RenderUtil.drawCircle((double)(startX + 416.0f) + render, startY + 54.0f + rightY + 20.0f + (float)valueY, 3.0, new Color(61, 133, 224));
                        Client.instance.FontLoaders.regular14.drawCenteredString(String.valueOf(value.getValue()), startX + 498.0f, startY + 55.0f + rightY + 14.0f + (float)valueY, -1);
                        if (RenderUtil.isHovered(startX + 415.0f, startY + 54.0f + rightY + 19.0f + (float)valueY, startX + 483.0f, startY + 54.0f + rightY + 21.0f + (float)valueY, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.previousMouse) {
                            render = (Double)s.getMinimum();
                            max = (Double)s.getMaximum();
                            double inc = ((Number)s.getIncrement()).doubleValue();
                            double valAbs = (float)mouseX - (startX + 415.0f);
                            double perc = valAbs / 68.0;
                            perc = Math.min(Math.max(0.0, perc), 1.0);
                            double valRel = (max - render) * perc;
                            double val = render + valRel;
                            double num = val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                            value.setValue(num);
                        }
                        valueY += 20;
                    }
                    if (value.listModes().isEmpty()) continue;
                    Client.instance.FontLoaders.regular18.drawString(value.getName(), startX + 328.0f, startY + 55.0f + rightY + 14.0f + (float)valueY, new Color(156, 178, 191).getRGB());
                    RenderUtil.drawFastRoundedRect((int)(startX + 439.0f), startY + 57.0f + rightY + 9.0f + (float)valueY, (int)(startX + 502.0f), startY + 65.0f + rightY + 15.0f + (float)valueY, 3.0f, new Color(3, 23, 46).getRGB());
                    RenderUtil.drawFastRoundedRect((int)(startX + 440.0f), startY + 58.0f + rightY + 9.0f + (float)valueY, (int)(startX + 501.0f), startY + 64.0f + rightY + 15.0f + (float)valueY, 3.0f, new Color(3, 5, 13).getRGB());
                    Client.instance.FontLoaders.regular16.drawCenteredString(value.isDownopen() ? "...." : (String)value.getValue(), startX + 470.0f, startY + 57.0f + rightY + 10.0f + (float)valueY + 3.0f, new Color(200, 200, 200).getRGB());
                    msg = (String)value.getValue();
                    if (RenderUtil.isHovered(startX + 440.0f, startY + 58.0f + rightY + 9.0f + (float)valueY, startX + 501.0f, startY + 64.0f + rightY + 15.0f + (float)valueY, mouseX, mouseY) && !this.previousMouse && Mouse.isButtonDown((int)0)) {
                        value.setDownopen(!value.isDownopen());
                        this.previousMouse = true;
                        Minecraft.thePlayer.playSound("random.click", 1.0f, 1.0f);
                    }
                    if (value.isDownopen()) {
                        RenderUtil.drawFastRoundedRect((int)(startX + 439.0f), startY + 65.0f + rightY + 17.0f + (float)valueY, (int)(startX + 502.0f), startY + 65.0f + rightY + 17.0f + (float)valueY + (float)(12 * value.listModes().size()), 3.0f, new Color(3, 23, 46).getRGB());
                        RenderUtil.drawFastRoundedRect((int)(startX + 440.0f), startY + 66.0f + rightY + 17.0f + (float)valueY, (int)(startX + 501.0f), startY + 65.0f + rightY + 16.0f + (float)valueY + (float)(12 * value.listModes().size()), 3.0f, new Color(3, 5, 13).getRGB());
                        int downY = 0;
                        for (v = 0; v < value.listModes().size(); ++v) {
                            Client.instance.FontLoaders.regular14.drawCenteredString(value.getModeAt(v), startX + 470.0f, startY + 60.0f + rightY + 24.0f + (float)valueY + (float)downY + 2.0f, msg.equals(value.getModeAt(v)) ? new Color(57, 124, 210).getRGB() : new Color(114, 132, 144).getRGB());
                            if (RenderUtil.isHovered(startX + 440.0f, startY + 66.0f + rightY + 17.0f + (float)valueY + (float)downY, startX + 501.0f, startY + 66.0f + rightY + 17.0f + (float)valueY + (float)downY + 12.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.previousMouse) {
                                if (value instanceof Mode) {
                                    Mode vs = (Mode)value;
                                    vs.setCurrentMode(v);
                                    value.setDownopen(!value.isDownopen());
                                }
                                Minecraft.thePlayer.playSound("random.click", 1.0f, 1.0f);
                                this.previousMouse = true;
                            }
                            downY += 12;
                        }
                    }
                    valueY += value.isDownopen() ? 20 + 12 * value.listModes().size() : 20;
                }
                rightY += (float)(16 + (size + 1) * 20);
                continue;
            }
            GL11.glScaled((double)sizes, (double)sizes, (double)sizes);
            listY = false;
            for (Value v3 : m.getValues()) {
                if (((Boolean)ClickGui.Visitable.getValue()).booleanValue() && !v3.isVisitable()) continue;
                size += v3.isDownopen() ? v3.listModes().size() + 1 : 1;
            }
            if (leftY > startY + 185.0f + 100.0f) break;
            RenderUtil.drawFastRoundedRect((int)(startX + 130.0f), startY + 50.0f + leftY, (int)(startX + 315.0f), startY + 62.0f + leftY + (float)((size + 1) * 20) + (float)listY, 3.0f, new Color(3, 13, 26).getRGB());
            RenderUtil.drawRect(startX + 133.0f, startY + 50.0f + leftY + 14.0f, startX + 312.0f, startY + 52.0f + leftY + 14.0f, new Color(5, 26, 38).getRGB());
            Client.instance.FontLoaders.regular19.drawString(m.getName(), startX + 134.0f, startY + 51.0f + leftY + 2.0f, -1);
            RenderUtil.drawRect(startX + 289.0f, startY + 58.0f + leftY + 13.0f - 18.0f, startX + 306.0f, startY + 64.0f + leftY + 15.0f - 18.0f, new Color(3, 23, 46).getRGB());
            RenderUtil.drawCircle(startX + 290.0f, startY + 61.0f + leftY + 14.0f - 18.0f, 4.0, new Color(3, 23, 46));
            RenderUtil.drawCircle(startX + 305.0f, startY + 61.0f + leftY + 14.0f - 18.0f, 4.0, new Color(3, 23, 46));
            RenderUtil.drawRect(startX + 290.0f, startY + 58.0f + leftY + 14.0f - 18.0f, startX + 305.0f, startY + 64.0f + leftY + 14.0f - 18.0f, m.isEnabled() ? new Color(0, 102, 148).getRGB() : new Color(3, 6, 14).getRGB());
            RenderUtil.drawCircle(startX + 290.0f, startY + 61.0f + leftY + 14.0f - 18.0f, 3.0, m.isEnabled() ? new Color(0, 102, 148) : new Color(3, 6, 14));
            RenderUtil.drawCircle(startX + 305.0f, startY + 61.0f + leftY + 14.0f - 18.0f, 3.0, m.isEnabled() ? new Color(0, 102, 148) : new Color(3, 6, 14));
            RenderUtil.drawCircle(startX + 292.0f + (float)(m.isEnabled() ? 11 : 0), startY + 61.0f - 18.0f + leftY + 14.0f, 5.0, m.isEnabled() ? new Color(3, 168, 245) : new Color(120, 139, 151));
            if (RenderUtil.isHovered(startX + 290.0f, startY + 58.0f + leftY + 14.0f - 18.0f, startX + 305.0f, startY + 64.0f + leftY + 14.0f - 18.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.previousMouse) {
                m.setEnabled(!m.isEnabled());
                this.previousMouse = true;
            }
            valueY = 5;
            for (Value value : m.getValues()) {
                if (((Boolean)ClickGui.Visitable.getValue()).booleanValue() && !value.isVisitable()) continue;
                if (value instanceof Option) {
                    Client.instance.FontLoaders.regular18.drawString(value.getName(), startX + 133.0f, startY + 55.0f + leftY + 14.0f + (float)valueY, new Color(156, 178, 191).getRGB());
                    RenderUtil.drawRect(startX + 289.0f, startY + 58.0f + leftY + 11.0f + (float)valueY, startX + 306.0f, startY + 64.0f + leftY + 13.0f + (float)valueY, new Color(3, 23, 46).getRGB());
                    RenderUtil.drawCircle(startX + 290.0f, startY + 61.0f + leftY + 12.0f + (float)valueY, 4.0, new Color(3, 23, 46));
                    RenderUtil.drawCircle(startX + 305.0f, startY + 61.0f + leftY + 12.0f + (float)valueY, 4.0, new Color(3, 23, 46));
                    RenderUtil.drawRect(startX + 290.0f, startY + 58.0f + leftY + 12.0f + (float)valueY, startX + 305.0f, startY + 64.0f + leftY + 12.0f + (float)valueY, (Boolean)value.getValue() != false ? new Color(0, 102, 148).getRGB() : new Color(3, 6, 14).getRGB());
                    RenderUtil.drawCircle(startX + 290.0f, startY + 61.0f + leftY + 12.0f + (float)valueY, 3.0, (Boolean)value.getValue() != false ? new Color(0, 102, 148) : new Color(3, 6, 14));
                    RenderUtil.drawCircle(startX + 305.0f, startY + 61.0f + leftY + 12.0f + (float)valueY, 3.0, (Boolean)value.getValue() != false ? new Color(0, 102, 148) : new Color(3, 6, 14));
                    RenderUtil.drawCircle(startX + 292.0f + (float)((Boolean)value.getValue() != false ? 11 : 0), startY + 61.0f + leftY + 12.0f + (float)valueY, 5.0, (Boolean)value.getValue() != false ? new Color(3, 168, 245) : new Color(120, 139, 151));
                    if (RenderUtil.isHovered(startX + 290.0f, startY + 58.0f + leftY + 10.0f + (float)valueY, startX + 305.0f, startY + 64.0f + leftY + 14.0f + (float)valueY, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.previousMouse) {
                        value.setValue((Boolean)value.getValue() == false);
                        Minecraft.thePlayer.playSound("random.click", 1.0f, 1.0f);
                        this.previousMouse = true;
                    }
                    valueY += 20;
                }
                if (value instanceof Numbers) {
                    Client.instance.FontLoaders.regular18.drawString(value.getName(), startX + 328.0f - 195.0f, startY + 55.0f + leftY + 14.0f + (float)valueY, new Color(156, 178, 191).getRGB());
                    RenderUtil.drawRect(startX + 415.0f - 195.0f, startY + 54.0f + leftY + 19.0f + (float)valueY, startX + 480.0f - 195.0f, startY + 54.0f + leftY + 21.0f + (float)valueY, new Color(3, 23, 46).getRGB());
                    s = (Numbers)value;
                    RenderUtil.drawRect(startX + 415.0f - 195.0f, startY + 54.0f + leftY + 19.0f + (float)valueY, startX + 480.0f - 195.0f, startY + 54.0f + leftY + 21.0f + (float)valueY, new Color(3, 23, 46).getRGB());
                    double render = 68.0 * (((Number)s.getValue()).doubleValue() - (Double)s.getMinimum()) / ((Double)s.getMaximum() - (Double)s.getMinimum());
                    RenderUtil.drawRect((double)(startX + 415.0f - 195.0f), (double)(startY + 54.0f + leftY + 19.0f + (float)valueY), (double)(startX + 415.0f) + render - 195.0, (double)(startY + 54.0f + leftY + 21.0f + (float)valueY), new Color(0, 102, 148).getRGB());
                    RenderUtil.drawCircle((double)(startX + 416.0f) + render - 195.0, startY + 54.0f + leftY + 20.0f + (float)valueY, 3.0, new Color(61, 133, 224));
                    Client.instance.FontLoaders.regular14.drawCenteredString(String.valueOf(value.getValue()), startX + 498.0f - 195.0f, startY + 55.0f + leftY + 14.0f + (float)valueY, -1);
                    if (RenderUtil.isHovered(startX + 415.0f - 195.0f, startY + 54.0f + leftY + 19.0f + (float)valueY, startX + 483.0f - 195.0f, startY + 54.0f + leftY + 21.0f + (float)valueY, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.previousMouse) {
                        render = (Double)s.getMinimum();
                        double max = (Double)s.getMaximum();
                        double inc = (Double)s.getIncrement();
                        double valAbs = (float)mouseX - (startX + 415.0f - 195.0f);
                        double perc = valAbs / 68.0;
                        perc = Math.min(Math.max(0.0, perc), 1.0);
                        double valRel = (max - render) * perc;
                        double val = render + valRel;
                        double num = val = (double)Math.round(val * (1.0 / inc)) / (1.0 / inc);
                        value.setValue(num);
                    }
                    valueY += 20;
                }
                if (!(value instanceof Mode)) continue;
                try {
                    if (value.listModes().isEmpty()) continue;
                    Client.instance.FontLoaders.regular18.drawString(value.getName(), startX + 328.0f - 195.0f, startY + 55.0f + leftY + 14.0f + (float)valueY, new Color(156, 178, 191).getRGB());
                    RenderUtil.drawFastRoundedRect((int)(startX + 439.0f - 195.0f), startY + 57.0f + leftY + 9.0f + (float)valueY, (int)(startX + 502.0f - 195.0f), startY + 65.0f + leftY + 15.0f + (float)valueY, 3.0f, new Color(3, 23, 46).getRGB());
                    RenderUtil.drawFastRoundedRect((int)(startX + 440.0f - 195.0f), startY + 58.0f + leftY + 9.0f + (float)valueY, (int)(startX + 501.0f - 195.0f), startY + 64.0f + leftY + 15.0f + (float)valueY, 3.0f, new Color(3, 5, 13).getRGB());
                    Client.instance.FontLoaders.regular16.drawCenteredString(value.isDownopen() ? "...." : (String)((Mode)value).getValue(), startX + 470.0f - 195.0f, startY + 57.0f + leftY + 10.0f + (float)valueY + 3.0f, new Color(200, 200, 200).getRGB());
                    msg = (String)((Mode)value).getValue();
                    if (RenderUtil.isHovered(startX + 440.0f - 195.0f, startY + 58.0f + leftY + 9.0f + (float)valueY, startX + 501.0f - 195.0f, startY + 64.0f + leftY + 15.0f + (float)valueY, mouseX, mouseY) && !this.previousMouse && Mouse.isButtonDown((int)0)) {
                        value.setDownopen(!value.isDownopen());
                        this.previousMouse = true;
                        Minecraft.thePlayer.playSound("random.click", 1.0f, 1.0f);
                    }
                    if (value.isDownopen()) {
                        RenderUtil.drawFastRoundedRect((int)(startX + 439.0f - 195.0f), startY + 65.0f + leftY + 17.0f + (float)valueY, (int)(startX + 502.0f - 195.0f), startY + 65.0f + leftY + 17.0f + (float)valueY + (float)(12 * value.listModes().size()), 3.0f, new Color(3, 23, 46).getRGB());
                        RenderUtil.drawFastRoundedRect((int)(startX + 440.0f - 195.0f), startY + 66.0f + leftY + 17.0f + (float)valueY, (int)(startX + 501.0f - 195.0f), startY + 65.0f + leftY + 16.0f + (float)valueY + (float)(12 * value.listModes().size()), 3.0f, new Color(3, 5, 13).getRGB());
                        int downY = 0;
                        for (v = 0; v < value.listModes().size(); ++v) {
                            Client.instance.FontLoaders.regular14.drawCenteredString(value.getModeAt(v), startX + 470.0f - 195.0f, startY + 60.0f + leftY + 24.0f + (float)valueY + (float)downY + 2.0f, value.getModeAt(v).equals(msg) ? new Color(57, 124, 210).getRGB() : new Color(114, 132, 144).getRGB());
                            if (RenderUtil.isHovered(startX + 440.0f - 195.0f, startY + 66.0f + leftY + 17.0f + (float)valueY + (float)downY, startX + 501.0f - 195.0f, startY + 66.0f + leftY + 17.0f + (float)valueY + (float)downY + 12.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0) && !this.previousMouse) {
                                Mode vs = (Mode)value;
                                vs.setCurrentMode(v);
                                value.setDownopen(!value.isDownopen());
                                Minecraft.thePlayer.playSound("random.click", 1.0f, 1.0f);
                                this.previousMouse = true;
                            }
                            downY += 12;
                        }
                    }
                    valueY += value.isDownopen() ? 20 + 12 * value.listModes().size() : 20;
                }
                catch (Exception exception) {}
            }
            leftY += (float)(16 + (size + 1) * 20);
        }
        RenderUtil.stopGlScissor();
        int real = Mouse.getDWheel();
        float moduleHeight = Math.max(leftY - this.translate.getY(), rightY - this.translate.getY());
        if (Mouse.hasWheel() && (float)mouseX > startX + 120.0f && (float)mouseY > startY && (float)mouseX < startX + 520.0f && (float)mouseY < startY + 40.0f + 420.0f) {
            if (real > 0 && wheel < 0) {
                for (int i = 0; i < 5 && wheel < 0; wheel += 5, ++i) {
                }
            } else {
                for (int i = 0; i < 5 && real < 0 && moduleHeight > 240.0f && (float)Math.abs(wheel) < moduleHeight - 236.0f; ++i) {
                    wheel -= 5;
                }
            }
        }
        this.translate.interpolate(0.0f, wheel, 0.25f);
        if (!Mouse.isButtonDown((int)0) && !Mouse.isButtonDown((int)1)) {
            this.previousMouse = false;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static void setX(int state) {
        startX = state;
    }

    public static void setY(int state) {
        startY = state;
    }

    public static void setCategory(ModuleType state) {
        currentCategory = state;
    }

    public static void setWheel(int state) {
        wheel = state;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ClickGui.currentModuleType = currentCategory;
        ClickGui.startX = startX;
        ClickGui.startY = startY;
        ClickGui.tempWheel = wheel;
    }
}

