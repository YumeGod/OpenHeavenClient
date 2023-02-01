/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.drakApi.MotionUpdateEvent;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.world.ChestStealer;
import heaven.main.module.modules.world.StealerUtils.Timer;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor
extends Module {
    public static final Numbers<Double> equipDelay = new Numbers<Double>("Delay", 1.0, 0.0, 10.0, 1.0);
    private final Option<Boolean> open_inv = new Option<Boolean>("Open Inventory", false);
    private static final Timer timer = new Timer();

    public AutoArmor() {
        super("AutoArmor", new String[]{"autoarmor"}, ModuleType.Combat);
        this.addValues(equipDelay, this.open_inv);
    }

    private float getProtection(ItemStack stack) {
        float protection = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            protection = (float)((double)protection + ((double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075));
            protection = (float)((double)protection + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            protection = (float)((double)protection + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            protection = (float)((double)protection + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            protection = (float)((double)protection + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            protection = (float)((double)protection + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100.0);
        }
        return protection;
    }

    @EventHandler
    public void onEvent(MotionUpdateEvent event) {
        if (event.getState().equals((Object)MotionUpdateEvent.State.PRE)) {
            if (!(AutoArmor.mc.currentScreen instanceof GuiInventory) && ((Boolean)this.open_inv.get()).booleanValue() || ChestStealer.isStealing()) {
                return;
            }
            if (AutoArmor.mc.currentScreen == null || AutoArmor.mc.currentScreen instanceof GuiInventory || AutoArmor.mc.currentScreen instanceof GuiChat) {
                for (int type = 1; type < 5; ++type) {
                    if (Minecraft.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                        ItemStack slotStack = Minecraft.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                        if (this.isBestArmor(slotStack, type)) continue;
                        Minecraft.thePlayer.drop(4 + type);
                    }
                    for (int i = 9; i < 45; ++i) {
                        if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
                        ItemStack slotStack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (!this.isBestArmor(slotStack, type) || !(this.getProtection(slotStack) > 0.0f) || !timer.check((float)((Double)equipDelay.get() * 50.0))) continue;
                        Minecraft.thePlayer.shiftClick(i);
                        timer.reset();
                    }
                }
            }
        }
    }

    public static boolean isWorking() {
        return !timer.check(((Double)equipDelay.get()).floatValue() * 100.0f);
    }

    public boolean isBestArmor(ItemStack stack, int type) {
        String strType = "";
        switch (type) {
            case 1: {
                strType = "helmet";
                break;
            }
            case 2: {
                strType = "chestplate";
                break;
            }
            case 3: {
                strType = "leggings";
                break;
            }
            case 4: {
                strType = "boots";
            }
        }
        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }
        float protection = this.getProtection(stack);
        for (int i = 5; i < 45; ++i) {
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (!(this.getProtection(is) > protection) || !is.getUnlocalizedName().contains(strType)) continue;
            return false;
        }
        return true;
    }
}

