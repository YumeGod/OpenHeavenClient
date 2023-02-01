/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.clickgui.astolfoclickgui;

import com.google.common.collect.Lists;
import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Button;
import heaven.main.ui.gui.clickgui.astolfoclickgui.Limitation;
import heaven.main.ui.gui.clickgui.astolfoclickgui.ValueButton;
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
    public int expand;
    public int dragX;
    public int dragY;
    public int scroll;
    public int scrollTo;
    public double angel;
    int staticColor;
    public int totalY;
    int offset;
    final TranslateUtil translate = new TranslateUtil(0.0f, 0.0f);
    int allX;

    public Window(ModuleType category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
        int y2 = y + 25;
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
        int iY = this.y + 25;
        this.totalY = 16;
        for (Button b3 : this.buttons) {
            b3.y = (int)((float)iY - this.translate.getY());
            iY += 15;
            this.totalY += 15;
            if (b3.expand) {
                for (ValueButton ignored : b3.buttons) {
                    current += 15;
                    this.totalY += 15;
                }
            }
            current += 15;
        }
        int height = 16 + current;
        if (height > 316) {
            height = 316;
        }
        this.allX = 10;
        this.expand = this.extended ? height : 0;
        this.angel = this.extended ? 180.0 : 0.0;
        boolean isOnPanel = mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + this.expand;
        this.translate.interpolate(0.0f, this.offset, 0.15f);
        if (isOnPanel) {
            this.runWheel(height);
        }
        switch (this.category.name()) {
            case "Combat": {
                this.staticColor = new Color(231, 76, 60).getRGB();
                break;
            }
            case "Render": {
                this.staticColor = new Color(54, 1, 205).getRGB();
                break;
            }
            case "Movement": {
                this.staticColor = new Color(45, 203, 113).getRGB();
                break;
            }
            case "Player": {
                this.staticColor = new Color(141, 68, 173).getRGB();
                break;
            }
            case "World": {
                this.staticColor = new Color(38, 154, 255).getRGB();
                break;
            }
            case "Misc": {
                this.staticColor = new Color(102, 101, 101).getRGB();
            }
        }
        RenderUtil.drawRect(this.x, this.y, this.x + 90 + this.allX, this.y + 17, new Color(25, 25, 25).getRGB());
        if (this.expand > 0) {
            RenderUtil.rectangleBordered((double)this.x - 0.5, (double)this.y - 0.5, (double)this.x + 90.5 + (double)this.allX, (double)this.y + 5.5 + (double)this.expand, 1.0, this.staticColor, this.staticColor);
            RenderUtil.rectangleBordered(this.x, this.y, this.x + 90 + this.allX, (double)this.y + 5.0 + (double)this.expand, 1.0, new Color(25, 25, 25).getRGB(), new Color(25, 25, 25).getRGB());
            for (Button b2 : this.buttons) {
                b2.render(mouseX, mouseY, new Limitation(this.x, this.y + 16, this.x + 90 + this.allX, this.y + this.expand));
            }
        }
        Client.instance.FontLoaders.bold16.drawString(this.category.name().toLowerCase(), this.x + 4, this.y + 6, new Color(233, 233, 233, 233).getRGB());
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
            if (this.totalY - height <= 0) {
                return;
            }
            if (wheel < 0) {
                if (this.offset < this.totalY - height) {
                    this.offset += 30 + Mouse.getDWheel();
                    if (this.offset < 0) {
                        this.offset = 0;
                    }
                }
            } else if (wheel > 0) {
                this.offset -= 30 + Mouse.getDWheel();
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
    }

    public void key(char typedChar, int keyCode) {
        this.buttons.forEach(b2 -> b2.key(typedChar, keyCode));
    }

    public void mouseScroll(int mouseX, int mouseY, int amount) {
        if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17 + this.expand) {
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
            this.buttons.stream().filter(b2 -> b2.y < this.y + this.expand).forEach(b2 -> b2.click(mouseX, mouseY, button));
        }
    }
}

