/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.chat.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class AutoFish
extends Module {
    public AutoFish() {
        super("AutoFish", new String[]{"autofish"}, ModuleType.Player);
    }

    @EventHandler
    public void onUpdate(EventPacketReceive e) {
        if (e.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
            Entity ent = Minecraft.theWorld.getEntityByID(packet.getEntityID());
            if (ent instanceof EntityFishHook) {
                EntityFishHook fishHook = (EntityFishHook)ent;
                if (fishHook.angler.getEntityId() == Minecraft.thePlayer.getEntityId()) {
                    if (Minecraft.thePlayer.inventory.currentItem != AutoFish.grabRodSlot()) {
                        return;
                    }
                    if (packet.getMotionX() == 0 && packet.getMotionY() != 0 && packet.getMotionZ() == 0) {
                        Helper.sendMessage("Auto Fishing...");
                        mc.rightClickMouse();
                        mc.rightClickMouse();
                    }
                }
            }
        }
    }

    private static int grabRodSlot() {
        for (int i2 = 0; i2 < 9; ++i2) {
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[i2];
            if (itemStack == null || !(itemStack.getItem() instanceof ItemFishingRod)) continue;
            return i2;
        }
        return -1;
    }
}

