/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Cylinder
 *  org.lwjgl.util.glu.GLU
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.misc.Teams;
import heaven.main.module.modules.render.HUD;
import heaven.main.module.modules.render.WingRenderer.ColorUtils;
import heaven.main.utils.render.RenderUtil;
import heaven.main.utils.render.RenderUtils;
import heaven.main.utils.render.color.Colors;
import heaven.main.utils.vec.Vec3f;
import heaven.main.value.Mode;
import heaven.main.value.Option;
import java.awt.Color;
import java.io.Serializable;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

public class ESP
extends Module {
    private final ArrayList<Vec3f> points = new ArrayList();
    private static final Map<EntityPlayer, float[][]> entities = new HashMap<EntityPlayer, float[][]>();
    public static final Mode<String> mode = new Mode("Mode", new String[]{"Box", "2DBox", "ExBox", "CornerA", "CornerB", "Csgo", "Other2D", "Outline", "TwoDimensional", "Flat", "Point"}, "ExBox");
    private final Option<Boolean> xyzRender = new Option<Boolean>("XYZRender", false, () -> mode.is("Other2D"));
    private static final Option<Boolean> self = new Option<Boolean>("Self", false);
    private final Option<Boolean> HEALTH = new Option<Boolean>("Health", true);
    private final Option<Boolean> invis = new Option<Boolean>("Invisible", false);
    float h;
    public static boolean renderNameTags = true;
    private final Map<EntityLivingBase, double[]> entityConvertedPointsMap;
    public final List<Entity> collectedEntities = new ArrayList<Entity>();
    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
    private final int color = Color.WHITE.getRGB();

    public ESP() {
        super("ESP", new String[]{"outline", "wallhack"}, ModuleType.Render);
        this.addValues(mode, this.xyzRender, self, this.HEALTH, this.invis);
        for (int i = 0; i < 8; ++i) {
            this.points.add(new Vec3f());
        }
        for (int i2 = 0; i2 < 8; ++i2) {
            ArrayList<Vec3f> points = new ArrayList<Vec3f>();
            points.add(new Vec3f());
        }
        this.entityConvertedPointsMap = new HashMap<EntityLivingBase, double[]>();
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        this.setSuffix((Serializable)mode.getValue());
    }

    @EventHandler
    public void onScreen(EventRender2D event) {
        int i;
        int collectedEntitiesSize;
        List<Entity> collectedEntities;
        EntityRenderer entityRenderer;
        RenderManager renderMng;
        int color;
        double scaling;
        int scaleFactor;
        ScaledResolution scaledResolution;
        float partialTicks;
        this.collectEntities();
        if (mode.is("TwoDimensional")) {
            GL11.glPushMatrix();
            partialTicks = event.getPartialTicks();
            scaledResolution = new ScaledResolution(mc);
            scaleFactor = scaledResolution.getScaleFactor();
            scaling = (double)scaleFactor / Math.pow(scaleFactor, 2.0);
            GL11.glScaled((double)scaling, (double)scaling, (double)scaling);
            color = this.color;
            renderMng = mc.getRenderManager();
            entityRenderer = ESP.mc.entityRenderer;
            collectedEntities = this.collectedEntities;
            collectedEntitiesSize = collectedEntities.size();
            for (i = 0; i < collectedEntitiesSize; ++i) {
                float f;
                Entity entity = collectedEntities.get(i);
                if (!this.isValid(entity)) continue;
                double x = RenderUtils.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
                double y = RenderUtils.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
                double z = RenderUtils.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
                double width = (double)entity.width / 1.5;
                double height = (double)entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = this.project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
                    if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                    if (position == null) {
                        position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                    }
                    position.x = Math.min(vector.x, position.x);
                    position.y = Math.min(vector.y, position.y);
                    position.z = Math.max(vector.x, position.z);
                    position.w = Math.max(vector.y, position.w);
                }
                if (position == null) continue;
                entityRenderer.setupOverlayRendering();
                double posX = position.x;
                double posY = position.y;
                double endPosX = position.z;
                double endPosY = position.w;
                Gui.drawRect(posX - 0.5, posY, posX + 0.5 - 0.5, endPosY, color);
                Gui.drawRect(posX, endPosY - 0.5, endPosX, endPosY, color);
                Gui.drawRect(posX - 0.5, posY, endPosX, posY + 0.5, color);
                Gui.drawRect(endPosX - 0.5, posY, endPosX, endPosY, color);
                if (!(entity instanceof EntityLivingBase)) continue;
                EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                float hp = entityLivingBase.getHealth();
                float maxHealth = entityLivingBase.getMaxHealth();
                if (hp > f) {
                    hp = maxHealth;
                }
                double hpPercentage = hp / maxHealth;
                double hpHeight = (endPosY - posY) * hpPercentage;
                if (!(hp > 0.0f)) continue;
                int healthColor = ColorUtils.getHealthColorint(hp, maxHealth).getRGB();
                Gui.drawRect(posX - 3.0, endPosY, posX - 2.5, endPosY - hpHeight, healthColor);
            }
            GL11.glPopMatrix();
            GlStateManager.enableBlend();
            entityRenderer.setupOverlayRendering();
        }
        if (mode.is("Flat")) {
            GL11.glPushMatrix();
            partialTicks = event.getPartialTicks();
            scaledResolution = new ScaledResolution(mc);
            scaleFactor = scaledResolution.getScaleFactor();
            scaling = (double)scaleFactor / Math.pow(scaleFactor, 2.0);
            GL11.glScaled((double)scaling, (double)scaling, (double)scaling);
            color = new Color(255, 255, 255, 102).getRGB();
            renderMng = mc.getRenderManager();
            entityRenderer = ESP.mc.entityRenderer;
            collectedEntities = this.collectedEntities;
            collectedEntitiesSize = collectedEntities.size();
            for (i = 0; i < collectedEntitiesSize; ++i) {
                Entity entity = collectedEntities.get(i);
                if (!this.isValid(entity)) continue;
                double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
                double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
                double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
                double width = (double)entity.width / 1.5;
                double height = (double)entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = this.project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
                    if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                    if (position == null) {
                        position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                    }
                    position.x = Math.min(vector.x, position.x);
                    position.y = Math.min(vector.y, position.y);
                    position.z = Math.max(vector.x, position.z);
                    position.w = Math.max(vector.y, position.w);
                }
                if (position == null) continue;
                entityRenderer.setupOverlayRendering();
                double posX = position.x;
                double posY = position.y;
                double endPosX = position.z;
                double endPosY = position.w;
                Gui.drawRect(posX, posY, endPosX, endPosY, color);
            }
            GL11.glPopMatrix();
            GlStateManager.enableBlend();
            entityRenderer.setupOverlayRendering();
        }
    }

    @EventHandler
    public void onRender1(EventRender3D event) {
        try {
            this.updatePositions();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @EventHandler
    public void onRender2D(EventRender2D event) {
        if (mode.isCurrentMode("ExBox") || mode.isCurrentMode("CornerA") || mode.isCurrentMode("CornerB")) {
            GlStateManager.pushMatrix();
            for (Entity entity : this.entityConvertedPointsMap.keySet()) {
                EntityPlayer ent = (EntityPlayer)entity;
                double[] renderPositions = this.entityConvertedPointsMap.get(ent);
                double[] renderPositionsBottom = new double[]{renderPositions[4], renderPositions[5], renderPositions[6]};
                double[] renderPositionsX = new double[]{renderPositions[7], renderPositions[8], renderPositions[9]};
                double[] renderPositionsX2 = new double[]{renderPositions[10], renderPositions[11], renderPositions[12]};
                double[] renderPositionsZ = new double[]{renderPositions[13], renderPositions[14], renderPositions[15]};
                double[] renderPositionsZ2 = new double[]{renderPositions[16], renderPositions[17], renderPositions[18]};
                double[] renderPositionsTop1 = new double[]{renderPositions[19], renderPositions[20], renderPositions[21]};
                double[] renderPositionsTop2 = new double[]{renderPositions[22], renderPositions[23], renderPositions[24]};
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.5, 0.5, 0.5);
                if (!(!((Boolean)this.invis.getValue()).booleanValue() && ent.isInvisible() || ent == null || ent instanceof EntityPlayerSP)) {
                    try {
                        int color;
                        double[] xValues = new double[]{renderPositions[0], renderPositionsBottom[0], renderPositionsX[0], renderPositionsX2[0], renderPositionsZ[0], renderPositionsZ2[0], renderPositionsTop1[0], renderPositionsTop2[0]};
                        double[] yValues = new double[]{renderPositions[1], renderPositionsBottom[1], renderPositionsX[1], renderPositionsX2[1], renderPositionsZ[1], renderPositionsZ2[1], renderPositionsTop1[1], renderPositionsTop2[1]};
                        double x2 = renderPositions[0];
                        double y2 = renderPositions[1];
                        double endx = renderPositionsBottom[0];
                        double endy = renderPositionsBottom[1];
                        int length = xValues.length;
                        for (int j2 = 0; j2 < length; ++j2) {
                            double bdubs = xValues[j2];
                            if (!(bdubs < x2)) continue;
                            x2 = bdubs;
                        }
                        for (double bdubs : xValues) {
                            if (!(bdubs > endx)) continue;
                            endx = bdubs;
                        }
                        int length3 = yValues.length;
                        for (int l2 = 0; l2 < length3; ++l2) {
                            double bdubs = yValues[l2];
                            if (!(bdubs < y2)) continue;
                            y2 = bdubs;
                        }
                        for (double bdubs : yValues) {
                            if (!(bdubs > endy)) continue;
                            endy = bdubs;
                        }
                        double xDiff = (endx - x2) / 4.0;
                        double x2Diff = (endx - x2) / 4.0;
                        int n = Teams.isOnSameTeam(ent) ? Colors.getColor(0, 255, 0, 255) : (ent.hurtTime > 0 ? Colors.getColor(255, 0, 0, 255) : (color = ent.isInvisible() ? Colors.getColor(255, 255, 0, 255) : Colors.getColor(255, 255, 255, 255)));
                        if (mode.isCurrentMode("ExBox")) {
                            RenderUtil.rectangleBordered(x2 + 0.5, y2 + 0.5, endx - 0.5, endy - 0.5, 1.0, Colors.getColor(0, 0, 0, 0), color);
                            RenderUtil.rectangleBordered(x2 - 0.5, y2 - 0.5, endx + 0.5, endy + 0.5, 1.0, Colors.getColor(0, 0), Colors.getColor(0, 150));
                            RenderUtil.rectangleBordered(x2 + 1.5, y2 + 1.5, endx - 1.5, endy - 1.5, 1.0, Colors.getColor(0, 0), Colors.getColor(0, 150));
                        }
                        if (mode.isCurrentMode("CornerB")) {
                            RenderUtil.rectangle(x2 + 0.5, y2 + 0.5, x2 + 1.5, y2 + xDiff + 0.5, color);
                            RenderUtil.rectangle(x2 + 0.5, endy - 0.5, x2 + 1.5, endy - xDiff - 0.5, color);
                            RenderUtil.rectangle(x2 - 0.5, y2 + 0.5, x2 + 0.5, y2 + xDiff + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 + 1.5, y2 + 2.5, x2 + 2.5, y2 + xDiff + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 - 0.5, y2 + xDiff + 0.5, x2 + 2.5, y2 + xDiff + 1.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 - 0.5, endy - 0.5, x2 + 0.5, endy - xDiff - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 + 1.5, endy - 2.5, x2 + 2.5, endy - xDiff - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 - 0.5, endy - xDiff - 0.5, x2 + 2.5, endy - xDiff - 1.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 + 1.0, y2 + 0.5, x2 + x2Diff, y2 + 1.5, color);
                            RenderUtil.rectangle(x2 - 0.5, y2 - 0.5, x2 + x2Diff, y2 + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 + 1.5, y2 + 1.5, x2 + x2Diff, y2 + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 + x2Diff, y2 - 0.5, x2 + x2Diff + 1.0, y2 + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 + 1.0, endy - 0.5, x2 + x2Diff, endy - 1.5, color);
                            RenderUtil.rectangle(x2 - 0.5, endy + 0.5, x2 + x2Diff, endy - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 + 1.5, endy - 1.5, x2 + x2Diff, endy - 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 + x2Diff, endy + 0.5, x2 + x2Diff + 1.0, endy - 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 0.5, y2 + 0.5, endx - 1.5, y2 + xDiff + 0.5, color);
                            RenderUtil.rectangle(endx - 0.5, endy - 0.5, endx - 1.5, endy - xDiff - 0.5, color);
                            RenderUtil.rectangle(endx + 0.5, y2 + 0.5, endx - 0.5, y2 + xDiff + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.5, y2 + 2.5, endx - 2.5, y2 + xDiff + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx + 0.5, y2 + xDiff + 0.5, endx - 2.5, y2 + xDiff + 1.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx + 0.5, endy - 0.5, endx - 0.5, endy - xDiff - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.5, endy - 2.5, endx - 2.5, endy - xDiff - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx + 0.5, endy - xDiff - 0.5, endx - 2.5, endy - xDiff - 1.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.0, y2 + 0.5, endx - x2Diff, y2 + 1.5, color);
                            RenderUtil.rectangle(endx + 0.5, y2 - 0.5, endx - x2Diff, y2 + 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.5, y2 + 1.5, endx - x2Diff, y2 + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - x2Diff, y2 - 0.5, endx - x2Diff - 1.0, y2 + 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.0, endy - 0.5, endx - x2Diff, endy - 1.5, color);
                            RenderUtil.rectangle(endx + 0.5, endy + 0.5, endx - x2Diff, endy - 0.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - 1.5, endy - 1.5, endx - x2Diff, endy - 2.5, Colors.getColor(0, 150));
                            RenderUtil.rectangle(endx - x2Diff, endy + 0.5, endx - x2Diff - 1.0, endy - 2.5, Colors.getColor(0, 150));
                        }
                        if (((Boolean)this.HEALTH.getValue()).booleanValue() && (mode.isCurrentMode("ExBox") || mode.isCurrentMode("CornerA") || mode.isCurrentMode("CornerB"))) {
                            float health = ent.getHealth();
                            float[] fractions = new float[]{0.0f, 0.5f, 1.0f};
                            Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
                            float progress = health / ent.getMaxHealth();
                            Color customColor = health >= 0.0f ? this.blendColors(fractions, colors, progress).brighter() : Color.RED;
                            double difference = y2 - endy + 0.5;
                            double healthLocation = endy + difference * (double)progress;
                            RenderUtil.rectangleBordered(x2 - 6.5, y2 - 0.5, x2 - 2.5, endy, 1.0, Colors.getColor(0, 100), Colors.getColor(0, 150));
                            RenderUtil.rectangle(x2 - 5.5, endy - 1.0, x2 - 3.5, healthLocation, customColor.getRGB());
                            if (-difference > 50.0) {
                                for (int i2 = 1; i2 < 10; ++i2) {
                                    double dThing = difference / 10.0 * (double)i2;
                                    RenderUtil.rectangle(x2 - 6.5, endy - 0.5 + dThing, x2 - 2.5, endy - 0.5 + dThing - 1.0, Colors.getColor(0));
                                }
                            }
                            if ((int)this.getIncremental(progress * 100.0f, 1.0) <= 40) {
                                GlStateManager.pushMatrix();
                                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                                GlStateManager.popMatrix();
                            }
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                GlStateManager.popMatrix();
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            }
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.popMatrix();
            RenderUtil.rectangle(0.0, 0.0, 0.0, 0.0, -1);
        }
    }

    @EventHandler
    public void onRender(EventRender3D event) {
        if (this.h > 255.0f) {
            this.h = 0.0f;
        }
        this.h = (float)((double)this.h + 0.1);
        if (mode.is("Point")) {
            this.collectEntities();
            List<Entity> collectedEntities = this.collectedEntities;
            for (Entity collectedEntity : collectedEntities) {
                EntityLivingBase entity = (EntityLivingBase)collectedEntity;
                if (!(entity instanceof EntityPlayer)) continue;
                ESP.drawCircle(entity, new Color(((Double)HUD.r.getValue()).intValue(), ((Double)HUD.g.getValue()).intValue(), ((Double)HUD.b.getValue()).intValue()).getRGB(), event);
            }
        }
        if (mode.isCurrentMode("Csgo")) {
            ESP.csgo();
        }
        if (mode.isCurrentMode("Box")) {
            this.doBoxESP(event);
        }
        if (mode.is("Other2D")) {
            this.doOther2DESP(event);
        }
        if (mode.is("2DBox")) {
            this.doCornerESP(event);
        }
    }

    private void doCornerESP(EventRender3D e) {
        for (EntityPlayer entity : Minecraft.theWorld.playerEntities) {
            if (entity == Minecraft.thePlayer) continue;
            if (!ESP.isValid(entity) && !entity.isInvisible()) {
                return;
            }
            if (entity.isInvisible()) {
                if (entity != Minecraft.thePlayer) {
                    return;
                }
            }
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.enableBlend();
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            double renderPosX = ESP.mc.getRenderManager().viewerPosX;
            double renderPosY = ESP.mc.getRenderManager().viewerPosY;
            double renderPosZ = ESP.mc.getRenderManager().viewerPosZ;
            float partialTicks = e.getPartialTicks();
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - renderPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - renderPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - renderPosZ;
            float DISTANCE = Minecraft.thePlayer.getDistanceToEntity(entity);
            float SCALE = 0.035f;
            SCALE /= 2.0f;
            GlStateManager.translate((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? entity.height / 2.0f : 0.0f), (float)z);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            mc.getRenderManager();
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GL11.glScalef((float)(-SCALE), (float)(-SCALE), (float)(-SCALE));
            Color color = entity.hurtTime > 0 ? new Color(Color.RED.getRGB()) : new Color(Color.WHITE.getRGB());
            Color gray = new Color(0, 0, 0);
            double thickness = 2.0f + DISTANCE * 0.08f;
            double xLeft = -30.0;
            double xRight = 30.0;
            double yUp = 20.0;
            double yDown = 130.0;
            double size = 10.0;
            this.drawVerticalLine(xLeft + size / 2.0 - 1.0, yUp + 1.0, size / 2.0, thickness, gray);
            this.drawHorizontalLine(xLeft + 1.0, yUp + size, size, thickness, gray);
            this.drawVerticalLine(xLeft + size / 2.0 - 1.0, yUp, size / 2.0, thickness, color);
            this.drawHorizontalLine(xLeft, yUp + size, size, thickness, color);
            this.drawVerticalLine(xRight - size / 2.0 + 1.0, yUp + 1.0, size / 2.0, thickness, gray);
            this.drawHorizontalLine(xRight - 1.0, yUp + size, size, thickness, gray);
            this.drawVerticalLine(xRight - size / 2.0 + 1.0, yUp, size / 2.0, thickness, color);
            this.drawHorizontalLine(xRight, yUp + size, size, thickness, color);
            this.drawVerticalLine(xLeft + size / 2.0 - 1.0, yDown - 1.0, size / 2.0, thickness, gray);
            this.drawHorizontalLine(xLeft + 1.0, yDown - size, size, thickness, gray);
            this.drawVerticalLine(xLeft + size / 2.0 - 1.0, yDown, size / 2.0, thickness, color);
            this.drawHorizontalLine(xLeft, yDown - size, size, thickness, color);
            this.drawVerticalLine(xRight - size / 2.0 + 1.0, yDown - 1.0, size / 2.0, thickness, gray);
            this.drawHorizontalLine(xRight - 1.0, yDown - size, size, thickness, gray);
            this.drawVerticalLine(xRight - size / 2.0 + 1.0, yDown, size / 2.0, thickness, color);
            this.drawHorizontalLine(xRight, yDown - size, size, thickness, color);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GlStateManager.disableBlend();
            GL11.glDisable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glNormal3f((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPopMatrix();
        }
    }

    private void doBoxESP(EventRender3D event) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        for (EntityPlayer o : Minecraft.theWorld.playerEntities) {
            if (o == null) continue;
            if (o == Minecraft.thePlayer) continue;
            if (Teams.isOnSameTeam(o)) {
                RenderUtil.entityESPBox(o, new Color(0, 255, 0), event);
                continue;
            }
            if (o.hurtTime > 0) {
                RenderUtil.entityESPBox(o, new Color(255, 0, 0), event);
                continue;
            }
            if (FriendManager.isFriend(o.getName())) {
                RenderUtil.entityESPBox(o, new Color(0.078431375f, 0.19607843f, 1.0f), event);
                continue;
            }
            RenderUtil.entityESPBox(o, new Color(1.0f, 1.0f, 1.0f), event);
        }
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static boolean isValid(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer && entity.getHealth() >= 0.0f) {
            return entity != Minecraft.thePlayer || (Boolean)self.getValue() != false;
        }
        return false;
    }

    private void doOther2DESP(EventRender3D e) {
        for (EntityPlayer entity : Minecraft.theWorld.playerEntities) {
            if (!ESP.isValid(entity) || !entity.isEntityAlive()) continue;
            if (ESP.mc.gameSettings.thirdPersonView == 0) {
                if (entity == Minecraft.thePlayer) continue;
            }
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.enableBlend();
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            double renderPosX = ESP.mc.getRenderManager().viewerPosX;
            double renderPosY = ESP.mc.getRenderManager().viewerPosY;
            double renderPosZ = ESP.mc.getRenderManager().viewerPosZ;
            float partialTicks = e.getPartialTicks();
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - renderPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - renderPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - renderPosZ;
            float DISTANCE = Minecraft.thePlayer.getDistanceToEntity(entity);
            float SCALE = 0.035f;
            SCALE /= 2.0f;
            entity.isChild();
            GlStateManager.translate((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? entity.height / 2.0f : 0.0f), (float)z);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            mc.getRenderManager();
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GL11.glScalef((float)(-SCALE), (float)(-SCALE), (float)(-SCALE));
            float HEALTH = entity.getHealth();
            int COLOR = (double)HEALTH > 20.0 ? -65292 : ((double)HEALTH >= 10.0 ? -16711936 : ((double)HEALTH >= 3.0 ? -23296 : -65536));
            new Color(0, 0, 0);
            double thickness = 1.5f + DISTANCE * 0.01f;
            double xLeft = -20.0;
            double xRight = 20.0;
            double yUp = 27.0;
            double yDown = 130.0;
            Color color = new Color(255, 255, 255);
            if (entity.hurtTime > 0) {
                color = new Color(255, 0, 0);
            } else if (Teams.isOnSameTeam(entity)) {
                color = new Color(0, 255, 0);
            }
            ESP.drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness + 0.5f, Color.BLACK.getRGB(), 0);
            ESP.drawBorderedRect((float)xLeft, (float)yUp, (float)xRight, (float)yDown, (float)thickness, color.getRGB(), 0);
            ESP.drawBorderedRect((float)xLeft - 3.0f - DISTANCE * 0.2f, (float)yDown - (float)(yDown - yUp), (float)xLeft - 2.0f, (float)yDown, 0.15f, Color.BLACK.getRGB(), new Color(100, 100, 100).getRGB());
            ESP.drawBorderedRect((float)xLeft - 3.0f - DISTANCE * 0.2f, (float)yDown - (float)(yDown - yUp) * Math.min(1.0f, entity.getHealth() / 20.0f), (float)xLeft - 2.0f, (float)yDown, 0.15f, Color.BLACK.getRGB(), COLOR);
            int c = entity.getHealth() < 5.0f ? new Color(255, 20, 10).getRGB() : ((double)entity.getHealth() < 12.5 ? new Color(16774441).getRGB() : new Color(0, 255, 0).getRGB());
            ESP.mc.fontRendererCrack.drawStringWithShadowX2(String.valueOf((int)entity.getHealth()), (float)xLeft - (float)ESP.mc.fontRendererCrack.getStringWidth(String.valueOf((int)entity.getHealth())) - 10.0f, (float)yDown / 2.0f, c);
            ESP.mc.fontRendererCrack.drawStringWithShadowX2(entity.getHeldItem() == null ? "" : entity.getHeldItem().getDisplayName(), (int)(xLeft / 2.0 - (double)(ESP.mc.fontRendererCrack.getStringWidth(entity.getHeldItem() == null ? "" : entity.getHeldItem().getDisplayName()) / 2)), (int)yDown + 10, new Color(0, 255, 0).getRGB());
            if (((Boolean)this.xyzRender.get()).booleanValue()) {
                String svar = "XYZ: " + (int)entity.posX + " " + (int)entity.posY + " " + (int)entity.posZ + " \ufffd\ufffd\ufffd\ufffd: " + (int)DISTANCE + "m";
                ESP.mc.fontRendererCrack.drawCenteredStringWithShadowX2(svar, (float)(xRight - (double)ESP.mc.fontRendererCrack.getStringWidth(svar)) / 8.0f, (int)yDown + (entity.getHeldItem() == null ? 10 : 20), new Color(255, 255, 255).getRGB());
            }
            int y2 = 0;
            for (PotionEffect effect : entity.getActivePotionEffects()) {
                Potion potion = Potion.potionTypes[effect.getPotionID()];
                String PType = I18n.format(potion.getName(), new Object[0]);
                switch (effect.getAmplifier()) {
                    case 1: {
                        PType = PType + " II";
                        break;
                    }
                    case 2: {
                        PType = PType + " III";
                        break;
                    }
                    case 3: {
                        PType = PType + " IV";
                    }
                }
                if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                    PType = PType + "\u00a77:\u00a76 " + Potion.getDurationString(effect);
                } else if (effect.getDuration() < 300) {
                    PType = PType + "\u00a77:\u00a7c " + Potion.getDurationString(effect);
                } else if (effect.getDuration() > 600) {
                    PType = PType + "\u00a77:\u00a77 " + Potion.getDurationString(effect);
                }
                ESP.mc.fontRendererCrack.drawStringWithShadow(PType, (float)xLeft - (float)ESP.mc.fontRendererCrack.getStringWidth(PType) - 5.0f, (float)yUp - (float)ESP.mc.fontRendererCrack.FONT_HEIGHT + (float)y2 + 20.0f, potion.getLiquidColor());
                y2 -= 10;
            }
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GlStateManager.disableBlend();
            GL11.glDisable((int)3042);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glNormal3f((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPopMatrix();
        }
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        ESP.drawRect(x, y, x2, y2, col2);
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
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

    public static void drawRect(float g, float h, float i, float j, int col1) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)i, (double)h);
        GL11.glVertex2d((double)g, (double)h);
        GL11.glVertex2d((double)g, (double)j);
        GL11.glVertex2d((double)i, (double)j);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    private void drawVerticalLine(double xPos, double yPos, double xSize, double thickness, Color color) {
        Tessellator tesselator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tesselator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(xPos - xSize, yPos - thickness / 2.0, 0.0).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f).endVertex();
        worldRenderer.pos(xPos - xSize, yPos + thickness / 2.0, 0.0).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f).endVertex();
        worldRenderer.pos(xPos + xSize, yPos + thickness / 2.0, 0.0).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f).endVertex();
        worldRenderer.pos(xPos + xSize, yPos - thickness / 2.0, 0.0).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f).endVertex();
        tesselator.draw();
    }

    private void drawHorizontalLine(double xPos, double yPos, double ySize, double thickness, Color color) {
        Tessellator tesselator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tesselator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(xPos - thickness / 2.0, yPos - ySize, 0.0).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f).endVertex();
        worldRenderer.pos(xPos - thickness / 2.0, yPos + ySize, 0.0).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f).endVertex();
        worldRenderer.pos(xPos + thickness / 2.0, yPos + ySize, 0.0).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f).endVertex();
        worldRenderer.pos(xPos + thickness / 2.0, yPos - ySize, 0.0).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f).endVertex();
        tesselator.draw();
    }

    public static void addEntity(EntityPlayer e, ModelRenderer bipedLeftLeg, ModelRenderer bipedLeftLegwear, ModelRenderer bipedRightLeg, ModelRenderer bipedLeftArm, ModelRenderer bipedLeftArmwear, ModelRenderer bipedRightArm, ModelRenderer bipedRightArmwear, ModelRenderer bipedBody, ModelRenderer bipedBodyWear, ModelRenderer bipedHead) {
        entities.put(e, new float[][]{{bipedHead.rotateAngleX, bipedHead.rotateAngleY, bipedHead.rotateAngleZ}, {bipedRightArm.rotateAngleX, bipedRightArm.rotateAngleY, bipedRightArm.rotateAngleZ}, {bipedLeftArm.rotateAngleX, bipedLeftArm.rotateAngleY, bipedLeftArm.rotateAngleZ}, {bipedRightLeg.rotateAngleX, bipedRightLeg.rotateAngleY, bipedRightLeg.rotateAngleZ}, {bipedLeftLeg.rotateAngleX, bipedLeftLeg.rotateAngleY, bipedLeftLeg.rotateAngleZ}});
    }

    public double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }

    public Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length != colors.length) {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
        int[] indicies = this.getFractionIndicies(fractions, progress);
        float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
        Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
        float max = range[1] - range[0];
        float value = progress - range[0];
        float weight = value / max;
        return this.blend(colorRange[0], colorRange[1], 1.0f - weight);
    }

    public Color blend(Color color1, Color color2, double ratio) {
        float r2 = (float)ratio;
        float ir2 = 1.0f - r2;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r2 + rgb2[0] * ir2;
        float green = rgb1[1] * r2 + rgb2[1] * ir2;
        float blue = rgb1[2] * r2 + rgb2[2] * ir2;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
            exp.printStackTrace();
        }
        return color3;
    }

    public int[] getFractionIndicies(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    private void updatePositions() {
        this.entityConvertedPointsMap.clear();
        float pTicks = ESP.mc.timer.renderPartialTicks;
        for (Entity e2 : Minecraft.theWorld.getLoadedEntityList()) {
            double topY;
            if (!(e2 instanceof EntityPlayer)) continue;
            EntityPlayer ent = (EntityPlayer)e2;
            if (ent == Minecraft.thePlayer) continue;
            double x2 = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - ESP.mc.getRenderManager().viewerPosX + 0.36;
            double y2 = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)pTicks - ESP.mc.getRenderManager().viewerPosY;
            double z2 = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - ESP.mc.getRenderManager().viewerPosZ + 0.36;
            y2 = topY = y2 + ((double)ent.height + 0.15);
            double[] convertedPoints = RenderUtil.convertTo2D(x2, y2, z2);
            double[] convertedPoints2 = RenderUtil.convertTo2D(x2 - 0.36, y2, z2 - 0.36);
            if (convertedPoints2[2] < 0.0 || convertedPoints2[2] >= 1.0) continue;
            x2 = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - ESP.mc.getRenderManager().viewerPosX - 0.36;
            z2 = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - ESP.mc.getRenderManager().viewerPosZ - 0.36;
            double[] convertedPointsBottom = RenderUtil.convertTo2D(x2, y2, z2);
            y2 = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)pTicks - ESP.mc.getRenderManager().viewerPosY - 0.05;
            double[] convertedPointsx = RenderUtil.convertTo2D(x2, y2, z2);
            x2 = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - ESP.mc.getRenderManager().viewerPosX - 0.36;
            z2 = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - ESP.mc.getRenderManager().viewerPosZ + 0.36;
            double[] convertedPointsTop1 = RenderUtil.convertTo2D(x2, topY, z2);
            double[] convertedPointsx2 = RenderUtil.convertTo2D(x2, y2, z2);
            x2 = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - ESP.mc.getRenderManager().viewerPosX + 0.36;
            z2 = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - ESP.mc.getRenderManager().viewerPosZ + 0.36;
            double[] convertedPointsz = RenderUtil.convertTo2D(x2, y2, z2);
            x2 = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - ESP.mc.getRenderManager().viewerPosX + 0.36;
            z2 = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - ESP.mc.getRenderManager().viewerPosZ - 0.36;
            double[] convertedPointsTop2 = RenderUtil.convertTo2D(x2, topY, z2);
            double[] convertedPointsz2 = RenderUtil.convertTo2D(x2, y2, z2);
            this.entityConvertedPointsMap.put(ent, new double[]{convertedPoints[0], convertedPoints[1], 0.0, convertedPoints[2], convertedPointsBottom[0], convertedPointsBottom[1], convertedPointsBottom[2], convertedPointsx[0], convertedPointsx[1], convertedPointsx[2], convertedPointsx2[0], convertedPointsx2[1], convertedPointsx2[2], convertedPointsz[0], convertedPointsz[1], convertedPointsz[2], convertedPointsz2[0], convertedPointsz2[1], convertedPointsz2[2], convertedPointsTop1[0], convertedPointsTop1[1], convertedPointsTop1[2], convertedPointsTop2[0], convertedPointsTop2[1], convertedPointsTop2[2]});
        }
    }

    private static void csgo() {
        for (Object o : Minecraft.theWorld.loadedEntityList) {
            if (!(o instanceof EntityPlayer)) continue;
            if (o == Minecraft.thePlayer) continue;
            EntityPlayer ent = (EntityPlayer)o;
            double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)ESP.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * (double)ESP.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)ESP.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            x -= 0.275;
            z -= 0.275;
            y += (double)ent.getEyeHeight() - 0.225 - (ent.isSneaking() ? 0.25 : 0.0);
            double mid = 0.275;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            double rotAdd = -0.25 * (double)(Math.abs(ent.rotationPitch) / 90.0f);
            GL11.glTranslated((double)0.0, (double)rotAdd, (double)0.0);
            GL11.glTranslated((double)(x + 0.275), (double)(y + 0.275), (double)(z + 0.275));
            GL11.glRotated((double)(-ent.rotationYaw % 360.0f), (double)0.0, (double)1.0, (double)0.0);
            GL11.glTranslated((double)(-(x + 0.275)), (double)(-(y + 0.275)), (double)(-(z + 0.275)));
            GL11.glTranslated((double)(x + 0.275), (double)(y + 0.275), (double)(z + 0.275));
            GL11.glRotated((double)ent.rotationPitch, (double)1.0, (double)0.0, (double)0.0);
            GL11.glTranslated((double)(-(x + 0.275)), (double)(-(y + 0.275)), (double)(-(z + 0.275)));
            GL11.glDisable((int)3553);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.5f);
            RenderUtil.drawBoundingBox(new AxisAlignedBB(x - 0.0025, y - 0.0025, z - 0.0025, x + 0.55 + 0.0025, y + 0.55 + 0.0025, z + 0.55 + 0.0025));
            GL11.glDisable((int)2848);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
    }

    private boolean isValid(Entity entity) {
        if (entity == Minecraft.thePlayer) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (entity.isInvisible()) {
            return false;
        }
        if (entity instanceof EntityItem) {
            return false;
        }
        if (entity instanceof EntityAnimal) {
            return false;
        }
        return entity instanceof EntityPlayer;
    }

    public static void drawCircle(EntityLivingBase entity, int color, EventRender3D e) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)e.getPartialTicks() - RenderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)e.getPartialTicks() - RenderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)e.getPartialTicks() - RenderManager.renderPosZ;
        float radius = 0.2f;
        int side = 6;
        GL11.glPushMatrix();
        GL11.glTranslated((double)x, (double)(y + 2.0), (double)z);
        GL11.glRotatef((float)(-entity.width), (float)0.0f, (float)1.0f, (float)0.0f);
        ESP.glColor(color);
        ESP.enableSmoothLine(1.0f);
        Cylinder c = new Cylinder();
        GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        c.setDrawStyle(100012);
        c.draw(0.0f, radius, 0.3f, side, 1);
        c.setDrawStyle(100012);
        GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
        c.draw(radius, 0.0f, 0.3f, side, 1);
        GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        c.setDrawStyle(100011);
        GL11.glTranslated((double)0.0, (double)0.0, (double)-0.3);
        c.draw(0.0f, radius, 0.3f, side, 1);
        c.setDrawStyle(100011);
        GL11.glTranslated((double)0.0, (double)0.0, (double)0.3);
        c.draw(radius, 0.0f, 0.3f, side, 1);
        ESP.disableSmoothLine();
        GL11.glPopMatrix();
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)(alpha == 0.0f ? 1.0f : alpha));
    }

    public static void enableSmoothLine(float width) {
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glLineWidth((float)width);
    }

    public static void disableSmoothLine() {
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glDepthMask((boolean)true);
        GL11.glCullFace((int)1029);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        GL11.glGetFloat((int)2982, (FloatBuffer)this.modelview);
        GL11.glGetFloat((int)2983, (FloatBuffer)this.projection);
        GL11.glGetInteger((int)2978, (IntBuffer)this.viewport);
        if (GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)this.modelview, (FloatBuffer)this.projection, (IntBuffer)this.viewport, (FloatBuffer)this.vector)) {
            return new Vector3d(this.vector.get(0) / (float)scaleFactor, ((float)Display.getHeight() - this.vector.get(1)) / (float)scaleFactor, this.vector.get(2));
        }
        return null;
    }

    private void collectEntities() {
        this.collectedEntities.clear();
        List playerEntities = Minecraft.theWorld.loadedEntityList;
        int playerEntitiesSize = playerEntities.size();
        for (int i = 0; i < playerEntitiesSize; ++i) {
            Entity entity = (Entity)playerEntities.get(i);
            if (!this.isValid(entity)) continue;
            this.collectedEntities.add(entity);
        }
    }
}

