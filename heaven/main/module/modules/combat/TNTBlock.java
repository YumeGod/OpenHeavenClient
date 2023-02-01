/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.EnumFacing;

public class TNTBlock
extends Module {
    static final int ticks = 10;
    private boolean hasBlocked;

    public TNTBlock() {
        super("TNTBlock", ModuleType.Combat);
    }

    @EventHandler
    public void onCombat(EventPreUpdate e2) {
        boolean foundTnt = false;
        if (!Minecraft.thePlayer.isDead) {
            for (Entity e1 : Minecraft.theWorld.loadedEntityList) {
                if (!(e1 instanceof EntityTNTPrimed)) continue;
                EntityTNTPrimed entityTNTPrimed = (EntityTNTPrimed)e1;
                if ((double)Minecraft.thePlayer.getDistanceToEntity(e1) > 4.0) continue;
                foundTnt = true;
                if ((double)entityTNTPrimed.fuse != 10.0 || entityTNTPrimed.isDead) continue;
                this.blockItem();
            }
            if (!foundTnt && this.hasBlocked) {
                this.unblockItem();
            }
        }
    }

    private void unblockItem() {
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, Minecraft.thePlayer.getPosition(), EnumFacing.DOWN));
        Minecraft.playerController.onStoppedUsingItem(Minecraft.thePlayer);
        this.hasBlocked = false;
    }

    private void blockItem() {
        if (Minecraft.thePlayer.getCurrentEquippedItem() != null) {
            if (Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                Minecraft.playerController.sendUseItem(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventory.getCurrentItem());
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.getPosition(), 0, Minecraft.thePlayer.getCurrentEquippedItem(), 0.0f, 0.0f, 0.0f));
                this.hasBlocked = true;
            }
        }
    }
}

