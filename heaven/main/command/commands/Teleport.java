/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.module.modules.movement.TeleportMod;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import net.minecraft.client.Minecraft;

public class Teleport
extends Command {
    public Teleport() {
        super("tp", new String[]{"tp", "teleport"});
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 3) {
            TeleportMod.x = Float.parseFloat(args[0]);
            TeleportMod.y = Float.parseFloat(args[1]);
            TeleportMod.z = Float.parseFloat(args[2]);
            Client.instance.getModuleManager().getModuleByClass(TeleportMod.class).setEnabled(true);
        } else if (args.length >= 1) {
            if (Minecraft.theWorld.getPlayerEntityByName(args[0]) != null) {
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 5.0, Minecraft.thePlayer.posZ);
                TeleportMod.isTPPlayer = true;
                TeleportMod.playername = args[0];
                Client.instance.getModuleManager().getModuleByClass(TeleportMod.class).setEnabled(true);
            } else {
                ClientNotification.sendClientMessage("Teleport", "Player do not exist", 5000L, ClientNotification.Type.INFO);
            }
        } else {
            ClientNotification.sendClientMessage("Teleport", Client.instance.prefix + "tp <x> <y> <z> or .tp <playername>", 5000L, ClientNotification.Type.WARNING);
        }
    }
}

