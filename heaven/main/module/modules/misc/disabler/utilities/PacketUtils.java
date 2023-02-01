/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc.disabler.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtils {
    public static void sendPacketNoEvent(Packet packet) {
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
    }
}

