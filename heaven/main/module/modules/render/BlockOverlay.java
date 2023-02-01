/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.render.RenderUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;

public class BlockOverlay
extends Module {
    public final Numbers<Double> r = new Numbers<Double>("Red", 255.0, 0.0, 255.0, 1.0);
    public final Numbers<Double> g = new Numbers<Double>("Green", 255.0, 0.0, 255.0, 1.0);
    public final Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 1.0);
    public final Option<Boolean> togg = new Option<Boolean>("RenderString", true);

    public BlockOverlay() {
        super("BlockOverlay", ModuleType.Render);
        this.addValues(this.r, this.g, this.b, this.togg);
    }

    public int getRed() {
        return ((Double)this.r.getValue()).intValue();
    }

    public int getGreen() {
        return ((Double)this.g.getValue()).intValue();
    }

    public int getBlue() {
        return ((Double)this.b.getValue()).intValue();
    }

    public boolean getRender() {
        return (Boolean)this.togg.getValue();
    }

    @EventHandler
    public void onRender(EventRender2D event) {
        if (BlockOverlay.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = BlockOverlay.mc.objectMouseOver.getBlockPos();
            Block block = Minecraft.theWorld.getBlockState(pos).getBlock();
            String s2 = block.getLocalizedName();
            if (BlockOverlay.mc.objectMouseOver != null && this.getRender()) {
                ScaledResolution res = new ScaledResolution(mc);
                int x = res.getScaledWidth() / 2 + 6;
                int y = res.getScaledHeight() / 2 - 1;
                Minecraft.fontRendererObj.drawStringWithShadow(s2, (float)x + 4.0f, (float)y - 2.65f, Color.gray.getRGB());
            }
        }
    }

    @EventHandler
    public void onRender3D(EventRender3D event) {
        if (BlockOverlay.mc.objectMouseOver != null && BlockOverlay.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = BlockOverlay.mc.objectMouseOver.getBlockPos();
            Block block = Minecraft.theWorld.getBlockState(pos).getBlock();
            String s = block.getLocalizedName();
            double n = pos.getX();
            double x = n - RenderManager.renderPosX;
            double n2 = pos.getY();
            double y = n2 - RenderManager.renderPosY;
            double n3 = pos.getZ();
            double z = n3 - RenderManager.renderPosZ;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glColor4f((float)((float)this.getRed() / 255.0f), (float)((float)this.getGreen() / 255.0f), (float)((float)this.getBlue() / 255.0f), (float)0.15f);
            double minX = block instanceof BlockStairs || Block.getIdFromBlock(block) == 134 ? 0.0 : block.getBlockBoundsMinX();
            double minY = block instanceof BlockStairs || Block.getIdFromBlock(block) == 134 ? 0.0 : block.getBlockBoundsMinY();
            double minZ = block instanceof BlockStairs || Block.getIdFromBlock(block) == 134 ? 0.0 : block.getBlockBoundsMinZ();
            RenderUtil.drawBoundingBox(new AxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
            GL11.glColor4f((float)((float)this.getRed() / 255.0f), (float)((float)this.getGreen() / 255.0f), (float)((float)this.getBlue() / 255.0f), (float)1.0f);
            GL11.glLineWidth((float)0.5f);
            RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
            GL11.glDisable((int)2848);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }
}

