/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.utils.InventoryUtils;
import heaven.main.utils.timer.TimerUtil;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class BetterSword
extends Module {
    private ItemStack prevBestSword;
    public final TimerUtil timer = new TimerUtil();

    public BetterSword() {
        super("BetterSword", new String[]{"betterSword"}, ModuleType.Combat);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        block29: {
            ItemStack bestSword;
            block31: {
                block30: {
                    block28: {
                        block27: {
                            if (Minecraft.thePlayer.getHeldItem() == null) break block27;
                            if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) break block28;
                        }
                        return;
                    }
                    if (Minecraft.thePlayer.ticksExisted % 100 != 0) break block29;
                    if (Minecraft.thePlayer.capabilities.isCreativeMode) break block30;
                    if (Minecraft.thePlayer.openContainer == null) break block31;
                    if (Minecraft.thePlayer.openContainer.windowId == 0) break block31;
                }
                return;
            }
            if ((bestSword = BetterSword.getBestItem(Comparator.comparingDouble(BetterSword::getSwordDamage))) == null) {
                return;
            }
            boolean isInHBSlot = InventoryUtils.hotbarHas(bestSword.getItem(), 0);
            if (isInHBSlot) {
                if (InventoryUtils.getItemBySlotID(0) != null) {
                    if (Objects.requireNonNull(InventoryUtils.getItemBySlotID(0)).getItem() instanceof ItemSword) {
                        isInHBSlot = BetterSword.getSwordDamage(Objects.requireNonNull(InventoryUtils.getItemBySlotID(0))) >= BetterSword.getSwordDamage(bestSword);
                    }
                } else {
                    isInHBSlot = false;
                }
            }
            boolean shouldSwitch = false;
            if (this.prevBestSword == null || !this.prevBestSword.equals(bestSword) || !isInHBSlot) {
                shouldSwitch = true;
                this.prevBestSword = bestSword;
            }
            if (shouldSwitch && this.timer.hasReached(1.0)) {
                int slotHB = InventoryUtils.getBestSwordSlotID(bestSword);
                switch (slotHB) {
                    case 0: {
                        slotHB = 36;
                        break;
                    }
                    case 1: {
                        slotHB = 37;
                        break;
                    }
                    case 2: {
                        slotHB = 38;
                        break;
                    }
                    case 3: {
                        slotHB = 39;
                        break;
                    }
                    case 4: {
                        slotHB = 40;
                        break;
                    }
                    case 5: {
                        slotHB = 41;
                        break;
                    }
                    case 6: {
                        slotHB = 42;
                        break;
                    }
                    case 7: {
                        slotHB = 43;
                        break;
                    }
                    case 8: {
                        slotHB = 44;
                    }
                }
                Minecraft.playerController.windowClick(0, slotHB, Minecraft.thePlayer.inventory.currentItem - 36, 2, Minecraft.thePlayer);
                if (this.prevBestSword == bestSword) {
                    return;
                }
                this.timer.reset();
            }
        }
    }

    private static ItemStack getBestItem(Comparator<? super ItemStack> comparator) {
        Optional<? super ItemStack> bestItem = Minecraft.thePlayer.inventoryContainer.inventorySlots.stream().map(Slot::getStack).filter(Objects::nonNull).filter(itemStack -> itemStack.getItem().getClass().equals(ItemSword.class)).max(comparator);
        return bestItem.orElse(null);
    }

    private static double getSwordDamage(ItemStack itemStack) {
        double damage = 0.0;
        Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
        if (attributeModifier.isPresent()) {
            damage = ((AttributeModifier)attributeModifier.get()).getAmount();
        }
        return damage + (double)EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
    }
}

