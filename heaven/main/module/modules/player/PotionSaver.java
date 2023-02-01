/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Fly;
import heaven.main.utils.chat.Helper;
import heaven.main.value.Mode;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionSaver
extends Module {
    private final Mode<String> mode = new Mode("Mode", new String[]{"Auto", "Simple"}, "Auto");
    private boolean canStop;
    private int goodPotions;
    private int badPotions;

    public PotionSaver() {
        super("PotionSaver", ModuleType.Player);
        this.addValues(this.mode);
    }

    @Override
    public void onEnable() {
        Helper.sendMessage("PotionSaver: Only not movement and not change body yaw pitch can save potion effect.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.canStop = false;
        this.goodPotions = 0;
        this.badPotions = 0;
    }

    public boolean shouldStopPotion() {
        return this.goodPotions >= this.badPotions && this.canStop;
    }

    /*
     * Unable to fully structure code
     */
    @EventHandler
    public void onPacket(EventPacketReceive e) {
        block10: {
            block9: {
                this.setSuffix((Serializable)this.mode.get());
                if (!this.mode.is("Simple")) break block9;
                if (Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled()) {
                    return;
                }
                if (e.getPacket() instanceof C03PacketPlayer && !(e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) && !(e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) && !(e.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook)) {
                    if (Minecraft.thePlayer != null) {
                        if (!Minecraft.thePlayer.isUsingItem()) {
                            e.setCancelled(true);
                            ** GOTO lbl40
                        }
                    }
                }
                break block10;
            }
            if (!this.mode.is("Auto")) break block10;
            collection = Minecraft.thePlayer.getActivePotionEffects();
            rot = (C03PacketPlayer)e.getPacket();
            if (Minecraft.thePlayer.moving() || collection.isEmpty()) ** GOTO lbl-1000
            if (Minecraft.thePlayer.isUsingItem()) ** GOTO lbl-1000
            if (!Minecraft.thePlayer.isSwingInProgress && !rot.isRotating()) {
                v0 = true;
            } else lbl-1000:
            // 3 sources

            {
                v0 = false;
            }
            this.canStop = v0;
            for (PotionEffect potioneffect : collection) {
                potion = Potion.potionTypes[potioneffect.getPotionID()];
                if (potion.isUsable()) {
                    ++this.goodPotions;
                    continue;
                }
                if (!potion.isBadEffect()) continue;
                ++this.badPotions;
            }
            e.setCancelled(this.shouldStopPotion());
            if (!this.canStop) {
                this.goodPotions = 0;
                this.badPotions = 0;
            }
        }
    }
}

