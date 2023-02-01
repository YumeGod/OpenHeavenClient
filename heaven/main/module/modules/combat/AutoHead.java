/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Mouse;

public class AutoHead
extends Module {
    private boolean eatingApple;
    private int switched = -1;
    public static boolean doingStuff;
    private final TimerUtil timer = new TimerUtil();
    private final Option<Boolean> eatHeads = new Option<Boolean>("Eatheads", true);
    private final Option<Boolean> eatApples = new Option<Boolean>("Eatapples", true);
    private final Option<Boolean> potcheck = new Option<Boolean>("PotionCheck", true);
    private final Option<Boolean> away = new Option<Boolean>("AwayRun", true);
    private final Numbers<Double> health = new Numbers<Double>("Health", 10.0, 1.0, 20.0, 1.0);
    private final Numbers<Double> delay = new Numbers<Double>("Delay", 750.0, 10.0, 2000.0, 25.0);

    public AutoHead() {
        super("AutoHead", ModuleType.Combat);
        this.addValues(this.health, this.delay, this.eatApples, this.eatHeads, this.potcheck, this.away);
    }

    @Override
    public void onEnable() {
        doingStuff = false;
        this.eatingApple = false;
        this.switched = -1;
        this.timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        doingStuff = false;
        if (this.eatingApple) {
            this.repairItemPress();
            this.repairItemSwitch();
        }
        super.onDisable();
    }

    private void repairItemPress() {
        KeyBinding keyBindUseItem;
        if (AutoHead.mc.gameSettings != null && (keyBindUseItem = AutoHead.mc.gameSettings.keyBindUseItem) != null) {
            keyBindUseItem.pressed = false;
        }
    }

    private boolean check() {
        block6: {
            block5: {
                if (((Boolean)this.potcheck.get()).booleanValue()) {
                    if (Minecraft.thePlayer.isPotionActive(Potion.regeneration)) {
                        return true;
                    }
                }
                if (Minecraft.thePlayer.capabilities.isCreativeMode) break block5;
                if (!((double)Minecraft.thePlayer.getHealth() >= (Double)this.health.getValue())) break block6;
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        InventoryPlayer inventory = Minecraft.thePlayer.inventory;
        if (!((Boolean)this.away.get()).booleanValue()) {
            if (Minecraft.thePlayer == null) {
                return;
            }
            if (inventory == null) {
                return;
            }
        }
        doingStuff = false;
        if (!Mouse.isButtonDown((int)0) && !Mouse.isButtonDown((int)1)) {
            KeyBinding useItem = AutoHead.mc.gameSettings.keyBindUseItem;
            if (!this.timer.hasReached((Double)this.delay.getValue())) {
                this.eatingApple = false;
                this.repairItemPress();
                this.repairItemSwitch();
                return;
            }
            if (this.check()) {
                this.timer.reset();
                if (this.eatingApple) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                }
                return;
            }
            for (int i = 0; i < 2; ++i) {
                int slot;
                boolean doEatHeads;
                boolean bl = doEatHeads = i != 0;
                if (doEatHeads) {
                    if (!((Boolean)this.eatHeads.getValue()).booleanValue()) {
                        continue;
                    }
                } else if (!((Boolean)this.eatApples.getValue()).booleanValue()) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                    continue;
                }
                if ((slot = doEatHeads ? this.getItemFromHotbar(397) : this.getItemFromHotbar(322)) == -1) continue;
                int tempSlot = inventory.currentItem;
                doingStuff = true;
                if (doEatHeads) {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                    this.timer.reset();
                    continue;
                }
                inventory.currentItem = slot;
                useItem.pressed = true;
                if (this.eatingApple) continue;
                this.eatingApple = true;
                this.switched = tempSlot;
            }
        }
    }

    private void repairItemSwitch() {
        EntityPlayerSP p = Minecraft.thePlayer;
        if (p == null) {
            return;
        }
        InventoryPlayer inventory = p.inventory;
        if (inventory == null) {
            return;
        }
        int switched = this.switched;
        if (switched == -1) {
            return;
        }
        inventory.currentItem = switched;
        this.switched = switched = -1;
    }

    private int getItemFromHotbar(int id) {
        for (int i = 0; i < 9; ++i) {
            if (Minecraft.thePlayer.inventory.mainInventory[i] == null) continue;
            ItemStack is = Minecraft.thePlayer.inventory.mainInventory[i];
            Item item = is.getItem();
            if (Item.getIdFromItem(item) != id) continue;
            return i;
        }
        return -1;
    }
}

