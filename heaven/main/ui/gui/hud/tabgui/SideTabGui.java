/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.ui.gui.hud.tabgui;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventKey;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.simplecore.SimpleRender;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class SideTabGui {
    public float baseCategoryWidth;
    public float baseCategoryHeight;
    private Section section = Section.CATEGORY;
    public int categoryTab;
    private int modTab;
    public float categoryPosition = 22.0f;
    private float categoryTargetPosition = 22.0f;
    public float modPosition = 22.0f;
    private float modTargetPosition = 22.0f;
    private float modulesXanim;
    public float shouldY;
    float hue;
    private int alpha;

    public void init() {
        int highestWidth = 0;
        for (ModuleType moduleType : ModuleType.values()) {
            String name = Character.toUpperCase(moduleType.name().charAt(0)) + moduleType.name().substring(1);
            int stringWidth = Client.instance.FontLoaders.Comfortaa18.getStringWidth(name);
            highestWidth = Math.max(stringWidth, highestWidth);
        }
        this.baseCategoryWidth = highestWidth + 25;
        this.baseCategoryHeight = (ModuleType.values().length - 3) * 14 + 2;
    }

    @EventHandler
    public void onKey(EventKey e) {
        block16: {
            block17: {
                if (!HUD.tabguimode.is("Side")) break block16;
                if (Minecraft.getMinecraft().currentScreen != null) {
                    return;
                }
                if (!Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled() || !((Boolean)HUD.tabgui.getValue()).booleanValue()) break block16;
                if (this.section != Section.CATEGORY) break block17;
                switch (e.getKey()) {
                    default: {
                        break;
                    }
                    case 200: {
                        --this.categoryTab;
                        this.categoryTargetPosition -= 12.0f;
                        if (this.categoryTab < 0) {
                            this.categoryTargetPosition = 22 + (ModuleType.values().length - 3) * 12;
                            this.categoryTab = ModuleType.values().length - 3;
                            break;
                        }
                        break block16;
                    }
                    case 205: {
                        int highestWidth = 0;
                        for (Module module : this.getModsInCategory(ModuleType.values()[this.categoryTab])) {
                            String name = Character.toUpperCase(module.getName().charAt(0)) + module.getName().substring(1);
                            int stringWidth = Client.instance.FontManager.baloo18.getStringWidth(name);
                            highestWidth = Math.max(stringWidth, highestWidth);
                        }
                        this.modTargetPosition = this.modPosition = this.categoryTargetPosition;
                        this.modTab = 0;
                        this.section = Section.MODS;
                        this.modulesXanim = 0.0f;
                        this.alpha = 0;
                        break;
                    }
                    case 208: {
                        ++this.categoryTab;
                        this.categoryTargetPosition += 12.0f;
                        if (this.categoryTab > ModuleType.values().length - 3) {
                            this.categoryTargetPosition = 22.0f;
                            this.categoryTab = 0;
                            break;
                        }
                        break block16;
                    }
                }
                break block16;
            }
            if (this.section == Section.MODS) {
                switch (e.getKey()) {
                    case 28: 
                    case 205: {
                        Module mod = this.getModsInCategory(ModuleType.values()[this.categoryTab]).get(this.modTab);
                        mod.setEnabled(!mod.isEnabled());
                        break;
                    }
                    case 200: {
                        --this.modTab;
                        this.modTargetPosition -= 12.0f;
                        if (this.modTab >= 0) break;
                        this.modTargetPosition = this.categoryTargetPosition + (float)((this.getModsInCategory(ModuleType.values()[this.categoryTab]).size() - 1) * 12);
                        this.modTab = this.getModsInCategory(ModuleType.values()[this.categoryTab]).size() - 1;
                        break;
                    }
                    case 203: {
                        this.section = Section.CATEGORY;
                        break;
                    }
                    case 208: {
                        ++this.modTab;
                        this.modTargetPosition += 12.0f;
                        if (this.modTab <= this.getModsInCategory(ModuleType.values()[this.categoryTab]).size() - 1) break;
                        this.modTargetPosition = this.categoryTargetPosition;
                        this.modTab = 0;
                    }
                }
            }
        }
    }

    private void updateBars() {
        if (HUD.tabguimode.is("Side") && Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled() && ((Boolean)HUD.tabgui.getValue()).booleanValue()) {
            if (this.categoryPosition < this.categoryTargetPosition) {
                this.categoryPosition = AnimationUtil.moveUD(this.categoryPosition, this.categoryTargetPosition, SimpleRender.processFPS(1000.0f, 0.015f), SimpleRender.processFPS(1000.0f, 0.013f));
                if (this.categoryPosition >= this.categoryTargetPosition) {
                    this.categoryPosition = this.categoryTargetPosition;
                }
            } else if (this.categoryPosition > this.categoryTargetPosition) {
                this.categoryPosition = AnimationUtil.moveUD(this.categoryPosition, this.categoryTargetPosition, SimpleRender.processFPS(1000.0f, 0.015f), SimpleRender.processFPS(1000.0f, 0.013f));
                if (this.categoryPosition <= this.categoryTargetPosition) {
                    this.categoryPosition = this.categoryTargetPosition;
                }
            }
            if (this.modPosition < this.modTargetPosition) {
                this.modPosition = AnimationUtil.moveUD(this.modPosition, this.modTargetPosition, SimpleRender.processFPS(1000.0f, 0.015f), SimpleRender.processFPS(1000.0f, 0.013f));
                if (this.modPosition >= this.modTargetPosition) {
                    this.modPosition = this.modTargetPosition;
                }
            } else if (this.modPosition > this.modTargetPosition) {
                this.modPosition = AnimationUtil.moveUD(this.modPosition, this.modTargetPosition, SimpleRender.processFPS(1000.0f, 0.015f), SimpleRender.processFPS(1000.0f, 0.013f));
                if (this.modPosition <= this.modTargetPosition) {
                    this.modPosition = this.modTargetPosition;
                }
            }
        }
    }

    public void renderTabGui() {
        if (HUD.tabguimode.is("Side") && Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled() && ((Boolean)HUD.tabgui.getValue()).booleanValue()) {
            boolean hudMode = HUD.watermark.is("Sense") || HUD.watermark.is("Weave");
            if (hudMode && this.shouldY != 5.0f) {
                this.shouldY = AnimationUtil.moveUD(this.shouldY, 5.0f, SimpleRender.processFPS(1000.0f, 0.015f), SimpleRender.processFPS(1000.0f, 0.013f));
            }
            if (!hudMode && this.shouldY != 0.0f) {
                this.shouldY = AnimationUtil.moveUD(this.shouldY, 0.0f, SimpleRender.processFPS(1000.0f, 0.015f), SimpleRender.processFPS(1000.0f, 0.013f));
            }
        }
        if (HUD.tabguimode.is("Side") && Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled() && ((Boolean)HUD.tabgui.getValue()).booleanValue()) {
            int i;
            if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
                return;
            }
            if (((Boolean)HUD.rainbow.getValue()).booleanValue()) {
                this.hue += SimpleRender.processFPS(1000.0f, 0.05f);
                if (this.hue > 255.0f) {
                    this.hue = 0.0f;
                }
            }
            float h = this.hue + 200.0f;
            float h2 = this.hue + 85.0f;
            float h3 = this.hue + 170.0f;
            Color color33 = Color.getHSBColor(h / 255.0f, 0.9f, 1.0f);
            Color color332 = Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
            Color color333 = Color.getHSBColor(h3 / 255.0f, 0.9f, 1.0f);
            GL11.glPushMatrix();
            this.updateBars();
            this.baseCategoryWidth = HUD.tabguimode.is("Side") ? 68.0f : 1.0f;
            this.baseCategoryHeight = (ModuleType.values().length - 2) * 14 - 2;
            RenderUtil.drawRect(5.0f, 21.0f + this.shouldY, 2.0f + this.baseCategoryWidth, 24.0f + this.baseCategoryHeight - 13.0f + this.shouldY, new Color(15, 15, 15, 200).getRGB());
            if (((Boolean)HUD.Breathinglamp.getValue()).booleanValue()) {
                if (((Boolean)HUD.rainbow.getValue()).booleanValue()) {
                    RenderUtil.drawGradientSidewaysV(6.0, this.categoryPosition - 1.0f + this.shouldY + 1.0f, 7.0, this.categoryPosition + 11.0f + this.shouldY - 2.0f, HUD.rainbowmode.is("Client") ? color332.getRGB() : color33.getRGB(), HUD.rainbowmode.is("Client") ? color333.getRGB() : color332.getRGB());
                } else {
                    Color Ranbow = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 70, 25);
                    RenderUtil.drawRect(6.0f, this.categoryPosition - 1.0f + this.shouldY + 1.0f, 7.0f, this.categoryPosition + 11.0f + this.shouldY + 0.1f - 2.0f, Ranbow.getRGB());
                }
            } else if (((Boolean)HUD.rainbow.getValue()).booleanValue()) {
                RenderUtil.drawGradientSidewaysV(6.0, this.categoryPosition - 1.0f + this.shouldY + 1.0f, 7.0, this.categoryPosition + 11.0f + this.shouldY - 2.0f, HUD.rainbowmode.is("Client") ? color332.getRGB() : color33.getRGB(), HUD.rainbowmode.is("Client") ? color333.getRGB() : color332.getRGB());
            } else {
                RenderUtil.drawGradientSidewaysV(6.0, this.categoryPosition - 1.0f + this.shouldY + 1.0f, 7.0, this.categoryPosition + 11.0f + this.shouldY - 2.0f, this.HUDColor(), this.HUDColor());
            }
            float yPos = 21.0f;
            for (i = 0; i < ModuleType.values().length - 2; ++i) {
                ModuleType category = ModuleType.values()[i];
                String name = Character.toUpperCase(category.name().toLowerCase().charAt(0)) + category.name().toLowerCase().substring(1);
                Client.instance.fontManager.comfortaa17.drawStringOther(name, 8.0, yPos + this.shouldY, -2105377);
                yPos += 12.0f;
            }
            if (this.modulesXanim >= 0.0f && this.section != Section.MODS) {
                this.modulesXanim = AnimationUtil.moveUD(this.modulesXanim, 0.0f, SimpleRender.processFPS(1000.0f, 0.015f), SimpleRender.processFPS(1000.0f, 0.013f));
            } else if (this.modulesXanim < 6.0f) {
                this.modulesXanim = AnimationUtil.moveUD(this.modulesXanim, 6.0f, SimpleRender.processFPS(1000.0f, 0.015f), SimpleRender.processFPS(1000.0f, 0.013f));
            }
            if (this.alpha != 0 && this.section != Section.MODS) {
                this.alpha = (int)AnimationUtil.moveUD(this.alpha, 0.0f, SimpleRender.processFPS(1000.0f, 0.015f), SimpleRender.processFPS(1000.0f, 0.013f));
            } else if (this.alpha != 200) {
                this.alpha = (int)AnimationUtil.moveUD(this.alpha, 200.0f, SimpleRender.processFPS(1000.0f, 0.015f), SimpleRender.processFPS(1000.0f, 0.013f));
            }
            if (this.section == Section.MODS || this.modulesXanim > 2.5f) {
                int size = this.getModsInCategory(ModuleType.values()[this.categoryTab]).size();
                RenderUtil.drawGradientSideways(this.baseCategoryWidth + this.modulesXanim, this.categoryPosition + this.shouldY - 1.0f, this.baseCategoryWidth + 2.0f - 16.0f + 79.0f + this.modulesXanim + 7.0f + 3.0f, this.categoryPosition + (float)(size * 12) - 1.0f + this.shouldY, new Color(15, 15, 15, this.alpha).getRGB(), new Color(15, 15, 15, this.alpha).getRGB());
                if (HUD.tabguimode.is("Side")) {
                    if (((Boolean)HUD.rainbow.getValue()).booleanValue()) {
                        RenderUtil.drawGradientSidewaysV(this.baseCategoryWidth + this.modulesXanim + 1.0f, this.modPosition + this.shouldY, this.baseCategoryWidth + 8.0f + 57.0f + this.modulesXanim + 7.0f - 70.0f, this.modPosition + 9.0f + this.shouldY, HUD.rainbowmode.is("Client") ? color332.getRGB() : color33.getRGB(), HUD.rainbowmode.is("Client") ? color333.getRGB() : color332.getRGB());
                    } else {
                        RenderUtil.drawGradientSidewaysV(this.baseCategoryWidth + this.modulesXanim + 1.0f, this.modPosition + this.shouldY, this.baseCategoryWidth + 8.0f + 57.0f + this.modulesXanim + 7.0f - 70.0f, this.modPosition + 9.0f + this.shouldY, this.HUDColorOnlyMobs(), this.HUDColorOnlyMobs());
                    }
                }
                yPos = this.categoryPosition;
                for (i = 0; i < size; ++i) {
                    Module mod = this.getModsInCategory(ModuleType.values()[this.categoryTab]).get(i);
                    Client.instance.fontManager.comfortaa17.drawStringOther(mod.getName(), (double)(this.baseCategoryWidth + 8.0f + this.modulesXanim) - 5.3, yPos + this.shouldY - 1.0f, !mod.isEnabled() ? 0x999999 : -2105377);
                    yPos += 12.0f;
                }
            }
            GL11.glPopMatrix();
        }
    }

    private int HUDColorOnlyMobs() {
        return new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue(), this.alpha).getRGB();
    }

    private int HUDColor() {
        return new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue(), 200).getRGB();
    }

    private List<Module> getModsInCategory(ModuleType category) {
        ArrayList<Module> modList = new ArrayList<Module>();
        for (Module mod : Client.instance.getModuleManager().modules) {
            if (mod.getType() != category) continue;
            modList.add(mod);
        }
        modList.sort(Comparator.comparing(Module::getName));
        return modList;
    }

    private static enum Section {
        CATEGORY,
        MODS;

    }
}

