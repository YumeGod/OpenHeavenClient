/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import org.lwjgl.opengl.Display;

public class Title
extends Command {
    public Title() {
        super("title", new String[]{"title"});
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 1) {
            if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {
                Display.setTitle((String)"Client Loading......");
                return;
            }
            StringBuilder string = new StringBuilder();
            for (int i = 0; i < args.length; ++i) {
                string.append(args[i]);
                if (i == args.length - 1) break;
                string.append(" ");
            }
            Display.setTitle((String)string.toString());
            ClientNotification.sendClientMessage("Title", "Set successfully!", 5000L, ClientNotification.Type.WARNING);
        } else {
            ClientNotification.sendClientMessage("Title", "Correct usage: " + Client.instance.prefix + "title <set/clear> <Title>", 5000L, ClientNotification.Type.WARNING);
        }
    }
}

