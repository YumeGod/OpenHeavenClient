/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NewRegen
extends Module {
    private final Mode<String> modeValue = new Mode("Mode", new String[]{"Hmxix", "SendQueueSilent", "sendQueueWithoutEvent", "SendNewQueue"}, "Hmxix");
    private final Numbers<Double> speedValue = new Numbers<Double>("Speed", 20.0, 0.0, 1000.0, 1.0);
    private final Numbers<Double> healthValue = new Numbers<Double>("Health", 18.0, 0.0, 100.0, 1.0);
    private final Numbers<Double> foodValue = new Numbers<Double>("Food", 100.0, 0.0, 100.0, 1.0);
    private final Option<Boolean> noAirValue = new Option<Boolean>("NoAir", false);
    private final Option<Boolean> potionEffectValue = new Option<Boolean>("PotionEffect", false);
    private boolean resetTimer = false;

    public NewRegen() {
        super("NewRegen", new String[]{"newregen"}, ModuleType.Combat);
        this.addValues(this.modeValue, this.speedValue, this.foodValue, this.noAirValue, this.potionEffectValue, this.healthValue);
    }

    @EventHandler
    @EventTarget
    public void onUpdate(EventPreUpdate event) {
        if (this.resetTimer) {
            NewRegen.mc.timer.timerSpeed = 1.0f;
        }
        this.resetTimer = false;
        switch (((String)this.modeValue.get()).toLowerCase()) {
            case "Hmxix": {
                int i = 0;
                while ((double)i < (Double)this.speedValue.getValue()) {
                    Minecraft.thePlayer.sendQueue.addToSendNewQueue(new C03PacketPlayer(Minecraft.thePlayer.onGround));
                    ++i;
                }
            }
            case "SendQueueSilent": {
                int i = 0;
                while ((double)i < (Double)this.speedValue.getValue()) {
                    mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer(Minecraft.thePlayer.onGround));
                    ++i;
                }
            }
            case "sendQueueWithoutEvent": {
                int i = 0;
                while ((double)i < (Double)this.speedValue.getValue()) {
                    mc.getNetHandler().sendQueueWithoutEvent(new C03PacketPlayer(Minecraft.thePlayer.onGround));
                    ++i;
                }
            }
            case "SendNewQueue": {
                int i = 0;
                while ((double)i < (Double)this.speedValue.getValue()) {
                    mc.getNetHandler().addToSendNewQueue(new C03PacketPlayer(Minecraft.thePlayer.onGround));
                    ++i;
                }
                break;
            }
        }
    }
}

