/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.chat.Helper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerPositions
extends Module {
    public final List<Entity> collectedEntities = new ArrayList<Entity>();

    public PlayerPositions() {
        super("PlayerPositions", ModuleType.World);
    }

    @Override
    public void onEnable() {
        this.collectEntities();
        Helper.sendMessageWithoutPrefix("\u00a7b=====================================================");
        for (Entity entity : this.collectedEntities) {
            Helper.sendMessageWithoutPrefix("\u00a7bName : \u00a77" + entity.getName() + " \u00a7a, X : \u00a77" + (int)entity.getPos().getX() + " \u00a7a, Y : \u00a77" + (int)entity.getPos().getY() + " \u00a7a, Z : \u00a77" + (int)entity.getPos().getZ());
        }
        Helper.sendMessageWithoutPrefix("\u00a7b=====================================================");
        this.collectedEntities.clear();
        this.setEnabled(false);
    }

    private void collectEntities() {
        this.collectedEntities.clear();
        List playerEntities = Minecraft.theWorld.loadedEntityList;
        for (Object playerEntity : playerEntities) {
            Entity entity = (Entity)playerEntity;
            if (!this.isValid(entity)) continue;
            this.collectedEntities.add(entity);
        }
    }

    private boolean isValid(Entity entity) {
        if (entity == Minecraft.thePlayer) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (entity.isInvisible()) {
            return false;
        }
        if (entity instanceof EntityItem) {
            return false;
        }
        if (entity instanceof EntityAnimal) {
            return false;
        }
        return entity instanceof EntityPlayer;
    }
}

