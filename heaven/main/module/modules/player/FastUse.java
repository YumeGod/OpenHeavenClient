/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastUse
extends Module {
    private final Option<Boolean> nomove = new Option<Boolean>("NoMove", false);
    private final Option<Boolean> g = new Option<Boolean>("OnlyGround", false);
    public final String[] eatt = new String[]{"Vanilla", "NCP"};
    private final Mode<String> eat = new Mode("Mode", this.eatt, this.eatt[0]);
    private final Numbers<Double> packet = new Numbers<Double>("Packet", 20.0, 1.0, 1000.0, 1.0, () -> this.eat.isCurrentMode("Vanilla"));

    public FastUse() {
        super("FastUse", ModuleType.Player);
        this.addValues(this.eat, this.packet, this.g, this.nomove);
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        int i;
        this.setSuffix((Serializable)this.eat.get());
        if (((Boolean)this.g.getValue()).booleanValue()) {
            if (!Minecraft.thePlayer.onGround) {
                return;
            }
        }
        if (((Boolean)this.nomove.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.moving()) {
                return;
            }
        }
        if (!Minecraft.thePlayer.isUsingItem()) {
            return;
        }
        if (FastUse.check() && this.eat.isCurrentMode("Vanilla")) {
            for (i = 0; i < ((Double)this.packet.getValue()).intValue(); ++i) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
            }
        }
        if (this.eat.isCurrentMode("NCP") && FastUse.check()) {
            if (Minecraft.thePlayer.getItemInUseDuration() == (Minecraft.thePlayer.onGround ? 15 : 16)) {
                for (i = 0; i < 20; ++i) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                }
            }
        }
    }

    private static boolean check() {
        Item usitem = Minecraft.thePlayer.itemInUse.item;
        return usitem instanceof ItemFood || usitem instanceof ItemBucketMilk || usitem instanceof ItemPotion;
    }
}

