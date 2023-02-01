/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.management;

import heaven.main.Client;
import heaven.main.management.FileManager;
import heaven.main.management.Manager;
import heaven.main.utils.chat.Helper;
import java.util.HashMap;
import java.util.List;
import net.minecraft.util.EnumChatFormatting;

public class FriendManager
implements Manager {
    private static HashMap<String, String> friends;

    @Override
    public void init() {
        friends = new HashMap();
        List<String> friends = FileManager.read("Friends.txt");
        for (String friend : friends) {
            if (friend.contains(":")) {
                String name = friend.split(":")[0];
                String alias = friend.split(":")[1];
                FriendManager.friends.put(name, alias);
                continue;
            }
            FriendManager.friends.put(friend, friend);
        }
        Client.instance.getCommandManager().add(new Command());
    }

    public static boolean isFriend(String name) {
        return friends.containsKey(name);
    }

    public static String getAlias(String friends2) {
        return friends.get(friends2);
    }

    static class Command
    extends heaven.main.command.Command {
        Command() {
            super("friend", new String[]{"f", "friends", "fr"});
        }

        @Override
        public void execute(String[] args) {
            if (args.length >= 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    String friends = "";
                    friends = friends + String.format("%s:%s%s", args[1], args[2], System.lineSeparator());
                    friends.put(args[1], args[2]);
                    Helper.sendMessage("> " + String.format("%s has been added as %s", args[1], args[2]));
                    FileManager.save("Friends.txt", friends, true);
                } else if (args[0].equalsIgnoreCase("del")) {
                    friends.remove(args[1]);
                    Helper.sendMessage("> " + String.format("%s has been removed from your friends list", args[1]));
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (!friends.isEmpty()) {
                        int var5 = 1;
                        for (String fr : friends.values()) {
                            Helper.sendMessage("> " + String.format("%s. %s", var5, fr));
                            ++var5;
                        }
                    } else {
                        Helper.sendMessage("> get some friends fag lmao");
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add")) {
                    String friends = "";
                    friends = friends + String.format("%s%s", args[1], System.lineSeparator());
                    friends.put(args[1], args[1]);
                    Helper.sendMessage("> " + String.format("%s has been added as %s", args[1], args[1]));
                    FileManager.save("Friends.txt", friends, true);
                } else if (args[0].equalsIgnoreCase("del")) {
                    friends.remove(args[1]);
                    Helper.sendMessage("> " + String.format("%s has been removed from your friends list", args[1]));
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (!friends.isEmpty()) {
                        int var5 = 1;
                        for (String fr : friends.values()) {
                            Helper.sendMessage("> " + String.format("%s. %s", var5, fr));
                            ++var5;
                        }
                    } else {
                        Helper.sendMessage("> you don't have any you lonely fuck");
                    }
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    if (!friends.isEmpty()) {
                        int var5 = 1;
                        for (String fr : friends.values()) {
                            Helper.sendMessage(String.format("%s. %s", var5, fr));
                            ++var5;
                        }
                    } else {
                        Helper.sendMessage("you don't have any you lonely fuck");
                    }
                } else if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("del")) {
                    Helper.sendMessage("> Correct usage: " + (Object)((Object)EnumChatFormatting.GRAY) + "Valid \\f add/del <player>");
                } else {
                    Helper.sendMessage("> " + (Object)((Object)EnumChatFormatting.GRAY) + "Please enter a players name");
                }
            } else {
                Helper.sendMessage("> Correct usage: " + (Object)((Object)EnumChatFormatting.GRAY) + "Valid \\f add/del <player>");
            }
        }
    }
}

