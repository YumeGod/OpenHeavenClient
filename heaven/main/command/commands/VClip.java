/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.command.commands;

import heaven.main.Client;
import heaven.main.command.Command;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.math.MathUtil;
import net.minecraft.client.Minecraft;

public class VClip
extends Command {
    public VClip() {
        super("vclip", new String[]{"vc"});
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            if (MathUtil.parsable(args[0], (byte)4)) {
                float distance = Float.parseFloat(args[0]);
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)distance, Minecraft.thePlayer.posZ);
                ClientNotification.sendClientMessage("VClip", "VClipped " + distance + " blocks.", 4000L, ClientNotification.Type.INFO);
            } else {
                ClientNotification.sendClientMessage("VClip", args[0] + " is not a valid number", 4000L, ClientNotification.Type.WARNING);
            }
        } else {
            ClientNotification.sendClientMessage("VClip", "Valid " + Client.instance.prefix + "vclip <number>", 4000L, ClientNotification.Type.WARNING);
        }
    }
}

