/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.utils.chat.Helper;

public class Help
extends Command {
    public Help() {
        super("Help");
    }

    @Override
    public void execute(String[] args) {
        Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l----------------------------------");
        Helper.sendMessageWithoutPrefix("                    \u00a7b\u00a7lHelper");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "help >\u00a77 List commands");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "bind >\u00a77 Bind a module to a key");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "binds >\u00a77 Show bind key list");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "config >\u00a77 List all configs");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "reload >\u00a77 reload all configs and settings");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "friend >\u00a77 Add Friend a player");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "modules >\u00a77 List all modules");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "nameprotect >\u00a77 Set the name what protects your username");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "clean >\u00a77 Refresh Chunks");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "say >\u00a77 Say a message");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "setspammer >\u00a77 Set a spammer message");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "tp >\u00a77 Teleport <x> <y> <z> or <playername>");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "vclip >\u00a77 Clip up custom blocks");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "prefix >\u00a77 Set the command prefix");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "name >\u00a77 Set the client name");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "setname >\u00a77 Custom set the module name");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "title >\u00a77 Set the client title");
        Helper.sendMessageWithoutPrefix("\u00a7b" + Client.instance.prefix + "t >\u00a77 Toggle a module on/off");
        Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l----------------------------------");
    }
}

