/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.Client;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Numbers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class PlayerSize
extends Module {
    public static final Numbers<Double> sizeValue = new Numbers<Double>("Size", 1.0, 0.0, 10.0, 0.1);

    public PlayerSize() {
        super("PlayerSize", ModuleType.Render);
        this.addValues(sizeValue);
    }

    public static void setSize(Entity entityIn) {
        float size = ((Double)sizeValue.getValue()).floatValue();
        if (size == 1.0f) {
            return;
        }
        if (entityIn == Minecraft.thePlayer && Client.instance.getModuleManager().getModuleByClass(PlayerSize.class).isEnabled()) {
            GlStateManager.scale(size, size, size);
        }
    }
}

