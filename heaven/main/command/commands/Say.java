/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Say
extends Command {
    public Say() {
        super("Say");
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            StringBuilder string = new StringBuilder();
            for (String s : args) {
                string.append(s);
            }
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(string.toString()));
        } else {
            ClientNotification.sendClientMessage("Say", "Correct usage: " + Client.instance.prefix + "Say <Message>", 5000L, ClientNotification.Type.WARNING);
        }
    }
}

