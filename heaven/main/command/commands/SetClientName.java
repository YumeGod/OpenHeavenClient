/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.utils.chat.Helper;

public class SetClientName
extends Command {
    public SetClientName() {
        super("name", new String[0]);
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            Client.instance.hudname = Client.instance.lastName;
        } else {
            StringBuilder Ganga = null;
            boolean a = false;
            for (String s : args) {
                if (!a) {
                    Ganga = new StringBuilder(s);
                    a = true;
                    continue;
                }
                Ganga.append(" ").append(s);
            }
            Client.instance.hudname = Ganga.toString();
            Helper.sendMessage("Client Name is " + Ganga + " now!");
        }
    }
}

