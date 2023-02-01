/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoSoup
extends Module {
    final TimerUtil timer = new TimerUtil();
    public final Numbers<Double> MaxDELAY = new Numbers<Double>("MaxDelay", 350.0, 0.0, 1000.0, 10.0);
    public final Numbers<Double> MinDELAY = new Numbers<Double>("MinDelay", 350.0, 0.0, 1000.0, 10.0);
    public final Numbers<Double> HEALTH = new Numbers<Double>("Health", 3.0, 0.0, 20.0, 1.0);
    public final Option<Boolean> DROP = new Option<Boolean>("Drop", true);

    public AutoSoup() {
        super("AutoSoup", new String[]{"autosoup"}, ModuleType.Combat);
        this.addValues(this.MaxDELAY, this.MinDELAY, this.HEALTH, this.DROP);
    }

    @EventHandler
    public void onEvent(EventPreUpdate event) {
        int soupSlot = this.getSoupFromInventory();
        if (soupSlot != -1) {
            if (Minecraft.thePlayer.getHealth() < ((Double)this.HEALTH.getValue()).floatValue() && this.timer.hasReached(MathUtil.randomNumber((Double)this.MaxDELAY.getValue(), (Double)this.MinDELAY.getValue()))) {
                AutoSoup.swap(this.getSoupFromInventory());
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
            }
        }
    }

    protected static void swap(int slot) {
        Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, 6, 2, Minecraft.thePlayer);
    }

    public int getSoupFromInventory() {
        int soup = -1;
        for (int i = 1; i < 45; ++i) {
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (Item.getIdFromItem(item) != 282) continue;
            soup = i;
        }
        return soup;
    }
}

