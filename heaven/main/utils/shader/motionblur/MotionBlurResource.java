/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
 */
package heaven.main.utils.shader.motionblur;

import heaven.main.Client;
import heaven.main.module.modules.render.MotionBlur;
import java.io.InputStream;
import java.util.Locale;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class MotionBlurResource
implements IResource {
    @Override
    public InputStream getInputStream() {
        MotionBlur module = (MotionBlur)Client.instance.getModuleManager().getModuleByClass(MotionBlur.class);
        double amount = (Double)module.multiplier.getValue();
        return IOUtils.toInputStream((String)String.format(Locale.ENGLISH, "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}", amount, amount, amount));
    }

    @Override
    public boolean hasMetadata() {
        return false;
    }

    public IMetadataSection getMetadata(String metadata) {
        return null;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return null;
    }

    @Override
    public String getResourcePackName() {
        return null;
    }
}

