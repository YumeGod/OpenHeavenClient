/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class AutoSword
extends Module {
    public AutoSword() {
        super("AutoSword", ModuleType.Combat);
    }

    @EventHandler
    private static void onPacket(EventPacketSend e) {
        if (e.getPacket() instanceof C02PacketUseEntity) {
            float damage = 1.0f;
            int bestSwordSlot = -1;
            for (int i = 0; i < 9; ++i) {
                float damageLevel;
                ItemStack itemStack = Minecraft.thePlayer.inventory.getStackInSlot(i);
                if (itemStack == null || !(itemStack.getItem() instanceof ItemSword) || !((damageLevel = (float)AutoSword.getSwordDamage(itemStack)) > damage)) continue;
                damage = damageLevel;
                bestSwordSlot = i;
            }
            if (bestSwordSlot != -1) {
                Minecraft.thePlayer.inventory.currentItem = bestSwordSlot;
            }
        }
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

