/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render.WingRenderer;

import heaven.main.module.modules.render.WingRenderer.ColorUtils;
import heaven.main.module.modules.render.Wings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderWings
extends ModelBase {
    final Minecraft mc = Minecraft.getMinecraft();
    private final ResourceLocation location = new ResourceLocation("client/Wings/wings.png");
    private final ModelRenderer wing;
    private final ModelRenderer wingTip;
    private final boolean playerUsesFullHeight;

    public RenderWings() {
        this.playerUsesFullHeight = true;
        this.setTextureOffset("wing.bone", 0, 0);
        this.setTextureOffset("wing.skin", -10, 8);
        this.setTextureOffset("wingtip.bone", 0, 5);
        this.setTextureOffset("wingtip.skin", -10, 18);
        this.wing = new ModelRenderer(this, "wing");
        this.wing.setTextureSize(30, 30);
        this.wing.setRotationPoint(-2.0f, 0.0f, 0.0f);
        this.wing.addBox("bone", -10.0f, -1.0f, -1.0f, 10, 2, 2);
        this.wing.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
        this.wingTip = new ModelRenderer(this, "wingtip");
        this.wingTip.setTextureSize(30, 30);
        this.wingTip.setRotationPoint(-10.0f, 0.0f, 0.0f);
        this.wingTip.addBox("bone", -10.0f, -0.5f, -0.5f, 10, 1, 1);
        this.wingTip.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
        this.wing.addChild(this.wingTip);
    }

    public void renderWings(float partialTicks) {
        if (this.mc.gameSettings.thirdPersonView == 0) {
            return;
        }
        double scale = 1.0;
        double rotate = RenderWings.interpolate(Minecraft.thePlayer.renderYawOffset, Minecraft.thePlayer.renderYawOffset, partialTicks);
        GL11.glPushMatrix();
        GL11.glScaled((double)-1.0, (double)-1.0, (double)1.0);
        GL11.glRotated((double)(180.0 + rotate), (double)0.0, (double)1.0, (double)0.0);
        GL11.glTranslated((double)0.0, (double)(-(this.playerUsesFullHeight ? 1.45 : 1.25) / 1.0), (double)0.0);
        GL11.glTranslated((double)0.0, (double)0.0, (double)0.2);
        if (Minecraft.thePlayer.isSneaking()) {
            GL11.glTranslated((double)0.0, (double)0.125, (double)0.0);
        }
        if (!((Boolean)Wings.rainbow.getValue()).booleanValue()) {
            GL11.glColor3f((float)(((Double)Wings.red.getValue()).floatValue() / 255.0f), (float)(((Double)Wings.green.getValue()).floatValue() / 255.0f), (float)(((Double)Wings.blue.getValue()).floatValue() / 255.0f));
        } else {
            GL11.glColor3f((float)((float)ColorUtils.rainbow(1L, 1.0f).getRed() / 255.0f), (float)((float)ColorUtils.rainbow(1L, 1.0f).getGreen() / 255.0f), (float)((float)ColorUtils.rainbow(1L, 1.0f).getBlue() / 255.0f));
        }
        RenderWings renderWings = this;
        renderWings.mc.getTextureManager().bindTexture(this.location);
        for (int j = 0; j < 2; ++j) {
            float f11 = (float)(System.currentTimeMillis() % 1000L) / 1000.0f * (float)Math.PI * ((Double)Wings.rotateSpeed.getValue()).floatValue();
            this.wing.rotateAngleX = -1.3962634f - (float)Math.cos(f11) * 0.2f;
            this.wing.rotateAngleY = 0.34906584f + (float)Math.sin(f11) * 0.4f;
            this.wing.rotateAngleZ = 0.34906584f;
            this.wingTip.rotateAngleZ = -((float)(Math.sin(f11 + 2.0f) + 0.5)) * 0.75f;
            this.wing.render(((Double)Wings.Size.getValue()).floatValue());
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            if (j != 0) continue;
            GlStateManager.cullFace(1028);
        }
        GlStateManager.cullFace(1029);
        GlStateManager.popMatrix();
    }

    private static double interpolate(float yaw1, float yaw2, float percent) {
        double f = (double)(yaw1 + (yaw2 - yaw1) * percent) % 360.0;
        if (f < 0.0) {
            f += 360.0;
        }
        return f;
    }
}

