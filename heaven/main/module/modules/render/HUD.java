/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.ModuleIndicator;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.hud.notification.ModNotification;
import heaven.main.ui.gui.login.GuiLogin;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.render.RenderUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class HUD
extends Module {
    public final String[] HUDMode = new String[]{"Simple", "HLine", "HNoLine", "Rect", "Rect2", "OutLine", "Side", "ExtendSide"};
    public static final String[] Fonts = new String[]{"Regular"};
    public static final String[] rainbows = new String[]{"Client", "Astolfo", "Sakura", "Chemical", "Mexico", "Neon", "Dark", "Random"};
    public static final String[] tagsmodes = new String[]{"Simple", "Box", "Hyphen", "VerticalBar", "XML", "Brackets", "Binds"};
    private static final String[] marks = new String[]{"Text", "Fade", "Sense", "Weave"};
    private static final String[] tabs = new String[]{"Normal", "Side"};
    private final String[] shadows = new String[]{"Simple", "Sideways"};
    private static final Option<Boolean> list = new Option<Boolean>("ArrayList", true);
    public final Numbers<Double> alpha = new Numbers<Double>("BackgroundAlpha", 120.0, 0.0, 255.0, 5.0, list::getValue);
    public static final Option<Boolean> rainbow = new Option<Boolean>("Rainbow", true, list::getValue);
    public static final Mode<String> rainbowmode = new Mode("RainbowMode", rainbows, rainbows[0], rainbow::getValue);
    public static final Numbers<Double> r = new Numbers<Double>("Red", 255.0, 0.0, 255.0, 5.0);
    public static final Numbers<Double> g = new Numbers<Double>("Green", 255.0, 0.0, 255.0, 5.0);
    public static final Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0);
    private final Mode<String> shadowmode = new Mode("ArrayShadow", this.shadows, this.shadows[0], list::getValue);
    private final Mode<String> mode = new Mode("ArrayList", this.HUDMode, this.HUDMode[0], list::getValue);
    public static final Numbers<Double> ez = new Numbers<Double>("e", 0.0, 0.0, 1000.0, 0.01);
    public static final Numbers<Double> eez = new Numbers<Double>("ee", 0.0, 0.0, 1000.0, 0.01);
    public static final Numbers<Double> eeez = new Numbers<Double>("eee", 0.0, 0.0, 360.0, 0.01);
    public static final Numbers<Double> eeeez = new Numbers<Double>("eeee", 0.0, 0.0, 360.0, 0.01);
    public static final Numbers<Double> az = new Numbers<Double>("a", 0.0, 0.0, 255.0, 0.01);
    public static final Numbers<Double> aaz = new Numbers<Double>("aa", 0.0, 0.0, 255.0, 0.01);
    public static final Numbers<Double> aaaz = new Numbers<Double>("aaa", 0.0, 0.0, 360.0, 0.01);
    public static final Numbers<Double> aaaaz = new Numbers<Double>("aaaa", 0.0, 0.0, 360.0, 0.01);
    public static final Option<Boolean> notifications = new Option<Boolean>("Notifications", true);
    public static final Option<Boolean> modnotifications = new Option<Boolean>("ModNotifications", true);
    private final Option<Boolean> potions = new Option<Boolean>("EffectsHUD", true);
    private final Option<Boolean> armors = new Option<Boolean>("ArmorHUD", true);
    private final Option<Boolean> bindlists = new Option<Boolean>("BindsHUD", false);
    public static final Option<Boolean> Breathinglamp = new Option<Boolean>("Breathinglamp", true);
    public static final Option<Boolean> breakname = new Option<Boolean>("BreakName", false);
    private final Option<Boolean> noRenderType = new Option<Boolean>("NoRenderCategory", true);
    public static final Option<Boolean> tags = new Option<Boolean>("Suffix", true);
    public static final Option<Boolean> tagswhite = new Option<Boolean>("SuffixWhite", true, tags::getValue);
    public static final Mode<String> tagsmode = new Mode("Suffixs", tagsmodes, tagsmodes[0], tags::getValue);
    private final Option<Boolean> enableSFUI = new Option<Boolean>("EnableSFUI", false);
    private final Option<Boolean> info = new Option<Boolean>("Info", true);
    private static final Option<Boolean> mark = new Option<Boolean>("WaterMark", true);
    public static final Mode<String> watermark = new Mode("WaterMarks", marks, marks[0], mark::getValue);
    public static final Option<Boolean> sound = new Option<Boolean>("ModuleSound", true);
    public static final Option<Boolean> tabgui = new Option<Boolean>("TabGui", false);
    public static final Mode<String> tabguimode = new Mode("TabGuis", tabs, tabs[0], tabgui::getValue);
    private static int count;
    float hue;
    int ychat;
    int color;
    private CFontRenderer font;
    int fadeTicks;
    float bindsy;

    public HUD() {
        super("HUD", new String[]{"gui", "hub"}, ModuleType.Render);
        this.addValues(ez, eez, eeez, eeeez, az, aaz, aaaz, aaaaz, this.mode, rainbowmode, tabguimode, watermark, tagsmode, this.shadowmode, tags, tagswhite, this.info, breakname, this.noRenderType, list, mark, Breathinglamp, notifications, modnotifications, this.potions, this.armors, this.bindlists, rainbow, r, g, b, this.alpha, sound, tabgui, this.enableSFUI);
    }

    public float processFPS(float fps, float defF, float defV) {
        return defV / (fps / defF);
    }

    @EventHandler
    private void renderHud(EventRender2D event) {
        CFontRenderer cFontRenderer = this.font = (Boolean)this.enableSFUI.get() != false ? Client.instance.FontLoaders.SF18 : Client.instance.FontLoaders.regular17;
        if (((Boolean)tabgui.getValue()).booleanValue()) {
            if (tabguimode.isCurrentMode("Normal")) {
                Client.instance.tabGUI.renderTabGui();
            }
            if (tabguimode.isCurrentMode("Side")) {
                Client.instance.sideTabGui.renderTabGui();
            }
        }
        if (!HUD.mc.gameSettings.showDebugInfo) {
            int[] counter = new int[]{0};
            if (((Boolean)mark.getValue()).booleanValue()) {
                this.TheWatermark();
            }
            ArrayList<Module> sorted = new ArrayList<Module>();
            for (Module m : Client.instance.getModuleManager().getModules()) {
                if (!m.isEnabled() && m.wasArrayRemoved() || m.wasRemoved() || m.getType() == ModuleType.Render && ((Boolean)this.noRenderType.getValue()).booleanValue()) continue;
                sorted.add(m);
            }
            sorted.sort((o1, o2) -> this.font.getStringWidth(o2.getSuffix().isEmpty() ? o2.getHUDName() : String.format("%s %s", o2.getHUDName(), o2.getSuffix())) - this.font.getStringWidth(o1.getSuffix().isEmpty() ? o1.getHUDName() : String.format("%s %s", o1.getHUDName(), o1.getSuffix())));
            int y = 1;
            if (((Boolean)rainbow.getValue()).booleanValue()) {
                count = 0;
            }
            int FPS = mc.getDebugFPS() == 0 ? 1 : mc.getDebugFPS();
            for (Module m : sorted) {
                String name;
                int nextIndex = sorted.indexOf(m) + 1;
                Module nextModule = null;
                if (sorted.size() > nextIndex) {
                    nextModule = HUD.getNextEnabledModule(sorted, nextIndex);
                }
                String string = name = m.getSuffix().isEmpty() ? m.getHUDName() : String.format("%s %s", m.getHUDName(), m.getSuffix(), Float.valueOf(m.getYAnim()));
                if (((Boolean)list.getValue()).booleanValue()) {
                    m.offsetY = AnimationUtil.moveUD(m.offsetY, y + 3, this.processFPS(FPS, 1000.0f, 0.015f), this.processFPS(FPS, 1000.0f, 0.013f));
                    if (m.isEnabled()) {
                        m.setArrayRemoved(false);
                        m.setAnimx(AnimationUtil.moveUD(m.getAnimx(), this.font.getStringWidth(name), this.processFPS(FPS, 1000.0f, 0.013f), this.processFPS(FPS, 1000.0f, 0.011f)));
                    } else if (m.getAnimx() <= -8.0f) {
                        m.setArrayRemoved(true);
                    } else {
                        m.setAnimx(AnimationUtil.moveUD(m.getAnimx(), -8.0f, this.processFPS(FPS, 1000.0f, 0.013f), this.processFPS(FPS, 1000.0f, 0.011f)));
                    }
                    Color Ranbow = HUD.fade(new Color(((Double)r.getValue()).intValue(), ((Double)g.getValue()).intValue(), ((Double)b.getValue()).intValue()), 70, (sorted.indexOf(m) << 2) + 25);
                    this.color = (Boolean)Breathinglamp.getValue() == false ? ((Boolean)rainbow.getValue() != false ? HUD.rainbow() : HUD.HUDColor()) : ((Boolean)rainbow.getValue() != false ? HUD.rainbow() : Ranbow.getRGB());
                    switch (this.mode.getModeAsString()) {
                        case "Simple": {
                            float x = (float)RenderUtil.width() - m.getAnimx();
                            if (((Double)this.alpha.getValue()).intValue() > 0) {
                                if (this.shadowmode.isCurrentMode("Simple")) {
                                    RenderUtil.drawBorderedRect(x - 6.0f, m.getYAnim() - 4.0f, RenderUtil.width(), m.getYAnim() + 8.0f, 0.1f, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                }
                                if (this.shadowmode.isCurrentMode("Sideways")) {
                                    RenderUtil.drawGradientSideways(x - 6.0f, m.getYAnim() - 4.0f, RenderUtil.width(), m.getYAnim() + 8.0f, new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue() / 2).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                    SimpleRender.drawRect(0.0, 0.0, 0.0, 0.0, 0);
                                }
                            }
                            this.font.HUDDrawString(name, x - 3.0f, m.getYAnim() - 1.0f, this.color);
                            break;
                        }
                        case "HLine": 
                        case "HNoLine": {
                            float x = (float)RenderUtil.width() - m.getAnimx();
                            if (this.mode.isCurrentMode("HLine")) {
                                SimpleRender.drawRect(x - 10.0f, 4.0, RenderUtil.width() - 4, 3.1, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : HUD.HUDColor());
                            }
                            if (((Double)this.alpha.getValue()).intValue() > 0) {
                                if (this.shadowmode.isCurrentMode("Simple")) {
                                    RenderUtil.drawBorderedRect(x - 10.0f, m.getYAnim(), RenderUtil.width() - 4, m.getYAnim() + 12.0f, 0.1f, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                }
                                if (this.shadowmode.isCurrentMode("Sideways")) {
                                    RenderUtil.drawGradientSideways(x - 10.0f, m.getYAnim(), RenderUtil.width() - 4, m.getYAnim() + 12.0f, new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue() / 2).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                    SimpleRender.drawRect(0.0, 0.0, 0.0, 0.0, 0);
                                }
                            }
                            this.font.HUDDrawString(name, x - 7.0f, m.getYAnim() + 4.0f, this.color);
                            break;
                        }
                        case "Rect": {
                            float x = (float)RenderUtil.width() - m.getAnimx();
                            if (((Double)this.alpha.getValue()).intValue() > 0) {
                                if (this.shadowmode.isCurrentMode("Simple")) {
                                    RenderUtil.drawBorderedRect(x - 7.0f, m.getYAnim() - 4.0f, RenderUtil.width(), m.getYAnim() + 11.0f - 3.0f, 0.1f, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                }
                                if (this.shadowmode.isCurrentMode("Sideways")) {
                                    RenderUtil.drawGradientSideways(x - 7.0f, m.getYAnim() - 4.0f, RenderUtil.width(), m.getYAnim() + 11.0f - 3.0f, new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue() / 2).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                    SimpleRender.drawRect(0.0, 0.0, 0.0, 0.0, 0);
                                }
                            }
                            this.font.HUDDrawString(name, x - 3.0f, m.getYAnim() - 2.0f, this.color);
                            RenderUtil.drawBorderedRect(x - 6.0f, m.getYAnim() + 4.0f - 6.0f, x - 6.0f, m.getYAnim() + 11.0f - 5.0f, 1.0f, this.color, this.color);
                            break;
                        }
                        case "Rect2": {
                            float x = (float)RenderUtil.width() - m.getAnimx();
                            if (((Double)this.alpha.getValue()).intValue() > 0) {
                                if (this.shadowmode.isCurrentMode("Simple")) {
                                    RenderUtil.drawBorderedRect(x - 7.0f, m.getYAnim() - 4.0f, RenderUtil.width(), m.getYAnim() + 11.0f - 3.0f, 0.1f, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                }
                                if (this.shadowmode.isCurrentMode("Sideways")) {
                                    RenderUtil.drawGradientSideways(x - 7.0f, m.getYAnim() - 4.0f, RenderUtil.width(), m.getYAnim() + 11.0f - 3.0f, new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue() / 2).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                    SimpleRender.drawRect(0.0, 0.0, 0.0, 0.0, 0);
                                }
                            }
                            this.font.HUDDrawString(name, x - 3.0f, m.getYAnim() - 2.0f, this.color);
                            SimpleRender.drawRect((double)x - 8.0, (double)m.getYAnim() - 4.0, (double)x - 7.0, (double)m.getYAnim() + 8.0, this.color);
                            break;
                        }
                        case "OutLine": {
                            float x = (float)RenderUtil.width() - m.getAnimx();
                            if (((Double)this.alpha.getValue()).intValue() > 0) {
                                if (this.shadowmode.isCurrentMode("Simple")) {
                                    RenderUtil.drawBorderedRect(x - 6.0f, m.getYAnim() - 1.0f - 3.0f, RenderUtil.width(), m.getYAnim() + 11.0f - 3.0f, 0.1f, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                }
                                if (this.shadowmode.isCurrentMode("Sideways")) {
                                    RenderUtil.drawGradientSideways(x - 6.0f, m.getYAnim() - 1.0f - 3.0f, RenderUtil.width(), m.getYAnim() + 11.0f - 3.0f, new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue() / 2).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                    SimpleRender.drawRect(0.0, 0.0, 0.0, 0.0, 0);
                                }
                            }
                            SimpleRender.drawRect((double)x - 7.0, (double)m.getYAnim() - 1.0 - 3.0, (double)x - 6.0, (double)m.getYAnim() + 11.0 - 3.0, this.color);
                            if (nextModule != null) {
                                if (!Objects.requireNonNull(HUD.getNextEnabledModule(sorted, nextIndex)).getSuffix().isEmpty()) {
                                    SimpleRender.drawRect((double)x - 7.0, (double)m.getYAnim() + 11.0 - 3.0, (double)(RenderUtil.width() - this.font.getStringWidth(Objects.requireNonNull(HUD.getNextEnabledModule(sorted, nextIndex)).getHUDName() + " " + Objects.requireNonNull(HUD.getNextEnabledModule(sorted, nextIndex)).getSuffix())) - 6.0, (double)m.getYAnim() + 12.0 - 3.0, this.color);
                                } else {
                                    SimpleRender.drawRect((double)x - 7.0, (double)m.getYAnim() + 11.0 - 3.0, (double)(RenderUtil.width() - this.font.getStringWidth(Objects.requireNonNull(HUD.getNextEnabledModule(sorted, nextIndex)).getHUDName())) - 6.0, (double)m.getYAnim() + 12.0 - 3.0, this.color);
                                }
                            } else {
                                SimpleRender.drawRect((double)x - 7.0, (double)m.getYAnim() + 11.0 - 3.0, RenderUtil.width(), (double)m.getYAnim() + 12.0 - 3.0, this.color);
                            }
                            this.font.HUDDrawString(name, x - 3.0f, m.getYAnim() + 2.0f - 3.0f, this.color);
                            break;
                        }
                        case "Side": {
                            float x = (float)RenderUtil.width() - m.getAnimx();
                            if (((Double)this.alpha.getValue()).intValue() > 0) {
                                if (this.shadowmode.isCurrentMode("Simple")) {
                                    RenderUtil.drawBorderedRect(x - 7.0f, m.getYAnim() - 4.0f, RenderUtil.width(), m.getYAnim() + 11.0f - 3.0f, 0.1f, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                }
                                if (this.shadowmode.isCurrentMode("Sideways")) {
                                    RenderUtil.drawGradientSideways(x - 7.0f, m.getYAnim() - 4.0f, RenderUtil.width(), m.getYAnim() + 11.0f - 3.0f, new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue() / 2).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                    SimpleRender.drawRect(0.0, 0.0, 0.0, 0.0, 0);
                                }
                            }
                            this.font.HUDDrawString(name, x - 5.0f, m.getYAnim() - 1.0f, this.color);
                            SimpleRender.drawRect(RenderUtil.width(), m.getYAnim() - 5.0f, RenderUtil.width() - 2, m.getYAnim() + 8.0f, this.color);
                            break;
                        }
                        case "ExtendSide": {
                            float x = (float)RenderUtil.width() - m.getAnimx();
                            if (((Double)this.alpha.getValue()).intValue() > 0) {
                                if (this.shadowmode.isCurrentMode("Simple")) {
                                    RenderUtil.drawBorderedRect(x - 10.0f, m.getYAnim() - 4.0f, RenderUtil.width(), m.getYAnim() + 11.0f - 3.0f, 0.1f, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                }
                                if (this.shadowmode.isCurrentMode("Sideways")) {
                                    RenderUtil.drawGradientSideways(x - 10.0f, m.getYAnim() - 4.0f, RenderUtil.width(), m.getYAnim() + 11.0f - 3.0f, new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue() / 2).getRGB(), new Color(0, 0, 0, ((Double)this.alpha.getValue()).intValue()).getRGB());
                                    SimpleRender.drawRect(0.0, 0.0, 0.0, 0.0, 0);
                                }
                            }
                            this.font.HUDDrawString(name, x - 6.5f, m.getYAnim() - 1.0f, this.color);
                            RenderUtil.drawBorderedRect(RenderUtil.width(), m.getYAnim() - 5.0f, RenderUtil.width() - 2, m.getYAnim() + 8.0f, 1.0f, this.color, this.color);
                        }
                    }
                }
                if (((Boolean)rainbow.getValue()).booleanValue() && (count += 3) > 100) {
                    count = 0;
                }
                if (((Boolean)Breathinglamp.get()).booleanValue() && ++this.fadeTicks >= 120) {
                    this.fadeTicks = 0;
                }
                if (((Boolean)modnotifications.getValue()).booleanValue()) {
                    if (Minecraft.thePlayer.ticksExisted <= 10) {
                        ModNotification.clear();
                    }
                }
                if (((Boolean)Breathinglamp.getValue()).booleanValue() && !((Boolean)rainbow.getValue()).booleanValue()) {
                    counter[0] = counter[0] + 3;
                    if (counter[0] > 100) {
                        counter[0] = 0;
                    }
                }
                y += 12;
            }
            if (((Boolean)this.info.getValue()).booleanValue()) {
                String userinfo;
                double xDif = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
                double zDif = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
                double lastDist = Math.sqrt(xDif * xDif + zDif * zDif) * 20.0;
                ScaledResolution sr = new ScaledResolution(mc);
                CFontRenderer font2 = Client.instance.FontLoaders.regular17;
                if (GuiLogin.checkDev) {
                    userinfo = (Object)((Object)EnumChatFormatting.GRAY) + "Developer: " + (Object)((Object)EnumChatFormatting.RED) + Client.instance.Users + " ";
                    font2.drawStringWithShadow(userinfo, (float)(sr.getScaledWidth() - font2.getStringWidth(userinfo)) - 2.0f, RenderUtil.height() - 12, -1);
                } else {
                    userinfo = (Object)((Object)EnumChatFormatting.GRAY) + "User: " + (Object)((Object)EnumChatFormatting.GREEN) + Client.instance.Users + " ";
                    font2.drawStringWithShadow(userinfo, (float)(sr.getScaledWidth() - font2.getStringWidth(userinfo)) - 2.0f, RenderUtil.height() - 12, -1);
                }
                String speedinfo = " Speed: ";
                char[] speedcharArray = " Speed: ".toCharArray();
                int uspeedlength = 0;
                for (char charIndexspeed : speedcharArray) {
                    if (!((Boolean)Breathinglamp.getValue()).booleanValue()) {
                        font2.drawStringWithShadow(String.valueOf(charIndexspeed), 2 + uspeedlength, RenderUtil.height() - 25, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : HUD.HUDColor());
                    } else {
                        Color Ranbow = HUD.fade(new Color(((Double)r.getValue()).intValue(), ((Double)g.getValue()).intValue(), ((Double)b.getValue()).intValue()), 65, uspeedlength + 39);
                        font2.drawStringWithShadow(String.valueOf(charIndexspeed), 2 + uspeedlength, RenderUtil.height() - 25, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : Ranbow.getRGB());
                    }
                    uspeedlength += font2.getCharWidth(charIndexspeed);
                }
                font2.drawStringWithShadow((Object)((Object)EnumChatFormatting.GRAY) + String.format("%.2f", lastDist) + " blocks/sec", 2 + font2.getStringWidth(" Speed: "), RenderUtil.height() - 25, -1);
                String xyzs = " XYZ: ";
                String xyz2 = String.valueOf((Object)EnumChatFormatting.GRAY) + MathHelper.floor_double(Minecraft.thePlayer.posX) + " " + (Object)((Object)EnumChatFormatting.GRAY) + (Object)((Object)EnumChatFormatting.GRAY) + MathHelper.floor_double(Minecraft.thePlayer.posY) + " " + (Object)((Object)EnumChatFormatting.GRAY) + (Object)((Object)EnumChatFormatting.GRAY) + MathHelper.floor_double(Minecraft.thePlayer.posZ);
                char[] xyzcharArray = " XYZ: ".toCharArray();
                int xyzlength = 0;
                for (char charIndex : xyzcharArray) {
                    if (!((Boolean)Breathinglamp.getValue()).booleanValue()) {
                        font2.drawStringWithShadow(String.valueOf(charIndex), 2 + xyzlength, RenderUtil.height() - 12, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : HUD.HUDColor());
                    } else {
                        Color Ranbow = HUD.fade(new Color(((Double)r.getValue()).intValue(), ((Double)g.getValue()).intValue(), ((Double)b.getValue()).intValue()), 65, uspeedlength + 39);
                        font2.drawStringWithShadow(String.valueOf(charIndex), 2 + xyzlength, RenderUtil.height() - 12, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : Ranbow.getRGB());
                    }
                    xyzlength += font2.getCharWidth(charIndex);
                }
                font2.drawStringWithShadow(xyz2, 2 + font2.getStringWidth(" XYZ: "), RenderUtil.height() - 12, Color.GRAY.getRGB());
            }
            if (((Boolean)this.potions.getValue()).booleanValue()) {
                this.drawPotionStatus(new ScaledResolution(mc));
                if (HUD.mc.ingameGUI.getChatGUI().getChatOpen()) {
                    if (this.ychat < 7) {
                        ++this.ychat;
                    }
                } else if (this.ychat > -7) {
                    --this.ychat;
                }
            }
            if (((Boolean)this.armors.getValue()).booleanValue()) {
                this.renderArmorStatus(new ScaledResolution(mc));
            }
            if (((Boolean)this.bindlists.getValue()).booleanValue()) {
                this.KeyBinds();
            }
        }
    }

    private static Module getNextEnabledModule(List<? extends Module> modules, int startingIndex) {
        int modulesSize = modules.size();
        for (int i = startingIndex; i < modulesSize; ++i) {
            Module module = modules.get(i);
            if (!module.isEnabled()) continue;
            return module;
        }
        return null;
    }

    public void renderArmorStatus(ScaledResolution sr) {
        GL11.glPushMatrix();
        RenderItem ir = new RenderItem(Minecraft.getTextureManager(), HUD.mc.modelManager);
        ArrayList<ItemStack> stuff = new ArrayList<ItemStack>();
        int split = 17;
        for (int index = 3; index >= 0; --index) {
            ItemStack armer = Minecraft.thePlayer.inventory.armorInventory[index];
            if (armer == null) continue;
            stuff.add(armer);
        }
        int y = sr.getScaledHeight() - 58 + 2;
        for (ItemStack errything : stuff) {
            int x = sr.getScaledWidth() / 2 + split - 7;
            if (Minecraft.theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
                ir.renderItemIntoGUI(errything, x, y);
                ir.renderItemOverlays(Minecraft.fontRendererObj, errything, x, y);
                RenderHelper.enableGUIStandardItemLighting();
                split += 18;
            }
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.clear(256);
            GL11.glPushMatrix();
            GL11.glPopMatrix();
            errything.getEnchantmentTagList();
        }
        GL11.glPopMatrix();
    }

    private void KeyBinds() {
        int x = 3;
        if (((Boolean)tabgui.getValue()).booleanValue()) {
            if (Client.instance.getModuleManager().getModuleByClass(ModuleIndicator.class).isEnabled()) {
                if (this.bindsy != 155.0f) {
                    this.bindsy = AnimationUtil.moveUDl(this.bindsy, 155.0f, 0.8f, 0.6f);
                }
            } else if (watermark.isCurrentMode("Sense")) {
                if (this.bindsy != 90.0f) {
                    this.bindsy = AnimationUtil.moveUDl(this.bindsy, 90.0f, 0.8f, 0.6f);
                }
            } else if (this.bindsy != 82.0f) {
                this.bindsy = AnimationUtil.moveUDl(this.bindsy, 82.0f, 0.8f, 0.6f);
            }
        } else if (watermark.isCurrentMode("Sense")) {
            if (this.bindsy != 11.0f) {
                this.bindsy = AnimationUtil.moveUDl(this.bindsy, 11.0f, 0.8f, 0.6f);
            }
        } else if (this.bindsy != 1.0f) {
            this.bindsy = AnimationUtil.moveUDl(this.bindsy, 1.0f, 0.8f, 0.6f);
        }
        CFontRenderer font = Client.instance.FontLoaders.Comfortaa16;
        ArrayList<Module> mod = new ArrayList<Module>(Client.instance.getModuleManager().getModules());
        mod.sort((o1, o2) -> font.getStringWidth(o2.getHUDName()) - font.getStringWidth(o1.getHUDName()));
        float my = this.bindsy + 15.0f;
        for (Module m : mod) {
            if (m.getKey() == 0) continue;
            if (!((Boolean)Breathinglamp.getValue()).booleanValue()) {
                font.drawStringWithShadow(m.getHUDName(), 5.0, my + 3.0f, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : HUD.HUDColor());
                font.drawStringWithShadow("[" + Keyboard.getKeyName((int)m.getKey()) + "]", 3 + font.getStringWidth(mod.get(1).getHUDName()) + 19 - font.getStringWidth("[" + Keyboard.getKeyName((int)m.getKey()) + "]"), my + 3.0f, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : HUD.HUDColor());
            } else {
                Color Ranbow = HUD.fade(new Color(((Double)r.getValue()).intValue(), ((Double)g.getValue()).intValue(), ((Double)b.getValue()).intValue()), 100, (mod.indexOf(m) << 1) + 50);
                font.drawStringWithShadow(m.getHUDName(), 5.0, my + 3.0f, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : Ranbow.getRGB());
                font.drawStringWithShadow("[" + Keyboard.getKeyName((int)m.getKey()) + "]", 3 + font.getStringWidth(mod.get(1).getHUDName()) + 19 - font.getStringWidth("[" + Keyboard.getKeyName((int)m.getKey()) + "]"), my + 3.0f, Ranbow.getRGB());
            }
            my += 9.0f;
        }
    }

    public static Color fade(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    private void drawPotionStatus(ScaledResolution sr) {
        int y2 = 0;
        for (PotionEffect effect : Minecraft.thePlayer.getActivePotionEffects()) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            StringBuilder PType = new StringBuilder(I18n.format(potion.getName(), new Object[0]));
            switch (effect.getAmplifier()) {
                case 1: {
                    PType.append(" II");
                    break;
                }
                case 2: {
                    PType.append(" III");
                    break;
                }
                case 3: {
                    PType.append(" IV");
                }
            }
            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                PType.append("\u00a77:\u00a76 ").append(Potion.getDurationString(effect));
            } else if (effect.getDuration() < 300) {
                PType.append("\u00a77:\u00a7c ").append(Potion.getDurationString(effect));
            } else if (effect.getDuration() > 600) {
                PType.append("\u00a77:\u00a77 ").append(Potion.getDurationString(effect));
            }
            Minecraft.fontRendererObj.drawStringWithShadow(String.valueOf(PType), sr.getScaledWidth() / 2 + 94, sr.getScaledHeight() - Minecraft.fontRendererObj.FONT_HEIGHT + y2 - 12 - this.ychat + 4, potion.getLiquidColor());
            y2 -= 10;
        }
    }

    private static int HUDColor() {
        return new Color(((Double)r.getValue()).intValue(), ((Double)g.getValue()).intValue(), ((Double)b.getValue()).intValue()).getRGB();
    }

    private void TheWatermark() {
        CFontRenderer sencefont;
        Color color333;
        Color color332;
        Color color33;
        float h3;
        float h2;
        float h1;
        if (watermark.isCurrentMode("Weave")) {
            this.hue += SimpleRender.processFPS(1000.0f, 0.05f);
            if (this.hue > 255.0f) {
                this.hue = 0.0f;
            }
            h1 = this.hue + 200.0f;
            h2 = this.hue + 85.0f;
            h3 = this.hue + 170.0f;
            color33 = Color.getHSBColor(h1 / 255.0f, 0.9f, 1.0f);
            color332 = Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
            color333 = Color.getHSBColor(h3 / 255.0f, 0.9f, 1.0f);
            sencefont = Client.instance.FontLoaders.regular16;
            if (((Boolean)rainbow.getValue()).booleanValue()) {
                RenderUtil.Weave(1.5, 5.0, 220.0, 23.0, 1.0, rainbowmode.is("Client") ? (float)color332.getRGB() : (float)color33.getRGB(), rainbowmode.is("Client") ? (float)color333.getRGB() : (float)color332.getRGB());
            } else {
                RenderUtil.Weave(1.5, 5.0, 220.0, 23.0, 1.0, 0.0f, 0.0f);
            }
            sencefont.drawString("Heaven | " + mc.getDebugFPS() + "FPS | " + Minecraft.thePlayer.getHealth() + "HP | " + (int)MoveUtils.getDirectionForAura() + " Yaw", 9.0f, 15.0f - (float)(sencefont.getStringHeight() / 2), -1);
            Gui.drawRect(0, 0, 0, 0, 0);
        }
        if (watermark.isCurrentMode("Sense")) {
            this.hue += SimpleRender.processFPS(1000.0f, 0.05f);
            if (this.hue > 255.0f) {
                this.hue = 0.0f;
            }
            h1 = this.hue + 200.0f;
            h2 = this.hue + 85.0f;
            h3 = this.hue + 170.0f;
            color33 = Color.getHSBColor(h1 / 255.0f, 0.9f, 1.0f);
            color332 = Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
            color333 = Color.getHSBColor(h3 / 255.0f, 0.9f, 1.0f);
            sencefont = Client.instance.FontLoaders.Comfortaa15;
            int ping = mc.getNetHandler().getPlayerInfo(Minecraft.thePlayer.getUniqueID()).getResponseTime();
            String pings = ping + "ms";
            String text = Client.instance.hudname + ".\u00a7ccc\u00a7  | " + Minecraft.thePlayer.getName() + " | " + (mc.isSingleplayer() ? "SinglePlayer" : HUD.mc.getCurrentServerData().serverIP) + " | " + Minecraft.getDebugFPS() + "fps | " + pings + " | 20.0ticks";
            if (((Boolean)rainbow.getValue()).booleanValue()) {
                RenderUtil.Gamesense(1.5, 5.0, sencefont.getStringWidth(text) + 15, 23.0, 1.0, rainbowmode.is("Client") ? (float)color332.getRGB() : (float)color33.getRGB(), rainbowmode.is("Client") ? (float)color333.getRGB() : (float)color332.getRGB());
            } else {
                RenderUtil.Gamesense(1.5, 5.0, sencefont.getStringWidth(text) + 15, 23.0, 1.0, 0.0f, 0.0f);
            }
            Gui.drawRect(0, 0, 0, 0, 0);
            sencefont.drawCenteredString(text, (double)(sencefont.getStringWidth(text) / 2 + 9) - 1.5, 16.0 - (double)(sencefont.getStringHeight() / 2) - 0.75, new Color(255, 255, 255).getRGB());
        }
        if (watermark.isCurrentMode("Text")) {
            CFontRenderer fonts = Client.instance.FontLoaders.regular20;
            String append5 = Client.instance.hudname.substring(0, 1);
            String append6 = Client.instance.hudname.substring(1);
            if (!((Boolean)Breathinglamp.getValue()).booleanValue()) {
                fonts.drawStringWithShadow(append5, 4.0, 6.0, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : HUD.HUDColor());
            } else {
                Color Ranbow = HUD.fade(new Color(((Double)r.getValue()).intValue(), ((Double)g.getValue()).intValue(), ((Double)b.getValue()).intValue()), 70, 25);
                fonts.drawStringWithShadow(append5, 4.0, 6.0, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : Ranbow.getRGB());
            }
            fonts.drawStringWithShadow(append6, 5.0 + (double)this.font.getStringWidth(append5), 6.0, new Color(255, 255, 255).getRGB());
        }
        if (watermark.isCurrentMode("Fade")) {
            CFontRenderer fonts = Client.instance.FontLoaders.regular20;
            Client.instance.FontLoaders.regular19.drawStringWithShadow((Object)((Object)EnumChatFormatting.GRAY) + " \ufffd\ufffd7(\ufffd\ufffdr" + new SimpleDateFormat("HH:mm").format(new Date()) + "\ufffd\ufffd7)\ufffd\ufffdr", 3.0 + (double)fonts.getStringWidth(Client.instance.hudname), 7.0, -1);
            char[] charArray = Client.instance.hudname.toCharArray();
            int length = 0;
            for (char charIndex : charArray) {
                if (!((Boolean)Breathinglamp.getValue()).booleanValue()) {
                    fonts.drawStringWithShadow(String.valueOf(charIndex), 5 + length, 6.0, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : HUD.HUDColor());
                } else {
                    Color Ranbow = HUD.fade(new Color(((Double)r.getValue()).intValue(), ((Double)g.getValue()).intValue(), ((Double)b.getValue()).intValue()), 65, length + 80);
                    fonts.drawStringWithShadow(String.valueOf(charIndex), 5 + length, 6.0, (Boolean)rainbow.getValue() != false ? HUD.rainbow() : Ranbow.getRGB());
                }
                length += fonts.getCharWidth(charIndex);
            }
        }
    }

    public static Color rainbowToEffect() {
        if (rainbowmode.isCurrentMode("Client") || rainbowmode.isCurrentMode("Dark") || rainbowmode.isCurrentMode("Random")) {
            float rainbowalpha = 0.0f;
            if (rainbowmode.isCurrentMode("Client")) {
                rainbowalpha = 0.42f;
            }
            if (rainbowmode.isCurrentMode("Dark")) {
                rainbowalpha = 0.7f;
            }
            if (rainbowmode.isCurrentMode("Random")) {
                rainbowalpha = 5.0f;
            }
            double rainbowState = Math.ceil((double)((System.currentTimeMillis() + (long)((double)(++count * -50) * 1.0)) / 8L) + -2.5);
            return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), rainbowalpha, 1.0f);
        }
        if (rainbowmode.isCurrentMode("Sakura")) {
            double rainbowState = Math.ceil(System.currentTimeMillis() / 5L + (long)(++count * -50 * 40) / 360L);
            Color rainbowcolor2 = Color.getHSBColor((float)((rainbowState %= 360.0) / 255.0), 0.6f, 1.0f);
            return new Color(rainbowcolor2.getRed(), 90, 220);
        }
        if (rainbowmode.isCurrentMode("Chemical")) {
            double rainbowState = Math.ceil(System.currentTimeMillis() / 5L + (long)(++count * -50 * 40) / 360L);
            Color rainbowcolor2 = Color.getHSBColor((float)((rainbowState %= 360.0) / 255.0), 0.6f, 1.0f);
            return new Color(155, rainbowcolor2.getGreen(), 246);
        }
        if (rainbowmode.isCurrentMode("Mexico")) {
            double rainbowState = Math.ceil(System.currentTimeMillis() / 5L + (long)(++count * -50 * 40) / 360L);
            Color rainbowcolor2 = Color.getHSBColor((float)((rainbowState %= 360.0) / 255.0), 0.6f, 1.0f);
            return new Color(rainbowcolor2.getRed(), 0, rainbowcolor2.getBlue());
        }
        if (rainbowmode.isCurrentMode("Neon")) {
            double rainbowState = Math.ceil(System.currentTimeMillis() / 5L + (long)(++count * -50 * 40) / 360L);
            Color rainbowcolor = Color.getHSBColor((float)((rainbowState %= 360.0) / 255.0), 0.4f, 0.8f);
            return new Color(rainbowcolor.getRed(), rainbowcolor.getGreen(), 255);
        }
        if (rainbowmode.isCurrentMode("Astolfo")) {
            double d;
            double rainbowDelay = Math.ceil((double)((System.currentTimeMillis() + (long)((double)(++count * -50) * 1.0)) / 8L) + -2.5);
            return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(rainbowDelay / 360.0)) : (float)((rainbowDelay %= 360.0) / 360.0), 0.5f, 1.0f);
        }
        return null;
    }

    public static int rainbow() {
        if (rainbowmode.isCurrentMode("Client") || rainbowmode.isCurrentMode("Dark") || rainbowmode.isCurrentMode("Random")) {
            float rainbowalpha = 0.0f;
            if (rainbowmode.isCurrentMode("Client")) {
                rainbowalpha = 0.42f;
            }
            if (rainbowmode.isCurrentMode("Dark")) {
                rainbowalpha = 0.7f;
            }
            if (rainbowmode.isCurrentMode("Random")) {
                rainbowalpha = 5.0f;
            }
            double rainbowState = Math.ceil((double)((System.currentTimeMillis() + (long)((double)(++count * -50) * 1.0)) / 8L) + -2.5);
            return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), rainbowalpha, 1.0f).getRGB();
        }
        if (rainbowmode.isCurrentMode("Sakura")) {
            double rainbowState = Math.ceil(System.currentTimeMillis() / 5L + (long)(++count * -50 * 40) / 360L);
            Color rainbowcolor2 = Color.getHSBColor((float)((rainbowState %= 360.0) / 255.0), 0.6f, 1.0f);
            return new Color(rainbowcolor2.getRed(), 90, 220).getRGB();
        }
        if (rainbowmode.isCurrentMode("Chemical")) {
            double rainbowState = Math.ceil(System.currentTimeMillis() / 5L + (long)(++count * -50 * 40) / 360L);
            Color rainbowcolor2 = Color.getHSBColor((float)((rainbowState %= 360.0) / 255.0), 0.6f, 1.0f);
            return new Color(155, rainbowcolor2.getGreen(), 246).getRGB();
        }
        if (rainbowmode.isCurrentMode("Mexico")) {
            double rainbowState = Math.ceil(System.currentTimeMillis() / 5L + (long)(++count * -50 * 40) / 360L);
            Color rainbowcolor2 = Color.getHSBColor((float)((rainbowState %= 360.0) / 255.0), 0.6f, 1.0f);
            return new Color(rainbowcolor2.getRed(), 0, rainbowcolor2.getBlue()).getRGB();
        }
        if (rainbowmode.isCurrentMode("Neon")) {
            double rainbowState = Math.ceil(System.currentTimeMillis() / 5L + (long)(++count * -50 * 40) / 360L);
            Color rainbowcolor = Color.getHSBColor((float)((rainbowState %= 360.0) / 255.0), 0.4f, 0.8f);
            return new Color(rainbowcolor.getRed(), rainbowcolor.getGreen(), 255).getRGB();
        }
        if (rainbowmode.isCurrentMode("Astolfo")) {
            double d;
            double rainbowDelay = Math.ceil((double)((System.currentTimeMillis() + (long)((double)(++count * -50) * 1.0)) / 8L) + -2.5);
            return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(rainbowDelay / 360.0)) : (float)((rainbowDelay %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
        }
        return 0;
    }
}

