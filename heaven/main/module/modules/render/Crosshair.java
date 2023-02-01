/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.utils.render.color.Colors;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MovementInput;

public class Crosshair
extends Module {
    private final Option<Boolean> DYNAMIC = new Option<Boolean>("Dynamic", true);
    public static Numbers<Double> GAP = new Numbers<Double>("Gap", 5.0, 0.25, 15.0, 0.25);
    private final Numbers<Double> WIDTH = new Numbers<Double>("Width", 2.0, 0.25, 10.0, 0.25);
    public static Numbers<Double> SIZE = new Numbers<Double>("Size", 7.0, 0.25, 15.0, 0.25);

    public Crosshair() {
        super("Crosshair", ModuleType.Render);
        this.addValues(this.DYNAMIC, GAP, this.WIDTH, SIZE);
    }

    @EventHandler
    public void onGui(EventRender2D e) {
        int red = ((Double)HUD.r.getValue()).intValue();
        int green = ((Double)HUD.g.getValue()).intValue();
        int blue = ((Double)HUD.b.getValue()).intValue();
        int alph = 255;
        double gap = (Double)GAP.getValue();
        double width = (Double)this.WIDTH.getValue();
        double size = (Double)SIZE.getValue();
        ScaledResolution scaledRes = new ScaledResolution(mc);
        RenderUtil.rectangleBordered((double)(scaledRes.getScaledWidth() / 2) - width, (double)(scaledRes.getScaledHeight() / 2) - gap - size - (double)(this.isMoving() ? 2 : 0), (double)((float)(scaledRes.getScaledWidth() / 2) + 1.0f) + width, (double)(scaledRes.getScaledHeight() / 2) - gap - (double)(this.isMoving() ? 2 : 0), 0.5, Colors.getColor(red, green, blue, 255), new Color(0, 0, 0, 255).getRGB());
        RenderUtil.rectangleBordered((double)(scaledRes.getScaledWidth() / 2) - width, (double)(scaledRes.getScaledHeight() / 2) + gap + 1.0 + (double)(this.isMoving() ? 2 : 0) - 0.15, (double)((float)(scaledRes.getScaledWidth() / 2) + 1.0f) + width, (double)(scaledRes.getScaledHeight() / 2 + 1) + gap + size + (double)(this.isMoving() ? 2 : 0) - 0.15, 0.5, Colors.getColor(red, green, blue, 255), new Color(0, 0, 0, 255).getRGB());
        RenderUtil.rectangleBordered((double)(scaledRes.getScaledWidth() / 2) - gap - size - (double)(this.isMoving() ? 2 : 0) + 0.15, (double)(scaledRes.getScaledHeight() / 2) - width, (double)(scaledRes.getScaledWidth() / 2) - gap - (double)(this.isMoving() ? 2 : 0) + 0.15, (double)((float)(scaledRes.getScaledHeight() / 2) + 1.0f) + width, 0.5, Colors.getColor(red, green, blue, 255), new Color(0, 0, 0, 255).getRGB());
        RenderUtil.rectangleBordered((double)(scaledRes.getScaledWidth() / 2 + 1) + gap + (double)(this.isMoving() ? 2 : 0), (double)(scaledRes.getScaledHeight() / 2) - width, (double)(scaledRes.getScaledWidth() / 2) + size + gap + 1.0 + (double)(this.isMoving() ? 2 : 0), (double)((float)(scaledRes.getScaledHeight() / 2) + 1.0f) + width, 0.5, Colors.getColor(red, green, blue, 255), new Color(0, 0, 0, 255).getRGB());
    }

    @Override
    public boolean isMoving() {
        if (((Boolean)this.DYNAMIC.getValue()).booleanValue()) {
            if (!Minecraft.thePlayer.isCollidedHorizontally) {
                if (!Minecraft.thePlayer.isSneaking()) {
                    MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
                    if (MovementInput.moveForward == 0.0f) {
                        MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
                        return MovementInput.moveStrafe != 0.0f;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}

