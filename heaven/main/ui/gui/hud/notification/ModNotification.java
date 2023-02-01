/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.hud.notification;

import heaven.main.Client;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.timer.TimeHelper;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.ScaledResolution;

public class ModNotification {
    private final String message;
    private final TimeHelper timer;
    private float posY;
    private final float width;
    public static float height = 0.0f;
    private float animationX;
    private final long stayTime;
    private String s;
    public static final ArrayList<ModNotification> notifications = new ArrayList();
    public static float startY;
    public static float modNotHeight;
    private final long ms = this.getCurrentMS();

    public ModNotification(String message, long delay, Type type) {
        this.message = message;
        this.timer = new TimeHelper();
        this.timer.reset();
        this.width = 140.0f;
        height = 35.0f;
        this.animationX = this.width;
        this.stayTime = delay;
        this.posY = 0.0f;
        if (type == Type.Enabled) {
            this.s = "Enabled";
        } else if (type == Type.Disabled) {
            this.s = "Disabled";
        }
    }

    public static void sendClientMessage(String message, long delay, Type type) {
        if (notifications.size() > 8) {
            notifications.remove(0);
        }
        notifications.add(new ModNotification(message, delay, type));
    }

    public static void drawNotifications() {
        startY = ScaledResolution.getScaledHeight() - 65 - 15;
        for (int i = 0; i < notifications.size(); ++i) {
            ModNotification not = notifications.get(i);
            if (not.shouldDelete()) {
                notifications.remove(i);
            }
            not.draw(startY);
            modNotHeight = height;
            startY -= height + 1.0f - 5.0f - 6.0f;
        }
    }

    public static void clear() {
        notifications.clear();
    }

    public void draw(double getY) {
        this.animationX = (float)AnimationUtil.getAnimationState((double)this.animationX, this.isFinished() ? (double)this.width : 0.0, (double)AnimationUtil.moveUD(this.isFinished() ? 400.0f : 10.0f, (float)(Math.abs((double)this.animationX - (this.isFinished() ? (double)this.width : 0.0)) * 20.0), SimpleRender.processFPS(1000.0f, 0.045f), SimpleRender.processFPS(1000.0f, 0.04f)));
        this.posY = (float)((double)this.posY == 0.0 ? getY : AnimationUtil.getAnimationState((double)this.posY, getY, (double)SimpleRender.processFPS(1000.0f, 13.0f)));
        float x1 = (float)ScaledResolution.getScaledWidth() - this.width + this.animationX;
        float x2 = (float)ScaledResolution.getScaledWidth() + this.animationX;
        float y1 = this.posY + 20.0f;
        float y2 = y1 + height - 5.0f;
        RenderUtil.drawGradientSideways(x1 - 1.0f, y1 + 11.0f, x2 + 1.0f - 8.0f, y2 + 1.0f, new Color(0, 0, 0, 180).getRGB(), new Color(0, 0, 0, 180).getRGB());
        RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
        Client.instance.FontLoaders.regular17.drawString(this.message, x1 + 20.0f, (float)((double)y1 + (double)height / 2.5 + 8.0) - 5.0f + 1.0f, -1);
        if (this.s.equals("Enabled")) {
            Client.instance.FontLoaders.novoicons25.drawString("H", (double)x1 + 3.5, (double)((float)((double)y1 + (double)height / 2.5 + 8.0) - 5.0f + 2.0f), (Boolean)HUD.rainbow.get() == false ? new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB() : new Color(39, 252, 110).getRGB());
        } else if (this.s.equals("Disabled")) {
            Client.instance.FontLoaders.novoicons25.drawString("I", (double)x1 + 3.5, (double)((float)((double)y1 + (double)height / 2.5 + 8.0) - 5.0f + 2.0f), (Boolean)HUD.rainbow.get() == false ? new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB() : new Color(252, 109, 109).getRGB());
        }
    }

    public boolean shouldDelete() {
        return this.isFinished() && this.animationX >= this.width;
    }

    private boolean isFinished() {
        return this.timer.isDelayComplete(this.stayTime);
    }

    public double getHeight() {
        return height;
    }

    private long getCurrentMS() {
        return System.currentTimeMillis();
    }

    public final long getElapsedTime() {
        return this.getCurrentMS() - this.ms;
    }

    public static enum Type {
        Enabled,
        Disabled;

    }
}

