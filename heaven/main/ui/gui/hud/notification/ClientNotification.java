/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.hud.notification;

import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.hud.notification.ModNotification;
import heaven.main.ui.simplecore.SimpleRender;
import heaven.main.utils.timer.TimeHelper;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class ClientNotification {
    private final String message;
    private final String message2;
    private final TimeHelper timer;
    private double posY;
    private final double width;
    private final double height;
    private double animationX;
    private String s;
    private final long stayTime;
    final CFontRenderer font;
    private static final ArrayList<ClientNotification> notifications = new ArrayList();
    private final long ms;

    public ClientNotification(String message2, String message, long delay, Type type) {
        this.font = Client.instance.FontLoaders.Comfortaa16;
        this.ms = ClientNotification.getCurrentMS();
        this.message = message;
        this.message2 = message2;
        this.timer = new TimeHelper();
        this.timer.reset();
        this.width = this.font.getStringWidth(message + message2) + 80;
        this.height = 35.0;
        this.animationX = this.width;
        this.stayTime = delay;
        this.posY = 0.0;
        if (type == Type.SUCCESS) {
            this.s = "SUCCESS";
        } else if (type == Type.ERROR) {
            this.s = "ERROR";
        } else if (type == Type.WARNING) {
            this.s = "WARNING";
        } else if (type == Type.INFO) {
            this.s = "INFO";
        }
    }

    public static void sendClientMessage(String message2, String message, long delay, Type type) {
        if (notifications.size() > 8) {
            notifications.remove(0);
        }
        notifications.add(new ClientNotification(message2, message, delay, type));
    }

    public static void drawNotifications() {
        for (int i = 0; i < notifications.size(); ++i) {
            ClientNotification not = notifications.get(i);
            if (not.shouldDelete()) {
                notifications.remove(i);
            }
            not.draw(ModNotification.startY);
            ModNotification.startY -= ModNotification.height + 1.0f;
        }
    }

    public void draw(double getY) {
        this.animationX = AnimationUtil.getAnimationState(this.animationX, this.isFinished() ? this.width : 0.0, (double)AnimationUtil.moveUD(this.isFinished() ? 400.0f : 10.0f, (float)(Math.abs(this.animationX - (this.isFinished() ? this.width : 0.0)) * 20.0), SimpleRender.processFPS(1000.0f, 0.045f), SimpleRender.processFPS(1000.0f, 0.04f)));
        this.posY = this.posY == 0.0 ? getY : AnimationUtil.getAnimationState(this.posY, getY, (double)SimpleRender.processFPS(1000.0f, 13.0f));
        int x1 = (int)((double)ScaledResolution.getScaledWidth() - this.width + this.animationX);
        int x2 = (int)((double)ScaledResolution.getScaledWidth() + this.animationX);
        int y1 = (int)this.posY + 20;
        int y2 = (int)((double)y1 + this.height) - 5;
        Gui.drawRect((double)x1, (double)y1, (double)x2, (float)y2 + 1.0f, new Color(0, 0, 0, 180).getRGB());
        if ((float)(this.stayTime - this.getElapsedTime()) / (float)this.stayTime > 0.0f) {
            if (this.s.equals("WARNING")) {
                RenderUtil.drawGradientSideways(x1, y2, (float)((double)x1 + this.width * (double)((float)(this.stayTime - this.getElapsedTime()) / (float)this.stayTime)), (float)y2 + 1.0f, new Color(255, 210, 70).getRGB(), new Color(255, 210, 70, 180).getRGB());
            }
            if (this.s.equals("INFO")) {
                RenderUtil.drawGradientSideways(x1, y2, (float)((double)x1 + this.width * (double)((float)(this.stayTime - this.getElapsedTime()) / (float)this.stayTime)), (float)y2 + 1.0f, new Color(255, 255, 255).getRGB(), new Color(255, 255, 255, 180).getRGB());
            }
            if (this.s.equals("SUCCESS")) {
                RenderUtil.drawGradientSideways(x1, y2, (float)((double)x1 + this.width * (double)((float)(this.stayTime - this.getElapsedTime()) / (float)this.stayTime)), (float)y2 + 1.0f, new Color(0, 200, 0).getRGB(), new Color(0, 200, 0, 180).getRGB());
            }
        }
        if (this.s.equals("INFO")) {
            RenderUtil.drawImage(new ResourceLocation("client/notifications/info.png"), x1 + 3, (int)((float)((int)((double)y1 + this.height / 2.5 - 10.0)) + 6.0f - 3.0f), 20, 20);
        }
        if (this.s.equals("SUCCESS")) {
            RenderUtil.drawImage(new ResourceLocation("client/notifications/success.png"), x1 + 3, (int)((float)((int)((double)y1 + this.height / 2.5 - 10.0)) + 6.0f - 3.0f), 20, 20);
        }
        if (this.s.equals("ERROR")) {
            RenderUtil.drawImage(new ResourceLocation("client/notifications/error.png"), x1 + 3, (int)((float)((int)((double)y1 + this.height / 2.5 - 10.0)) + 6.0f - 3.0f), 20, 20);
        }
        if (this.s.equals("WARNING")) {
            RenderUtil.drawImage(new ResourceLocation("client/notifications/warning.png"), x1 + 3, (int)((float)((int)((double)y1 + this.height / 2.5 - 10.0)) + 6.0f - 3.0f), 20, 20);
        }
        Client.instance.FontLoaders.thebold20.drawString(this.message2, (float)x1 + 27.0f, (float)((double)y1 + this.height / 2.5 - 5.0), -1);
        Client.instance.FontLoaders.regular16.drawString(this.message, (float)x1 + 27.0f, (float)((double)y1 + this.height / 2.5 + 6.5), -1);
    }

    public boolean shouldDelete() {
        return this.isFinished() && this.animationX >= this.width;
    }

    private boolean isFinished() {
        return this.timer.isDelayComplete(this.stayTime);
    }

    public double getHeight() {
        return this.height;
    }

    private static long getCurrentMS() {
        return System.currentTimeMillis();
    }

    public final long getElapsedTime() {
        return ClientNotification.getCurrentMS() - this.ms;
    }

    public static enum Type {
        SUCCESS,
        ERROR,
        WARNING,
        INFO;

    }
}

