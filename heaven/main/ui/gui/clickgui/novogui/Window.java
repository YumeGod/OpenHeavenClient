/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.clickgui.novogui;

import com.google.common.collect.Lists;
import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.novogui.Button;
import heaven.main.ui.gui.clickgui.novogui.ValueButton;
import heaven.main.utils.TranslateUtil;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
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
    public final int max;
    public int scroll;
    public int scrollTo;
    int allX;
    final TranslateUtil translate = new TranslateUtil(0.0f, 0.0f);
    int wheely;
    public float totalY;
    boolean buttonanim;

    public Window(ModuleType category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.max = 120;
        int y2 = y + 22;
        for (Module c : Client.instance.getModuleManager().getModules()) {
            if (c.getType() != category) continue;
            this.buttons.add(new Button(c, x + 5, y2));
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
        this.allX = 12;
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
        boolean isOnPanel = mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && (float)mouseY < (float)this.y + this.expand;
        this.translate.interpolate(0.0f, this.wheely, 0.15f);
        if (isOnPanel) {
            this.runWheel(height);
        }
        this.expand = this.extended ? (this.buttonanim ? AnimationUtil.moveUD(this.expand, height, 0.2f, 0.15f) : (!this.category.name().equals("World") ? AnimationUtil.moveUD(this.expand, height, 0.5f, 0.5f) : AnimationUtil.moveUD(this.expand, height, 0.2f, 0.15f))) : (this.buttonanim ? AnimationUtil.moveUD(this.expand, 0.0f, 0.2f, 0.15f) : (!this.category.name().equals("World") ? AnimationUtil.moveUD(this.expand, 0.0f, 0.5f, 0.5f) : AnimationUtil.moveUD(this.expand, 0.0f, 0.2f, 0.15f)));
        Gui.drawRect(this.x - 1, this.y, this.x + 91 + this.allX, this.y + 17, new Color(29, 29, 29).getRGB());
        RenderUtil.rectangleBordered((double)this.x - 0.5, (double)this.y - 0.5, this.x + 91 + this.allX, (float)this.y + this.expand, 0.05f, new Color(29, 29, 29).getRGB(), new Color(40, 40, 40).getRGB());
        if (this.category.name().equals("Misc")) {
            Client.instance.FontLoaders.Comfortaa19.drawStringWithShadow("Exploit", this.x + 4, this.y + 5, -1);
        }
        if (this.category.name().equals("World")) {
            Client.instance.FontLoaders.Comfortaa19.drawStringWithShadow("Misc", this.x + 4, this.y + 5, -1);
        }
        if (this.category.name().equals("Render")) {
            Client.instance.FontLoaders.Comfortaa19.drawStringWithShadow("Visuals", this.x + 4, this.y + 5, -1);
        }
        if (!(this.category.name().equals("Render") || this.category.name().equals("Misc") || this.category.name().equals("World"))) {
            Client.instance.FontLoaders.Comfortaa19.drawStringWithShadow(this.category.name(), this.x + 4, this.y + 5, -1);
        }
        CFontRenderer novoicons = Client.instance.FontLoaders.novoicons25;
        if (this.category.name().equals("Combat")) {
            novoicons.drawString("D", this.x + 78 + this.allX, this.y + 7, -1);
        }
        if (this.category.name().equals("Misc")) {
            novoicons.drawString("G", this.x + 78 + this.allX, this.y + 5, -1);
        }
        if (this.category.name().equals("Render")) {
            novoicons.drawString("C", this.x + 78 + this.allX, this.y + 6, -1);
        }
        if (this.category.name().equals("Player")) {
            novoicons.drawString("B", this.x + 78 + this.allX, this.y + 6, -1);
        }
        if (this.category.name().equals("Movement")) {
            novoicons.drawString("A", this.x + 78 + this.allX, this.y + 6, -1);
        }
        if (this.category.name().equals("World")) {
            novoicons.drawString("F", this.x + 78 + this.allX, this.y + 5, -1);
        }
        if (this.expand > 0.0f) {
            this.buttons.forEach(b2 -> b2.render(mouseX, mouseY, new Limitation(this.x, this.y + 16, this.x + 90 + this.allX, (int)((float)this.y + this.expand))));
        }
        if (this.drag) {
            if (!Mouse.isButtonDown((int)0)) {
                this.drag = false;
            }
            this.x = mouseX - this.dragX;
            this.y = mouseY - this.dragY;
            this.buttons.get((int)0).y = this.y + 22 - this.scroll;
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

    public void key(char typedChar, int keyCode) {
        this.buttons.forEach(b2 -> b2.key(typedChar, keyCode));
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

