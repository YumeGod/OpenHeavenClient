/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.event.events.rendering;

import heaven.main.event.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class EventRenderCape
extends Event {
    private static ResourceLocation capeLocation;
    private final EntityPlayer player;

    public EventRenderCape(ResourceLocation capeLocation, EntityPlayer player) {
        EventRenderCape.capeLocation = capeLocation;
        this.player = player;
    }

    public static ResourceLocation getLocation() {
        return capeLocation;
    }

    public void setLocation(ResourceLocation location) {
        capeLocation = location;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }
}

