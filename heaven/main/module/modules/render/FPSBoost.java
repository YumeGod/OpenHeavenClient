/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventRenderEntity;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class FPSBoost
extends Module {
    private final Option<Boolean> adaptiveRenderDistance = new Option<Boolean>("AdaptiveRenderDistance", true);
    private final Option<Boolean> crappyModels = new Option<Boolean>("RemoveBots", true);
    private final Option<Boolean> clearFarEntities = new Option<Boolean>("ClearFarEntities", true);
    public static final Option<Boolean> lazyStrategy = new Option<Boolean>("LazyStrategy", false);
    public static boolean clear = false;

    public FPSBoost() {
        super("FPSBoost", ModuleType.Render);
        this.addValues(this.adaptiveRenderDistance, this.crappyModels, this.clearFarEntities, lazyStrategy);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        clear = (Boolean)this.crappyModels.getValue();
        if (((Boolean)this.adaptiveRenderDistance.getValue()).booleanValue()) {
            EntityLivingBase entity = FPSBoost.getFarthest(96.0);
            if (entity == null) {
                FPSBoost.mc.gameSettings.renderDistanceChunks = 4;
            } else {
                GameSettings gameSettings = FPSBoost.mc.gameSettings;
                int renderDistanceChunks = Minecraft.thePlayer.getDistanceToEntity(entity) > 96.0f ? 6 : (int)(Minecraft.thePlayer.getDistanceToEntity(entity) / 16.0f);
                gameSettings.renderDistanceChunks = renderDistanceChunks;
            }
        }
    }

    @EventHandler
    public void onRenderPlayer(EventRenderEntity event) {
        Entity entity = event.getEntity();
        if (entity.getDistanceToEntity(Minecraft.thePlayer) > 120.0f && ((Boolean)this.clearFarEntities.getValue()).booleanValue()) {
            event.setCancelled(true);
        }
    }

    private static EntityLivingBase getFarthest(double range) {
        double dist = range;
        EntityLivingBase target = null;
        for (Entity object : Minecraft.theWorld.loadedEntityList) {
            if (!(object instanceof EntityLivingBase)) continue;
            EntityLivingBase player = (EntityLivingBase)object;
            double currentDist = Minecraft.thePlayer.getDistanceToEntity(player);
            if (currentDist < dist) continue;
            dist = currentDist;
            target = player;
        }
        return target;
    }
}

