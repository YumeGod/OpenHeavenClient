/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.me.guichaguri.betterfps.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;

public class GuiCycleButton
extends GuiButton {
    private final String title;
    protected int key = 0;
    protected List<? extends Object> keys;
    protected HashMap<? extends Object, String> values;
    private final String[] helpLines;

    public <T> GuiCycleButton(int buttonId, String title, HashMap<T, String> values, T defaultValue, String[] helpLines) {
        super(buttonId, 0, 0, title);
        this.title = title;
        this.keys = new ArrayList<T>(values.keySet());
        this.helpLines = helpLines;
        for (int i = 0; i < this.keys.size(); ++i) {
            if (!defaultValue.equals(this.keys.get(i))) continue;
            this.key = i;
            break;
        }
        this.values = values;
        this.updateTitle();
    }

    public void actionPerformed() {
        if (Keyboard.isKeyDown((int)42) && this.shiftClick()) {
            return;
        }
        ++this.key;
        if (this.key >= this.keys.size()) {
            this.key = 0;
        }
        this.updateTitle();
    }

    public boolean shiftClick() {
        return false;
    }

    protected void updateTitle() {
        this.displayString = this.title + ": " + this.values.get(this.keys.get(this.key));
    }

    public <T> T getSelectedValue() {
        return (T)this.keys.get(this.key);
    }

    public String[] getHelpText() {
        return this.helpLines;
    }

    public static class GuiBooleanButton
    extends GuiCycleButton {
        private static final HashMap<Boolean, String> booleanValues = new HashMap();

        public GuiBooleanButton(int buttonId, String title, boolean defaultValue, String[] helpLines) {
            super(buttonId, title, booleanValues, defaultValue, helpLines);
        }

        static {
            booleanValues.put(true, "On");
            booleanValues.put(false, "Off");
        }
    }
}

