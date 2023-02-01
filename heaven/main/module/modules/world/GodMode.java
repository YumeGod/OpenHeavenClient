/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Fly;
import heaven.main.utils.chat.Helper;
import heaven.main.utils.timer.TimeHelper;
import heaven.main.value.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class GodMode
extends Module {
    private static final TimeHelper delay = new TimeHelper();
    private static final Mode<String> modes = new Mode("Mode", new String[]{"HmXix", "AAC3.0.1"}, "HmXix");

    public GodMode() {
        super("GodMode", ModuleType.World);
        this.addValues(modes);
    }

    @Override
    public void onEnable() {
        Helper.sendMessageWithoutPrefix("GodMode is Enabled now!");
    }

    @Override
    public void onDisable() {
        Helper.sendMessageWithoutPrefix("GodMode is Disabled now");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean PlayerCheck() {
        if (!(Minecraft.thePlayer.fallDistance <= 2.0f)) return false;
        if (!(Minecraft.thePlayer.getHealth() < 20.0f)) return false;
        if (Minecraft.thePlayer.getFoodStats().getFoodLevel() < 19) return false;
        return true;
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (modes.isCurrentMode("AAC3.0.1")) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.1, Minecraft.thePlayer.posZ, Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, true));
        }
        if (modes.isCurrentMode("HmXix")) {
            if (Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled()) {
                return;
            }
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.motionY = 0.13;
                Minecraft.thePlayer.fallDistance = 0.13f;
            }
            if (delay.isDelayComplete(0L) && GodMode.PlayerCheck()) {
                for (int i = 0; i < 280; ++i) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                    delay.reset();
                }
            }
        }
    }
}

