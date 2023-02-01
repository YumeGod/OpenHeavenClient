/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.render.RenderUtils;
import heaven.main.utils.render.gl.GLUtils;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.util.LinkedList;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class Breadcrumbs
extends Module {
    public final Numbers<Double> colorRedValue = new Numbers<Double>("Red", 255.0, 0.0, 255.0, 1.0);
    public final Numbers<Double> colorGreenValue = new Numbers<Double>("Green", 179.0, 0.0, 255.0, 1.0);
    public final Numbers<Double> colorBlueValue = new Numbers<Double>("Blue", 72.0, 0.0, 255.0, 1.0);
    public final Option<Boolean> colorRainbow = new Option<Boolean>("Rainbow", false);
    private final LinkedList<double[]> positions = new LinkedList();

    public Breadcrumbs() {
        super("Breadcrumbs", new String[]{"Breadcrumb"}, ModuleType.Render);
        this.addValues(this.colorRedValue, this.colorGreenValue, this.colorBlueValue, this.colorRainbow);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    public void onRender3D(EventRender3D event) {
        int color = (Boolean)this.colorRainbow.getValue() != false ? RenderUtils.rainbow(0) : new Color(((Double)this.colorRedValue.getValue()).intValue(), ((Double)this.colorGreenValue.getValue()).intValue(), ((Double)this.colorBlueValue.getValue()).intValue()).getRGB();
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            GL11.glPushMatrix();
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            Breadcrumbs.mc.entityRenderer.disableLightmap();
            GL11.glBegin((int)3);
            GLUtils.glColor(color);
            double renderPosX = Breadcrumbs.mc.getRenderManager().viewerPosX;
            double renderPosY = Breadcrumbs.mc.getRenderManager().viewerPosY;
            double renderPosZ = Breadcrumbs.mc.getRenderManager().viewerPosZ;
            for (double[] pos : this.positions) {
                GL11.glVertex3d((double)(pos[0] - renderPosX), (double)(pos[1] - renderPosY), (double)(pos[2] - renderPosZ));
            }
            GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
            GL11.glEnd();
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glPopMatrix();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            double[] dArray = new double[3];
            dArray[0] = Minecraft.thePlayer.posX;
            dArray[1] = Minecraft.thePlayer.getEntityBoundingBox().minY;
            dArray[2] = Minecraft.thePlayer.posZ;
            this.positions.add(dArray);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onEnable() {
        if (Minecraft.thePlayer == null) {
            return;
        }
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            double[] dArray = new double[3];
            dArray[0] = Minecraft.thePlayer.posX;
            dArray[1] = Minecraft.thePlayer.getEntityBoundingBox().minY + (double)(Minecraft.thePlayer.getEyeHeight() * 0.5f);
            dArray[2] = Minecraft.thePlayer.posZ;
            this.positions.add(dArray);
            double[] dArray2 = new double[3];
            dArray2[0] = Minecraft.thePlayer.posX;
            dArray2[1] = Minecraft.thePlayer.getEntityBoundingBox().minY;
            dArray2[2] = Minecraft.thePlayer.posZ;
            this.positions.add(dArray2);
        }
        super.onEnable();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onDisable() {
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.clear();
        }
        super.onDisable();
    }
}

