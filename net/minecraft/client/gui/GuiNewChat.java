/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import heaven.main.Client;
import heaven.main.event.events.misc.EventChat;
import heaven.main.management.EventManager;
import heaven.main.module.modules.globals.Chat;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiNewChat
extends Gui {
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft mc;
    private final List<String> sentMessages = Lists.newArrayList();
    private final List<ChatLine> chatLines = Lists.newArrayList();
    private final List<ChatLine> drawnChatLines = Lists.newArrayList();
    private int scrollPos;
    private boolean isScrolled;
    public static float percentComplete = 0.0f;
    public static int newLines;
    public static long prevMillis;

    public GuiNewChat(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public static void updatePercentage(long diff) {
        if (percentComplete < 1.0f) {
            percentComplete += 0.004f * (float)diff;
        }
        percentComplete = AnimationUtil.clamp(percentComplete, 0.0f, 1.0f);
    }

    public void drawChat(int updateCounter) {
        if (Client.instance.getModuleManager().getModuleByClass(Chat.class).isEnabled() && Chat.animations.is("HeightSmooth") && prevMillis == -1L) {
            prevMillis = System.currentTimeMillis();
            return;
        }
        long current = System.currentTimeMillis();
        long diff = current - prevMillis;
        if (Client.instance.getModuleManager().getModuleByClass(Chat.class).isEnabled() && Chat.animations.is("HeightSmooth")) {
            prevMillis = current;
            GuiNewChat.updatePercentage(diff);
        }
        float t = percentComplete;
        float percent = 1.0f - (t -= 1.0f) * t * t * t;
        if (Client.instance.getModuleManager().getModuleByClass(Chat.class).isEnabled() && Chat.animations.is("HeightSmooth")) {
            percent = AnimationUtil.clamp(percent, 0.0f, 1.0f);
        }
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            boolean flag = false;
            int j = 0;
            float f = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (!this.drawnChatLines.isEmpty()) {
                if (this.getChatOpen()) {
                    flag = true;
                }
                float f1 = this.getChatScale();
                int l = MathHelper.ceiling_float_int((float)this.getChatWidth() / f1);
                GlStateManager.pushMatrix();
                if (Client.instance.getModuleManager().getModuleByClass(Chat.class).isEnabled()) {
                    if (Chat.animations.is("HeightSmooth")) {
                        if (!this.isScrolled) {
                            GlStateManager.translate(2.0f, 20.0f + (9.0f - 9.0f * percent) * f1, 0.0f);
                        }
                    } else {
                        GlStateManager.translate(2.0f, 20.0f, 0.0f);
                    }
                } else {
                    GlStateManager.translate(2.0f, 20.0f, 0.0f);
                }
                GlStateManager.scale(f1, f1, 1.0f);
                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1) {
                    int j1;
                    ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);
                    if (chatline == null || (j1 = updateCounter - chatline.getUpdatedCounter()) >= 200 && !flag) continue;
                    double d0 = (double)j1 / 200.0;
                    d0 = 1.0 - d0;
                    d0 *= 10.0;
                    d0 = MathHelper.clamp_double(d0, 0.0, 1.0);
                    d0 *= d0;
                    int l1 = (int)(255.0 * d0);
                    if (flag) {
                        l1 = 255;
                    }
                    l1 = (int)((float)l1 * f);
                    ++j;
                    float x = 0.0f;
                    float y = 0.0f;
                    if (l1 <= 3) continue;
                    boolean i2 = false;
                    int j2 = -i1 * 9;
                    if (!Client.instance.getModuleManager().getModuleByClass(Chat.class).isEnabled()) {
                        x = (float)i2;
                        y = j2;
                        GuiNewChat.drawRect(x, y - 9.0f, x + (float)l + 4.0f, y, l1 / 2 << 24);
                    } else {
                        if (Chat.animations.isCurrentMode("Off") || Chat.animations.isCurrentMode("HeightSmooth")) {
                            x = (float)i2;
                            y = j2;
                        }
                        if (((Boolean)Chat.bgshadow.getValue()).booleanValue()) {
                            GuiNewChat.drawRect(x, y - 9.0f, x + (float)l + 4.0f, y, l1 / 2 << 24);
                        }
                    }
                    String s = chatline.getChatComponent().getFormattedText();
                    GlStateManager.enableBlend();
                    if (!Client.instance.getModuleManager().getModuleByClass(Chat.class).isEnabled()) {
                        Minecraft.fontRendererObj.drawStringWithShadow(s, (float)i2, j2 - 8, 0xFFFFFF + (l1 << 24));
                    } else {
                        if (Chat.animations.is("HeightSmooth")) {
                            if (i1 <= newLines) {
                                this.drawString(s, 0.0f, j2 - 8, 0xFFFFFF + ((int)((float)l1 * percent) << 24));
                            } else {
                                this.drawString(s, (float)i2, j2 - 8, 0xFFFFFF + (l1 << 24));
                            }
                        }
                        if (Chat.animations.isCurrentMode("Off")) {
                            this.drawString(s, x, y - 8.0f, 0xFFFFFF + (l1 << 24));
                        }
                    }
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                if (flag) {
                    int k2 = Minecraft.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                    int l2 = this.drawnChatLines.size() * k2 + this.drawnChatLines.size();
                    int i3 = j * k2 + j;
                    int j3 = this.scrollPos * i3 / this.drawnChatLines.size();
                    int k1 = i3 * i3 / l2;
                    if (l2 != i3) {
                        int l3;
                        int k3 = j3 > 0 ? 170 : 96;
                        int n = l3 = this.isScrolled ? 0xCC3333 : 0x3333AA;
                        if (!Client.instance.getModuleManager().getModuleByClass(Chat.class).isEnabled()) {
                            GuiNewChat.drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                            GuiNewChat.drawRect(2, -j3, 1, -j3 - k1, 0xCCCCCC + (k3 << 24));
                        } else if (((Boolean)Chat.bgshadow.getValue()).booleanValue()) {
                            GuiNewChat.drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                            GuiNewChat.drawRect(2, -j3, 1, -j3 - k1, 0xCCCCCC + (k3 << 24));
                        }
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }

    public int drawString(String text, float x, float y, int color) {
        if (Chat.font.is("Normal")) {
            return Minecraft.fontRendererObj.drawStringWithShadow(text, x, y, color);
        }
        if (Chat.font.is("Crack")) {
            return this.mc.fontRendererCrack.drawStringWithShadow(text, x, y, color);
        }
        return 0;
    }

    public void clearChatMessages() {
        this.drawnChatLines.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }

    public void printChatMessage(IChatComponent chatComponent) {
        this.printChatMessageWithOptionalDeletion(chatComponent, 0);
    }

    public void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId) {
        EventChat e = new EventChat(chatComponent.getUnformattedText(), chatComponent, this.drawnChatLines);
        EventManager.call(e);
        if (e.isCancelled()) {
            return;
        }
        if (Chat.animations.is("HeightSmooth")) {
            percentComplete = 0.0f;
        }
        this.setChatLine(chatComponent, chatLineId, this.mc.ingameGUI.getUpdateCounter(), false);
        logger.info("[CHAT] " + chatComponent.getUnformattedText());
    }

    private void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }
        int i = MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale());
        List<IChatComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, Minecraft.fontRendererObj, false, false);
        boolean flag = this.getChatOpen();
        if (Chat.animations.is("HeightSmooth")) {
            newLines = list.size() - 1;
        }
        for (IChatComponent ichatcomponent : list) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }
            this.drawnChatLines.add(0, new ChatLine(updateCounter, ichatcomponent, chatLineId));
        }
        while (this.drawnChatLines.size() > 100) {
            this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
        }
        if (!displayOnly) {
            this.chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));
            while (this.chatLines.size() > 100) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

    public void refreshChat() {
        this.drawnChatLines.clear();
        this.resetScroll();
        for (int i = this.chatLines.size() - 1; i >= 0; --i) {
            ChatLine chatline = this.chatLines.get(i);
            this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }
    }

    public List<String> getSentMessages() {
        return this.sentMessages;
    }

    public void addToSentMessages(String message) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(message)) {
            this.sentMessages.add(message);
        }
    }

    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    public void scroll(int amount) {
        this.scrollPos += amount;
        int i = this.drawnChatLines.size();
        if (this.scrollPos > i - this.getLineCount()) {
            this.scrollPos = i - this.getLineCount();
        }
        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }

    public IChatComponent getChatComponent(int mouseX, int mouseY) {
        if (!this.getChatOpen()) {
            return null;
        }
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int i = scaledresolution.getScaleFactor();
        float f = this.getChatScale();
        int j = mouseX / i - 3;
        int k = mouseY / i - 27;
        j = MathHelper.floor_float((float)j / f);
        k = MathHelper.floor_float((float)k / f);
        if (j >= 0 && k >= 0) {
            int l = Math.min(this.getLineCount(), this.drawnChatLines.size());
            if (j <= MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale())) {
                if (k < Minecraft.fontRendererObj.FONT_HEIGHT * l + l) {
                    int i1 = k / Minecraft.fontRendererObj.FONT_HEIGHT + this.scrollPos;
                    if (i1 >= 0 && i1 < this.drawnChatLines.size()) {
                        ChatLine chatline = this.drawnChatLines.get(i1);
                        int j1 = 0;
                        for (IChatComponent ichatcomponent : chatline.getChatComponent()) {
                            if (!(ichatcomponent instanceof ChatComponentText)) continue;
                            if ((j1 += Minecraft.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)ichatcomponent).getChatComponentText_TextValue(), false))) <= j) continue;
                            return ichatcomponent;
                        }
                    }
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    public boolean getChatOpen() {
        return this.mc.currentScreen instanceof GuiChat;
    }

    public void deleteChatLine(int id) {
        Iterator<ChatLine> iterator = this.drawnChatLines.iterator();
        while (iterator.hasNext()) {
            ChatLine chatline = iterator.next();
            if (chatline.getChatLineID() != id) continue;
            iterator.remove();
        }
        iterator = this.chatLines.iterator();
        while (iterator.hasNext()) {
            ChatLine chatline1 = iterator.next();
            if (chatline1.getChatLineID() != id) continue;
            iterator.remove();
            break;
        }
    }

    public int getChatWidth() {
        return GuiNewChat.calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }

    public int getChatHeight() {
        return GuiNewChat.calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }

    public static int calculateChatboxWidth(float scale) {
        int i = 320;
        int j = 40;
        return MathHelper.floor_float(scale * (float)(i - j) + (float)j);
    }

    public static int calculateChatboxHeight(float scale) {
        int i = 180;
        int j = 20;
        return MathHelper.floor_float(scale * (float)(i - j) + (float)j);
    }

    public int getLineCount() {
        return this.getChatHeight() / 9;
    }

    static {
        prevMillis = -1L;
    }
}

