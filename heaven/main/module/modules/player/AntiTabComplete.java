/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import java.util.Random;
import net.minecraft.network.play.client.C14PacketTabComplete;

public class AntiTabComplete
extends Module {
    public AntiTabComplete() {
        super("AntiTabComplete", ModuleType.Player);
    }

    public void onPacket(EventPacketReceive event) {
        C14PacketTabComplete packet;
        if (event.getPacket() instanceof C14PacketTabComplete && !(packet = (C14PacketTabComplete)event.getPacket()).getMessage().isEmpty() && packet.getMessage().charAt(0) == '.') {
            String[] arguments = packet.getMessage().split(" ");
            String[] messages = new String[]{"hey what's up ", "dude ", "hey ", "hi ", "man ", "yo ", "howdy ", "omg "};
            Random random = new Random();
            packet.setMessage(messages[random.nextInt(messages.length)] + arguments[arguments.length - 1]);
        }
    }
}

