/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.commons.io.Charsets
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiWinGame
extends GuiScreen {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
    private int field_146581_h;
    private List<String> field_146582_i;
    private int field_146579_r;
    private final float field_146578_s = 0.5f;

    @Override
    public void updateScreen() {
        MusicTicker musicticker = this.mc.getMusicTicker();
        SoundHandler soundhandler = this.mc.getSoundHandler();
        if (this.field_146581_h == 0) {
            musicticker.func_181557_a();
            musicticker.func_181558_a(MusicTicker.MusicType.CREDITS);
            soundhandler.resumeSounds();
        }
        soundhandler.update();
        ++this.field_146581_h;
        float f = (float)(this.field_146579_r + height + height + 24) / this.field_146578_s;
        if ((float)this.field_146581_h > f) {
            this.sendRespawnPacket();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            this.sendRespawnPacket();
        }
    }

    private void sendRespawnPacket() {
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
        this.mc.displayGuiScreen(null);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public void initGui() {
        if (this.field_146582_i == null) {
            this.field_146582_i = Lists.newArrayList();
            try {
                String s = "";
                String s1 = String.valueOf((Object)EnumChatFormatting.WHITE) + (Object)((Object)EnumChatFormatting.OBFUSCATED) + (Object)((Object)EnumChatFormatting.GREEN) + (Object)((Object)EnumChatFormatting.AQUA);
                int i = 274;
                InputStream inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream();
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
                Random random = new Random(8124371L);
                while ((s = bufferedreader.readLine()) != null) {
                    s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    while (s.contains(s1)) {
                        int j = s.indexOf(s1);
                        String s2 = s.substring(0, j);
                        String s3 = s.substring(j + s1.length());
                        s = s2 + (Object)((Object)EnumChatFormatting.WHITE) + (Object)((Object)EnumChatFormatting.OBFUSCATED) + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3;
                    }
                    this.field_146582_i.addAll(Minecraft.fontRendererObj.listFormattedStringToWidth(s, i));
                    this.field_146582_i.add("");
                }
                inputstream.close();
                for (int k = 0; k < 8; ++k) {
                    this.field_146582_i.add("");
                }
                inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
                bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
                while ((s = bufferedreader.readLine()) != null) {
                    s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    s = s.replaceAll("\t", "    ");
                    this.field_146582_i.addAll(Minecraft.fontRendererObj.listFormattedStringToWidth(s, i));
                    this.field_146582_i.add("");
                }
                inputstream.close();
                this.field_146579_r = this.field_146582_i.size() * 12;
            }
            catch (Exception exception) {
                logger.error("Couldn't load credits", (Throwable)exception);
            }
        }
    }

    private void drawWinGameScreen(int p_146575_1_, int p_146575_2_, float p_146575_3_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GuiWinGame guiWinGame = this;
        guiWinGame.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        int i = width;
        float f = 0.0f - ((float)this.field_146581_h + p_146575_3_) * 0.5f * this.field_146578_s;
        float f1 = (float)height - ((float)this.field_146581_h + p_146575_3_) * 0.5f * this.field_146578_s;
        float f2 = 0.015625f;
        float f3 = ((float)this.field_146581_h + p_146575_3_ - 0.0f) * 0.02f;
        float f4 = (float)(this.field_146579_r + height + height + 24) / this.field_146578_s;
        float f5 = (f4 - 20.0f - ((float)this.field_146581_h + p_146575_3_)) * 0.005f;
        if (f5 < f3) {
            f3 = f5;
        }
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        f3 *= f3;
        f3 = f3 * 96.0f / 255.0f;
        worldrenderer.pos(0.0, height, zLevel).tex(0.0, f * f2).color(f3, f3, f3, 1.0f).endVertex();
        worldrenderer.pos(i, height, zLevel).tex((float)i * f2, f * f2).color(f3, f3, f3, 1.0f).endVertex();
        worldrenderer.pos(i, 0.0, zLevel).tex((float)i * f2, f1 * f2).color(f3, f3, f3, 1.0f).endVertex();
        worldrenderer.pos(0.0, 0.0, zLevel).tex(0.0, f1 * f2).color(f3, f3, f3, 1.0f).endVertex();
        tessellator.draw();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawWinGameScreen(mouseX, mouseY, partialTicks);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 274;
        int j = width / 2 - i / 2;
        int k = height + 50;
        float f = -((float)this.field_146581_h + partialTicks) * this.field_146578_s;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, f, 0.0f);
        GuiWinGame guiWinGame = this;
        guiWinGame.mc.getTextureManager().bindTexture(MINECRAFT_LOGO);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(j, k, 0, 0, 155, 44);
        this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
        int l = k + 200;
        for (int i1 = 0; i1 < this.field_146582_i.size(); ++i1) {
            if (i1 == this.field_146582_i.size() - 1) {
                float f1 = (float)l + f - (float)(height / 2 - 6);
                if (f1 < 0.0f) {
                    GlStateManager.translate(0.0f, -f1, 0.0f);
                }
            }
            if ((float)l + f + 12.0f + 8.0f > 0.0f) {
                if ((float)l + f < (float)height) {
                    String s = this.field_146582_i.get(i1);
                    if (s.startsWith("[C]")) {
                        this.fontRendererObj.drawStringWithShadow(s.substring(3), j + (i - this.fontRendererObj.getStringWidth(s.substring(3))) / 2, l, 0xFFFFFF);
                    } else {
                        this.fontRendererObj.fontRandom.setSeed((long)i1 * 4238972211L + (long)(this.field_146581_h / 4));
                        this.fontRendererObj.drawStringWithShadow(s, j, l, 0xFFFFFF);
                    }
                }
            }
            l += 12;
        }
        GlStateManager.popMatrix();
        GuiWinGame guiWinGame2 = this;
        guiWinGame2.mc.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0, 769);
        int j1 = width;
        int k1 = height;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, k1, zLevel).tex(0.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(j1, k1, zLevel).tex(1.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(j1, 0.0, zLevel).tex(1.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(0.0, 0.0, zLevel).tex(0.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

