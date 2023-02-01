/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.guimainmenu.ColorCreator;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.render.gl.ScaleUtils;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class SessionInfo
extends Module {
    private final Mode<String> mode = new Mode("Mode", new String[]{"Line", "Round", "Current", "Fence"}, "Line");
    public final Numbers<Double> xe = new Numbers<Double>("X", 0.0, 0.0, 1000.0, 0.5);
    public final Numbers<Double> ye = new Numbers<Double>("Y", 0.0, 0.0, 1000.0, 0.5);
    float x;
    float y;

    public SessionInfo() {
        super("SessionInfo", ModuleType.Render);
        this.addValues(this.mode, this.xe, this.ye);
    }

    @Override
    public void onDisable() {
        this.y = 0.0f;
        this.x = 0.0f;
    }

    @EventHandler
    public void onRenderGui(EventRender2D event) {
        Color Ranbow;
        Color Ranbow2;
        double lastDist;
        double zDist;
        double xDist;
        if (SessionInfo.mc.gameSettings.showDebugInfo) {
            return;
        }
        if (this.x != ((Double)this.xe.get()).floatValue()) {
            this.x = AnimationUtil.moveUD(this.x, ((Double)this.xe.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        }
        if (this.y != ((Double)this.ye.get()).floatValue()) {
            this.y = AnimationUtil.moveUD(this.y, ((Double)this.ye.get()).floatValue(), SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        }
        if (this.mode.is("Fence")) {
            xDist = Minecraft.thePlayer.posX - Minecraft.thePlayer.lastTickPosX;
            zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.lastTickPosZ;
            lastDist = Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(zDist, 2.0));
            CFontRenderer icon = Client.instance.FontLoaders.sessionInfo16;
            String time = Client.instance.combatManager.getHour() + "h " + Client.instance.combatManager.getMinute() + "m " + Client.instance.combatManager.getSecond() + "s";
            if (!((Boolean)HUD.rainbow.get()).booleanValue()) {
                if (((Boolean)HUD.Breathinglamp.get()).booleanValue()) {
                    Color Ranbow22 = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 65, 160);
                    Color Ranbow3 = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 65, 80);
                    RenderUtil.drawGradientSideways(this.x, -1.0f + this.y, 96.0f + this.x + (float)(time.length() / 2), this.y, Ranbow3.getRGB(), Ranbow22.getRGB());
                    RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
                } else {
                    RenderUtil.drawRect(this.x, -1.0f + this.y, 96.0f + this.x + (float)(time.length() / 2), this.y, this.hudColor());
                }
            } else {
                RenderUtil.drawGradientSideways(this.x, -1.0f + this.y, 96.0f + this.x + (float)(time.length() / 2), this.y, HUD.rainbow(), ColorCreator.createRainbowFromOffset(-6000, 5));
                RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
            }
            RenderUtil.drawRect(this.x, this.y, 96.0f + this.x + (float)(time.length() / 2), 60.5f + this.y, new Color(0, 0, 0, 150).getRGB());
            icon.drawString("B", 6.0f + this.x, 5.0f + this.y, -1);
            Client.instance.FontLoaders.regular15.drawString("Session Info", 17.0f + this.x, 4.0f + this.y, -1);
            if (System.currentTimeMillis() - Client.instance.combatManager.getStartTime() > 1000L) {
                Client.instance.combatManager.setSecond(Client.instance.combatManager.getSecond() + 1);
                Client.instance.combatManager.setStartTime(System.currentTimeMillis());
            }
            if (Client.instance.combatManager.getSecond() > 59) {
                Client.instance.combatManager.setSecond(0);
                Client.instance.combatManager.setMinute(Client.instance.combatManager.getMinute() + 1);
            }
            if (Client.instance.combatManager.getMinute() > 59) {
                Client.instance.combatManager.setMinute(0);
                Client.instance.combatManager.setHour(Client.instance.combatManager.getHour() + 1);
            }
            Client.instance.FontLoaders.regular13.drawString("Time Played: " + Client.instance.combatManager.getHour() + "h " + Client.instance.combatManager.getMinute() + "m " + Client.instance.combatManager.getSecond() + "s", 15.0f + this.x, 20.0f + this.y, -1);
            Client.instance.FontLoaders.regular13.drawString("Speed: " + String.format("%.2f bps", lastDist * 20.0 * (double)SessionInfo.mc.timer.timerSpeed), 15.0f + this.x, 30.0f + this.y, -1);
            Client.instance.FontLoaders.regular13.drawString("Winner / Total: " + Client.instance.combatManager.getWin() + "/" + Client.instance.combatManager.getTotalPlayed(), 15.0f + this.x, 40.0f + this.y, -1);
            Client.instance.FontLoaders.regular13.drawString("Kills: " + this.getModule(KillAura.class).killed, 15.0f + this.x, 50.0f + this.y, -1);
            icon.drawString("A", 7.5f + this.x - 1.0f, 20.0f + this.y, -1);
            icon.drawString("F", 6.0f + this.x - 1.0f, 30.0f + this.y, -1);
            icon.drawString("D", 6.5f + this.x - 1.0f, 40.0f + this.y, -1);
            icon.drawString("C", 5.0f + this.x - 1.0f, 50.0f + this.y, -1);
        }
        if (this.mode.is("Line")) {
            GL11.glPushMatrix();
            ScaleUtils.scale(mc);
            xDist = Minecraft.thePlayer.posX - Minecraft.thePlayer.lastTickPosX;
            zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.lastTickPosZ;
            lastDist = Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(zDist, 2.0));
            int leftX = -2;
            if (!((Boolean)HUD.rainbow.get()).booleanValue()) {
                if (((Boolean)HUD.Breathinglamp.get()).booleanValue()) {
                    Ranbow2 = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 65, 160);
                    Ranbow = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 65, 80);
                    RenderUtil.drawGradientSideways(this.x, -1.0f + this.y, 95.0f + this.x + 60.0f, this.y, Ranbow.getRGB(), Ranbow2.getRGB());
                    RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
                } else {
                    RenderUtil.drawRect(this.x, -1.0f + this.y, 95.0f + this.x + 60.0f, this.y, this.hudColor());
                }
            } else {
                RenderUtil.drawGradientSideways(this.x, -1.0f + this.y, 95.0f + this.x + 60.0f, this.y, HUD.rainbow(), ColorCreator.createRainbowFromOffset(-6000, 5));
                RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
            }
            RenderUtil.drawRect(this.x, this.y, 95.0f + this.x + 60.0f, 60.0f + this.y + 16.0f, new Color(0, 0, 0, 180).getRGB());
            if (((Boolean)HUD.Breathinglamp.get()).booleanValue() || ((Boolean)HUD.rainbow.get()).booleanValue()) {
                char[] charArray = "Session".toCharArray();
                int length = 0;
                for (char charIndex : charArray) {
                    Color Ranbow4 = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 65, length + 80);
                    Client.instance.FontLoaders.regular18.drawString(String.valueOf(charIndex), (float)(18 + length) + this.x - 15.0f, 3.5f + this.y, (Boolean)HUD.rainbow.get() != false ? HUD.rainbow() : Ranbow4.getRGB());
                    length += Client.instance.FontLoaders.regular18.getCharWidth(charIndex);
                }
            } else {
                Client.instance.FontLoaders.regular18.drawString("Session ", 18.0f + this.x - 15.0f, 3.5f + this.y, this.hudColor());
            }
            Client.instance.FontLoaders.regular18.drawString("Info", 18.0f + this.x - 15.0f + (float)Client.instance.FontLoaders.regular16.getStringWidth("Session "), 3.5f + this.y, -1);
            if (System.currentTimeMillis() - Client.instance.combatManager.getStartTime() > 1000L) {
                Client.instance.combatManager.setSecond(Client.instance.combatManager.getSecond() + 1);
                Client.instance.combatManager.setStartTime(System.currentTimeMillis());
            }
            if (Client.instance.combatManager.getSecond() > 59) {
                Client.instance.combatManager.setSecond(0);
                Client.instance.combatManager.setMinute(Client.instance.combatManager.getMinute() + 1);
            }
            if (Client.instance.combatManager.getMinute() > 59) {
                Client.instance.combatManager.setMinute(0);
                Client.instance.combatManager.setHour(Client.instance.combatManager.getHour() + 1);
            }
            Client.instance.FontLoaders.sessionInfo22.drawString("A", 7.5f + this.x + -2.0f, 20.0f + this.y - 1.0f, this.hudColor());
            Client.instance.FontLoaders.sessionInfo20.drawString("F", 6.0f + this.x + -2.0f - 0.5f, 30.0f + this.y + 5.0f - 1.0f, this.hudColor());
            Client.instance.FontLoaders.sessionInfo22.drawString("D", 6.5f + this.x + -2.0f - 1.0f, 40.0f + this.y + 10.0f - 1.0f, this.hudColor());
            Client.instance.FontLoaders.sessionInfo19.drawString("C", 5.0f + this.x + -2.0f - 0.0f, 50.0f + this.y + 15.0f - 1.0f, this.hudColor());
            CFontRenderer font = Client.instance.FontLoaders.regular15;
            font.drawString("Time Played: " + Client.instance.combatManager.getHour() + "h " + Client.instance.combatManager.getMinute() + "m " + Client.instance.combatManager.getSecond() + "s", 16.0f + this.x, 19.0f + this.y, -1);
            font.drawString("Speed: " + String.format("%.2f bps", lastDist * 20.0 * (double)SessionInfo.mc.timer.timerSpeed), 16.0f + this.x, 29.0f + this.y + 5.0f, -1);
            font.drawString("Winner / Total: " + Client.instance.combatManager.getWin() + "/" + Client.instance.combatManager.getTotalPlayed(), 16.0f + this.x, 39.0f + this.y + 10.0f, -1);
            font.drawString("Kills: " + this.getModule(KillAura.class).killed, 16.0f + this.x, 49.0f + this.y + 15.0f, -1);
            GL11.glPopMatrix();
        }
        if (this.mode.is("Current")) {
            GL11.glPushMatrix();
            ScaleUtils.scale(mc);
            if (!((Boolean)HUD.rainbow.get()).booleanValue()) {
                if (((Boolean)HUD.Breathinglamp.get()).booleanValue()) {
                    Color Ranbow23 = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 65, 160);
                    Color Ranbow5 = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 65, 80);
                    RenderUtil.drawGradientSideways(this.x, -1.0f + this.y, 95.0f + this.x + 60.0f, this.y, Ranbow5.getRGB(), Ranbow23.getRGB());
                    RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
                } else {
                    RenderUtil.drawRect(this.x, -1.0f + this.y, 95.0f + this.x + 60.0f, this.y, this.hudColor());
                }
            } else {
                RenderUtil.drawGradientSideways(this.x, -1.0f + this.y, 95.0f + this.x + 60.0f, this.y, HUD.rainbow(), ColorCreator.createRainbowFromOffset(-6000, 5));
                RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
            }
            RenderUtil.drawRect(this.x, this.y, 95.0f + this.x + 60.0f, 60.0f + this.y + 16.0f, new Color(0, 0, 0, 200).getRGB());
            if (((Boolean)HUD.Breathinglamp.get()).booleanValue() || ((Boolean)HUD.rainbow.get()).booleanValue()) {
                char[] charArray = "Current Session".toCharArray();
                int length = 0;
                for (char charIndex : charArray) {
                    Color Ranbow6 = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 65, length + 80);
                    Client.instance.FontLoaders.regular18.drawString(String.valueOf(charIndex), (float)(18 + length) + this.x - 15.0f, 3.5f + this.y, (Boolean)HUD.rainbow.get() != false ? HUD.rainbow() : Ranbow6.getRGB());
                    length += Client.instance.FontLoaders.regular18.getCharWidth(charIndex);
                }
            } else {
                Client.instance.FontLoaders.regular18.drawString("Current Session", 18.0f + this.x - 15.0f, 3.5f + this.y, this.hudColor());
            }
            if (System.currentTimeMillis() - Client.instance.combatManager.getStartTime() > 1000L) {
                Client.instance.combatManager.setSecond(Client.instance.combatManager.getSecond() + 1);
                Client.instance.combatManager.setStartTime(System.currentTimeMillis());
            }
            if (Client.instance.combatManager.getSecond() > 59) {
                Client.instance.combatManager.setSecond(0);
                Client.instance.combatManager.setMinute(Client.instance.combatManager.getMinute() + 1);
            }
            if (Client.instance.combatManager.getMinute() > 59) {
                Client.instance.combatManager.setMinute(0);
                Client.instance.combatManager.setHour(Client.instance.combatManager.getHour() + 1);
            }
            CFontRenderer font = Client.instance.FontLoaders.regular16;
            font.drawString("Playing Time: " + Client.instance.combatManager.getHour() + "h " + Client.instance.combatManager.getMinute() + "m " + Client.instance.combatManager.getSecond() + "s", 5.0f + this.x, 19.0f + this.y, -1);
            font.drawString("Hurt TIme: " + Minecraft.thePlayer.hurtResistantTime, 5.0f + this.x, 29.0f + this.y + 5.0f, -1);
            font.drawString("Winner / Total: " + Client.instance.combatManager.getWin() + "/" + Client.instance.combatManager.getTotalPlayed(), 5.0f + this.x, 39.0f + this.y + 10.0f, -1);
            font.drawString("Player KIlled: " + this.getModule(KillAura.class).killed, 5.0f + this.x, 49.0f + this.y + 15.0f, -1);
            GL11.glPopMatrix();
        }
        if (this.mode.is("Round")) {
            GL11.glPushMatrix();
            ScaleUtils.scale(mc);
            double xDist2 = Minecraft.thePlayer.posX - Minecraft.thePlayer.lastTickPosX;
            double zDist2 = Minecraft.thePlayer.posZ - Minecraft.thePlayer.lastTickPosZ;
            double lastDist2 = Math.sqrt(Math.pow(xDist2, 2.0) + Math.pow(zDist2, 2.0));
            RenderUtil.drawGradientSidewaysV(this.x, this.y - 2.0f + 16.0f, 55.0f + this.x + 60.0f, 55.0f + this.y + 14.0f, new Color(20, 20, 20, 255).getRGB(), new Color(0, 0, 0, 180).getRGB());
            RenderUtil.drawRoundRect(this.x, this.y - 2.0f, 55.0f + this.x + 60.0f, 14.0f + this.y, 2, new Color(20, 20, 20).getRGB());
            Client.instance.FontLoaders.guiicons22.drawCenteredString("C", 58.0f + this.x, 2.0f + this.y, -1);
            if (System.currentTimeMillis() - Client.instance.combatManager.getStartTime() > 1000L) {
                Client.instance.combatManager.setSecond(Client.instance.combatManager.getSecond() + 1);
                Client.instance.combatManager.setStartTime(System.currentTimeMillis());
            }
            if (Client.instance.combatManager.getSecond() > 59) {
                Client.instance.combatManager.setSecond(0);
                Client.instance.combatManager.setMinute(Client.instance.combatManager.getMinute() + 1);
            }
            if (Client.instance.combatManager.getMinute() > 59) {
                Client.instance.combatManager.setMinute(0);
                Client.instance.combatManager.setHour(Client.instance.combatManager.getHour() + 1);
            }
            CFontRenderer font = Client.instance.FontLoaders.regular15;
            font.drawString("FPS: " + Minecraft.debugFPS, 11.0f + this.x, 15.0f + this.y + 8.0f, -1);
            font.drawString("Move: " + String.format("%.2f bps", lastDist2 * 20.0 * (double)SessionInfo.mc.timer.timerSpeed), 11.0f + this.x, 30.0f + this.y + 8.0f, -1);
            font.drawString("Kills: " + this.getModule(KillAura.class).killed, 11.0f + this.x, 45.0f + this.y + 8.0f, -1);
            if (!((Boolean)HUD.rainbow.get()).booleanValue()) {
                if (((Boolean)HUD.Breathinglamp.get()).booleanValue()) {
                    Ranbow2 = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 65, 140);
                    Ranbow = HUD.fade(new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()), 65, 80);
                    RenderUtil.drawGradientSideways(this.x, -1.0f + this.y + 14.0f, 55.0f + this.x + 60.0f, this.y + 14.0f, Ranbow.getRGB(), Ranbow2.getRGB());
                } else {
                    RenderUtil.drawRect(this.x, -1.0f + this.y + 14.0f, 55.0f + this.x + 60.0f, this.y + 14.0f, this.hudColor());
                }
            } else {
                RenderUtil.drawGradientSideways(this.x, -1.0f + this.y + 14.0f, 55.0f + this.x + 60.0f, this.y + 14.0f, HUD.rainbow(), ColorCreator.createRainbowFromOffset(-6000, 5));
            }
            RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
            GL11.glPopMatrix();
        }
    }

    private int hudColor() {
        if (!((Boolean)HUD.rainbow.get()).booleanValue()) {
            return new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB();
        }
        return HUD.rainbow();
    }
}

