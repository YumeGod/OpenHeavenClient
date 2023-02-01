/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.clickgui.listgui;

import com.google.common.collect.Lists;
import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.listgui.Button;
import heaven.main.ui.gui.clickgui.listgui.ValueButton;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.TranslateUtil;
import java.awt.Color;
import java.util.ArrayList;
import org.lwjgl.input.Mouse;

public class Window {
    public final ModuleType category;
    public final ArrayList<Button> buttons = Lists.newArrayList();
    public boolean drag;
    public boolean extended;
    public int x;
    public int y;
    public float expand;
    public int dragX;
    public int dragY;
    public int scrollTo;
    final TranslateUtil translate = new TranslateUtil(0.0f, 0.0f);
    int wheely;
    public float totalY;
    int smoothalpha;
    int height;

    public Window(ModuleType category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
        int y2 = y + 22;
        for (Module c : Client.instance.getModuleManager().getModules()) {
            if (c.getType() != category) continue;
            this.buttons.add(new Button(c, x + 5, y2, 0));
            y2 += 15;
        }
        for (Button b2 : this.buttons) {
            b2.setParent(this);
        }
    }

    public void render(int mouseX, int mouseY) {
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
        this.height = 15 + current;
        if (this.category.name().equals("Misc") && this.height > 107) {
            this.height = 200;
        }
        if (this.category.name().equals("Player") && this.height > 137) {
            this.height = 190;
        }
        if (this.category.name().equals("Movement") && this.height > 272) {
            this.height = 272;
        }
        if (this.category.name().equals("Combat") && this.height > 212) {
            this.height = 240;
        }
        if (!(this.category.name().equals("Player") || this.category.name().equals("Misc") || this.category.name().equals("Combat") || this.category.name().equals("Movement") || this.height <= 316)) {
            this.height = 316;
        }
        if (this.height > 316) {
            this.height = 316;
        }
        boolean isOnPanel = mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && (float)mouseY < (float)this.y + this.expand;
        this.translate.interpolate(0.0f, this.wheely, 0.15f);
        if (isOnPanel) {
            this.runWheel(this.height);
        }
        this.expand = this.extended ? AnimationUtil.moveUD(this.expand, this.height, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f)) : AnimationUtil.moveUD(this.expand, 0.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        SimpleRender.drawRect(this.x, (float)this.y + 1.0f, this.x + 90, (float)this.y + this.expand, new Color(40, 40, 40).getRGB());
        SimpleRender.drawRect(this.x, (float)this.y + 1.0f, this.x + 90, this.y + 17, new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB());
        SimpleRender.drawRect(this.x, (float)this.y + 1.0f, this.x + 15, this.y + 17, new Color(0, 0, 0, 75).getRGB());
        this.smoothalphas();
        SimpleRender.drawRect(this.x, (float)this.y + 1.0f, this.x + 90, this.y + 17, new Color(0, 0, 0, this.smoothalpha).getRGB());
        Client.instance.FontLoaders.regular18.drawString(this.category.name(), this.x + 20, (float)this.y + 5.0f, new Color(255, 255, 255).getRGB());
        if (this.category.name().equals("Combat")) {
            Client.instance.FontLoaders.icon18.drawString("1", this.x + 3, this.y + 7, new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("Misc")) {
            Client.instance.FontLoaders.guiicons24.drawString("G", (float)this.x + 3.0f, (float)this.y + 6.0f, new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("Globals")) {
            Client.instance.FontLoaders.guiicons24.drawString("J", (float)this.x + 3.0f, (float)this.y + 6.0f, new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("Script")) {
            Client.instance.FontLoaders.novoicons24.drawString("G", (float)this.x + 2.0f, (float)this.y + 6.5f, new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("Render")) {
            Client.instance.FontLoaders.guiicons18.drawString("F", this.x + 3, (float)this.y + 7.0f, new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("Player")) {
            Client.instance.FontLoaders.guiicons22.drawString("C", (float)this.x + 3.5f, this.y + 6, new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("Movement")) {
            Client.instance.FontLoaders.icon20.drawString("5", this.x + 3, this.y + 6, new Color(255, 255, 255).getRGB());
        }
        if (this.category.name().equals("World")) {
            Client.instance.FontLoaders.guiicons24.drawString("E", this.x + 3, (float)this.y + 6.0f, new Color(255, 255, 255).getRGB());
        }
        if (this.expand > 0.0f) {
            this.buttons.forEach(b2 -> b2.render(mouseX, mouseY, new Limitation(this.x, this.y + 17, this.x + 90, (int)((float)this.y + this.expand))));
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

    private void smoothalphas() {
        this.smoothalpha = this.drag ? (int)AnimationUtil.moveUD(this.smoothalpha, 100.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f)) : (int)AnimationUtil.moveUD(this.smoothalpha, 0.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
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

    public void mouseScroll(int mouseX, int mouseY, int amount) {
        if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && (float)mouseY < (float)(this.y + 17) + this.expand) {
            this.scrollTo = (int)((float)this.scrollTo - (float)(amount / 120 * 28));
        }
    }

    public void click(int mouseX, int mouseY, int button) {
        if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17) {
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

