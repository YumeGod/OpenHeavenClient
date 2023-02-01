/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.module.modules.world.StealerUtils.Timer;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.PotionEffect;

public class AutoPotion
extends Module {
    private final Numbers<Double> health = new Numbers<Double>("Health", 10.0, 0.0, 20.0, 0.5);
    private final Numbers<Double> delay = new Numbers<Double>("Delay", 1000.0, 0.0, 5000.0, 250.0);
    private final Option<Boolean> Heal = new Option<Boolean>("Heal", true);
    private final Option<Boolean> Regen = new Option<Boolean>("Regen", true);
    private final Option<Boolean> Jump = new Option<Boolean>("Jump", true);
    private final Option<Boolean> Speed = new Option<Boolean>("Speed", true);
    private final Option<Boolean> Fire = new Option<Boolean>("Fire", true);
    private final Option<Boolean> overlap = new Option<Boolean>("Overlap", false);
    private final Option<Boolean> OnlyGround = new Option<Boolean>("OnlyGround", false);
    private final Mode<String> spoof = new Mode("Spoof", new String[]{"None", "CombatSpoof", "OnlyNoTarget"}, "CombatSpoof");
    private final Timer timer = new Timer();

    public AutoPotion() {
        super("AutoPotion", ModuleType.Combat);
        this.addValues(this.spoof, this.health, this.delay, this.Heal, this.Jump, this.Speed, this.Fire, this.overlap, this.OnlyGround);
    }

    private int getBestSpoofSlot() {
        int spoofSlot = 5;
        for (int i = 36; i < 45; ++i) {
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                spoofSlot = i - 36;
                break;
            }
            if (!(Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemPotion)) continue;
            spoofSlot = i - 36;
            break;
        }
        return spoofSlot;
    }

    private int[] potions() {
        int[] pots = new int[]{-1, -1, -1, -1, -1, -1};
        if (((Boolean)this.Heal.get()).booleanValue()) {
            pots[0] = 6;
        }
        if (((Boolean)this.Regen.get()).booleanValue()) {
            pots[1] = 10;
        }
        if (((Boolean)this.Jump.get()).booleanValue()) {
            pots[2] = 8;
        }
        if (((Boolean)this.Speed.get()).booleanValue()) {
            pots[3] = 1;
        }
        if (((Boolean)this.Fire.get()).booleanValue()) {
            pots[4] = 12;
        }
        return pots;
    }

    private boolean shouldBuff(int potID) {
        if (potID == 6 || potID == 10) {
            return (double)Minecraft.thePlayer.getHealth() < (Double)this.health.get();
        }
        if (potID == 12) {
            return Minecraft.thePlayer.isBurning();
        }
        return potID == 1 || potID == 8;
    }

    @EventHandler
    public void onPre(EventPreUpdate event) {
        if (this.spoof.is("OnlyNoTarget") && this.getModule(KillAura.class).getTarget() != null) {
            return;
        }
        if (((Boolean)this.OnlyGround.get()).booleanValue()) {
            if (!Minecraft.thePlayer.onGround) {
                return;
            }
        }
        if (Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() || AutoPotion.mc.currentScreen instanceof GuiContainerCreative) {
            return;
        }
        for (int slot = 9; slot < 45; ++slot) {
            Item item;
            ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(slot).getStack();
            if (stack == null || !((item = stack.getItem()) instanceof ItemPotion)) continue;
            ItemPotion itemPotion = (ItemPotion)item;
            PotionEffect effect = itemPotion.getEffects(stack).get(0);
            for (int potID : this.potions()) {
                if (effect.getPotionID() != potID || !ItemPotion.isSplash(stack.getItemDamage()) || !this.onOverlap(potID, effect) || !this.shouldBuff(potID) || !this.isBestPotion(itemPotion, stack) || !this.timer.delay((Double)this.delay.get())) continue;
                if (this.spoof.is("CombatSpoof")) {
                    this.getModule(KillAura.class);
                    KillAura.target = null;
                }
                this.throwPot(event, slot);
                this.timer.reset();
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean onOverlap(int potID, PotionEffect effect) {
        if (((Boolean)this.overlap.get()).booleanValue()) {
            if (!Minecraft.thePlayer.isPotionActive(potID)) return true;
            if (!Minecraft.thePlayer.getActivePotionsMap().containsKey(potID)) return false;
            if (Minecraft.thePlayer.getActivePotionsMap().get(potID).getAmplifier() >= effect.getAmplifier()) return false;
            return true;
        }
        if (Minecraft.thePlayer.isPotionActive(potID)) return false;
        return true;
    }

    private void throwPot(EventPreUpdate event, int slot) {
        Minecraft.thePlayer.swap(slot, this.getBestSpoofSlot());
        this.sendPacket(new C09PacketHeldItemChange(this.getBestSpoofSlot()));
        float prevPitch = event.getPitch();
        this.sendLookPacket(event, 89.0f + ThreadLocalRandom.current().nextFloat());
        this.sendPacket(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.getHeldItem()));
        this.sendLookPacket(event, prevPitch);
        this.sendPacket(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
    }

    private void sendLookPacket(EventPreUpdate event, float pitch) {
        this.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(EventPreUpdate.x, event.getY(), EventPreUpdate.z, event.getYaw(), pitch, event.isOnGround()));
    }

    private boolean isBestPotion(ItemPotion potion, ItemStack stack) {
        if (potion.getEffects(stack) == null || potion.getEffects(stack).size() != 1) {
            return false;
        }
        PotionEffect effect = potion.getEffects(stack).get(0);
        int potionID = effect.getPotionID();
        int amplifier = effect.getAmplifier();
        int duration = effect.getDuration();
        for (int slot = 9; slot < 45; ++slot) {
            ItemPotion potionStack;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(slot).getHasStack()) continue;
            ItemStack slotStack = Minecraft.thePlayer.inventoryContainer.getSlot(slot).getStack();
            if (!(slotStack.getItem() instanceof ItemPotion) || (potionStack = (ItemPotion)slotStack.getItem()).getEffects(slotStack) == null) continue;
            for (PotionEffect potionEffect : potionStack.getEffects(slotStack)) {
                int id = potionEffect.getPotionID();
                int ampl = potionEffect.getAmplifier();
                int dur = potionEffect.getDuration();
                if (id != potionID || !ItemPotion.isSplash(slotStack.getItemDamage())) continue;
                if (ampl > amplifier) {
                    return false;
                }
                if (ampl != amplifier || dur <= duration) continue;
                return false;
            }
        }
        return true;
    }
}

