/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.clickgui.oldFlux;

import com.google.common.collect.Lists;
import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.oldFlux.Button;
import heaven.main.ui.gui.clickgui.oldFlux.ClickUIOld;
import heaven.main.ui.gui.clickgui.oldFlux.ValueButton;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.TranslateUtil;
import java.awt.Color;
import java.util.ArrayList;
import org.lwjgl.input.Mouse;

public class Window {
    public ModuleType category;
    public ArrayList<Button> buttons = Lists.newArrayList();
    public boolean drag;
    public boolean extended;
    public int x;
    public int y;
    public float expand;
    public int dragX;
    public int dragY;
    public int max;
    public int scroll;
    public int scrollTo;
    public double angel;
    protected ClickUIOld parent;
    int allX;
    int smoothalpha;
    final TranslateUtil translate = new TranslateUtil(0.0f, 0.0f);
    int wheely;
    public float totalY;

    public Window(ModuleType category, int x2, int y2, ClickUIOld parent) {
        this.category = category;
        this.x = x2;
        this.y = y2;
        this.max = 120;
        this.parent = parent;
        int y22 = y2 + 22;
        for (Module c2 : Client.instance.getModuleManager().getModules()) {
            if (c2.getType() != category) continue;
            this.buttons.add(new Button(c2, x2 + 5, y22));
            y22 += 15;
        }
        for (Button b2 : this.buttons) {
            b2.setParent(this);
        }
    }

    public void render(int mouseX, int mouseY) {
        if (this.category == null) {
            this.y = 5;
        }
        this.allX = -15;
        int current = 0;
        int iY = this.y + 22;
        this.totalY = 17.0f;
        for (Button b3 : this.buttons) {
            b3.y = (int)((float)iY - this.translate.getY());
            iY += 15;
            this.totalY += 15.0f;
            if (b3.expand) {
                for (ValueButton ignored : b3.buttons) {
                    current += 15;
                    this.totalY += 15.0f;
                }
            }
            current += 15;
        }
        int height = 15 + current;
        if (this.category.name().equals("Misc") && height > 107) {
            height = 107;
        }
        if (this.category.name().equals("Player") && height > 137) {
            height = 137;
        }
        if (this.category.name().equals("Movement") && height > 272) {
            height = 272;
        }
        if (this.category.name().equals("Combat") && height > 212) {
            height = 212;
        }
        if (!(this.category.name().equals("Player") || this.category.name().equals("Misc") || this.category.name().equals("Combat") || this.category.name().equals("Movement") || height <= 316)) {
            height = 316;
        }
        this.smoothalphas();
        boolean isOnPanel = mouseX > this.x - 2 && mouseX < this.x + 92 + this.allX && mouseY > this.y - 2 && (float)mouseY < (float)this.y + this.expand;
        this.translate.interpolate(0.0f, this.wheely, 0.15f);
        if (isOnPanel) {
            this.runWheel(height);
        }
        this.expand = this.extended ? AnimationUtil.moveUD(this.expand, height, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f)) : AnimationUtil.moveUD(this.expand, 5.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        RenderUtil.drawBorderedRect(this.x, (double)this.y + 3.5 + 13.0, this.x + 90 + this.allX, (float)this.y + this.expand, 1.0f, new Color(30, 30, 30, 180).getRGB(), new Color(15, 15, 15, 200).getRGB());
        SimpleRender.drawRect(this.x - 2, this.y + 17, this.x + 92 + this.allX, (double)this.y + 3.5, new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue(), this.smoothalpha).getRGB());
        Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(this.category.name(), (float)(this.x + 35) - (float)Client.instance.FontLoaders.Comfortaa16.getStringWidth(this.category.name()) / 2.0f, (double)this.y + 8.5, -1);
        if (this.expand > 5.0f) {
            for (Button b2 : this.buttons) {
                b2.render(mouseX, mouseY, new Limitation(this.x, this.y + 16, this.x + 90 + this.allX, (int)((float)this.y + this.expand)));
            }
        }
        if (this.drag) {
            if (!Mouse.isButtonDown((int)0)) {
                this.drag = false;
            }
            this.x = mouseX - this.dragX;
            this.y = mouseY - this.dragY;
            this.buttons.get((int)0).y = (int)((float)(this.y + 22) - this.translate.getY());
            for (Button b4 : this.buttons) {
                b4.x = this.x + 5;
            }
        }
    }

    protected void runWheel(int height) {
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (this.totalY - (float)height <= 0.0f) {
                return;
            }
            if (wheel < 0) {
                if ((float)this.wheely < this.totalY - (float)height) {
                    this.wheely += 20 + Mouse.getDWheel();
                    if (this.wheely < 0) {
                        this.wheely = 0;
                    }
                }
            } else if (wheel > 0) {
                this.wheely -= 20 + Mouse.getDWheel();
                if (this.wheely < 0) {
                    this.wheely = 0;
                }
            }
        }
    }

    public int hudColor() {
        return new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB();
    }

    public void mouseScroll(int mouseX, int mouseY, int amount) {
        if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && (float)mouseY < (float)(this.y + 17) + this.expand) {
            this.scrollTo = (int)((float)this.scrollTo - (float)(amount / 120 * 28));
        }
    }

    private void smoothalphas() {
        this.smoothalpha = this.drag ? (int)AnimationUtil.moveUD(this.smoothalpha, 150.0f, 0.2f, 0.15f) : (int)AnimationUtil.moveUD(this.smoothalpha, 255.0f, 0.2f, 0.15f);
    }

    public void click(int mouseX, int mouseY, int button) {
        if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y + 4 && mouseY < this.y + 16) {
            if (button == 1) {
                boolean bl = this.extended = !this.extended;
            }
            if (button == 0) {
                this.drag = true;
                this.dragX = mouseX - this.x;
                this.dragY = mouseY - this.y;
            }
        }
        if (this.extended) {
            this.buttons.stream().filter(b2 -> b2.y < (float)this.y + this.expand).forEach(b2 -> b2.click(mouseX, mouseY, button));
        }
    }
}

