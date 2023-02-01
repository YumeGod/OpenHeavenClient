/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomGuis;
import net.optifine.EmissiveTextures;
import net.optifine.RandomEntities;
import net.optifine.shaders.ShadersTex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureManager
implements ITickable,
IResourceManagerReloadListener {
    private static final Logger logger = LogManager.getLogger();
    private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.newHashMap();
    private final List<ITickable> listTickables = Lists.newArrayList();
    private final Map<String, Integer> mapTextureCounters = Maps.newHashMap();
    private final IResourceManager theResourceManager;
    private ITextureObject boundTexture;
    private ResourceLocation boundTextureLocation;

    public TextureManager(IResourceManager resourceManager) {
        this.theResourceManager = resourceManager;
    }

    public void bindTexture(ResourceLocation resource) {
        if (Config.isRandomEntities()) {
            resource = RandomEntities.getTextureLocation(resource);
        }
        if (Config.isCustomGuis()) {
            resource = CustomGuis.getTextureLocation(resource);
        }
        ITextureObject itextureobject = this.mapTextureObjects.get(resource);
        if (EmissiveTextures.isActive()) {
            itextureobject = EmissiveTextures.getEmissiveTexture(itextureobject, this.mapTextureObjects);
        }
        if (itextureobject == null) {
            itextureobject = new SimpleTexture(resource);
            this.loadTexture(resource, itextureobject);
        }
        if (Config.isShaders()) {
            ShadersTex.bindTexture(itextureobject);
        } else {
            TextureUtil.bindTexture(itextureobject.getGlTextureId());
        }
        this.boundTexture = itextureobject;
        this.boundTextureLocation = resource;
    }

    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
        if (this.loadTexture(textureLocation, textureObj)) {
            this.listTickables.add(textureObj);
            return true;
        }
        return false;
    }

    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
        boolean flag = true;
        try {
            textureObj.loadTexture(this.theResourceManager);
        }
        catch (IOException ioexception) {
            logger.warn("Failed to load texture: " + textureLocation, (Throwable)ioexception);
            textureObj = TextureUtil.missingTexture;
            this.mapTextureObjects.put(textureLocation, textureObj);
            flag = false;
        }
        catch (Throwable throwable) {
            final ITextureObject textureObjf = textureObj;
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            crashreportcategory.addCrashSectionCallable("Texture object class", new Callable<String>(){

                @Override
                public String call() {
                    return textureObjf.getClass().getName();
                }
            });
            throw new ReportedException(crashreport);
        }
        this.mapTextureObjects.put(textureLocation, textureObj);
        return flag;
    }

    public ITextureObject getTexture(ResourceLocation textureLocation) {
        return this.mapTextureObjects.get(textureLocation);
    }

    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
        Integer integer;
        if (name.equals("logo")) {
            texture = Config.getMojangLogoTexture(texture);
        }
        integer = (integer = this.mapTextureCounters.get(name)) == null ? Integer.valueOf(1) : Integer.valueOf(integer + 1);
        this.mapTextureCounters.put(name, integer);
        ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", name, integer));
        this.loadTexture(resourcelocation, texture);
        return resourcelocation;
    }

    @Override
    public void tick() {
        for (ITickable itickable : this.listTickables) {
            itickable.tick();
        }
    }

    public void deleteTexture(ResourceLocation textureLocation) {
        ITextureObject itextureobject = this.getTexture(textureLocation);
        if (itextureobject != null) {
            this.mapTextureObjects.remove(textureLocation);
            TextureUtil.deleteTexture(itextureobject.getGlTextureId());
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());
        Iterator<ResourceLocation> iterator = this.mapTextureObjects.keySet().iterator();
        while (iterator.hasNext()) {
            ResourceLocation resourcelocation = iterator.next();
            String s = resourcelocation.getResourcePath();
            if (!s.startsWith("mcpatcher/") && !s.startsWith("optifine/") && !EmissiveTextures.isEmissive(resourcelocation)) continue;
            ITextureObject itextureobject = this.mapTextureObjects.get(resourcelocation);
            if (itextureobject instanceof AbstractTexture) {
                AbstractTexture abstracttexture = (AbstractTexture)itextureobject;
                abstracttexture.deleteGlTexture();
            }
            iterator.remove();
        }
        EmissiveTextures.update();
        Iterator<Map.Entry<ResourceLocation, ITextureObject>> iterator2 = new HashSet<Map.Entry<ResourceLocation, ITextureObject>>(this.mapTextureObjects.entrySet()).iterator();
        while (iterator2.hasNext()) {
            Map.Entry<ResourceLocation, ITextureObject> e;
            Map.Entry<ResourceLocation, ITextureObject> entry = e = iterator2.next();
            this.loadTexture(entry.getKey(), entry.getValue());
        }
    }

    public void reloadBannerTextures() {
        Iterator<Map.Entry<ResourceLocation, ITextureObject>> iterator = new HashSet<Map.Entry<ResourceLocation, ITextureObject>>(this.mapTextureObjects.entrySet()).iterator();
        while (iterator.hasNext()) {
            Map.Entry<ResourceLocation, ITextureObject> e;
            Map.Entry<ResourceLocation, ITextureObject> entry = e = iterator.next();
            ResourceLocation resourcelocation = entry.getKey();
            ITextureObject itextureobject = entry.getValue();
            if (!(itextureobject instanceof LayeredColorMaskTexture)) continue;
            this.loadTexture(resourcelocation, itextureobject);
        }
    }

    public ITextureObject getBoundTexture() {
        return this.boundTexture;
    }

    public ResourceLocation getBoundTextureLocation() {
        return this.boundTextureLocation;
    }
}

