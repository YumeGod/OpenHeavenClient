/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Mode;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;

public class Trails
extends Module {
    private final Mode<String> mode = new Mode("Mode", new String[]{"SMOKE", "HEART", "FIREWORK", "FLAME", "CLOUD", "WATER", "LAVA", "SLIME"}, "SMOKE");

    public Trails() {
        super("Trails", ModuleType.Render);
        this.addValues(this.mode);
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        this.setSuffix((Serializable)this.mode.get());
        if (this.isMoving()) {
            switch ((String)this.mode.get()) {
                case "HEART": {
                    Trails.mc.effectRenderer.emitParticleAtEntity(Minecraft.thePlayer, EnumParticleTypes.HEART);
                    break;
                }
                case "LAVA": {
                    Trails.mc.effectRenderer.emitParticleAtEntity(Minecraft.thePlayer, EnumParticleTypes.LAVA);
                    break;
                }
                case "SMOKE": {
                    Trails.mc.effectRenderer.emitParticleAtEntity(Minecraft.thePlayer, EnumParticleTypes.REDSTONE);
                    break;
                }
                case "CLOUD": {
                    Trails.mc.effectRenderer.emitParticleAtEntity(Minecraft.thePlayer, EnumParticleTypes.CLOUD);
                    break;
                }
                case "FLAME": {
                    Trails.mc.effectRenderer.emitParticleAtEntity(Minecraft.thePlayer, EnumParticleTypes.FLAME);
                    break;
                }
                case "SLIME": {
                    Trails.mc.effectRenderer.emitParticleAtEntity(Minecraft.thePlayer, EnumParticleTypes.SLIME);
                    break;
                }
                case "WATER": {
                    Trails.mc.effectRenderer.emitParticleAtEntity(Minecraft.thePlayer, EnumParticleTypes.WATER_SPLASH);
                    break;
                }
                case "FIREWORK": {
                    Trails.mc.effectRenderer.emitParticleAtEntity(Minecraft.thePlayer, EnumParticleTypes.FIREWORKS_SPARK);
                }
            }
        }
    }
}

