/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat.utils;

import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

public class InventoryUtils {
    public static boolean hotbarHas(Item item, int slotID) {
        for (int index = 0; index <= 36; ++index) {
            Minecraft.getMinecraft();
            ItemStack stack = Minecraft.thePlayer.inventory.getStackInSlot(index);
            if (stack == null || stack.getItem() != item || InventoryUtils.getSlotID(stack.getItem()) != slotID) continue;
            return true;
        }
        return false;
    }

    public static int getSlotID(Item item) {
        for (int index = 0; index <= 36; ++index) {
            Minecraft.getMinecraft();
            ItemStack stack = Minecraft.thePlayer.inventory.getStackInSlot(index);
            if (stack == null || stack.getItem() != item) continue;
            return index;
        }
        return -1;
    }

    public static ItemStack getItemBySlotID(int slotID) {
        for (int index = 0; index <= 36; ++index) {
            Minecraft.getMinecraft();
            ItemStack stack = Minecraft.thePlayer.inventory.getStackInSlot(index);
            if (stack == null || InventoryUtils.getSlotID(stack.getItem()) != slotID) continue;
            return stack;
        }
        return null;
    }

    public static int getBestSwordSlotID(ItemStack item) {
        for (int index = 0; index <= 36; ++index) {
            Minecraft.getMinecraft();
            ItemStack stack = Minecraft.thePlayer.inventory.getStackInSlot(index);
            if (stack == null || stack != item || InventoryUtils.getSwordDamage(stack) != InventoryUtils.getSwordDamage(item)) continue;
            return index;
        }
        return -1;
    }

    private static double getSwordDamage(ItemStack itemStack) {
        double damage = 0.0;
        Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
        if (attributeModifier.isPresent()) {
            damage = ((AttributeModifier)attributeModifier.get()).getAmount();
        }
        return damage + (double)EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
    }

    public static boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (PotionEffect o : potion.getEffects(stack)) {
                    if (o.getPotionID() != Potion.poison.getId() && o.getPotionID() != Potion.harm.getId() && o.getPotionID() != Potion.moveSlowdown.getId() && o.getPotionID() != Potion.weakness.getId()) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private static float getDamage(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0.0f;
        }
        return (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + ((ItemSword)stack.getItem()).getDamageVsEntity();
    }

    public static float getProtection(ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot = (float)((double)prot + ((double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075));
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100.0);
        }
        return prot;
    }

    public static boolean isBestWeapon(ItemStack stack) {
        float damage = InventoryUtils.getDamage(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || InventoryUtils.getDamage(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) <= damage || !(is.getItem() instanceof ItemSword)) continue;
            return false;
        }
        return stack.getItem() instanceof ItemSword;
    }

    public static boolean isBad(ItemStack item) {
        return item.getItem() instanceof ItemArmor || item.getItem() instanceof ItemTool || item.getItem() instanceof ItemBlock || item.getItem() instanceof ItemSword || item.getItem() instanceof ItemEnderPearl || item.getItem() instanceof ItemFood || item.getItem() instanceof ItemPotion && !InventoryUtils.isBadPotion(item) || item.getDisplayName().toLowerCase().contains((Object)((Object)EnumChatFormatting.GRAY) + "(right click)");
    }

    public static int findItem(int startSlot, int endSlot, Item item) {
        for (int i = startSlot; i < endSlot; ++i) {
            Minecraft.getMinecraft();
            ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack == null || stack.getItem() != item) continue;
            return i;
        }
        return -1;
    }
}

