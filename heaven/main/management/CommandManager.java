/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.management;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.command.commands.Bind;
import heaven.main.command.commands.Binds;
import heaven.main.command.commands.Clean;
import heaven.main.command.commands.Config;
import heaven.main.command.commands.Help;
import heaven.main.command.commands.Hide;
import heaven.main.command.commands.Modules;
import heaven.main.command.commands.NameProtect;
import heaven.main.command.commands.Prefix;
import heaven.main.command.commands.Reload;
import heaven.main.command.commands.Say;
import heaven.main.command.commands.SetClientName;
import heaven.main.command.commands.SetSpammer;
import heaven.main.command.commands.Teleport;
import heaven.main.command.commands.Title;
import heaven.main.command.commands.Toggle;
import heaven.main.command.commands.VClip;
import heaven.main.command.commands.setName;
import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventChat;
import heaven.main.management.EventManager;
import heaven.main.management.Manager;
import heaven.main.module.modules.misc.NoCommand;
import heaven.main.utils.chat.Helper;
import heaven.main.utils.string.StringUtils;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;

public class CommandManager
implements Manager {
    private List<Command> commands;

    @Override
    public void init() {
        this.commands = new ArrayList<Command>();
        this.commands.add(new Help());
        this.commands.add(new Toggle());
        this.commands.add(new Bind());
        this.commands.add(new Binds());
        this.commands.add(new Modules());
        this.commands.add(new Say());
        this.commands.add(new Config());
        this.commands.add(new Reload());
        this.commands.add(new SetSpammer());
        this.commands.add(new NameProtect());
        this.commands.add(new Title());
        this.commands.add(new SetClientName());
        this.commands.add(new setName());
        this.commands.add(new Prefix());
        this.commands.add(new Hide());
        this.commands.add(new Clean());
        this.commands.add(new Teleport());
        this.commands.add(new VClip());
        EventManager.register(this);
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    private Optional<Command> getCommandByName(String name) {
        return this.commands.stream().filter(c2 -> {
            boolean isAlias = false;
            for (String str : c2.getAlias()) {
                if (!str.equalsIgnoreCase(name)) continue;
                isAlias = true;
                break;
            }
            return c2.getName().equalsIgnoreCase(name) || isAlias;
        }).findFirst();
    }

    public void add(Command command) {
        this.commands.add(command);
    }

    @EventHandler
    public void onSendMessage(EventChat event) {
        String s;
        String[] command;
        String message = event.getMessage();
        if (!message.isEmpty() && message.charAt(0) == '/' && (command = (s = message.substring(1)).split(" ")).length > 0) {
            StringBuilder msg = new StringBuilder();
            for (int index = 1; index < command.length; ++index) {
                msg.append(command[index]).append(" ");
            }
            switch (command[0]) {
                case "messagecopy": {
                    event.setCancelled(true);
                    this.copy(msg.toString());
                    Helper.sendMessage("Heaven-ChatCopy", "Chat copy out.");
                    break;
                }
                case "translate": {
                    event.setCancelled(true);
                    this.translate(msg.toString());
                }
            }
        }
    }

    public void translate(String str) {
        Thread translate = new Thread(() -> {
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer != null) {
                Helper.sendMessage("HeavenTranslate", StringUtils.translate(str));
            }
        });
        translate.setDaemon(true);
        translate.start();
    }

    public void copy(String str) {
        StringSelection stsel = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stsel, stsel);
    }

    @EventHandler
    private void onChat(EventChat e) {
        if (!Client.instance.getModuleManager().getModuleByClass(NoCommand.class).isEnabled() && e.getMessage().length() > 1 && e.getMessage().startsWith(Client.instance.prefix)) {
            e.setCancelled(true);
            String[] args = e.getMessage().trim().substring(1).split(" ");
            Optional<Command> possibleCmd = this.getCommandByName(args[0]);
            if (possibleCmd.isPresent()) {
                possibleCmd.get().execute(Arrays.copyOfRange(args, 1, args.length));
            } else {
                Helper.sendMessage("Command not found Try '" + Client.instance.prefix + "help'");
            }
        }
    }
}

