/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class AutoEat
extends Module {
    private int oldSlot;
    private int bestSlot;
    private final Numbers<Double> FoodLevel = new Numbers<Double>("FoodLevel", 10.0, 1.0, 20.0, 1.0);
    private final Option<Boolean> Gapple = new Option<Boolean>("Gapple", false);
    private final Option<Boolean> RottenFlesh = new Option<Boolean>("RottenFlesh", false);

    public AutoEat() {
        super("AutoEat", ModuleType.Player);
        this.addValues(this.FoodLevel, this.Gapple, this.RottenFlesh);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        block14: {
            block16: {
                block15: {
                    block11: {
                        block13: {
                            block12: {
                                if (this.oldSlot == -1) {
                                    return;
                                }
                                if (this.oldSlot != -1) break block11;
                                if (Minecraft.thePlayer.capabilities.isCreativeMode) break block12;
                                if (!((double)Minecraft.thePlayer.getFoodStats().getFoodLevel() >= (Double)this.FoodLevel.getValue())) break block13;
                            }
                            return;
                        }
                        float item = 0.0f;
                        this.bestSlot = -1;
                        for (int i = 0; i < 9; ++i) {
                            ItemStack item1 = Minecraft.thePlayer.inventory.getStackInSlot(i);
                            if (item1 == null) continue;
                            float saturation = 0.0f;
                            if (item1.getItem() instanceof ItemFood && (((Boolean)this.Gapple.getValue()).booleanValue() || item1.getItem() != Items.golden_apple) && (((Boolean)this.RottenFlesh.getValue()).booleanValue() || item1.getItem() != Items.rotten_flesh)) {
                                saturation = ((ItemFood)item1.getItem()).getSaturationModifier(item1);
                            }
                            if (!(saturation > item)) continue;
                            item = saturation;
                            this.bestSlot = i;
                        }
                        if (this.bestSlot == -1) {
                            return;
                        }
                        this.oldSlot = Minecraft.thePlayer.inventory.currentItem;
                        break block14;
                    }
                    if (Minecraft.thePlayer.capabilities.isCreativeMode) break block15;
                    if (Minecraft.thePlayer.getFoodStats().getFoodLevel() < 20) break block16;
                }
                this.stop();
                return;
            }
            ItemStack var6 = Minecraft.thePlayer.inventory.getStackInSlot(this.bestSlot);
            if (var6 == null || !(var6.getItem() instanceof ItemFood)) {
                this.stop();
                return;
            }
            Minecraft.thePlayer.inventory.currentItem = this.bestSlot;
            AutoEat.mc.gameSettings.keyBindUseItem.pressed = true;
        }
    }

    private void stop() {
        AutoEat.mc.gameSettings.keyBindUseItem.pressed = false;
        Minecraft.thePlayer.inventory.currentItem = this.oldSlot;
        this.oldSlot = -1;
    }
}

