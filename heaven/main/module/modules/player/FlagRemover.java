/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.event.events.world.EventMove;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.timer.TimerUtil;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class FlagRemover
extends Module {
    private final TimerUtil timerUtil = new TimerUtil();

    public FlagRemover() {
        super("FlagRemover", ModuleType.Player);
    }

    @Override
    public void onEnable() {
        this.timerUtil.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.timerUtil.reset();
        super.onDisable();
    }

    @EventHandler
    public void on2D(EventRender2D e) {
        ScaledResolution sr = new ScaledResolution(mc);
        int x = sr.getScaledWidth() / 2;
        int y = sr.getScaledHeight() - 65;
        double ok = Math.min(100.0f, (float)this.timerUtil.getElapsedTime() / 5000.0f * 100.0f);
        RenderUtil.drawRect(x - 50, y - 10, x + 50, y, new Color(0, 0, 0).getRGB());
        RenderUtil.drawRect((double)(x - 50), (double)(y - 10), (double)(x - 50) + ok, (double)y, new Color(49151).getRGB());
        Client.instance.FontLoaders.Comfortaa16.drawStringWithShadow(String.format("%.0f", ok) + "%", (float)x - (float)Client.instance.FontLoaders.Comfortaa16.getStringWidth(String.format("%.0f", ok) + "%") / 2.0f, y - 10, -1);
    }

    @EventHandler
    public void onTick(EventTick e) {
        if (this.timerUtil.hasReached(5000.0)) {
            this.timerUtil.reset();
            this.setEnabled(false);
        }
    }

    @EventHandler
    public void onMove(EventMove e) {
        MoveUtils.setSpeed(0.0);
        MoveUtils.setSpeed(e, 0.0);
        Minecraft.thePlayer.motionY = 0.0;
        e.setY(0.0);
        Minecraft.thePlayer.setVelocity(0.0, 0.0, 0.0);
    }
}

