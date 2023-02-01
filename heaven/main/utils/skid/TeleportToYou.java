/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils.skid;

import heaven.main.utils.skid.PathFinder;
import heaven.main.utils.vec.SigmaVec3;
import java.util.List;
import net.minecraft.client.Minecraft;

public abstract class TeleportToYou {
    protected final Minecraft mc;
    protected final double x;
    protected final double y;
    protected final double z;

    public TeleportToYou(Minecraft mc, double x, double y, double z) {
        this.mc = mc;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    protected List<SigmaVec3> findPath() {
        SigmaVec3 topFrom = new SigmaVec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
        SigmaVec3 to = new SigmaVec3(this.x, this.y, this.z);
        return PathFinder.computePath(topFrom, to);
    }

    public abstract void teleportToXYZ();
}

