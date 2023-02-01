/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.BlockUtils;
import heaven.main.utils.object.WorldBlockObject;
import heaven.main.utils.render.RenderUtils;
import java.awt.Color;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;

public class WillFallIn
extends Module {
    public WillFallIn() {
        super("WillFallIn", ModuleType.Render);
    }

    @EventHandler
    public void on3D(EventRender3D e) {
        WorldBlockObject worldBlock = BlockUtils.getWillFallInBlock(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
        if (!(worldBlock.getBlock() instanceof BlockAir)) {
            RenderUtils.drawBlockBox(worldBlock.getBlockPos(), Color.RED, true);
        }
    }
}

