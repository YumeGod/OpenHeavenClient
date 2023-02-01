/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.skid;

import heaven.main.utils.skid.TeleportToYou;
import heaven.main.utils.vec.SigmaVec3;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class VanillaTeleport
extends TeleportToYou {
    public VanillaTeleport(Minecraft mc, double x, double y, double z) {
        super(mc, x, y, z);
    }

    @Override
    public void teleportToXYZ() {
        for (SigmaVec3 vec3 : this.findPath()) {
            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), true));
            Minecraft.thePlayer.setPositionAndUpdate(this.x, this.y, this.z);
        }
    }
}

