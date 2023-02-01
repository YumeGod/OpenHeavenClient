/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import java.util.regex.Pattern;

public class NameProtect
extends Command {
    private static final Pattern COMPILE = Pattern.compile(" nmsl");

    public NameProtect() {
        super("NameProtect");
    }

    @Override
    public void execute(String[] args) {
        StringBuilder string = new StringBuilder();
        if (args.length >= 1) {
            for (String str : args) {
                string.append(str).append(" ");
            }
            string.append("nmsl");
            heaven.main.module.modules.render.NameProtect.name = COMPILE.split(string.toString())[0];
            ClientNotification.sendClientMessage("NameProtect", "Set successfully!", 5000L, ClientNotification.Type.WARNING);
        } else {
            ClientNotification.sendClientMessage("NameProtect", "Correct usage: " + Client.instance.prefix + "nameprotect <Message>", 5000L, ClientNotification.Type.WARNING);
        }
    }
}

