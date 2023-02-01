/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package heaven.main.utils.render;

import com.tessellate.Tessellation;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.module.modules.render.HUD;
import heaven.main.utils.chat.Helper;
import heaven.main.utils.render.RenderUtils;
import heaven.main.utils.render.gl.GLClientState;
import heaven.main.utils.vec.Vec2f;
import heaven.main.utils.vec.Vec3f;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class RenderUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static int fade;
    private static boolean fadeTurn;
    private static final List<Integer> csBuffer;
    private static final Consumer<Integer> ENABLE_CLIENT_STATE;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static final Consumer<Integer> DISABLE_CLIENT_STATE;
    private static Framebuffer buffer;
    public static float delta;
    private static Frustum frustrum;
    public static final Tessellation tessellator;

    public static int width() {
        new ScaledResolution(Minecraft.getMinecraft());
        return ScaledResolution.getScaledWidth();
    }

    public static int height() {
        new ScaledResolution(Minecraft.getMinecraft());
        return ScaledResolution.getScaledHeight();
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)2896);
        GL11.glDepthMask((boolean)false);
        GL11.glHint((int)3154, (int)4354);
    }

    public static void drawESP(Entity entity, int color) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)RenderUtil.mc.timer.renderPartialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)RenderUtil.mc.timer.renderPartialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)RenderUtil.mc.timer.renderPartialTicks;
        double width = Math.abs(entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX);
        double height = Math.abs(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY);
        Vec3 vec = new Vec3(x - width / 2.0, y, z - width / 2.0);
        Vec3 vec2 = new Vec3(x + width / 2.0, y + height, z + width / 2.0);
        RenderUtil.pre3D();
        RenderUtil.mc.entityRenderer.setupCameraTransform(RenderUtil.mc.timer.renderPartialTicks, 2);
        RenderUtils.glColor(color);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(vec.xCoord - RenderManager.renderPosX, vec.yCoord - RenderManager.renderPosY, vec.zCoord - RenderManager.renderPosZ, vec2.xCoord - RenderManager.renderPosX, vec2.yCoord - RenderManager.renderPosY, vec2.zCoord - RenderManager.renderPosZ));
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        RenderUtil.post3D();
    }

    public static void drawPath(Vec3 vec) {
        double x = vec.xCoord - RenderManager.renderPosX;
        double y = vec.yCoord - RenderManager.renderPosY;
        double z = vec.zCoord - RenderManager.renderPosZ;
        double width = 0.3;
        double height = Minecraft.thePlayer.getEyeHeight();
        RenderUtil.pre3D();
        GL11.glLoadIdentity();
        RenderUtil.mc.entityRenderer.setupCameraTransform(RenderUtil.mc.timer.renderPartialTicks, 2);
        if (fadeTurn) {
            if (++fade >= 255) {
                fadeTurn = false;
            }
        } else if (--fade <= 0) {
            fadeTurn = true;
        }
        int[] colors = new int[]{new Color(255, 0, 0, fade).getRGB(), new Color(255, 0, 0, 0).getRGB()};
        for (int i = 0; i < 2; ++i) {
            RenderUtils.glColor(colors[i]);
            GL11.glLineWidth((float)(3 - (i << 1)));
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)(x - 0.3), (double)y, (double)(z - 0.3));
            GL11.glVertex3d((double)(x - 0.3), (double)y, (double)(z - 0.3));
            GL11.glVertex3d((double)(x - 0.3), (double)(y + height), (double)(z - 0.3));
            GL11.glVertex3d((double)(x + 0.3), (double)(y + height), (double)(z - 0.3));
            GL11.glVertex3d((double)(x + 0.3), (double)y, (double)(z - 0.3));
            GL11.glVertex3d((double)(x - 0.3), (double)y, (double)(z - 0.3));
            GL11.glVertex3d((double)(x - 0.3), (double)y, (double)(z + 0.3));
            GL11.glEnd();
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)(x + 0.3), (double)y, (double)(z + 0.3));
            GL11.glVertex3d((double)(x + 0.3), (double)(y + height), (double)(z + 0.3));
            GL11.glVertex3d((double)(x - 0.3), (double)(y + height), (double)(z + 0.3));
            GL11.glVertex3d((double)(x - 0.3), (double)y, (double)(z + 0.3));
            GL11.glVertex3d((double)(x + 0.3), (double)y, (double)(z + 0.3));
            GL11.glVertex3d((double)(x + 0.3), (double)y, (double)(z - 0.3));
            GL11.glEnd();
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)(x + 0.3), (double)(y + height), (double)(z + 0.3));
            GL11.glVertex3d((double)(x + 0.3), (double)(y + height), (double)(z - 0.3));
            GL11.glEnd();
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)(x - 0.3), (double)(y + height), (double)(z + 0.3));
            GL11.glVertex3d((double)(x - 0.3), (double)(y + height), (double)(z - 0.3));
            GL11.glEnd();
        }
        RenderUtil.post3D();
    }

    public static void post3D() {
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawBorderedRect(double x, double y, double x2, double y2, double l1, int col1, int col2) {
        Gui.drawRect(x, y, x2, y2, col2);
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glLineWidth((float)((float)l1));
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        Gui.drawRect(x, y, x2, y2, col2);
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f2, (float)f3, (float)f4, (float)f);
        GL11.glLineWidth((float)l1);
        GL11.glBegin((int)1);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x2, (double)y);
        GL11.glVertex2d((double)x, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void startDrawing() {
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        Helper.mc.entityRenderer.setupCameraTransform(Helper.mc.timer.renderPartialTicks, 0);
    }

    public static void stopDrawing() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        RenderUtil.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }
        float var11 = (float)(color >> 24 & 0xFF) / 255.0f;
        float var6 = (float)(color >> 16 & 0xFF) / 255.0f;
        float var7 = (float)(color >> 8 & 0xFF) / 255.0f;
        float var8 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0).endVertex();
        worldRenderer.pos(right, bottom, 0.0).endVertex();
        worldRenderer.pos(right, top, 0.0).endVertex();
        worldRenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        float f = (float)(c >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(c >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(c >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(c & 0xFF) / 255.0f;
        float theta = (float)(6.2831852 / (double)num_segments);
        float p = (float)Math.cos(theta);
        float s = (float)Math.sin(theta);
        float x = r * 2.0f;
        float y = 0.0f;
        RenderUtil.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f((float)(x + cx), (float)(y + cy));
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        GL11.glEnd();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        RenderUtil.disableGL2D();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth((float)lineWdith);
        GL11.glColor4f((float)lineRed, (float)lineGreen, (float)lineBlue, (float)lineAlpha);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    private static int HUDColor() {
        return new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB();
    }

    public static void LongRect(double x, double y, double x1, double y1, double size) {
        RenderUtil.rectangleBordered(x + 2.5, y + 2.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, new Color(0, 0, 0, 200).getRGB(), new Color(233, 233, 233).getRGB());
    }

    public static void Weave(double x, double y, double x1, double y1, double size, float color2, float color3) {
        int rainbowCol = RenderUtils.rainbow(System.nanoTime(), 0.0f, 1.0f).getRGB();
        Color col = new Color(rainbowCol);
        Color cuscolor = new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue());
        Color Ranbow = new Color((float)cuscolor.getRed() / 255.0f, (float)cuscolor.getGreen() / 255.0f, (float)cuscolor.getBlue() / 255.0f, 1.0f - (float)col.getGreen() / 400.0f);
        int rainbowCol2 = RenderUtils.rainbow(System.nanoTime() + 10L, 0.0f, 1.0f).getRGB();
        Color col2 = new Color(rainbowCol2);
        Color Ranbow2 = new Color((float)cuscolor.getRed() / 255.0f, (float)cuscolor.getGreen() / 255.0f, (float)cuscolor.getBlue() / 255.0f, 1.0f - (float)col2.getGreen() / 400.0f);
        RenderUtil.drawGradientSideways(x + 2.5, y + 2.5, x1 + size - 2.5, y1 + size - 2.5, new Color(0, 0, 0, 180).getRGB(), new Color(0, 0, 0, 0).getRGB());
        if (((Boolean)HUD.rainbow.getValue()).booleanValue()) {
            heaven.main.ui.gui.clickgui.RenderUtil.drawGradientSidewaysV(x + 2.5, y + 2.5, x1 - 273.0 + 59.0, y1 + size - 2.5, (Boolean)HUD.rainbow.getValue() != false ? (int)color2 : RenderUtil.HUDColor(), (Boolean)HUD.rainbow.getValue() != false ? (int)color3 : RenderUtil.HUDColor());
        } else {
            heaven.main.ui.gui.clickgui.RenderUtil.drawGradientSidewaysV(x + 2.5, y + 2.5, x1 - 273.0 + 59.0, y1 + size - 2.5, (Boolean)HUD.Breathinglamp.getValue() != false ? Ranbow2.getRGB() : RenderUtil.HUDColor(), (Boolean)HUD.Breathinglamp.getValue() != false ? Ranbow.getRGB() : RenderUtil.HUDColor());
        }
    }

    public static void Gamesense(double x, double y, double x1, double y1, double size, float color2, float color3) {
        int rainbowCol = RenderUtils.rainbow(System.nanoTime(), 0.0f, 1.0f).getRGB();
        Color col = new Color(rainbowCol);
        Color cuscolor = new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue());
        Color Ranbow = new Color((float)cuscolor.getRed() / 255.0f, (float)cuscolor.getGreen() / 255.0f, (float)cuscolor.getBlue() / 255.0f, 1.0f - (float)col.getGreen() / 400.0f);
        int rainbowCol2 = RenderUtils.rainbow(System.nanoTime() + 10L, 0.0f, 1.0f).getRGB();
        Color col2 = new Color(rainbowCol2);
        Color Ranbow2 = new Color((float)cuscolor.getRed() / 255.0f, (float)cuscolor.getGreen() / 255.0f, (float)cuscolor.getBlue() / 255.0f, 1.0f - (float)col2.getGreen() / 400.0f);
        RenderUtil.rectangleBordered(x + 2.5, y + 2.5, x1 + size - 2.5, y1 + size - 2.5, 0.5, new Color(0, 0, 0, 150).getRGB(), new Color(0, 0, 0, 0).getRGB());
        if (((Boolean)HUD.rainbow.getValue()).booleanValue()) {
            RenderUtil.drawGradientSideways(x + size * 3.0, y + 3.0, x1 - size * 2.0, y + 4.0, (Boolean)HUD.rainbow.getValue() != false ? (int)color2 : RenderUtil.HUDColor(), (Boolean)HUD.rainbow.getValue() != false ? (int)color3 : RenderUtil.HUDColor());
        } else {
            RenderUtil.drawGradientSideways(x + size * 3.0, y + 3.0, x1 - size * 2.0, y + 4.0, (Boolean)HUD.Breathinglamp.getValue() != false ? Ranbow2.getRGB() : RenderUtil.HUDColor(), (Boolean)HUD.Breathinglamp.getValue() != false ? Ranbow.getRGB() : RenderUtil.HUDColor());
        }
    }

    public static void entityESPBox(Entity e, Color color, EventRender3D event) {
        double renderPosX = RenderManager.renderPosX;
        double renderPosY = RenderManager.renderPosY;
        double renderPosZ = RenderManager.renderPosZ;
        double posX = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)event.getPartialTicks() - renderPosX;
        double posY = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)event.getPartialTicks() - renderPosY;
        double posZ = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)event.getPartialTicks() - renderPosZ;
        AxisAlignedBB box = AxisAlignedBB.fromBounds(posX - (double)e.width, posY, posZ - (double)e.width, posX + (double)e.width, posY + (double)e.height + 0.2, posZ + (double)e.width);
        if (e instanceof EntityLivingBase) {
            box = AxisAlignedBB.fromBounds(posX - (double)e.width + 0.2, posY, posZ - (double)e.width + 0.2, posX + (double)e.width - 0.2, posY + (double)e.height + (e.isSneaking() ? 0.02 : 0.2), posZ + (double)e.width - 0.2);
        }
        EntityLivingBase e2 = (EntityLivingBase)e;
        if (e2.hurtTime != 0) {
            GL11.glColor4f((float)1.0f, (float)0.2f, (float)0.1f, (float)0.2f);
        } else {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.2f);
        }
        RenderUtil.drawBoundingBox(box);
    }

    public static void entityESPBox(Entity e, EventRender3D event) {
        double renderPosX = RenderManager.renderPosX;
        double renderPosY = RenderManager.renderPosY;
        double renderPosZ = RenderManager.renderPosZ;
        double posX = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)event.getPartialTicks() - renderPosX;
        double posY = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)event.getPartialTicks() - renderPosY;
        double posZ = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)event.getPartialTicks() - renderPosZ;
        AxisAlignedBB box = AxisAlignedBB.fromBounds(posX - (double)e.width, posY, posZ - (double)e.width, posX + (double)e.width, posY + (double)e.height + 0.2, posZ + (double)e.width);
        if (e instanceof EntityLivingBase) {
            box = AxisAlignedBB.fromBounds(posX - (double)e.width + 0.2, posY, posZ - (double)e.width + 0.2, posX + (double)e.width - 0.2, posY + (double)e.height + (e.isSneaking() ? 0.02 : 0.2), posZ + (double)e.width - 0.2);
        }
        assert (e instanceof EntityLivingBase);
        EntityLivingBase e2 = (EntityLivingBase)e;
        if (e2.hurtTime != 0) {
            GL11.glColor4f((float)1.0f, (float)0.2f, (float)0.1f, (float)0.2f);
        } else {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.2f);
        }
        RenderUtil.drawBoundingBox(box);
    }

    public static double[] convertTo2D(double x, double y, double z) {
        double[] dArray;
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int)3);
        IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer((int)16);
        FloatBuffer projection = BufferUtils.createFloatBuffer((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenCoords);
        if (result) {
            double[] dArray2 = new double[3];
            dArray2[0] = screenCoords.get(0);
            dArray2[1] = (float)Display.getHeight() - screenCoords.get(1);
            dArray = dArray2;
            dArray2[2] = screenCoords.get(2);
        } else {
            dArray = null;
        }
        return dArray;
    }

    public static void doGlScissor(float x, float y, float windowWidth2, float windowHeight2) {
        int scaleFactor = 1;
        float k = RenderUtil.mc.gameSettings.guiScale;
        if (k == 0.0f) {
            k = 1000.0f;
        }
        while ((float)scaleFactor < k) {
            if (Minecraft.displayWidth / (scaleFactor + 1) < 320) break;
            if (Minecraft.displayHeight / (scaleFactor + 1) < 240) break;
            ++scaleFactor;
        }
        GL11.glScissor((int)((int)(x * (float)scaleFactor)), (int)((int)((float)Minecraft.displayHeight - (y + windowHeight2) * (float)scaleFactor)), (int)((int)(windowWidth2 * (float)scaleFactor)), (int)((int)(windowHeight2 * (float)scaleFactor)));
    }

    public static void doGlScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scale = new ScaledResolution(mc);
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k) {
            if (Minecraft.displayWidth / (scaleFactor + 1) < 320) break;
            if (Minecraft.displayHeight / (scaleFactor + 1) < 240) break;
            ++scaleFactor;
        }
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glScissor((int)(x * scaleFactor), (int)(Minecraft.displayHeight - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)(height * scaleFactor));
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static void arcEllipse(float n, float n2, float n3, float n4, float n5, float n6, int n7) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        if (n3 > n4) {
            float n8 = n4;
            n4 = n3;
            n3 = n8;
        }
        float p_color_3_ = (float)(n7 >> 24 & 0xFF) / 255.0f;
        float p_color_0_ = (float)(n7 >> 16 & 0xFF) / 255.0f;
        float p_color_1_ = (float)(n7 >> 8 & 0xFF) / 255.0f;
        float p_color_2_ = (float)(n7 & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(p_color_0_, p_color_1_, p_color_2_, p_color_3_);
        if (p_color_3_ > 0.5f) {
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)1.0f);
            GL11.glBegin((int)3);
            for (float n9 = n4; n9 >= n3; n9 -= 4.0f) {
                GL11.glVertex2f((float)(n + (float)Math.cos((double)n9 * Math.PI / 180.0) * (n5 * 1.001f)), (float)(n2 + (float)Math.sin((double)n9 * Math.PI / 180.0) * (n6 * 1.001f)));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
        }
        GL11.glBegin((int)6);
        for (float n10 = n4; n10 >= n3; n10 -= 4.0f) {
            GL11.glVertex2f((float)(n + (float)Math.cos((double)n10 * Math.PI / 180.0) * n5), (float)(n2 + (float)Math.sin((double)n10 * Math.PI / 180.0) * n6));
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void circle(float n, float n2, float n3, int n4) {
        RenderUtil.arc(n, n2, 0.0f, 360.0f, n3, n4);
    }

    public static void circle(float n, float n2, float n3, Color color) {
        RenderUtil.arc(n, n2, 0.0f, 360.0f, n3, color);
    }

    public static void arc(float n, float n2, float n3, float n4, float n5, int n6) {
        RenderUtil.arcEllipse(n, n2, n3, n4, n5, n5, n6);
    }

    public static void arc(float n, float n2, float n3, float n4, float n5, Color color) {
        RenderUtil.arcEllipse(n, n2, n3, n4, n5, n5, color);
    }

    public static void arcEllipse(float n, float n2, float n3, float n4, float n5, float n6, Color color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        if (n3 > n4) {
            float n7 = n4;
            n4 = n3;
            n3 = n7;
        }
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        if ((float)color.getAlpha() > 0.5f) {
            GL11.glEnable((int)2848);
            GL11.glLineWidth((float)1.0f);
            GL11.glBegin((int)3);
            for (float n8 = n4; n8 >= n3; n8 -= 4.0f) {
                GL11.glVertex2f((float)(n + (float)Math.cos((double)n8 * Math.PI / 180.0) * (n5 * 1.001f)), (float)(n2 + (float)Math.sin((double)n8 * Math.PI / 180.0) * (n6 * 1.001f)));
            }
            GL11.glEnd();
            GL11.glDisable((int)2848);
        }
        GL11.glBegin((int)6);
        for (float n9 = n4; n9 >= n3; n9 -= 4.0f) {
            GL11.glVertex2f((float)(n + (float)Math.cos((double)n9 * Math.PI / 180.0) * n5), (float)(n2 + (float)Math.sin((double)n9 * Math.PI / 180.0) * n6));
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void makeScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)scaledResolution.getScaledHeight() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }

    public static void drawCornerBox(double x, double y, double x2, double y2, double lw, Color color) {
        double width = Math.abs(x2 - x);
        double height = Math.abs(y2 - y);
        double halfWidth = width / 4.0;
        double halfHeight = height / 4.0;
        RenderUtils.start2D();
        GL11.glPushMatrix();
        GL11.glLineWidth((float)((float)lw));
        RenderUtils.setColor(color);
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)(x + halfWidth), (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + halfHeight));
        GL11.glEnd();
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)x, (double)(y + height - halfHeight));
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2d((double)(x + halfWidth), (double)(y + height));
        GL11.glEnd();
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)(x + width - halfWidth), (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height - halfHeight));
        GL11.glEnd();
        GL11.glBegin((int)3);
        GL11.glVertex2d((double)(x + width), (double)(y + halfHeight));
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glVertex2d((double)(x + width - halfWidth), (double)y);
        GL11.glEnd();
        GL11.glPopMatrix();
        RenderUtils.stop2D();
    }

    public static void drawLine(Vec2f start, Vec2f end, float width) {
        RenderUtil.drawLine(start.getX(), start.getY(), end.getX(), end.getY(), width);
    }

    public static void drawLine(Vec3f start, Vec3f end, float width) {
        RenderUtil.drawLine((float)start.getX(), (float)start.getY(), (float)start.getZ(), (float)end.getX(), (float)end.getY(), (float)end.getZ(), width);
    }

    public static void drawLine(float x, float y, float x1, float y1, float width) {
        RenderUtil.drawLine(x, y, 0.0f, x1, y1, 0.0f, width);
    }

    public static void setupRender(boolean start) {
        if (start) {
            GlStateManager.enableBlend();
            GL11.glEnable((int)2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            GL11.glHint((int)3154, (int)4354);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable((int)2848);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(!start);
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Frustum frustrum = new Frustum();
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtil.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static void setupClientState(GLClientState state, boolean enabled) {
        csBuffer.clear();
        if (state.ordinal() > 0) {
            csBuffer.add(state.getCap());
        }
        csBuffer.add(32884);
        csBuffer.forEach(enabled ? ENABLE_CLIENT_STATE : DISABLE_CLIENT_STATE);
    }

    public static void drawLine(float x, float y, float z, float x1, float y1, float z1, float width) {
        GL11.glLineWidth((float)width);
        RenderUtil.setupRender(true);
        RenderUtil.setupClientState(GLClientState.VERTEX, true);
        tessellator.addVertex(x, y, z).addVertex(x1, y1, z1).draw(3);
        RenderUtil.setupClientState(GLClientState.VERTEX, false);
        RenderUtil.setupRender(false);
    }

    static {
        tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
        csBuffer = new ArrayList<Integer>();
        ENABLE_CLIENT_STATE = GL11::glEnableClientState;
        DISABLE_CLIENT_STATE = GL11::glEnableClientState;
    }
}

