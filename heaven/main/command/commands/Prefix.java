/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.utils.chat.Helper;

public class Prefix
extends Command {
    public Prefix() {
        super("prefix");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            Client.instance.prefix = Client.instance.lastPrefix;
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
            Client.instance.prefix = Ganga.toString();
            Helper.sendMessage("Prefix is " + Ganga + " now!");
        }
    }
}

