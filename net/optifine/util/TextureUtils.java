/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GLContext
 */
package net.optifine.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.src.Config;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.BetterGrass;
import net.optifine.BetterSnow;
import net.optifine.CustomBlockLayers;
import net.optifine.CustomColors;
import net.optifine.CustomGuis;
import net.optifine.CustomItems;
import net.optifine.CustomLoadingScreens;
import net.optifine.CustomPanorama;
import net.optifine.CustomSky;
import net.optifine.Lang;
import net.optifine.NaturalTextures;
import net.optifine.RandomEntities;
import net.optifine.SmartLeaves;
import net.optifine.TextureAnimations;
import net.optifine.entity.model.CustomEntityModels;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.Shaders;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class TextureUtils {
    public static final String texGrassTop = "grass_top";
    public static final String texStone = "stone";
    public static final String texDirt = "dirt";
    public static final String texCoarseDirt = "coarse_dirt";
    public static final String texGrassSide = "grass_side";
    public static final String texStoneslabSide = "stone_slab_side";
    public static final String texStoneslabTop = "stone_slab_top";
    public static final String texBedrock = "bedrock";
    public static final String texSand = "sand";
    public static final String texGravel = "gravel";
    public static final String texLogOak = "log_oak";
    public static final String texLogBigOak = "log_big_oak";
    public static final String texLogAcacia = "log_acacia";
    public static final String texLogSpruce = "log_spruce";
    public static final String texLogBirch = "log_birch";
    public static final String texLogJungle = "log_jungle";
    public static final String texLogOakTop = "log_oak_top";
    public static final String texLogBigOakTop = "log_big_oak_top";
    public static final String texLogAcaciaTop = "log_acacia_top";
    public static final String texLogSpruceTop = "log_spruce_top";
    public static final String texLogBirchTop = "log_birch_top";
    public static final String texLogJungleTop = "log_jungle_top";
    public static final String texLeavesOak = "leaves_oak";
    public static final String texLeavesBigOak = "leaves_big_oak";
    public static final String texLeavesAcacia = "leaves_acacia";
    public static final String texLeavesBirch = "leaves_birch";
    public static final String texLeavesSpuce = "leaves_spruce";
    public static final String texLeavesJungle = "leaves_jungle";
    public static final String texGoldOre = "gold_ore";
    public static final String texIronOre = "iron_ore";
    public static final String texCoalOre = "coal_ore";
    public static final String texObsidian = "obsidian";
    public static final String texGrassSideOverlay = "grass_side_overlay";
    public static final String texSnow = "snow";
    public static final String texGrassSideSnowed = "grass_side_snowed";
    public static final String texMyceliumSide = "mycelium_side";
    public static final String texMyceliumTop = "mycelium_top";
    public static final String texDiamondOre = "diamond_ore";
    public static final String texRedstoneOre = "redstone_ore";
    public static final String texLapisOre = "lapis_ore";
    public static final String texCactusSide = "cactus_side";
    public static final String texClay = "clay";
    public static final String texFarmlandWet = "farmland_wet";
    public static final String texFarmlandDry = "farmland_dry";
    public static final String texNetherrack = "netherrack";
    public static final String texSoulSand = "soul_sand";
    public static final String texGlowstone = "glowstone";
    public static final String texLeavesSpruce = "leaves_spruce";
    public static final String texLeavesSpruceOpaque = "leaves_spruce_opaque";
    public static final String texEndStone = "end_stone";
    public static final String texSandstoneTop = "sandstone_top";
    public static final String texSandstoneBottom = "sandstone_bottom";
    public static final String texRedstoneLampOff = "redstone_lamp_off";
    public static final String texRedstoneLampOn = "redstone_lamp_on";
    public static final String texWaterStill = "water_still";
    public static final String texWaterFlow = "water_flow";
    public static final String texLavaStill = "lava_still";
    public static final String texLavaFlow = "lava_flow";
    public static final String texFireLayer0 = "fire_layer_0";
    public static final String texFireLayer1 = "fire_layer_1";
    public static final String texPortal = "portal";
    public static final String texGlass = "glass";
    public static final String texGlassPaneTop = "glass_pane_top";
    public static final String texCompass = "compass";
    public static final String texClock = "clock";
    public static TextureAtlasSprite iconGrassTop;
    public static TextureAtlasSprite iconGrassSide;
    public static TextureAtlasSprite iconGrassSideOverlay;
    public static TextureAtlasSprite iconSnow;
    public static TextureAtlasSprite iconGrassSideSnowed;
    public static TextureAtlasSprite iconMyceliumSide;
    public static TextureAtlasSprite iconMyceliumTop;
    public static TextureAtlasSprite iconWaterStill;
    public static TextureAtlasSprite iconWaterFlow;
    public static TextureAtlasSprite iconLavaStill;
    public static TextureAtlasSprite iconLavaFlow;
    public static TextureAtlasSprite iconPortal;
    public static TextureAtlasSprite iconFireLayer0;
    public static TextureAtlasSprite iconFireLayer1;
    public static TextureAtlasSprite iconGlass;
    public static TextureAtlasSprite iconGlassPaneTop;
    public static TextureAtlasSprite iconCompass;
    public static TextureAtlasSprite iconClock;
    public static final String SPRITE_PREFIX_BLOCKS = "minecraft:blocks/";
    public static final String SPRITE_PREFIX_ITEMS = "minecraft:items/";
    private static final IntBuffer staticBuffer;

    public static void update() {
        TextureMap texturemap = TextureUtils.getTextureMapBlocks();
        if (texturemap != null) {
            String s = SPRITE_PREFIX_BLOCKS;
            iconGrassTop = texturemap.getSpriteSafe(s + texGrassTop);
            iconGrassSide = texturemap.getSpriteSafe(s + texGrassSide);
            iconGrassSideOverlay = texturemap.getSpriteSafe(s + texGrassSideOverlay);
            iconSnow = texturemap.getSpriteSafe(s + texSnow);
            iconGrassSideSnowed = texturemap.getSpriteSafe(s + texGrassSideSnowed);
            iconMyceliumSide = texturemap.getSpriteSafe(s + texMyceliumSide);
            iconMyceliumTop = texturemap.getSpriteSafe(s + texMyceliumTop);
            iconWaterStill = texturemap.getSpriteSafe(s + texWaterStill);
            iconWaterFlow = texturemap.getSpriteSafe(s + texWaterFlow);
            iconLavaStill = texturemap.getSpriteSafe(s + texLavaStill);
            iconLavaFlow = texturemap.getSpriteSafe(s + texLavaFlow);
            iconFireLayer0 = texturemap.getSpriteSafe(s + texFireLayer0);
            iconFireLayer1 = texturemap.getSpriteSafe(s + texFireLayer1);
            iconPortal = texturemap.getSpriteSafe(s + texPortal);
            iconGlass = texturemap.getSpriteSafe(s + texGlass);
            iconGlassPaneTop = texturemap.getSpriteSafe(s + texGlassPaneTop);
            String s1 = SPRITE_PREFIX_ITEMS;
            iconCompass = texturemap.getSpriteSafe(s1 + texCompass);
            iconClock = texturemap.getSpriteSafe(s1 + texClock);
        }
    }

    public static BufferedImage fixTextureDimensions(String name, BufferedImage bi) {
        int j;
        int i;
        if ((name.startsWith("/mob/zombie") || name.startsWith("/mob/pigzombie")) && (i = bi.getWidth()) == (j = bi.getHeight()) << 1) {
            BufferedImage bufferedimage = new BufferedImage(i, j << 1, 2);
            Graphics2D graphics2d = bufferedimage.createGraphics();
            graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2d.drawImage(bi, 0, 0, i, j, null);
            return bufferedimage;
        }
        return bi;
    }

    public static int ceilPowerOfTwo(int val) {
        int i;
        for (i = 1; i < val; i <<= 1) {
        }
        return i;
    }

    public static int getPowerOfTwo(int val) {
        int i = 1;
        int j = 0;
        while (i < val) {
            i <<= 1;
            ++j;
        }
        return j;
    }

    public static int twoToPower(int power) {
        int i = 1;
        for (int j = 0; j < power; ++j) {
            i <<= 1;
        }
        return i;
    }

    public static ITextureObject getTexture(ResourceLocation loc) {
        ITextureObject itextureobject = Config.getTextureManager().getTexture(loc);
        if (itextureobject != null) {
            return itextureobject;
        }
        if (!Config.hasResource(loc)) {
            return null;
        }
        SimpleTexture simpletexture = new SimpleTexture(loc);
        Config.getTextureManager().loadTexture(loc, simpletexture);
        return simpletexture;
    }

    public static void resourcesReloaded(IResourceManager rm) {
        if (TextureUtils.getTextureMapBlocks() != null) {
            Config.dbg("*** Reloading custom textures ***");
            CustomSky.reset();
            TextureAnimations.reset();
            TextureUtils.update();
            NaturalTextures.update();
            BetterGrass.update();
            BetterSnow.update();
            TextureAnimations.update();
            CustomColors.update();
            CustomSky.update();
            RandomEntities.update();
            CustomItems.updateModels();
            CustomEntityModels.update();
            Shaders.resourcesReloaded();
            Lang.resourcesReloaded();
            Config.updateTexturePackClouds();
            SmartLeaves.updateLeavesModels();
            CustomPanorama.update();
            CustomGuis.update();
            LayerMooshroomMushroom.update();
            CustomLoadingScreens.update();
            CustomBlockLayers.update();
            Config.getTextureManager().tick();
        }
    }

    public static TextureMap getTextureMapBlocks() {
        return Minecraft.getMinecraft().getTextureMapBlocks();
    }

    public static void registerResourceListener() {
        IResourceManager iresourcemanager = Config.getResourceManager();
        if (iresourcemanager instanceof IReloadableResourceManager) {
            IReloadableResourceManager ireloadableresourcemanager = (IReloadableResourceManager)iresourcemanager;
            IResourceManagerReloadListener iresourcemanagerreloadlistener = new IResourceManagerReloadListener(){

                @Override
                public void onResourceManagerReload(IResourceManager var1) {
                    TextureUtils.resourcesReloaded(var1);
                }
            };
            ireloadableresourcemanager.registerReloadListener(iresourcemanagerreloadlistener);
        }
        ITickableTextureObject itickabletextureobject = new ITickableTextureObject(){

            @Override
            public void tick() {
                TextureAnimations.updateAnimations();
            }

            @Override
            public void loadTexture(IResourceManager var1) {
            }

            @Override
            public int getGlTextureId() {
                return 0;
            }

            @Override
            public void setBlurMipmap(boolean p_174936_1, boolean p_174936_2) {
            }

            @Override
            public void restoreLastBlurMipmap() {
            }

            @Override
            public MultiTexID getMultiTexID() {
                return null;
            }
        };
        ResourceLocation resourcelocation = new ResourceLocation("optifine/TickableTextures");
        Config.getTextureManager().loadTickableTexture(resourcelocation, itickabletextureobject);
    }

    public static ResourceLocation fixResourceLocation(ResourceLocation loc, String basePath) {
        if (!loc.getResourceDomain().equals("minecraft")) {
            return loc;
        }
        String s = loc.getResourcePath();
        String s1 = TextureUtils.fixResourcePath(s, basePath);
        if (s1 != s) {
            loc = new ResourceLocation(loc.getResourceDomain(), s1);
        }
        return loc;
    }

    public static String fixResourcePath(String path, String basePath) {
        String s = "assets/minecraft/";
        if (path.startsWith(s)) {
            path = path.substring(s.length());
            return path;
        }
        if (path.startsWith("./")) {
            path = path.substring(2);
            if (basePath.isEmpty() || basePath.charAt(basePath.length() - 1) != '/') {
                basePath = basePath + "/";
            }
            path = basePath + path;
            return path;
        }
        if (path.startsWith("/~")) {
            path = path.substring(1);
        }
        String s1 = "mcpatcher/";
        if (path.startsWith("~/")) {
            path = path.substring(2);
            path = s1 + path;
            return path;
        }
        if (!path.isEmpty() && path.charAt(0) == '/') {
            path = s1 + path.substring(1);
            return path;
        }
        return path;
    }

    public static String getBasePath(String path) {
        int i = path.lastIndexOf(47);
        return i < 0 ? "" : path.substring(0, i);
    }

    public static void applyAnisotropicLevel() {
        if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            float f = GL11.glGetFloat((int)34047);
            float f1 = Config.getAnisotropicFilterLevel();
            f1 = Math.min(f1, f);
            GL11.glTexParameterf((int)3553, (int)34046, (float)f1);
        }
    }

    public static void bindTexture(int glTexId) {
        GlStateManager.bindTexture(glTexId);
    }

    public static boolean isPowerOfTwo(int x) {
        int i = MathHelper.roundUpToPowerOfTwo(x);
        return i == x;
    }

    public static BufferedImage scaleImage(BufferedImage bi, int w2) {
        int i = bi.getWidth();
        int j = bi.getHeight();
        int k = j * w2 / i;
        BufferedImage bufferedimage = new BufferedImage(w2, k, 2);
        Graphics2D graphics2d = bufferedimage.createGraphics();
        Object object = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        if (w2 < i || w2 % i != 0) {
            object = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        }
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, object);
        graphics2d.drawImage(bi, 0, 0, w2, k, null);
        return bufferedimage;
    }

    public static int scaleToGrid(int size, int sizeGrid) {
        int i;
        if (size == sizeGrid) {
            return size;
        }
        for (i = size / sizeGrid * sizeGrid; i < size; i += sizeGrid) {
        }
        return i;
    }

    public static int scaleToMin(int size, int sizeMin) {
        int i;
        if (size >= sizeMin) {
            return size;
        }
        for (i = sizeMin / size * size; i < sizeMin; i += size) {
        }
        return i;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Dimension getImageSize(InputStream in, String suffix) {
        Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix(suffix);
        while (iterator.hasNext()) {
            Dimension dimension;
            ImageReader imagereader = iterator.next();
            try {
                ImageInputStream imageinputstream = ImageIO.createImageInputStream(in);
                imagereader.setInput(imageinputstream);
                int i = imagereader.getWidth(imagereader.getMinIndex());
                int j = imagereader.getHeight(imagereader.getMinIndex());
                dimension = new Dimension(i, j);
            }
            catch (IOException var11) {}
            continue;
            finally {
                imagereader.dispose();
                continue;
            }
            return dimension;
        }
        return null;
    }

    public static void dbgMipmaps(TextureAtlasSprite textureatlassprite) {
        int[][] aint = textureatlassprite.getFrameTextureData(0);
        for (int i = 0; i < aint.length; ++i) {
            int[] aint1 = aint[i];
            if (aint1 == null) {
                Config.dbg(i + ": " + aint1);
                continue;
            }
            Config.dbg(i + ": " + aint1.length);
        }
    }

    public static void saveGlTexture(String name, int textureId, int mipmapLevels, int width, int height) {
        TextureUtils.bindTexture(textureId);
        GL11.glPixelStorei((int)3333, (int)1);
        GL11.glPixelStorei((int)3317, (int)1);
        File file1 = new File(name);
        File file2 = file1.getParentFile();
        if (file2 != null) {
            file2.mkdirs();
        }
        for (int i = 0; i < 16; ++i) {
            File file3 = new File(name + "_" + i + ".png");
            file3.delete();
        }
        for (int i1 = 0; i1 <= mipmapLevels; ++i1) {
            File file4 = new File(name + "_" + i1 + ".png");
            int j = width >> i1;
            int k = height >> i1;
            int l = j * k;
            IntBuffer intbuffer = BufferUtils.createIntBuffer((int)l);
            int[] aint = new int[l];
            GL11.glGetTexImage((int)3553, (int)i1, (int)32993, (int)33639, (IntBuffer)intbuffer);
            intbuffer.get(aint);
            BufferedImage bufferedimage = new BufferedImage(j, k, 2);
            bufferedimage.setRGB(0, 0, j, k, aint, 0, j);
            try {
                ImageIO.write((RenderedImage)bufferedimage, "png", file4);
                Config.dbg("Exported: " + file4);
                continue;
            }
            catch (Exception exception) {
                Config.warn("Error writing: " + file4);
                Config.warn(exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
    }

    public static void generateCustomMipmaps(TextureAtlasSprite tas, int mipmaps) {
        int i = tas.getIconWidth();
        int j = tas.getIconHeight();
        if (tas.getFrameCount() < 1) {
            ArrayList<int[][]> list = new ArrayList<int[][]>();
            int[][] aint = new int[mipmaps + 1][];
            int[] aint1 = new int[i * j];
            aint[0] = aint1;
            list.add(aint);
            tas.setFramesTextureData(list);
        }
        ArrayList<int[][]> list1 = new ArrayList<int[][]>();
        int l = tas.getFrameCount();
        for (int i1 = 0; i1 < l; ++i1) {
            int[] aint2 = TextureUtils.getFrameData(tas, i1, 0);
            if (aint2 == null || aint2.length < 1) {
                aint2 = new int[i * j];
            }
            if (aint2.length != i * j) {
                int k = (int)Math.round(Math.sqrt(aint2.length));
                if (k * k != aint2.length) {
                    aint2 = new int[1];
                    k = 1;
                }
                BufferedImage bufferedimage = new BufferedImage(k, k, 2);
                bufferedimage.setRGB(0, 0, k, k, aint2, 0, k);
                BufferedImage bufferedimage1 = TextureUtils.scaleImage(bufferedimage, i);
                int[] aint3 = new int[i * j];
                bufferedimage1.getRGB(0, 0, i, j, aint3, 0, i);
                aint2 = aint3;
            }
            int[][] aint4 = new int[mipmaps + 1][];
            aint4[0] = aint2;
            list1.add(aint4);
        }
        tas.setFramesTextureData(list1);
        tas.generateMipmaps(mipmaps);
    }

    public static int[] getFrameData(TextureAtlasSprite tas, int frame, int level) {
        List<int[][]> list = tas.getFramesTextureData();
        if (list.size() <= frame) {
            return null;
        }
        int[][] aint = list.get(frame);
        if (aint != null && aint.length > level) {
            int[] aint1 = aint[level];
            return aint1;
        }
        return null;
    }

    public static int getGLMaximumTextureSize() {
        for (int i = 65536; i > 0; i >>= 1) {
            GlStateManager.glTexImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, null);
            int j = GL11.glGetError();
            int k = GlStateManager.glGetTexLevelParameteri(32868, 0, 4096);
            if (k == 0) continue;
            return i;
        }
        return -1;
    }

    static {
        staticBuffer = GLAllocation.createDirectIntBuffer(256);
    }
}

