/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import heaven.main.Client;
import heaven.main.module.modules.globals.LowFire;
import heaven.main.module.modules.render.Animations;
import heaven.main.ui.gui.hud.lefthand.LeftHand;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.optifine.DynamicLights;
import net.optifine.reflect.Reflector;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

public class ItemRenderer {
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
    private final Minecraft mc;
    private ItemStack itemToRender;
    private float equippedProgress;
    private float prevEquippedProgress;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;
    private int equippedItemSlot = -1;
    private long circleTicks;

    public ItemRenderer(Minecraft mcIn) {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }

    public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform) {
        if (heldStack != null) {
            Item item = heldStack.getItem();
            Block block = Block.getBlockFromItem(item);
            GlStateManager.pushMatrix();
            if (this.itemRenderer.shouldRenderItemIn3D(heldStack)) {
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                if (!(!ItemRenderer.isBlockTranslucent(block) || Config.isShaders() && Shaders.renderItemKeepDepthMask)) {
                    GlStateManager.depthMask(false);
                }
            }
            this.itemRenderer.renderItemModelForEntity(heldStack, entityIn, transform);
            if (ItemRenderer.isBlockTranslucent(block)) {
                GlStateManager.depthMask(true);
            }
            GlStateManager.popMatrix();
        }
    }

    private static boolean isBlockTranslucent(Block blockIn) {
        return blockIn != null && blockIn.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
    }

    private static void rotateArroundXAndY(float angle, float angleY) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(angleY, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void setLightMapFromPlayer(AbstractClientPlayer clientPlayer) {
        int i = Minecraft.theWorld.getCombinedLight(new BlockPos(clientPlayer.posX, clientPlayer.posY + (double)clientPlayer.getEyeHeight(), clientPlayer.posZ), 0);
        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), i);
        }
        float f = i & 0xFFFF;
        float f1 = i >> 16;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
    }

    private static void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks) {
        float f = entityplayerspIn.prevRenderArmPitch + (entityplayerspIn.renderArmPitch - entityplayerspIn.prevRenderArmPitch) * partialTicks;
        float f1 = entityplayerspIn.prevRenderArmYaw + (entityplayerspIn.renderArmYaw - entityplayerspIn.prevRenderArmYaw) * partialTicks;
        GlStateManager.rotate((entityplayerspIn.rotationPitch - f) * 0.1f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((entityplayerspIn.rotationYaw - f1) * 0.1f, 0.0f, 1.0f, 0.0f);
    }

    private static float getMapAngleFromPitch(float pitch) {
        float f = 1.0f - pitch / 45.0f + 0.1f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        f = -MathHelper.cos(f * (float)Math.PI) * 0.5f + 0.5f;
        return f;
    }

    private void renderRightArm(RenderPlayer renderPlayerIn) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(54.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(64.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-62.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(0.25f, -0.85f, 0.75f);
        renderPlayerIn.renderRightArm(Minecraft.thePlayer);
        GlStateManager.popMatrix();
    }

    private void renderLeftArm(RenderPlayer renderPlayerIn) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(92.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(41.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-0.3f, -1.1f, 0.45f);
        renderPlayerIn.renderLeftArm(Minecraft.thePlayer);
        GlStateManager.popMatrix();
    }

    private void renderPlayerArms(AbstractClientPlayer clientPlayer) {
        ItemRenderer itemRenderer = this;
        itemRenderer.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        Render render = this.renderManager.getEntityRenderObject(Minecraft.thePlayer);
        RenderPlayer renderplayer = (RenderPlayer)render;
        if (!clientPlayer.isInvisible()) {
            GlStateManager.disableCull();
            this.renderRightArm(renderplayer);
            this.renderLeftArm(renderplayer);
            GlStateManager.enableCull();
        }
    }

    private void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress) {
        float f = -0.4f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        float f1 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0f);
        float f2 = -0.2f * MathHelper.sin(swingProgress * (float)Math.PI);
        GlStateManager.translate(f, f1, f2);
        float f3 = ItemRenderer.getMapAngleFromPitch(pitch);
        GlStateManager.translate(0.0f, 0.04f, -0.72f);
        GlStateManager.translate(0.0f, equipmentProgress * -1.2f, 0.0f);
        GlStateManager.translate(0.0f, f3 * -0.5f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f3 * -85.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        this.renderPlayerArms(clientPlayer);
        float f4 = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f5 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f4 * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f5 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f5 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.38f, 0.38f, 0.38f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-1.0f, -1.0f, 0.0f);
        GlStateManager.scale(0.015625f, 0.015625f, 0.015625f);
        ItemRenderer itemRenderer = this;
        itemRenderer.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)-1.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        worldrenderer.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        worldrenderer.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        worldrenderer.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        MapData mapdata = Items.filled_map.getMapData(this.itemToRender, Minecraft.theWorld);
        if (mapdata != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
    }

    private void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress) {
        float f = -0.3f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        float f1 = 0.4f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0f);
        float f2 = -0.4f * MathHelper.sin(swingProgress * (float)Math.PI);
        GlStateManager.translate(f, f1, f2);
        GlStateManager.translate(0.64000005f, -0.6f, -0.71999997f);
        GlStateManager.translate(0.0f, equipProgress * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float f3 = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f4 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f4 * 70.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f3 * -20.0f, 0.0f, 0.0f, 1.0f);
        ItemRenderer itemRenderer = this;
        itemRenderer.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        GlStateManager.translate(-1.0f, 3.6f, 3.5f);
        GlStateManager.rotate(120.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(200.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.translate(5.6f, 0.0f, 0.0f);
        Render render = this.renderManager.getEntityRenderObject(Minecraft.thePlayer);
        GlStateManager.disableCull();
        RenderPlayer renderplayer = (RenderPlayer)render;
        renderplayer.renderRightArm(Minecraft.thePlayer);
        GlStateManager.enableCull();
    }

    private static void doItemUsedTransformations(float swingProgress) {
        float f = -0.4f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        float f1 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0f);
        float f2 = -0.2f * MathHelper.sin(swingProgress * (float)Math.PI);
        if (!Client.instance.getModuleManager().getModuleByClass(Animations.class).isEnabled()) {
            GlStateManager.translate(f, f1, f2);
        } else {
            float var4;
            float var3;
            float var2;
            if (((Boolean)Animations.Smooth.getValue()).booleanValue()) {
                var2 = -0.15f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
                var3 = -0.05f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 1.5f);
                var4 = -0.0f * MathHelper.sin(swingProgress * (float)Math.PI);
            } else {
                var2 = -0.4f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
                var3 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0f);
                var4 = -0.2f * MathHelper.sin(swingProgress * (float)Math.PI);
            }
            GlStateManager.translate((double)var2 + (Double)Animations.swingx.getValue(), (double)var3 + (Double)Animations.swingy.getValue(), (double)var4 + (Double)Animations.swingz.getValue());
        }
    }

    private void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks) {
        float f = (float)clientPlayer.getItemInUseCount() - partialTicks + 1.0f;
        float f1 = f / (float)this.itemToRender.getMaxItemUseDuration();
        float f2 = MathHelper.abs(MathHelper.cos(f / 4.0f * (float)Math.PI) * 0.1f);
        if (f1 >= 0.8f) {
            f2 = 0.0f;
        }
        GlStateManager.translate(0.0f, f2, 0.0f);
        float f3 = 1.0f - (float)Math.pow(f1, 27.0);
        GlStateManager.translate(f3 * 0.6f, f3 * -0.5f, f3 * 0.0f);
        GlStateManager.rotate(f3 * 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f3 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f3 * 30.0f, 0.0f, 0.0f, 1.0f);
    }

    private static void transformFirstPersonItem(float equipProgress, float swingProgress) {
        GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.translate(0.0f, equipProgress * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f1 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f1 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }

    private void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer) {
        GlStateManager.rotate(-18.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-12.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-8.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-0.9f, 0.2f, 0.0f);
        float f = (float)this.itemToRender.getMaxItemUseDuration() - ((float)clientPlayer.getItemInUseCount() - partialTicks + 1.0f);
        float f1 = f / 20.0f;
        f1 = (f1 * f1 + f1 * 2.0f) / 3.0f;
        if (f1 > 1.0f) {
            f1 = 1.0f;
        }
        if (f1 > 0.1f) {
            float f2 = MathHelper.sin((f - 0.1f) * 1.3f);
            float f3 = f1 - 0.1f;
            float f4 = f2 * f3;
            GlStateManager.translate(f4 * 0.0f, f4 * 0.01f, f4 * 0.0f);
        }
        GlStateManager.translate(f1 * 0.0f, f1 * 0.0f, f1 * 0.1f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f + f1 * 0.2f);
    }

    private static void doBlockTransformations() {
        GlStateManager.translate(-0.5f, 0.2f, 0.0f);
        GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void renderItemInFirstPerson(float partialTicks) {
        block41: {
            float f1;
            EntityPlayerSP entityplayersp;
            float f;
            block37: {
                block39: {
                    block40: {
                        float var4;
                        float var2;
                        block38: {
                            if (Client.instance.getModuleManager().getModuleByClass(Animations.class).isEnabled() && ((Boolean)Animations.highHand.get()).booleanValue()) {
                                if (Minecraft.thePlayer.getHeldItem() == null) {
                                    GL11.glTranslated((double)0.0, (double)0.15, (double)0.0);
                                }
                            }
                            var2 = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
                            var4 = Minecraft.thePlayer.getSwingProgress(partialTicks);
                            f = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
                            entityplayersp = Minecraft.thePlayer;
                            f1 = entityplayersp.getSwingProgress(partialTicks);
                            float f2 = entityplayersp.prevRotationPitch + (entityplayersp.rotationPitch - entityplayersp.prevRotationPitch) * partialTicks;
                            float f3 = entityplayersp.prevRotationYaw + (entityplayersp.rotationYaw - entityplayersp.prevRotationYaw) * partialTicks;
                            ItemRenderer.rotateArroundXAndY(f2, f3);
                            this.setLightMapFromPlayer(entityplayersp);
                            ItemRenderer.rotateWithPlayerRotations(entityplayersp, partialTicks);
                            GlStateManager.enableRescaleNormal();
                            GlStateManager.pushMatrix();
                            if (LeftHand.use(this.itemToRender)) {
                                GL11.glScaled((double)-1.0, (double)1.0, (double)1.0);
                                GlStateManager.disableCull();
                            } else {
                                GlStateManager.enableCull();
                            }
                            if (this.itemToRender == null) break block37;
                            if (!(this.itemToRender.getItem() instanceof ItemMap)) break block38;
                            this.renderItemMap(entityplayersp, f2, f, f1);
                            break block39;
                        }
                        if (entityplayersp.getItemInUseCount() <= 0) break block40;
                        EnumAction enumaction = this.itemToRender.getItemUseAction();
                        switch (enumaction) {
                            case NONE: {
                                ItemRenderer.transformFirstPersonItem(f, 0.0f);
                                if (Client.instance.getModuleManager().getModuleByClass(Animations.class).isEnabled()) {
                                    GlStateManager.translate((Double)Animations.swingx.getValue(), (Double)Animations.swingy.getValue(), (Double)Animations.swingz.getValue());
                                    break;
                                }
                                break block39;
                            }
                            case EAT: {
                                if (Client.instance.getModuleManager().getModuleByClass(Animations.class).isEnabled()) {
                                    GlStateManager.translate((Double)Animations.swingx.getValue(), (Double)Animations.swingy.getValue(), (Double)Animations.swingz.getValue());
                                }
                            }
                            case DRINK: {
                                this.performDrinking(entityplayersp, partialTicks);
                                ItemRenderer.transformFirstPersonItem(f, 0.0f);
                                if (Client.instance.getModuleManager().getModuleByClass(Animations.class).isEnabled()) {
                                    GlStateManager.translate((Double)Animations.swingx.getValue(), (Double)Animations.swingy.getValue(), (Double)Animations.swingz.getValue());
                                    break;
                                }
                                break block39;
                            }
                            case BLOCK: {
                                float var9 = MathHelper.sin(MathHelper.sqrt_float(f1) * (float)Math.PI);
                                if (Client.instance.getModuleManager().getModuleByClass(Animations.class).isEnabled()) {
                                    float var15;
                                    float f4;
                                    GL11.glTranslated((double)((Double)Animations.x.getValue()), (double)((Double)Animations.y.getValue()), (double)((Double)Animations.z.getValue()));
                                    Animations animations = (Animations)Client.instance.getModuleManager().getModuleByClass(Animations.class);
                                    if (((String)animations.mode.getValue()).equals("Slide")) {
                                        ItemRenderer.transformFirstPersonItem(f, 0.0f);
                                        ItemRenderer.doBlockTransformations();
                                        GL11.glTranslated((double)-0.3, (double)0.3, (double)0.0);
                                        GL11.glRotatef((float)(-var9 * 70.0f / 2.0f), (float)-8.0f, (float)0.0f, (float)9.0f);
                                        GL11.glRotatef((float)(-var9 * 70.0f), (float)1.0f, (float)-0.4f, (float)-0.0f);
                                        break;
                                    }
                                    if (((String)animations.mode.getValue()).equals("Thinking")) {
                                        ItemRenderer.transformFirstPersonItem(var2 * 0.1f, 0.0f);
                                        ItemRenderer.doBlockTransformations();
                                        if (enumaction == EnumAction.BLOCK) {
                                            GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                        }
                                        f4 = (double)var4 > 0.5 ? 1.0f - var4 : var4;
                                        float var18 = MathHelper.sin(MathHelper.sqrt_float(f1) * (float)Math.PI);
                                        GlStateManager.rotate(-var18 * 55.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                                        GlStateManager.rotate(-var18 * 45.0f, 1.0f, var18 / 12.0f, -0.0f);
                                        GlStateManager.rotate(-f4 * 10.0f, 10.0f, 10.0f, -9.0f);
                                        GlStateManager.translate(0.0f, 0.0f, 0.4f);
                                        GL11.glTranslated((double)1.5, (double)0.3, (double)0.5);
                                        GL11.glTranslatef((float)-1.0f, (float)(Minecraft.thePlayer.isSneaking() ? -0.9f : -0.2f), (float)0.2f);
                                    }
                                    if (((String)animations.mode.getValue()).equals("Jigsaw")) {
                                        this.Jigsaw(f, f1);
                                        ItemRenderer.doBlockTransformations();
                                    }
                                    if (((String)animations.mode.getValue()).equals("Stitch")) {
                                        ItemRenderer.transformFirstPersonItem(0.1f, f1);
                                        ItemRenderer.doBlockTransformations();
                                        GlStateManager.translate(-0.5, 0.0, 0.0);
                                    }
                                    if (((String)animations.mode.getValue()).equals("Jello2")) {
                                        ItemRenderer.transformFirstPersonItem(0.0f, 0.0f);
                                        ItemRenderer.doBlockTransformations();
                                        int alpha = (int)Math.min(255L, System.currentTimeMillis() % 255L > 127L ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : System.currentTimeMillis() % 255L);
                                        GlStateManager.translate(0.3f, -0.0f, 0.4f);
                                        GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                                        GlStateManager.translate(0.0f, 0.5f, 0.0f);
                                        GlStateManager.rotate(90.0f, 1.0f, 0.0f, -1.0f);
                                        GlStateManager.translate(0.6f, 0.5f, 0.0f);
                                        GlStateManager.rotate(-90.0f, 1.0f, 0.0f, -1.0f);
                                        GlStateManager.rotate(-10.0f, 1.0f, 0.0f, -1.0f);
                                        GlStateManager.rotate(Minecraft.thePlayer.isSwingInProgress ? (float)(-alpha) / 5.0f : 1.0f, 1.0f, -0.0f, 1.0f);
                                    }
                                    if (((String)animations.mode.getValue()).equals("Astro")) {
                                        ItemRenderer.transformFirstPersonItem(var2 / 2.0f, f1);
                                        float var9e = MathHelper.sin(MathHelper.sqrt_float(f1) * (float)Math.PI);
                                        GlStateManager.rotate(var9e * 30.0f / 2.0f, -var9e, -0.0f, 9.0f);
                                        GlStateManager.rotate(var9e * 40.0f, 1.0f, -var9e / 2.0f, -0.0f);
                                        ItemRenderer.doBlockTransformations();
                                    }
                                    if (((String)animations.mode.getValue()).equals("Avatar")) {
                                        this.Avatar(f1);
                                        ItemRenderer.doBlockTransformations();
                                    }
                                    if (((String)animations.mode.getValue()).equals("Sigma")) {
                                        var15 = MathHelper.sin(MathHelper.sqrt_float(var4) * (float)Math.PI);
                                        ItemRenderer.transformFirstPersonItem(var2 * 0.5f, 0.0f);
                                        GlStateManager.rotate(-var15 * 55.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                                        GlStateManager.rotate(-var15 * 45.0f, 1.0f, var15 / 2.0f, -0.0f);
                                        ItemRenderer.doBlockTransformations();
                                        GL11.glTranslated((double)1.2, (double)0.3, (double)0.5);
                                        GL11.glTranslatef((float)-1.0f, (float)(Minecraft.thePlayer.isSneaking() ? -0.1f : -0.2f), (float)0.2f);
                                        GlStateManager.scale(1.2f, 1.2f, 1.2f);
                                    }
                                    if (((String)animations.mode.getValue()).equals("Remix")) {
                                        ItemRenderer.transformFirstPersonItem(f, 0.83f);
                                        ItemRenderer.doBlockTransformations();
                                        float remixVar = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.83f);
                                        GlStateManager.translate(-0.5f, 0.2f, 0.2f);
                                        GlStateManager.rotate(-remixVar * 0.0f, 0.0f, 0.0f, 0.0f);
                                        GlStateManager.rotate(-remixVar * 43.0f, 58.0f, 23.0f, 45.0f);
                                    }
                                    if (((String)animations.mode.getValue()).equals("LiquidBounce")) {
                                        ItemRenderer.transformFirstPersonItem(f + 0.1f, f1);
                                        ItemRenderer.doBlockTransformations();
                                        GlStateManager.translate(-0.5f, 0.2f, 0.0f);
                                    }
                                    if (((String)animations.mode.getValue()).equals("Push")) {
                                        ItemRenderer.transformFirstPersonItem(f, 0.0f);
                                        GlStateManager.rotate(f1 * -15.0f, 0.0f, 0.0f, 0.0f);
                                        ItemRenderer.doBlockTransformations();
                                    }
                                    if (((String)animations.mode.getValue()).equals("Target")) {
                                        this.transformFirstPersonSwordBlock(f1);
                                        ItemRenderer.doBlockTransformations();
                                    }
                                    if (((String)animations.mode.getValue()).equals("Circle")) {
                                        ++this.circleTicks;
                                        GlStateManager.translate(-0.0f, -0.2f, -0.6f);
                                        GlStateManager.rotate((float)(-this.circleTicks) * 0.07f * 50.0f, 0.0f, 0.0f, -1.0f);
                                        GlStateManager.rotate(44.0f, 0.0f, 1.0f, 0.6f);
                                        GlStateManager.rotate(44.0f, 1.0f, 0.0f, -0.6f);
                                        GlStateManager.translate(1.0f, -0.2f, 0.5f);
                                        GlStateManager.rotate(-44.0f, 1.0f, 0.0f, -0.6f);
                                        GlStateManager.scale(0.5, 0.5, 0.5);
                                        ItemRenderer.doBlockTransformations();
                                    }
                                    if (((String)animations.mode.getValue()).equals("Rainy")) {
                                        ItemRenderer.transformFirstPersonItem(f, 0.83f);
                                        ItemRenderer.doBlockTransformations();
                                        f4 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.83f);
                                        GlStateManager.translate(-0.0f, 0.2f, 0.2f);
                                        GlStateManager.rotate(-f4 * 0.0f, 0.0f, 0.0f, 0.0f);
                                        GlStateManager.rotate(-f4 * 43.0f, 58.0f, 23.0f, 45.0f);
                                        break;
                                    }
                                    if (((String)animations.mode.getValue()).equals("Jello")) {
                                        ItemRenderer.transformFirstPersonItem(f / 2.0f, 0.0f);
                                        GL11.glRotatef((float)(-var9 * 40.0f / 4.0f), (float)(-(var9 / 2.0f)), (float)-0.0f, (float)-15.0f);
                                        GL11.glRotatef((float)(-var9 * 30.0f), (float)-1.0f, (float)(-(var9 / 2.0f)), (float)-0.0f);
                                        GlStateManager.rotate(-15.0f, 1.0f, 0.0f, -0.0f);
                                        ItemRenderer.doBlockTransformations();
                                    }
                                    if (((String)animations.mode.getValue()).equals("Swang")) {
                                        ItemRenderer.transformFirstPersonItem(f / 2.0f, 0.0f);
                                        var15 = MathHelper.sin(f1 * f1 * (float)Math.PI);
                                        GlStateManager.rotate(-var15 * 40.0f / 2.0f, var15 / 2.0f, -0.0f, 9.0f);
                                        GlStateManager.rotate(-var15 * 30.0f, 1.0f, var15 / 2.0f, -0.0f);
                                        ItemRenderer.doBlockTransformations();
                                    }
                                    if (((String)animations.mode.getValue()).equals("Swank")) {
                                        ItemRenderer.transformFirstPersonItem(var2 / 2.0f, var4);
                                        var15 = MathHelper.sin(MathHelper.sqrt_float(var4) * (float)Math.PI);
                                        GlStateManager.rotate(var15 * 30.0f, -var15, -0.0f, 9.0f);
                                        GlStateManager.rotate(var15 * 50.0f, 1.0f, -var15, -0.0f);
                                        ItemRenderer.doBlockTransformations();
                                    }
                                    if (((String)animations.mode.getValue()).equals("1.7")) {
                                        ItemRenderer.transformFirstPersonItem(f, f1);
                                        ItemRenderer.doBlockTransformations();
                                    }
                                    if (((String)animations.mode.getValue()).equals("Exhibition")) {
                                        ItemRenderer.transformFirstPersonItem(f / 2.0f, 0.0f);
                                        GL11.glRotatef((float)(-var9 * 40.0f / 2.0f), (float)(var9 / 2.0f), (float)-0.0f, (float)9.0f);
                                        GL11.glRotatef((float)(-var9 * 30.0f), (float)1.0f, (float)(var9 / 2.0f), (float)-0.0f);
                                        ItemRenderer.doBlockTransformations();
                                        break;
                                    }
                                    if (!((String)animations.mode.getValue()).equals("Lucky")) break;
                                    GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
                                    GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                                    float ff = MathHelper.sin(0.0f);
                                    float f1f = MathHelper.sin(MathHelper.sqrt_float(0.0f) * (float)Math.PI);
                                    GlStateManager.rotate(ff * -20.0f, 0.0f, 1.0f, 0.0f);
                                    GlStateManager.rotate(f1f * -20.0f, 0.0f, 0.0f, 1.0f);
                                    GlStateManager.rotate(f1f * -80.0f, 1.0f, 0.0f, 0.0f);
                                    GlStateManager.scale(0.4f, 0.4f, 0.4f);
                                    ItemRenderer.doBlockTransformations();
                                    float f42 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.83f);
                                    GlStateManager.translate(-0.0f, 0.2f, -0.2f);
                                    GlStateManager.rotate(-f42 * 0.0f, 0.0f, 0.0f, 0.0f);
                                    GlStateManager.rotate(-f42 * 7.0f, 58.0f, 23.0f, 45.0f);
                                    break;
                                }
                                ItemRenderer.doItemUsedTransformations(f1);
                                ItemRenderer.transformFirstPersonItem(f, f1);
                                break;
                            }
                            case BOW: {
                                ItemRenderer.transformFirstPersonItem(f, 0.0f);
                                this.doBowTransformations(partialTicks, entityplayersp);
                                if (Client.instance.getModuleManager().getModuleByClass(Animations.class).isEnabled()) {
                                    GlStateManager.translate((Double)Animations.swingx.getValue(), (Double)Animations.swingy.getValue(), (Double)Animations.swingz.getValue());
                                    break;
                                }
                                break block39;
                            }
                        }
                        break block39;
                    }
                    ItemRenderer.doItemUsedTransformations(f1);
                    ItemRenderer.transformFirstPersonItem(f, f1);
                }
                this.renderItem(entityplayersp, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
                break block41;
            }
            if (!entityplayersp.isInvisible()) {
                this.renderPlayerArm(entityplayersp, f, f1);
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void Avatar(float swingProgress) {
        GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f1 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f1 * -40.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }

    private void Jigsaw(float var2, float swingProgress) {
        GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.translate(0.0f, var2 * -0.6f, 0.0f);
        float v = swingProgress * 0.8f - swingProgress * swingProgress * 0.8f;
        GlStateManager.rotate(45.0f, 0.0f, 2.0f + v * 0.5f, v * 3.0f);
        GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(0.37f, 0.37f, 0.37f);
    }

    public void renderOverlays(float partialTicks) {
        GlStateManager.disableAlpha();
        if (Minecraft.thePlayer.isEntityInsideOpaqueBlock()) {
            IBlockState iblockstate = Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer));
            BlockPos blockpos = new BlockPos(Minecraft.thePlayer);
            EntityPlayerSP entityplayer = Minecraft.thePlayer;
            for (int i = 0; i < 8; ++i) {
                double d0 = entityplayer.posX + (double)(((float)(i % 2) - 0.5f) * entityplayer.width * 0.8f);
                double d1 = entityplayer.posY + (double)(((float)((i >> 1) % 2) - 0.5f) * 0.1f);
                double d2 = entityplayer.posZ + (double)(((float)((i >> 2) % 2) - 0.5f) * entityplayer.width * 0.8f);
                BlockPos blockpos1 = new BlockPos(d0, d1 + (double)entityplayer.getEyeHeight(), d2);
                IBlockState iblockstate1 = Minecraft.theWorld.getBlockState(blockpos1);
                if (!iblockstate1.getBlock().isVisuallyOpaque()) continue;
                iblockstate = iblockstate1;
                blockpos = blockpos1;
            }
            if (iblockstate.getBlock().getRenderType() != -1) {
                Object object = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);
                Object[] objectArray = new Object[5];
                objectArray[0] = Minecraft.thePlayer;
                objectArray[1] = Float.valueOf(partialTicks);
                objectArray[2] = object;
                objectArray[3] = iblockstate;
                objectArray[4] = blockpos;
                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, objectArray)) {
                    this.renderBlockInHand(this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
                }
            }
        }
        if (!Minecraft.thePlayer.isSpectator()) {
            if (Minecraft.thePlayer.isInsideOfMaterial(Material.water)) {
                Object[] objectArray = new Object[2];
                objectArray[0] = Minecraft.thePlayer;
                objectArray[1] = Float.valueOf(partialTicks);
                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, objectArray)) {
                    this.renderWaterOverlayTexture(partialTicks);
                }
            }
            if (Minecraft.thePlayer.isBurning()) {
                Object[] objectArray = new Object[2];
                objectArray[0] = Minecraft.thePlayer;
                objectArray[1] = Float.valueOf(partialTicks);
                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, objectArray)) {
                    this.renderFireInFirstPerson();
                }
            }
        }
        GlStateManager.enableAlpha();
    }

    private void transformFirstPersonSwordBlock(float swingProgress) {
        GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f1 * -70.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }

    private void renderBlockInHand(TextureAtlasSprite atlas) {
        ItemRenderer itemRenderer = this;
        itemRenderer.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(0.1f, 0.1f, 0.1f, 0.5f);
        GlStateManager.pushMatrix();
        float f6 = atlas.getMinU();
        float f7 = atlas.getMaxU();
        float f8 = atlas.getMinV();
        float f9 = atlas.getMaxV();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-1.0, -1.0, -0.5).tex(f7, f9).endVertex();
        worldrenderer.pos(1.0, -1.0, -0.5).tex(f6, f9).endVertex();
        worldrenderer.pos(1.0, 1.0, -0.5).tex(f6, f8).endVertex();
        worldrenderer.pos(-1.0, 1.0, -0.5).tex(f7, f8).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderWaterOverlayTexture(float partialTicks) {
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
            Minecraft.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            float f = Minecraft.thePlayer.getBrightness(partialTicks);
            GlStateManager.color(f, f, f, 0.5f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            float f7 = -Minecraft.thePlayer.rotationYaw / 64.0f;
            float f8 = Minecraft.thePlayer.rotationPitch / 64.0f;
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(-1.0, -1.0, -0.5).tex(4.0f + f7, 4.0f + f8).endVertex();
            worldrenderer.pos(1.0, -1.0, -0.5).tex(0.0f + f7, 4.0f + f8).endVertex();
            worldrenderer.pos(1.0, 1.0, -0.5).tex(0.0f + f7, 0.0f + f8).endVertex();
            worldrenderer.pos(-1.0, 1.0, -0.5).tex(4.0f + f7, 0.0f + f8).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
        }
    }

    private void renderFireInFirstPerson() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.9f);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        float f = 1.0f;
        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            ItemRenderer itemRenderer = this;
            itemRenderer.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            float f1 = textureatlassprite.getMinU();
            float f2 = textureatlassprite.getMaxU();
            float f3 = textureatlassprite.getMinV();
            float f4 = textureatlassprite.getMaxV();
            float f5 = (0.0f - f) / 2.0f;
            float f6 = f5 + f;
            float f7 = 0.0f;
            f7 = Client.instance.getModuleManager().getModuleByClass(LowFire.class).isEnabled() ? (f7 += ((Number)LowFire.y.getValue()).floatValue()) : 0.0f - f / 2.0f;
            float f8 = f7 + f;
            float f9 = -0.5f;
            GlStateManager.translate((float)(-((i << 1) - 1)) * 0.24f, -0.3f, 0.0f);
            GlStateManager.rotate((float)((i << 1) - 1) * 10.0f, 0.0f, 1.0f, 0.0f);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.setSprite(textureatlassprite);
            worldrenderer.pos(f5, f7, f9).tex(f2, f4).endVertex();
            worldrenderer.pos(f6, f7, f9).tex(f1, f4).endVertex();
            worldrenderer.pos(f6, f8, f9).tex(f1, f3).endVertex();
            worldrenderer.pos(f5, f8, f9).tex(f2, f3).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }

    public void updateEquippedItem() {
        this.prevEquippedProgress = this.equippedProgress;
        EntityPlayerSP entityplayer = Minecraft.thePlayer;
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        boolean flag = false;
        if (this.itemToRender != null && itemstack != null) {
            if (!this.itemToRender.getIsItemStackEqual(itemstack)) {
                boolean flag1;
                if (Reflector.ForgeItem_shouldCauseReequipAnimation.exists() && !(flag1 = Reflector.callBoolean(this.itemToRender.getItem(), Reflector.ForgeItem_shouldCauseReequipAnimation, this.itemToRender, itemstack, this.equippedItemSlot != entityplayer.inventory.currentItem))) {
                    this.itemToRender = itemstack;
                    this.equippedItemSlot = entityplayer.inventory.currentItem;
                    return;
                }
                flag = true;
            }
        } else {
            flag = this.itemToRender != null || itemstack != null;
        }
        float f2 = 0.4f;
        float f = flag ? 0.0f : 1.0f;
        float f1 = MathHelper.clamp_float(f - this.equippedProgress, -0.4f, 0.4f);
        this.equippedProgress += f1;
        if (this.equippedProgress < 0.1f) {
            this.itemToRender = itemstack;
            this.equippedItemSlot = entityplayer.inventory.currentItem;
            if (Config.isShaders()) {
                Shaders.setItemToRenderMain(itemstack);
            }
        }
    }

    public void resetEquippedProgress() {
        this.equippedProgress = 0.0f;
    }

    public void resetEquippedProgress2() {
        this.equippedProgress = 0.0f;
    }
}

