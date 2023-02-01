/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.clickgui.powerx;

import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.ClickGui;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import heaven.main.value.Value;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class PXClickGui
extends GuiScreen {
    public static ModuleType currentModuleType = ModuleType.Combat;
    public static Module currentModule = !Client.instance.getModuleManager().getModulesInType(currentModuleType).isEmpty() ? Client.instance.getModuleManager().getModulesInType(currentModuleType).get(0) : null;
    public static float startX = 100.0f;
    public static float startY = 100.0f;
    public int moduleStart;
    public int valueStart;
    boolean previousmouse = true;
    boolean mouse;
    public float moveX;
    public float moveY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        CFontRenderer typefont = Client.instance.FontLoaders.Comfortaa24;
        if (PXClickGui.isHovered(startX, startY - 25.0f, startX + 400.0f, startY + 25.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
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
        RenderUtil.drawRoundedRect(startX - 1.0f, startY - 1.0f, startX + 370.0f + 1.0f, startY + 230.0f + 1.0f, 3.0f, new Color(11, 84, 150, 150).getRGB());
        RenderUtil.drawRoundedRect(startX, startY, startX + 370.0f, startY + 230.0f, 2.0f, new Color(27, 27, 27).getRGB());
        RenderUtil.drawRect((double)startX, (double)startY + 20.5, (double)(startX + 370.0f), (double)(startY + 20.0f), new Color(21, 21, 21, 140).getRGB());
        RenderUtil.drawRect(startX, startY + 23.0f, startX + 370.0f, startY + 20.0f, new Color(23, 23, 23, 130).getRGB());
        RenderUtil.drawRect((double)(startX + 80.0f), (double)startY + 20.3, (double)(startX + 180.0f), (double)(startY + 230.0f), new Color(27, 27, 27).getRGB());
        RenderUtil.drawRect((double)(startX + 80.0f), (double)startY + 20.3, (double)startX + 80.8, (double)(startY + 230.0f), new Color(12, 12, 12, 200).getRGB());
        RenderUtil.drawRect((double)startX + 80.8, (double)startY + 20.3, (double)startX + 81.6, (double)(startY + 230.0f), new Color(13, 13, 13, 200).getRGB());
        RenderUtil.drawRect((double)startX + 81.6, (double)startY + 20.3, (double)startX + 82.4, (double)(startY + 230.0f), new Color(14, 14, 14, 200).getRGB());
        RenderUtil.drawRect((double)startX + 82.4, (double)startY + 20.3, (double)startX + 83.2, (double)(startY + 230.0f), new Color(15, 15, 15, 200).getRGB());
        RenderUtil.drawRect((double)startX + 83.2, (double)startY + 20.3, (double)(startX + 84.0f), (double)(startY + 230.0f), new Color(16, 16, 16, 200).getRGB());
        RenderUtil.drawRect((double)(startX + 84.0f), (double)startY + 20.3, (double)startX + 84.8, (double)(startY + 230.0f), new Color(17, 17, 17, 200).getRGB());
        RenderUtil.drawRect((double)startX + 84.8, (double)startY + 20.3, (double)startX + 85.8, (double)(startY + 230.0f), new Color(18, 18, 18, 190).getRGB());
        RenderUtil.drawRect((double)startX + 85.8, (double)startY + 20.3, (double)(startX + 90.0f), (double)(startY + 230.0f), new Color(20, 20, 20, 110).getRGB());
        RenderUtil.drawRect((double)(startX + 185.0f), (double)startY + 20.3, (double)(startX + 182.0f), (double)(startY + 230.0f), new Color(21, 21, 21, 110).getRGB());
        RenderUtil.drawRect((double)(startX + 182.0f), (double)startY + 20.3, (double)(startX + 180.0f), (double)(startY + 230.0f), new Color(21, 21, 21, 100).getRGB());
        Client.instance.FontLoaders.Comfortaa22.drawString(Client.instance.name, startX + 5.0f, startY + 5.0f, new Color(152, 152, 152).getRGB());
        for (int i = 0; i < ModuleType.values().length; ++i) {
            if (ModuleType.values()[i] != currentModuleType) {
                RenderUtil.circle(startX + 30.0f, startY + 50.0f + (float)(i * 20), 6.0f, new Color(255, 255, 255, 0).getRGB());
            }
            try {
                if (!PXClickGui.isCategoryHovered(startX + 15.0f, startY + 40.0f + (float)(i * 20), startX + 60.0f, startY + 50.0f + (float)(i * 20), mouseX, mouseY) || !Mouse.isButtonDown((int)0)) continue;
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
        if (PXClickGui.isCategoryHovered(startX + 60.0f, startY, startX + 200.0f, startY + 320.0f, mouseX, mouseY)) {
            if (m < 0 && this.moduleStart < Client.instance.getModuleManager().getModulesInType(currentModuleType).size() - 1) {
                ++this.moduleStart;
            }
            if (m > 0 && this.moduleStart > 0) {
                --this.moduleStart;
            }
        }
        if (PXClickGui.isCategoryHovered(startX + 200.0f, startY, startX + 420.0f, startY + 320.0f, mouseX, mouseY)) {
            if (m < 0 && this.valueStart < currentModule.getValues().size() - 1) {
                ++this.valueStart;
            }
            if (m > 0 && this.valueStart > 0) {
                --this.valueStart;
            }
        }
        Client.instance.FontLoaders.Comfortaa20.drawString(currentModuleType.toString(), startX + 85.0f, startY + 5.0f, new Color(152, 152, 152).getRGB());
        Client.instance.FontLoaders.Comfortaa20.drawString(currentModule == null ? currentModuleType.toString() : "Module:" + currentModule.getName(), startX + 185.0f, startY + 5.0f, new Color(152, 152, 152).getRGB());
        if (currentModule != null) {
            int i;
            float mY = startY + 16.0f;
            for (i = 0; i < Client.instance.getModuleManager().getModulesInType(currentModuleType).size(); ++i) {
                Module module = Client.instance.getModuleManager().getModulesInType(currentModuleType).get(i);
                if (mY > startY + 200.0f) break;
                if (i < this.moduleStart) continue;
                if (module.isEnabled()) {
                    RenderUtil.drawRoundedRect(startX + 88.0f, mY + 8.0f, startX + 175.0f, mY + 25.0f, 8.0f, new Color(58, 58, 58).getRGB());
                    RenderUtil.circle(startX + 95.0f, mY + 16.0f, 2.0f, new Color(0, 100, 237).getRGB());
                } else {
                    RenderUtil.drawRoundedRect(startX + 88.0f, mY + 8.0f, startX + 175.0f, mY + 25.0f, 8.0f, new Color(39, 39, 39).getRGB());
                    RenderUtil.circle(startX + 95.0f, mY + 16.0f, 2.0f, new Color(152, 152, 152).getRGB());
                }
                RenderUtil.startGlScissor((int)startX, (int)(startY - 16.0f), 165, 285);
                Client.instance.FontLoaders.Comfortaa20.drawString(module.getName(), startX + 108.0f - 5.0f, mY + 13.0f, module.isEnabled() ? new Color(152, 152, 152).getRGB() : new Color(68, 68, 68).getRGB());
                RenderUtil.stopGlScissor();
                if (!module.getValues().isEmpty()) {
                    Client.instance.FontLoaders.Comfortaa18.drawString("+", startX + 167.0f, mY + 15.0f, new Color(150, 150, 150).getRGB());
                }
                if (PXClickGui.isSettingsButtonHovered(startX + 108.0f, mY, startX + 100.0f + (float)typefont.getStringWidth(module.getName()), mY + 12.0f + (float)typefont.getStringHeight(), mouseX, mouseY)) {
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
                if (PXClickGui.isSettingsButtonHovered(startX + 90.0f, mY, startX + 100.0f + (float)Client.instance.FontLoaders.Comfortaa20.getStringWidth(module.getName()), mY + 12.0f + (float)Client.instance.FontLoaders.Comfortaa20.getStringHeight(), mouseX, mouseY) && Mouse.isButtonDown((int)1)) {
                    currentModule = module;
                    this.valueStart = 0;
                }
                mY += 18.0f;
            }
            mY = startY + 25.0f;
            for (i = 0; i < currentModule.getValues().size() && !(mY > startY + 215.0f); ++i) {
                float x;
                if (i < this.valueStart) continue;
                CFontRenderer font = Client.instance.FontLoaders.Comfortaa18;
                Value value = currentModule.getValues().get(i);
                if (((Boolean)ClickGui.Visitable.getValue()).booleanValue() && !value.isVisitable()) continue;
                if (value instanceof Numbers) {
                    x = startX + 290.0f;
                    double render = 68.0f * (((Number)value.getValue()).floatValue() - ((Number)((Numbers)value).getMinimum()).floatValue()) / (((Number)((Numbers)value).getMaximum()).floatValue() - ((Number)((Numbers)value).getMinimum()).floatValue());
                    RenderUtil.drawRect(x - 6.0f, mY + 2.0f, (float)((double)x + 74.0), mY + 3.0f, new Color(200, 200, 200).getRGB());
                    RenderUtil.drawRect(x - 6.0f, mY + 2.0f, (float)((double)x + render + 6.5), mY + 3.0f, new Color(0, 100, 237).getRGB());
                    RenderUtil.circle((float)((double)x + render + 4.0), mY + 2.0f, 2.0f, new Color(0, 100, 237).getRGB());
                    font.drawString(value.getName() + ": " + value.getValue(), startX + 190.0f, mY, new Color(152, 152, 152).getRGB());
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousmouse = false;
                    }
                    if (PXClickGui.isButtonHovered(x, mY - 2.0f, x + 100.0f, mY + 7.0f, mouseX, mouseY) && Mouse.isButtonDown((int)0)) {
                        if (!this.previousmouse && Mouse.isButtonDown((int)0)) {
                            render = ((Number)((Numbers)value).getMinimum()).doubleValue();
                            double max = ((Number)((Numbers)value).getMaximum()).doubleValue();
                            double inc = ((Number)((Numbers)value).getIncrement()).doubleValue();
                            double valAbs = (double)mouseX - ((double)x + 1.0);
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
                    mY += 20.0f;
                }
                if (value instanceof Option) {
                    x = startX + 230.0f;
                    font.drawString(value.getName(), startX + 190.0f, mY, new Color(152, 152, 152).getRGB());
                    if (((Boolean)value.getValue()).booleanValue()) {
                        RenderUtil.drawRoundedRect(x + 56.0f, mY - 1.0f, x + 76.0f, mY + 9.0f, 4.0f, new Color(58, 58, 58).getRGB());
                        RenderUtil.circle(x + 72.0f, mY + 4.0f, 4.0f, new Color(0, 100, 237).getRGB());
                    } else {
                        RenderUtil.drawRoundedRect(x + 56.0f, mY - 1.0f, x + 76.0f, mY + 9.0f, 4.0f, new Color(58, 58, 58).getRGB());
                        RenderUtil.circle(x + 60.0f, mY + 4.0f, 4.0f, new Color(152, 152, 152).getRGB());
                    }
                    if (PXClickGui.isCheckBoxHovered(x + 56.0f, mY, x + 76.0f, mY + 9.0f, mouseX, mouseY)) {
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
                    mY += 20.0f;
                }
                if (!(value instanceof Mode)) continue;
                Mode mo = (Mode)value;
                float x2 = startX + 275.0f;
                font.drawStringWithShadow(value.getName(), startX + 190.0f, mY, new Color(152, 152, 152).getRGB());
                RenderUtil.drawRect(x2 + 20.0f, mY, x2 + 65.0f, mY + 10.0f, new Color(120, 120, 120).getRGB());
                RenderUtil.drawRect(x2 + 5.0f, mY, x2 + 15.0f, mY + 10.0f, new Color(0, 100, 237).getRGB());
                RenderUtil.drawRect(x2 + 70.0f, mY, x2 + 80.0f, mY + 10.0f, new Color(0, 100, 237).getRGB());
                font.drawStringWithShadow("<", x2 + 8.0f - 1.0f, mY + 3.0f, new Color(152, 152, 152).getRGB());
                font.drawStringWithShadow(">", x2 + 72.0f + 1.0f, mY + 3.0f, new Color(152, 152, 152).getRGB());
                RenderUtil.startGlScissor((int)(x2 + 20.0f), (int)mY, (int)(x2 + 65.0f), (int)(mY + 10.0f));
                font.drawStringWithShadow(((Mode)value).getModeAsString(), x2 + 40.0f - (float)(font.getStringWidth(((Mode)value).getModeAsString()) / 2), mY + 2.5f, -1);
                RenderUtil.stopGlScissor();
                if (PXClickGui.isStringHovered(x2, mY - 5.0f, x2 + 100.0f, mY + 15.0f, mouseX, mouseY)) {
                    if (!this.previousmouse) {
                        if (Mouse.isButtonDown((int)0) || Mouse.isButtonDown((int)1)) {
                            List<String> options = Arrays.asList(mo.getModes());
                            int index = options.indexOf(mo.getValue());
                            index = Mouse.isButtonDown((int)0) ? ++index : --index;
                            if (index >= options.size()) {
                                index = 0;
                            } else if (index < 0) {
                                index = options.size() - 1;
                            }
                            mo.setValue(mo.getModes()[index]);
                        }
                        this.previousmouse = true;
                    }
                    if (!Mouse.isButtonDown((int)0)) {
                        this.previousmouse = false;
                    }
                }
                mY += 25.0f;
            }
            RenderUtil.startGlScissor((int)startX, (int)(startY - 16.0f), 78, 285);
            float typeY = 0.0f;
            for (int i2 = 0; i2 < ModuleType.values().length; ++i2) {
                ModuleType category = ModuleType.values()[i2];
                typefont.drawString(category.name(), startX + 25.0f, startY + 40.0f + typeY, category == currentModuleType ? new Color(0, 100, 237).getRGB() : new Color(152, 152, 152).getRGB());
                typeY += 20.0f;
            }
            Client.instance.FontLoaders.guiicons28.drawString("G", startX + 7.0f, startY + 140.0f, currentModuleType.name().equals("Misc") ? new Color(0, 100, 237).getRGB() : new Color(152, 152, 152).getRGB());
            Client.instance.FontLoaders.guiicons28.drawString("J", startX + 7.0f, startY + 160.0f, currentModuleType.name().equals("Globals") ? new Color(0, 100, 237).getRGB() : new Color(152, 152, 152).getRGB());
            Client.instance.FontLoaders.novoicons25.drawString("G", startX + 7.0f, startY + 180.0f, currentModuleType.name().equals("Script") ? new Color(0, 100, 237).getRGB() : new Color(152, 152, 152).getRGB());
            int typeWidth = -4;
            switch (currentModuleType.name()) {
                case "Combat": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/" + (Object)((Object)currentModuleType) + 2 + ".png"), (int)startX + 5, (int)startY + 40 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Render.png"), (int)startX + 5, (int)startY + 80 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Movement.png"), (int)startX + 5, (int)startY + 60 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Player.png"), (int)startX + 5, (int)startY + 100 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/World.png"), (int)startX + 5, (int)startY + 120 + -4, 14, 14);
                    break;
                }
                case "Render": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Combat.png"), (int)startX + 5, (int)startY + 40 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/" + (Object)((Object)currentModuleType) + 2 + ".png"), (int)startX + 5, (int)startY + 80 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Movement.png"), (int)startX + 5, (int)startY + 60 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Player.png"), (int)startX + 5, (int)startY + 100 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/World.png"), (int)startX + 5, (int)startY + 120 + -4, 14, 14);
                    break;
                }
                case "Movement": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Combat.png"), (int)startX + 5, (int)startY + 40 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Render.png"), (int)startX + 5, (int)startY + 80 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/" + (Object)((Object)currentModuleType) + 2 + ".png"), (int)startX + 5, (int)startY + 60 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Player.png"), (int)startX + 5, (int)startY + 100 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/World.png"), (int)startX + 5, (int)startY + 120 + -4, 14, 14);
                    break;
                }
                case "Player": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Combat.png"), (int)startX + 5, (int)startY + 40 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Render.png"), (int)startX + 5, (int)startY + 80 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Movement.png"), (int)startX + 5, (int)startY + 60 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/" + (Object)((Object)currentModuleType) + 2 + ".png"), (int)startX + 5, (int)startY + 100 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/World.png"), (int)startX + 5, (int)startY + 120 + -4, 14, 14);
                    break;
                }
                case "World": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Combat.png"), (int)startX + 5, (int)startY + 40 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Render.png"), (int)startX + 5, (int)startY + 80 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Movement.png"), (int)startX + 5, (int)startY + 60 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Player.png"), (int)startX + 5, (int)startY + 100 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/" + (Object)((Object)currentModuleType) + 2 + ".png"), (int)startX + 5, (int)startY + 120 + -4, 14, 14);
                    break;
                }
                case "Globals": 
                case "Script": 
                case "Misc": {
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Combat.png"), (int)startX + 5, (int)startY + 40 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Render.png"), (int)startX + 5, (int)startY + 80 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Movement.png"), (int)startX + 5, (int)startY + 60 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/Player.png"), (int)startX + 5, (int)startY + 100 + -4, 14, 14);
                    RenderUtil.drawImage(new ResourceLocation("client/clickgui/Clickui/World.png"), (int)startX + 5, (int)startY + 120 + -4, 14, 14);
                }
            }
            RenderUtil.stopGlScissor();
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
    }
}

