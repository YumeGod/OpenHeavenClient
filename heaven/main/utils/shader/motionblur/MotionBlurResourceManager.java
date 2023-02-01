/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.shader.motionblur;

import heaven.main.utils.shader.motionblur.MotionBlurResource;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class MotionBlurResourceManager
implements IResourceManager {
    @Override
    public Set<String> getResourceDomains() {
        return null;
    }

    @Override
    public IResource getResource(ResourceLocation location) throws IOException {
        return new MotionBlurResource();
    }

    @Override
    public List<IResource> getAllResources(ResourceLocation location) throws IOException {
        return null;
    }
}

