/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.collect.Lists
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.optifine.SmartAnimations;
import net.optifine.TextureAnimations;
import net.optifine.reflect.Reflector;
import net.optifine.util.MemoryMonitor;
import net.optifine.util.NativeMemory;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GuiOverlayDebug
extends Gui {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
    private String debugOF;
    private List<String> debugInfoLeft;
    private List<String> debugInfoRight;
    private long updateInfoLeftTimeMs;
    private long updateInfoRightTimeMs;

    public GuiOverlayDebug(Minecraft mc) {
        this.mc = mc;
        this.fontRenderer = Minecraft.fontRendererObj;
    }

    public void renderDebugInfo(ScaledResolution scaledResolutionIn) {
        this.mc.mcProfiler.startSection("debug");
        GlStateManager.pushMatrix();
        this.renderDebugInfoLeft();
        this.renderDebugInfoRight(scaledResolutionIn);
        GlStateManager.popMatrix();
        if (this.mc.gameSettings.showLagometer) {
            this.renderLagometer();
        }
        this.mc.mcProfiler.endSection();
    }

    private boolean isReducedDebug() {
        return Minecraft.thePlayer.hasReducedDebug() || this.mc.gameSettings.reducedDebugInfo;
    }

    protected void renderDebugInfoLeft() {
        List<String> list = this.debugInfoLeft;
        if (list == null || System.currentTimeMillis() > this.updateInfoLeftTimeMs) {
            this.debugInfoLeft = list = this.call();
            this.updateInfoLeftTimeMs = System.currentTimeMillis() + 100L;
        }
        for (int i = 0; i < list.size(); ++i) {
            String s = list.get(i);
            if (Strings.isNullOrEmpty((String)s)) continue;
            int j = this.fontRenderer.FONT_HEIGHT;
            int k = this.fontRenderer.getStringWidth(s);
            int i1 = 2 + j * i;
            GuiOverlayDebug.drawRect(1, i1 - 1, 2 + k + 1, i1 + j - 1, -1873784752);
            this.fontRenderer.drawString(s, 2, i1, 0xE0E0E0);
        }
    }

    protected void renderDebugInfoRight(ScaledResolution scaledRes) {
        List<String> list = this.debugInfoRight;
        if (list == null || System.currentTimeMillis() > this.updateInfoRightTimeMs) {
            this.debugInfoRight = list = this.getDebugInfoRight();
            this.updateInfoRightTimeMs = System.currentTimeMillis() + 100L;
        }
        for (int i = 0; i < list.size(); ++i) {
            String s = list.get(i);
            if (Strings.isNullOrEmpty((String)s)) continue;
            int j = this.fontRenderer.FONT_HEIGHT;
            int k = this.fontRenderer.getStringWidth(s);
            int l = scaledRes.getScaledWidth() - 2 - k;
            int i1 = 2 + j * i;
            GuiOverlayDebug.drawRect(l - 1, i1 - 1, l + k + 1, i1 + j - 1, -1873784752);
            this.fontRenderer.drawString(s, l, i1, 0xE0E0E0);
        }
    }

    protected List<String> call() {
        BlockPos blockpos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
        if (this.mc.debug != this.debugOF) {
            StringBuilder stringbuffer = new StringBuilder(this.mc.debug);
            int i = Config.getFpsMin();
            int j = this.mc.debug.indexOf(" fps ");
            if (j >= 0) {
                stringbuffer.insert(j, "/" + i);
            }
            if (Config.isSmoothFps()) {
                stringbuffer.append(" sf");
            }
            if (Config.isFastRender()) {
                stringbuffer.append(" fr");
            }
            if (Config.isAnisotropicFiltering()) {
                stringbuffer.append(" af");
            }
            if (Config.isAntialiasing()) {
                stringbuffer.append(" aa");
            }
            if (Config.isRenderRegions()) {
                stringbuffer.append(" reg");
            }
            if (Config.isShaders()) {
                stringbuffer.append(" sh");
            }
            this.debugOF = this.mc.debug = stringbuffer.toString();
        }
        StringBuilder stringbuilder = new StringBuilder();
        TextureMap texturemap = Config.getTextureMap();
        stringbuilder.append(", A: ");
        if (SmartAnimations.isActive()) {
            stringbuilder.append(texturemap.getCountAnimationsActive() + TextureAnimations.getCountAnimationsActive());
            stringbuilder.append("/");
        }
        stringbuilder.append(texturemap.getCountAnimations() + TextureAnimations.getCountAnimations());
        String s1 = stringbuilder.toString();
        if (this.isReducedDebug()) {
            Object[] objectArray = new String[8];
            objectArray[0] = "Minecraft 1.8.9 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")";
            objectArray[1] = this.mc.debug;
            objectArray[2] = this.mc.renderGlobal.getDebugInfoRenders();
            objectArray[3] = this.mc.renderGlobal.getDebugInfoEntities();
            objectArray[4] = "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + Minecraft.theWorld.getDebugLoadedEntities() + s1;
            objectArray[5] = Minecraft.theWorld.getProviderName();
            objectArray[6] = "";
            objectArray[7] = String.format("Chunk-relative: %d %d %d", blockpos.getX() & 0xF, blockpos.getY() & 0xF, blockpos.getZ() & 0xF);
            return Lists.newArrayList((Object[])objectArray);
        }
        Entity entity = this.mc.getRenderViewEntity();
        EnumFacing enumfacing = entity.getHorizontalFacing();
        String s = "Invalid";
        switch (enumfacing) {
            case NORTH: {
                s = "Towards negative Z";
                break;
            }
            case SOUTH: {
                s = "Towards positive Z";
                break;
            }
            case WEST: {
                s = "Towards negative X";
                break;
            }
            case EAST: {
                s = "Towards positive X";
            }
        }
        Object[] objectArray = new String[11];
        objectArray[0] = "Minecraft 1.8.9 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")";
        objectArray[1] = this.mc.debug;
        objectArray[2] = this.mc.renderGlobal.getDebugInfoRenders();
        objectArray[3] = this.mc.renderGlobal.getDebugInfoEntities();
        objectArray[4] = "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + Minecraft.theWorld.getDebugLoadedEntities() + s1;
        objectArray[5] = Minecraft.theWorld.getProviderName();
        objectArray[6] = "";
        objectArray[7] = String.format("XYZ: %.3f / %.5f / %.3f", this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
        objectArray[8] = String.format("Block: %d %d %d", blockpos.getX(), blockpos.getY(), blockpos.getZ());
        objectArray[9] = String.format("Chunk: %d %d %d in %d %d %d", blockpos.getX() & 0xF, blockpos.getY() & 0xF, blockpos.getZ() & 0xF, blockpos.getX() >> 4, blockpos.getY() >> 4, blockpos.getZ() >> 4);
        objectArray[10] = String.format("Facing: %s (%s) (%.1f / %.1f)", enumfacing, s, Float.valueOf(MathHelper.wrapAngleTo180_float(entity.rotationYaw)), Float.valueOf(MathHelper.wrapAngleTo180_float(entity.rotationPitch)));
        ArrayList list = Lists.newArrayList((Object[])objectArray);
        if (Minecraft.theWorld != null) {
            if (Minecraft.theWorld.isBlockLoaded(blockpos)) {
                Chunk chunk = Minecraft.theWorld.getChunkFromBlockCoords(blockpos);
                list.add("Biome: " + chunk.getBiome((BlockPos)blockpos, (WorldChunkManager)Minecraft.theWorld.getWorldChunkManager()).biomeName);
                list.add("Light: " + chunk.getLightSubtracted(blockpos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos) + " block)");
                DifficultyInstance difficultyinstance = Minecraft.theWorld.getDifficultyForLocation(blockpos);
                if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
                    DifficultyInstance difficultyinstance1;
                    EntityPlayerMP entityplayermp = this.mc.getIntegratedServer().getConfigurationManager().getPlayerByUUID(Minecraft.thePlayer.getUniqueID());
                    if (entityplayermp != null && (difficultyinstance1 = this.mc.getIntegratedServer().getDifficultyAsync(entityplayermp.worldObj, new BlockPos(entityplayermp))) != null) {
                        difficultyinstance = difficultyinstance1;
                    }
                }
                Object[] objectArray2 = new Object[2];
                objectArray2[0] = Float.valueOf(difficultyinstance.getAdditionalDifficulty());
                objectArray2[1] = Minecraft.theWorld.getWorldTime() / 24000L;
                list.add(String.format("Local Difficulty: %.2f (Day %d)", objectArray2));
            }
        }
        if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            list.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            BlockPos blockpos1 = this.mc.objectMouseOver.getBlockPos();
            list.add(String.format("Looking at: %d %d %d", blockpos1.getX(), blockpos1.getY(), blockpos1.getZ()));
        }
        return list;
    }

    protected List<String> getDebugInfoRight() {
        long i = Runtime.getRuntime().maxMemory();
        long j = Runtime.getRuntime().totalMemory();
        long k = Runtime.getRuntime().freeMemory();
        long l = j - k;
        ArrayList list = Lists.newArrayList((Object[])new String[]{String.format("Java: %s %dbit", System.getProperty("java.version"), this.mc.isJava64bit() ? 64 : 32), String.format("Mem: % 2d%% %03d/%03dMB", l * 100L / i, GuiOverlayDebug.bytesToMb(l), GuiOverlayDebug.bytesToMb(i)), String.format("Allocated: % 2d%% %03dMB", j * 100L / i, GuiOverlayDebug.bytesToMb(j)), "", String.format("CPU: %s", OpenGlHelper.getCpu()), "", String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GL11.glGetString((int)7936)), GL11.glGetString((int)7937), GL11.glGetString((int)7938)});
        long i1 = NativeMemory.getBufferAllocated();
        long j1 = NativeMemory.getBufferMaximum();
        String s = "Native: " + GuiOverlayDebug.bytesToMb(i1) + "/" + GuiOverlayDebug.bytesToMb(j1) + "MB";
        list.add(4, s);
        list.set(5, "GC: " + MemoryMonitor.getAllocationRateMb() + "MB/s");
        if (Reflector.FMLCommonHandler_getBrandings.exists()) {
            Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            list.add("");
            list.addAll((Collection)Reflector.call(object, Reflector.FMLCommonHandler_getBrandings, Boolean.FALSE));
        }
        if (this.isReducedDebug()) {
            return list;
        }
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
            BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
            IBlockState iblockstate = Minecraft.theWorld.getBlockState(blockpos);
            if (Minecraft.theWorld.getWorldType() != WorldType.DEBUG_WORLD) {
                iblockstate = iblockstate.getBlock().getActualState(iblockstate, Minecraft.theWorld, blockpos);
            }
            list.add("");
            list.add(String.valueOf(Block.blockRegistry.getNameForObject(iblockstate.getBlock())));
            for (Map.Entry entry : iblockstate.getProperties().entrySet()) {
                StringBuilder s1 = new StringBuilder(((Comparable)entry.getValue()).toString());
                if (entry.getValue() == Boolean.TRUE) {
                    s1.append((Object)EnumChatFormatting.GREEN);
                } else if (entry.getValue() == Boolean.FALSE) {
                    s1.append((Object)EnumChatFormatting.RED);
                }
                list.add(((IProperty)entry.getKey()).getName() + ": " + s1);
            }
        }
        return list;
    }

    private void renderLagometer() {
    }

    private static long bytesToMb(long bytes) {
        return bytes / 1024L / 1024L;
    }
}

