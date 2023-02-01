/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;

public class FastThrow
extends Module {
    public FastThrow() {
        super("FastThrow", ModuleType.Combat);
    }

    @EventHandler
    public void onTick(EventTick e) {
        Item item = Minecraft.thePlayer.getHeldItem().getItem();
        if ((item instanceof ItemSnowball || item instanceof ItemPotion || item instanceof ItemEgg || item instanceof ItemExpBottle || item instanceof ItemFishingRod) && FastThrow.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            FastThrow.mc.rightClickDelayTimer = 0;
        }
    }
}

