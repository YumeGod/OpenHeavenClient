/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.utils.TranslateUtil;
import net.minecraft.util.IChatComponent;

public class ChatLine {
    private final int updateCounterCreated;
    private final IChatComponent lineString;
    public float x;
    public float y;
    private final int chatLineID;
    public final TranslateUtil translate = new TranslateUtil(-10.0f, 0.0f);
    public String animaName;
    public AnimationUtil animationUtil = new AnimationUtil();

    public ChatLine(int p_i45000_1_, IChatComponent p_i45000_2_, int p_i45000_3_) {
        this.lineString = p_i45000_2_;
        this.updateCounterCreated = p_i45000_1_;
        this.chatLineID = p_i45000_3_;
    }

    public IChatComponent getChatComponent() {
        return this.lineString;
    }

    public int getUpdatedCounter() {
        return this.updateCounterCreated;
    }

    public int getChatLineID() {
        return this.chatLineID;
    }
}

