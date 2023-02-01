/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class ChestESP
extends Module {
    public final Mode<String> mode = new Mode("Mode", new String[]{"Outline", "Filled", "Chams", "Box"}, "Outline");
    public final Option<Boolean> hudColor = new Option<Boolean>("hudColor", true);
    public final Numbers<Double> r = new Numbers<Double>("Red", 120.0, 0.0, 255.0, 5.0, () -> (Boolean)this.hudColor.get() == false);
    public final Numbers<Double> g = new Numbers<Double>("Green", 120.0, 0.0, 255.0, 5.0, () -> (Boolean)this.hudColor.get() == false);
    public final Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 5.0, () -> (Boolean)this.hudColor.get() == false);
    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);

    public ChestESP() {
        super("ChestESP", ModuleType.Render);
        this.addValues(this.mode, this.hudColor, this.r, this.g, this.b);
    }

    private void renderOutline(TileEntity tileEntity) {
        double posX = tileEntity.getPos().getX();
        double posY = tileEntity.getPos().getY();
        double posZ = tileEntity.getPos().getZ();
        AxisAlignedBB axisAlignedBB = null;
        Block block = Minecraft.theWorld.getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
        Block x1 = Minecraft.theWorld.getBlockState(new BlockPos(posX + 1.0, posY, posZ)).getBlock();
        Block x2 = Minecraft.theWorld.getBlockState(new BlockPos(posX - 1.0, posY, posZ)).getBlock();
        Block z1 = Minecraft.theWorld.getBlockState(new BlockPos(posX, posY, posZ + 1.0)).getBlock();
        Block z2 = Minecraft.theWorld.getBlockState(new BlockPos(posX, posY, posZ - 1.0)).getBlock();
        if (x1 == block) {
            axisAlignedBB = this.renderOutlineZero(posX, posY, posZ);
        } else if (z2 == block) {
            axisAlignedBB = this.renderOutlineFirst(posX, posY, posZ);
        } else if (x2 != block && z1 != block) {
            axisAlignedBB = this.renderOutlineSecond(posX, posY, posZ);
        }
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GL11.glEnable((int)2848);
        float[] colors = this.getColorForTileEntity();
        RenderHelper.drawCompleteBoxFilled(axisAlignedBB, 1.0f, this.toRGBAHex(colors[0] / 255.0f, colors[1] / 255.0f, colors[2] / 255.0f, 0.2f));
        GL11.glDisable((int)2848);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
    }

    private AxisAlignedBB renderOutlineSecond(double posX, double posY, double posZ) {
        RenderManager renderManager = mc.getRenderManager();
        return new AxisAlignedBB(posX + (double)0.05f - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ + (double)0.05f - RenderManager.renderPosZ, posX + (double)0.95f - RenderManager.renderPosX, posY + (double)0.9f - RenderManager.renderPosY, posZ + (double)0.95f - RenderManager.renderPosZ);
    }

    private AxisAlignedBB renderOutlineFirst(double posX, double posY, double posZ) {
        RenderManager renderManager = mc.getRenderManager();
        return new AxisAlignedBB(posX + (double)0.05f - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ + (double)0.05f - RenderManager.renderPosZ - 1.0, posX + (double)0.95f - RenderManager.renderPosX, posY + (double)0.9f - RenderManager.renderPosY, posZ + (double)0.95f - RenderManager.renderPosZ);
    }

    private AxisAlignedBB renderOutlineZero(double posX, double posY, double posZ) {
        RenderManager renderManager = mc.getRenderManager();
        return new AxisAlignedBB(posX + (double)0.05f - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ + (double)0.05f - RenderManager.renderPosZ, posX + (double)1.95f - RenderManager.renderPosX, posY + (double)0.9f - RenderManager.renderPosY, posZ + (double)0.95f - RenderManager.renderPosZ);
    }

    public float[] getColorForTileEntity() {
        Color color = (Boolean)this.hudColor.get() != false ? new Color(((Double)this.r.get()).intValue(), ((Double)this.g.get()).intValue(), ((Double)this.b.get()).intValue()) : new Color(((Double)HUD.r.get()).intValue(), ((Double)HUD.g.get()).intValue(), ((Double)HUD.b.get()).intValue());
        return new float[]{color.getRed(), color.getGreen(), color.getBlue(), 200.0f};
    }

    public int toRGBAHex(float r, float g, float b, float a) {
        return ((int)(a * 255.0f) & 0xFF) << 24 | ((int)(r * 255.0f) & 0xFF) << 16 | ((int)(g * 255.0f) & 0xFF) << 8 | (int)(b * 255.0f) & 0xFF;
    }

    @EventHandler
    public void onRender3D(EventRender3D event) {
        if (this.mode.is("Filled")) {
            for (TileEntity tileEntity : Minecraft.theWorld.getLoadedTileEntityList()) {
                if (!(tileEntity instanceof TileEntityChest) && !(tileEntity instanceof TileEntityEnderChest) || tileEntity.isInvalid()) continue;
                if (Minecraft.theWorld.getBlockState(tileEntity.getPos()) == null) continue;
                this.renderOutline(tileEntity);
            }
        }
    }

    @EventHandler
    public void on2D(EventRender2D event) {
        if (this.mode.is("Box")) {
            for (TileEntity collectedEntity : Minecraft.theWorld.getLoadedTileEntityList().stream().filter(e -> e instanceof TileEntityChest).collect(Collectors.toList())) {
                BlockPos pos = collectedEntity.getPos();
                AxisAlignedBB aabb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                ChestESP.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = this.project2D(event.getResolution(), vector.x - ChestESP.mc.getRenderManager().viewerPosX, vector.y - ChestESP.mc.getRenderManager().viewerPosY, vector.z - ChestESP.mc.getRenderManager().viewerPosZ);
                    if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                    if (position == null) {
                        position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                    }
                    position.x = Math.min(vector.x, position.x);
                    position.y = Math.min(vector.y, position.y);
                    position.z = Math.max(vector.x, position.z);
                    position.w = Math.max(vector.y, position.w);
                }
                ChestESP.mc.entityRenderer.setupOverlayRendering();
                if (position == null) continue;
                double posX = position.x;
                double posY = position.y;
                double endPosX = position.z;
                double endPosY = position.w;
                RenderUtil.drawCornerBox(posX, posY, endPosX, endPosY, 3.0, Color.BLACK);
                RenderUtil.drawCornerBox(posX, posY, endPosX, endPosY, 1.0, (Boolean)this.hudColor.get() != false ? new Color(((Double)this.r.get()).intValue(), ((Double)this.g.get()).intValue(), ((Double)this.b.get()).intValue()) : new Color(((Double)HUD.r.get()).intValue(), ((Double)HUD.g.get()).intValue(), ((Double)HUD.b.get()).intValue()));
            }
        }
    }

    private Vector3d project2D(ScaledResolution scaledResolution, double x, double y, double z) {
        GL11.glGetFloat((int)2982, (FloatBuffer)this.modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)this.projection);
        GL11.glGetInteger((int)2978, (IntBuffer)this.viewport);
        if (GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)this.modelView, (FloatBuffer)this.projection, (IntBuffer)this.viewport, (FloatBuffer)this.vector)) {
            return new Vector3d(this.vector.get(0) / (float)scaledResolution.getScaleFactor(), ((float)Display.getHeight() - this.vector.get(1)) / (float)scaledResolution.getScaleFactor(), this.vector.get(2));
        }
        return null;
    }

    public void pre3D() {
        this.checkSetupFBO();
        GL11.glPushAttrib((int)1048575);
        GL11.glDisable((int)3008);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)3.0f);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2960);
        GL11.glClear((int)1024);
        GL11.glClearStencil((int)15);
        GL11.glStencilFunc((int)512, (int)1, (int)15);
        GL11.glStencilOp((int)7681, (int)7681, (int)7681);
        GL11.glPolygonMode((int)1032, (int)6913);
    }

    public void checkSetupFBO() {
        Framebuffer framebuffer = Minecraft.getMinecraft().getFramebuffer();
        if (framebuffer != null && framebuffer.depthBuffer > -1) {
            this.setupFBO(framebuffer);
            framebuffer.depthBuffer = -1;
        }
    }

    public void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT((int)fbo.depthBuffer);
        int stencilDepthBufferId = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)stencilDepthBufferId);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)Minecraft.displayWidth, (int)Minecraft.displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)stencilDepthBufferId);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36096, (int)36161, (int)stencilDepthBufferId);
    }

    public void setupStencil() {
        GL11.glStencilFunc((int)512, (int)0, (int)15);
        GL11.glStencilOp((int)7681, (int)7681, (int)7681);
        GL11.glPolygonMode((int)1032, (int)6914);
    }

    public void setupStencil2() {
        GL11.glStencilFunc((int)514, (int)1, (int)15);
        GL11.glStencilOp((int)7680, (int)7680, (int)7680);
        GL11.glPolygonMode((int)1032, (int)6913);
    }

    public void setupStencilFirst() {
        GL11.glStencilFunc((int)512, (int)0, (int)15);
        GL11.glStencilOp((int)7681, (int)7681, (int)7681);
        GL11.glPolygonMode((int)1032, (int)6914);
    }

    public void setupStencilSecond() {
        GL11.glStencilFunc((int)514, (int)1, (int)15);
        GL11.glStencilOp((int)7680, (int)7680, (int)7680);
        GL11.glPolygonMode((int)1032, (int)6913);
    }

    public void renderOutline(int color) {
        this.setColor(color);
        GL11.glDepthMask((boolean)false);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)10754);
        GL11.glPolygonOffset((float)1.0f, (float)-2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

    public void setColor(int i) {
        float f = (float)(i >> 24 & 0xFF) / 255.0f;
        float f0 = (float)(i >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(i >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(i & 0xFF) / 255.0f;
        if (f == 0.0f) {
            f = 1.0f;
        }
        GL11.glColor4f((float)f0, (float)f1, (float)f2, (float)f);
    }

    public void post3D() {
        GL11.glPolygonOffset((float)1.0f, (float)2000000.0f);
        GL11.glDisable((int)10754);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2960);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)3008);
        GL11.glPopAttrib();
    }
}

