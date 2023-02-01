/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.gui;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import heaven.main.Client;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.management.EventManager;
import heaven.main.module.modules.globals.AnimatedView;
import heaven.main.module.modules.render.Crosshair;
import heaven.main.module.modules.render.HUD;
import heaven.main.module.modules.render.NoPumpkinHead;
import heaven.main.module.modules.render.SetScoreboard;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.ui.gui.hud.notification.ModNotification;
import heaven.main.utils.TranslateUtil;
import heaven.main.utils.render.color.ColorManager;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.GuiStreamIndicator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.border.WorldBorder;
import net.optifine.CustomColors;

public class GuiIngame
extends Gui {
    private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
    private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    private final Random rand = new Random();
    private final Minecraft mc;
    private final RenderItem itemRenderer;
    private final GuiNewChat persistantChatGUI;
    private final GuiStreamIndicator streamIndicator;
    private int updateCounter;
    private String recordPlaying = "";
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    public float prevVignetteBrightness = 1.0f;
    private int remainingHighlightTicks;
    private ItemStack highlightingItemStack;
    private final GuiOverlayDebug overlayDebug;
    private final GuiSpectator spectatorGui;
    private final GuiPlayerTabOverlay overlayPlayerList;
    private int titlesTimer;
    private String displayedTitle = "";
    private String displayedSubTitle = "";
    private int titleFadeIn;
    private int titleDisplayTime;
    private int titleFadeOut;
    private int playerHealth;
    private int lastPlayerHealth;
    private long lastSystemTime;
    private long healthUpdateCounter;
    private float lastX;
    private float lastY;
    private final TranslateUtil translate = new TranslateUtil(0.0f, 0.0f);

    public GuiIngame(Minecraft mcIn) {
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(mcIn);
        this.spectatorGui = new GuiSpectator(mcIn);
        this.persistantChatGUI = new GuiNewChat(mcIn);
        this.streamIndicator = new GuiStreamIndicator(mcIn);
        this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
        this.setDefaultTitlesTimes();
    }

    public void setDefaultTitlesTimes() {
        this.titleFadeIn = 10;
        this.titleDisplayTime = 70;
        this.titleFadeOut = 20;
    }

    /*
     * Unable to fully structure code
     */
    public void renderGameOverlay(float partialTicks) {
        module = (AnimatedView)Client.instance.getModuleManager().getModuleByClass(AnimatedView.class);
        currentX = Minecraft.thePlayer.rotationYaw;
        currentY = Minecraft.thePlayer.rotationPitch;
        curX = 0.0f;
        curY = 0.0f;
        diffX = currentX - this.lastX;
        diffY = currentY - this.lastY;
        this.translate.interpolate(diffX * 10.0f, diffY * 10.0f, 0.1f);
        if (module.isEnabled()) {
            curX = this.translate.getX();
            curY = this.translate.getY();
        }
        scaledresolution = new ScaledResolution(this.mc);
        i = scaledresolution.getScaledWidth();
        j = scaledresolution.getScaledHeight();
        this.mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();
        if (Config.isVignetteEnabled()) {
            this.renderVignette(Minecraft.thePlayer.getBrightness(partialTicks), scaledresolution);
        } else {
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
        itemstack = Minecraft.thePlayer.inventory.armorItemInSlot(3);
        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            this.renderPumpkinOverlay(scaledresolution);
        }
        if (!Minecraft.thePlayer.isPotionActive(Potion.confusion)) {
            f = Minecraft.thePlayer.prevTimeInPortal + (Minecraft.thePlayer.timeInPortal - Minecraft.thePlayer.prevTimeInPortal) * partialTicks;
            if (f > 0.0f) {
                this.renderPortal(f, scaledresolution);
            }
        }
        GlStateManager.translate(-curX, -curY, 0.0f);
        if (Minecraft.playerController.isSpectator()) {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        } else {
            this.renderTooltip(scaledresolution, partialTicks);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        v0 = this;
        v0.mc.getTextureManager().bindTexture(GuiIngame.icons);
        GlStateManager.translate(curX, curY, 0.0f);
        GlStateManager.enableBlend();
        if (this.showCrosshair()) {
            GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
        }
        GlStateManager.translate(-curX, -curY, 0.0f);
        GlStateManager.enableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        this.mc.mcProfiler.startSection("bossHealth");
        this.renderBossHealth();
        this.mc.mcProfiler.endSection();
        if (Minecraft.playerController.shouldDrawHUD()) {
            this.renderPlayerStats(scaledresolution);
        }
        GlStateManager.disableBlend();
        if (Minecraft.thePlayer.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            j1 = Minecraft.thePlayer.getSleepTimer();
            f1 = (float)j1 / 100.0f;
            if (f1 > 1.0f) {
                f1 = 1.0f - (float)(j1 - 100) / 10.0f;
            }
            k = (int)(220.0f * f1) << 24 | 0x101020;
            GuiIngame.drawRect(0, 0, i, j, k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        k1 = i / 2 - 91;
        if (Minecraft.thePlayer.isRidingHorse()) {
            this.renderHorseJumpBar(scaledresolution, k1);
        } else if (Minecraft.playerController.gameIsSurvivalOrAdventure()) {
            this.renderExpBar(scaledresolution, k1);
        }
        if (!this.mc.gameSettings.heldItemTooltips) ** GOTO lbl-1000
        if (!Minecraft.playerController.isSpectator()) {
            this.renderSelectedItem(scaledresolution);
        } else if (Minecraft.thePlayer.isSpectator()) {
            this.spectatorGui.renderSelectedItem(scaledresolution);
        }
        if (this.mc.isDemo()) {
            this.renderDemo(scaledresolution);
        }
        if (this.mc.gameSettings.showDebugInfo) {
            this.overlayDebug.renderDebugInfo(scaledresolution);
        }
        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            f2 = (float)this.recordPlayingUpFor - partialTicks;
            l1 = (int)(f2 * 255.0f / 20.0f);
            if (l1 > 255) {
                l1 = 255;
            }
            if (l1 > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(i / 2, j - 68, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                l = 0xFFFFFF;
                if (this.recordIsPlaying) {
                    l = MathHelper.hsvToRGB(f2 / 50.0f, 0.7f, 0.6f) & 0xFFFFFF;
                }
                this.getFontRenderer().drawString(this.recordPlaying, -this.getFontRenderer().getStringWidth(this.recordPlaying) / 2, -4, l + (l1 << 24 & -16777216));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        if (this.titlesTimer > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            f3 = (float)this.titlesTimer - partialTicks;
            i2 = 255;
            if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
                f4 = (float)(this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut) - f3;
                i2 = (int)(f4 * 255.0f / (float)this.titleFadeIn);
            }
            if (this.titlesTimer <= this.titleFadeOut) {
                i2 = (int)(f3 * 255.0f / (float)this.titleFadeOut);
            }
            if ((i2 = MathHelper.clamp_int(i2, 0, 255)) > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(i / 2, j / 2, 0.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0f, 4.0f, 4.0f);
                j2 = i2 << 24 & -16777216;
                this.getFontRenderer().drawString(this.displayedTitle, -this.getFontRenderer().getStringWidth(this.displayedTitle) / 2, -10.0f, 0xFFFFFF | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                this.getFontRenderer().drawString(this.displayedSubTitle, -this.getFontRenderer().getStringWidth(this.displayedSubTitle) / 2, 5.0f, 0xFFFFFF | j2, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
            this.mc.mcProfiler.endSection();
        }
        scoreboard = Minecraft.theWorld.getScoreboard();
        scoreobjective = null;
        scoreplayerteam = scoreboard.getPlayersTeam(Minecraft.thePlayer.getName());
        if (scoreplayerteam != null && (i1 = scoreplayerteam.getChatFormat().getColorIndex()) >= 0) {
            scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
        }
        v1 = scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
        if (scoreobjective1 != null) {
            this.renderScoreboard(scoreobjective1, scaledresolution);
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, j - 48, 0.0f);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
        if (!this.mc.gameSettings.keyBindPlayerList.isKeyDown()) ** GOTO lbl-1000
        if (!this.mc.isIntegratedServerRunning()) ** GOTO lbl-1000
        if (Minecraft.thePlayer.sendQueue.getPlayerInfoMap().size() > 1 || scoreobjective1 != null) lbl-1000:
        // 2 sources

        {
            this.overlayPlayerList.updatePlayerList(true);
            this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
        } else lbl-1000:
        // 2 sources

        {
            this.overlayPlayerList.updatePlayerList(false);
        }
        EventManager.call(new EventRender2D(scaledresolution, partialTicks));
        if (((Boolean)HUD.notifications.getValue()).booleanValue()) {
            ClientNotification.drawNotifications();
        }
        if (((Boolean)HUD.modnotifications.getValue()).booleanValue()) {
            ModNotification.drawNotifications();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.translate(curX, curY, 0.0f);
        this.lastX = currentX;
        this.lastY = currentY;
    }

    protected void renderTooltip(ScaledResolution sr, float partialTicks) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GuiIngame guiIngame = this;
            guiIngame.mc.getTextureManager().bindTexture(widgetsTexPath);
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int i = sr.getScaledWidth() / 2;
            float f = zLevel;
            zLevel = -90.0f;
            this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            zLevel = f;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
            for (int j = 0; j < 9; ++j) {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityplayer);
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

    public void renderHorseJumpBar(ScaledResolution scaledRes, int x) {
        this.mc.mcProfiler.startSection("jumpBar");
        GuiIngame guiIngame = this;
        guiIngame.mc.getTextureManager().bindTexture(Gui.icons);
        float f = Minecraft.thePlayer.getHorseJumpPower();
        int i = 182;
        int j = (int)(f * (float)(i + 1));
        int k = scaledRes.getScaledHeight() - 32 + 3;
        this.drawTexturedModalRect(x, k, 0, 84, i, 5);
        if (j > 0) {
            this.drawTexturedModalRect(x, k, 0, 89, j, 5);
        }
        this.mc.mcProfiler.endSection();
    }

    public void renderExpBar(ScaledResolution scaledRes, int x) {
        this.mc.mcProfiler.startSection("expBar");
        GuiIngame guiIngame = this;
        guiIngame.mc.getTextureManager().bindTexture(Gui.icons);
        int i = Minecraft.thePlayer.xpBarCap();
        if (i > 0) {
            int j = 182;
            int k = (int)(Minecraft.thePlayer.experience * (float)(j + 1));
            int l = scaledRes.getScaledHeight() - 32 + 3;
            this.drawTexturedModalRect(x, l, 0, 64, j, 5);
            if (k > 0) {
                this.drawTexturedModalRect(x, l, 0, 69, k, 5);
            }
        }
        this.mc.mcProfiler.endSection();
        if (Minecraft.thePlayer.experienceLevel > 0) {
            this.mc.mcProfiler.startSection("expLevel");
            int k1 = 8453920;
            if (Config.isCustomColors()) {
                k1 = CustomColors.getExpBarTextColor(k1);
            }
            String s = String.valueOf(Minecraft.thePlayer.experienceLevel);
            int l1 = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int i1 = scaledRes.getScaledHeight() - 31 - 4;
            this.getFontRenderer().drawString(s, l1 + 1, i1, 0);
            this.getFontRenderer().drawString(s, l1 - 1, i1, 0);
            this.getFontRenderer().drawString(s, l1, i1 + 1, 0);
            this.getFontRenderer().drawString(s, l1, i1 - 1, 0);
            this.getFontRenderer().drawString(s, l1, i1, k1);
            this.mc.mcProfiler.endSection();
        }
    }

    public void renderSelectedItem(ScaledResolution scaledRes) {
        this.mc.mcProfiler.startSection("selectedItemName");
        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            int k;
            String s = this.highlightingItemStack.getDisplayName();
            if (this.highlightingItemStack.hasDisplayName()) {
                s = (Object)((Object)EnumChatFormatting.ITALIC) + s;
            }
            int i = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
            int j = scaledRes.getScaledHeight() - 59;
            if (!Minecraft.playerController.shouldDrawHUD()) {
                j += 14;
            }
            if ((k = (int)((float)this.remainingHighlightTicks * 256.0f / 10.0f)) > 255) {
                k = 255;
            }
            if (k > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.getFontRenderer().drawStringWithShadow(s, i, j, 0xFFFFFF + (k << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
        this.mc.mcProfiler.endSection();
    }

    public void renderDemo(ScaledResolution scaledRes) {
        String s;
        this.mc.mcProfiler.startSection("demo");
        if (Minecraft.theWorld.getTotalWorldTime() >= 120500L) {
            s = I18n.format("demo.demoExpired", new Object[0]);
        } else {
            Object[] objectArray = new Object[1];
            objectArray[0] = StringUtils.ticksToElapsedTime((int)(120500L - Minecraft.theWorld.getTotalWorldTime()));
            s = I18n.format("demo.remainingTime", objectArray);
        }
        int i = this.getFontRenderer().getStringWidth(s);
        this.getFontRenderer().drawStringWithShadow(s, scaledRes.getScaledWidth() - i - 10, 5.0f, 0xFFFFFF);
        this.mc.mcProfiler.endSection();
    }

    protected boolean showCrosshair() {
        block8: {
            block7: {
                block6: {
                    if (!this.mc.gameSettings.showDebugInfo) break block6;
                    if (!Minecraft.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) break block7;
                }
                if (!Client.instance.getModuleManager().getModuleByClass(Crosshair.class).isEnabled()) break block8;
            }
            return false;
        }
        if (Minecraft.playerController.isSpectator()) {
            if (this.mc.pointedEntity != null) {
                return true;
            }
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
                return Minecraft.theWorld.getTileEntity(blockpos) instanceof IInventory;
            }
            return false;
        }
        return true;
    }

    public void renderStreamIndicator(ScaledResolution scaledRes) {
        this.streamIndicator.render(scaledRes.getScaledWidth() - 10, 10);
    }

    private void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes) {
        int[] counter = new int[1];
        Scoreboard scoreboard = objective.getScoreboard();
        List<Object> collection = scoreboard.getSortedScores(objective);
        List list = collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null && (p_apply_1_.getPlayerName().isEmpty() || p_apply_1_.getPlayerName().charAt(0) != '#')).collect(Collectors.toList());
        collection = list.size() > 15 ? Lists.newArrayList((Iterable)Iterables.skip(list, (int)(collection.size() - 15))) : list;
        int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());
        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + (Object)((Object)EnumChatFormatting.RED) + score.getScorePoints();
            i = Math.max(i, this.getFontRenderer().getStringWidth(s));
        }
        int i1 = collection.size() * this.getFontRenderer().FONT_HEIGHT;
        int j1 = scaledRes.getScaledHeight() / 2 + i1 / 3;
        int k1 = 3;
        int l1 = scaledRes.getScaledWidth() - i - k1;
        int j = 0;
        for (Score score1 : collection) {
            ++j;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = (Object)((Object)EnumChatFormatting.RED) + String.valueOf(score1.getScorePoints());
            if (!Client.instance.getModuleManager().getModuleByClass(SetScoreboard.class).isEnabled()) continue;
            int k = j1 - j * this.getFontRenderer().FONT_HEIGHT;
            int l = ScaledResolution.getScaledWidth() - k1 + 2;
            if (!((Boolean)SetScoreboard.fastbord.getValue()).booleanValue()) {
                GuiIngame.drawRect(l1 - 2 - ((Double)SetScoreboard.X.getValue()).intValue(), k + ((Double)SetScoreboard.Y.getValue()).intValue(), l - ((Double)SetScoreboard.X.getValue()).intValue(), k + this.getFontRenderer().FONT_HEIGHT + ((Double)SetScoreboard.Y.getValue()).intValue(), 0x50000000);
            }
            if (!(s1.contains("ZQAT.top") || s1.contains("mushmc.com") || s1.contains("mc110.net") || s1.contains("redesky.com") || s1.toLowerCase().contains("pixel") || s1.contains("Mc986") || s1.contains("loyisa.cn"))) {
                this.getFontRenderer().drawString(s1, l1 - ((Double)SetScoreboard.X.getValue()).intValue(), k + ((Double)SetScoreboard.Y.getValue()).intValue(), 0x20FFFFFF);
            } else {
                char[] charArray = "bluelun.cc".toCharArray();
                int length = 0;
                for (char charIndex : charArray) {
                    if (!((Boolean)HUD.Breathinglamp.getValue()).booleanValue()) {
                        this.mc.fontRendererCrack.drawStringWithShadow(String.valueOf(charIndex), l1 - ((Double)SetScoreboard.X.getValue()).intValue() + length, k + ((Double)SetScoreboard.Y.getValue()).intValue(), (Boolean)HUD.rainbow.getValue() != false ? HUD.rainbow() : ColorManager.HUDColor());
                    } else {
                        Color Ranbow = ColorManager.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 65, length + 39);
                        this.mc.fontRendererCrack.drawStringWithShadow(String.valueOf(charIndex), l1 - ((Double)SetScoreboard.X.getValue()).intValue() + length, k + ((Double)SetScoreboard.Y.getValue()).intValue(), (Boolean)HUD.rainbow.getValue() != false ? HUD.rainbow() : Ranbow.getRGB());
                    }
                    length += this.mc.fontRendererCrack.getCharWidth(charIndex);
                    counter[0] = counter[0] + 1;
                }
            }
            this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2) - ((Double)SetScoreboard.X.getValue()).intValue(), k + ((Double)SetScoreboard.Y.getValue()).intValue(), 0x20FFFFFF);
            if (j != collection.size()) continue;
            String s3 = objective.getDisplayName();
            if (!((Boolean)SetScoreboard.fastbord.getValue()).booleanValue()) {
                GuiIngame.drawRect(l1 - 2 - ((Double)SetScoreboard.X.getValue()).intValue(), k - this.getFontRenderer().FONT_HEIGHT - 1 + ((Double)SetScoreboard.Y.getValue()).intValue(), l - ((Double)SetScoreboard.X.getValue()).intValue(), k - 1 + ((Double)SetScoreboard.Y.getValue()).intValue(), 0x60000000);
                GuiIngame.drawRect(l1 - 2 - ((Double)SetScoreboard.X.getValue()).intValue(), k - 1 + ((Double)SetScoreboard.Y.getValue()).intValue(), l - ((Double)SetScoreboard.X.getValue()).intValue(), k + ((Double)SetScoreboard.Y.getValue()).intValue(), 0x50000000);
            }
            if (((Boolean)SetScoreboard.noServername.getValue()).booleanValue()) continue;
            this.getFontRenderer().drawString(s3, l1 + i / 2 - this.getFontRenderer().getStringWidth(s3) / 2 - ((Double)SetScoreboard.X.getValue()).intValue(), k - this.getFontRenderer().FONT_HEIGHT + ((Double)SetScoreboard.Y.getValue()).intValue(), 0x20FFFFFF);
        }
    }

    private void renderPlayerStats(ScaledResolution scaledRes) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            boolean flag;
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
            boolean bl = flag = this.healthUpdateCounter > (long)this.updateCounter && (this.healthUpdateCounter - (long)this.updateCounter) / 3L % 2L == 1L;
            if (i < this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 20;
            } else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = this.updateCounter + 10;
            }
            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
                this.playerHealth = i;
                this.lastPlayerHealth = i;
                this.lastSystemTime = Minecraft.getSystemTime();
            }
            this.playerHealth = i;
            int j = this.lastPlayerHealth;
            this.rand.setSeed((long)this.updateCounter * 312871L);
            FoodStats foodstats = entityplayer.getFoodStats();
            int k = foodstats.getFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int i1 = scaledRes.getScaledWidth() / 2 - 91;
            int j1 = scaledRes.getScaledWidth() / 2 + 91;
            int k1 = scaledRes.getScaledHeight() - 39;
            float f = (float)iattributeinstance.getAttributeValue();
            float f1 = entityplayer.getAbsorptionAmount();
            int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0f / 10.0f);
            int i2 = Math.max(10 - (l1 - 2), 3);
            int j2 = k1 - (l1 - 1) * i2 - 10;
            float f2 = f1;
            int k2 = entityplayer.getTotalArmorValue();
            int l2 = -1;
            if (entityplayer.isPotionActive(Potion.regeneration)) {
                l2 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0f);
            }
            this.mc.mcProfiler.startSection("armor");
            for (int i3 = 0; i3 < 10; ++i3) {
                if (k2 <= 0) continue;
                int j3 = i1 + (i3 << 3);
                if ((i3 << 1) + 1 < k2) {
                    this.drawTexturedModalRect(j3, j2, 34, 9, 9, 9);
                }
                if ((i3 << 1) + 1 == k2) {
                    this.drawTexturedModalRect(j3, j2, 25, 9, 9, 9);
                }
                if ((i3 << 1) + 1 <= k2) continue;
                this.drawTexturedModalRect(j3, j2, 16, 9, 9, 9);
            }
            this.mc.mcProfiler.endStartSection("health");
            for (int i6 = MathHelper.ceiling_float_int((f + f1) / 2.0f) - 1; i6 >= 0; --i6) {
                int j6 = 16;
                if (entityplayer.isPotionActive(Potion.poison)) {
                    j6 += 36;
                } else if (entityplayer.isPotionActive(Potion.wither)) {
                    j6 += 72;
                }
                int k3 = 0;
                if (flag) {
                    k3 = 1;
                }
                int l3 = MathHelper.ceiling_float_int((float)(i6 + 1) / 10.0f) - 1;
                int i4 = i1 + (i6 % 10 << 3);
                int j4 = k1 - l3 * i2;
                if (i <= 4) {
                    j4 += this.rand.nextInt(2);
                }
                if (i6 == l2) {
                    j4 -= 2;
                }
                int k4 = 0;
                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
                    k4 = 5;
                }
                this.drawTexturedModalRect(i4, j4, 16 + k3 * 9, 9 * k4, 9, 9);
                if (flag) {
                    if ((i6 << 1) + 1 < j) {
                        this.drawTexturedModalRect(i4, j4, j6 + 54, 9 * k4, 9, 9);
                    }
                    if ((i6 << 1) + 1 == j) {
                        this.drawTexturedModalRect(i4, j4, j6 + 63, 9 * k4, 9, 9);
                    }
                }
                if (f2 <= 0.0f) {
                    if ((i6 << 1) + 1 < i) {
                        this.drawTexturedModalRect(i4, j4, j6 + 36, 9 * k4, 9, 9);
                    }
                    if ((i6 << 1) + 1 != i) continue;
                    this.drawTexturedModalRect(i4, j4, j6 + 45, 9 * k4, 9, 9);
                    continue;
                }
                if (f2 == f1 && f1 % 2.0f == 1.0f) {
                    this.drawTexturedModalRect(i4, j4, j6 + 153, 9 * k4, 9, 9);
                } else {
                    this.drawTexturedModalRect(i4, j4, j6 + 144, 9 * k4, 9, 9);
                }
                f2 -= 2.0f;
            }
            Entity entity = entityplayer.ridingEntity;
            if (entity == null) {
                this.mc.mcProfiler.endStartSection("food");
                for (int k6 = 0; k6 < 10; ++k6) {
                    int j7 = k1;
                    int l7 = 16;
                    int k8 = 0;
                    if (entityplayer.isPotionActive(Potion.hunger)) {
                        l7 += 36;
                        k8 = 13;
                    }
                    if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0f && this.updateCounter % (k * 3 + 1) == 0) {
                        j7 = k1 + (this.rand.nextInt(3) - 1);
                    }
                    int j9 = j1 - (k6 << 3) - 9;
                    this.drawTexturedModalRect(j9, j7, 16 + k8 * 9, 27, 9, 9);
                    if ((k6 << 1) + 1 < k) {
                        this.drawTexturedModalRect(j9, j7, l7 + 36, 27, 9, 9);
                    }
                    if ((k6 << 1) + 1 != k) continue;
                    this.drawTexturedModalRect(j9, j7, l7 + 45, 27, 9, 9);
                }
            } else if (entity instanceof EntityLivingBase) {
                this.mc.mcProfiler.endStartSection("mountHealth");
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                int i7 = (int)Math.ceil(entitylivingbase.getHealth());
                float f3 = entitylivingbase.getMaxHealth();
                int j8 = (int)(f3 + 0.5f) / 2;
                if (j8 > 30) {
                    j8 = 30;
                }
                int i9 = k1;
                int k9 = 0;
                while (j8 > 0) {
                    int l4 = Math.min(j8, 10);
                    j8 -= l4;
                    for (int i5 = 0; i5 < l4; ++i5) {
                        int j5 = 52;
                        int l5 = j1 - (i5 << 3) - 9;
                        this.drawTexturedModalRect(l5, i9, 0, 9, 9, 9);
                        if ((i5 << 1) + 1 + k9 < i7) {
                            this.drawTexturedModalRect(l5, i9, j5 + 36, 9, 9, 9);
                        }
                        if ((i5 << 1) + 1 + k9 != i7) continue;
                        this.drawTexturedModalRect(l5, i9, j5 + 45, 9, 9, 9);
                    }
                    i9 -= 10;
                    k9 += 20;
                }
            }
            this.mc.mcProfiler.endStartSection("air");
            if (entityplayer.isInsideOfMaterial(Material.water)) {
                int l6 = Minecraft.thePlayer.getAir();
                int k7 = MathHelper.ceiling_double_int((double)(l6 - 2) * 10.0 / 300.0);
                int i8 = MathHelper.ceiling_double_int((double)l6 * 10.0 / 300.0) - k7;
                for (int l8 = 0; l8 < k7 + i8; ++l8) {
                    if (l8 < k7) {
                        this.drawTexturedModalRect(j1 - (l8 << 3) - 9, j2, 16, 18, 9, 9);
                        continue;
                    }
                    this.drawTexturedModalRect(j1 - (l8 << 3) - 9, j2, 25, 18, 9, 9);
                }
            }
            this.mc.mcProfiler.endSection();
        }
    }

    private void renderBossHealth() {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
            --BossStatus.statusBarTime;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i = scaledresolution.getScaledWidth();
            int j = 182;
            int k = i / 2 - j / 2;
            int l = (int)(BossStatus.healthScale * (float)(j + 1));
            int i1 = 12;
            this.drawTexturedModalRect(k, i1, 0, 74, j, 5);
            this.drawTexturedModalRect(k, i1, 0, 74, j, 5);
            if (l > 0) {
                this.drawTexturedModalRect(k, i1, 0, 79, l, 5);
            }
            String s = BossStatus.bossName;
            this.getFontRenderer().drawStringWithShadow(s, i / 2 - this.getFontRenderer().getStringWidth(s) / 2, i1 - 10, 0xFFFFFF);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GuiIngame guiIngame = this;
            guiIngame.mc.getTextureManager().bindTexture(icons);
        }
    }

    private void renderPumpkinOverlay(ScaledResolution scaledRes) {
        if (Client.instance.getModuleManager().getModuleByClass(NoPumpkinHead.class).isEnabled()) {
            return;
        }
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableAlpha();
        GuiIngame guiIngame = this;
        guiIngame.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderVignette(float lightLevel, ScaledResolution scaledRes) {
        if (!Config.isVignetteEnabled()) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        } else {
            lightLevel = 1.0f - lightLevel;
            lightLevel = MathHelper.clamp_float(lightLevel, 0.0f, 1.0f);
            WorldBorder worldborder = Minecraft.theWorld.getWorldBorder();
            float f = (float)worldborder.getClosestDistance(Minecraft.thePlayer);
            double d0 = Math.min(worldborder.getResizeSpeed() * (double)worldborder.getWarningTime() * 1000.0, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
            double d1 = Math.max((double)worldborder.getWarningDistance(), d0);
            f = (double)f < d1 ? 1.0f - (float)((double)f / d1) : 0.0f;
            this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(lightLevel - this.prevVignetteBrightness) * 0.01);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
            if (f > 0.0f) {
                GlStateManager.color(0.0f, f, f, 1.0f);
            } else {
                GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0f);
            }
            GuiIngame guiIngame = this;
            guiIngame.mc.getTextureManager().bindTexture(vignetteTexPath);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
            worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
            worldrenderer.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
            worldrenderer.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
    }

    private void renderPortal(float timeInPortal, ScaledResolution scaledRes) {
        if (timeInPortal < 1.0f) {
            timeInPortal *= timeInPortal;
            timeInPortal *= timeInPortal;
            timeInPortal = timeInPortal * 0.8f + 0.2f;
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, timeInPortal);
        GuiIngame guiIngame = this;
        guiIngame.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
        float f = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMinV();
        float f2 = textureatlassprite.getMaxU();
        float f3 = textureatlassprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(f, f3).endVertex();
        worldrenderer.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(f2, f3).endVertex();
        worldrenderer.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(f2, f1).endVertex();
        worldrenderer.pos(0.0, 0.0, -90.0).tex(f, f1).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player) {
        ItemStack itemstack = player.inventory.mainInventory[index];
        if (itemstack != null) {
            float f = (float)itemstack.animationsToGo - partialTicks;
            if (f > 0.0f) {
                GlStateManager.pushMatrix();
                float f1 = 1.0f + f / 5.0f;
                GlStateManager.translate(xPos + 8, yPos + 12, 0.0f);
                GlStateManager.scale(1.0f / f1, (f1 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate(-(xPos + 8), -(yPos + 12), 0.0f);
            }
            this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);
            if (f > 0.0f) {
                GlStateManager.popMatrix();
            }
            this.itemRenderer.renderItemOverlays(Minecraft.fontRendererObj, itemstack, xPos, yPos);
        }
    }

    public void updateTick() {
        if (this.recordPlayingUpFor > 0) {
            --this.recordPlayingUpFor;
        }
        if (this.titlesTimer > 0) {
            --this.titlesTimer;
            if (this.titlesTimer <= 0) {
                this.displayedTitle = "";
                this.displayedSubTitle = "";
            }
        }
        ++this.updateCounter;
        if (Minecraft.thePlayer != null) {
            ItemStack itemstack = Minecraft.thePlayer.inventory.getCurrentItem();
            if (itemstack == null) {
                this.remainingHighlightTicks = 0;
            } else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
                if (this.remainingHighlightTicks > 0) {
                    --this.remainingHighlightTicks;
                }
            } else {
                this.remainingHighlightTicks = 40;
            }
            this.highlightingItemStack = itemstack;
        }
    }

    public void setRecordPlayingMessage(String recordName) {
        this.setRecordPlaying(I18n.format("record.nowPlaying", recordName), true);
    }

    public void setRecordPlaying(String message, boolean isPlaying) {
        this.recordPlaying = message;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = isPlaying;
    }

    public void displayTitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut) {
        if (title == null && subTitle == null && timeFadeIn < 0 && displayTime < 0 && timeFadeOut < 0) {
            this.displayedTitle = "";
            this.displayedSubTitle = "";
            this.titlesTimer = 0;
        } else if (title != null) {
            this.displayedTitle = title;
            this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
        } else if (subTitle != null) {
            this.displayedSubTitle = subTitle;
        } else {
            if (timeFadeIn >= 0) {
                this.titleFadeIn = timeFadeIn;
            }
            if (displayTime >= 0) {
                this.titleDisplayTime = displayTime;
            }
            if (timeFadeOut >= 0) {
                this.titleFadeOut = timeFadeOut;
            }
            if (this.titlesTimer > 0) {
                this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
            }
        }
    }

    public void setRecordPlaying(IChatComponent component, boolean isPlaying) {
        this.setRecordPlaying(component.getUnformattedText(), isPlaying);
    }

    public GuiNewChat getChatGUI() {
        return this.persistantChatGUI;
    }

    public int getUpdateCounter() {
        return this.updateCounter;
    }

    public FontRenderer getFontRenderer() {
        return Minecraft.fontRendererObj;
    }

    public GuiSpectator getSpectatorGui() {
        return this.spectatorGui;
    }

    public GuiPlayerTabOverlay getTabList() {
        return this.overlayPlayerList;
    }

    public void resetPlayersOverlayFooterHeader() {
        this.overlayPlayerList.resetFooterHeader();
    }
}

