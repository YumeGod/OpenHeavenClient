/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class AntiInvisibility
extends Module {
    private final List<Entity> invisibilityEntities = new CopyOnWriteArrayList<Entity>();

    public AntiInvisibility() {
        super("AntiInvisibility", ModuleType.World);
    }

    @Override
    public void onEnable() {
        for (Entity entity : Minecraft.theWorld.loadedEntityList) {
            if (!entity.isInvisible()) continue;
            this.invisibilityEntities.add(entity);
            this.renderInfo("AntiInvisibility", "Remove Invisibility of " + entity.getName(), 4000L, ClientNotification.Type.INFO);
            entity.setInvisible(false);
        }
    }

    @Override
    public void onDisable() {
        for (Entity invisibilityEntity : this.invisibilityEntities) {
            invisibilityEntity.setInvisible(true);
        }
    }

    @EventHandler
    public void onTick(EventTick e) {
        for (Entity entity : Minecraft.theWorld.loadedEntityList) {
            if (!entity.isInvisible()) continue;
            this.invisibilityEntities.add(entity);
            entity.setInvisible(false);
        }
    }
}

