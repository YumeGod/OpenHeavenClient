/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.drakApi.MotionUpdateEvent;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.AutoArmor;
import heaven.main.module.modules.world.ChestStealer;
import heaven.main.module.modules.world.StealerUtils.Timer;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

public class InvCleaner
extends Module {
    private final Option<Boolean> inventoryCleaner = new Option<Boolean>("Cleaner", true);
    private final Numbers<Double> blocks = new Numbers<Double>("Blocks", 128.0, 0.0, 256.0, 64.0, this.inventoryCleaner::get);
    private final Numbers<Double> delay = new Numbers<Double>("Delay", 1.0, 0.0, 10.0, 1.0, this.inventoryCleaner::get);
    private final Option<Boolean> Sword = new Option<Boolean>("Sword", true);
    private final Option<Boolean> Bow = new Option<Boolean>("Bow", true);
    private final Option<Boolean> PickAxe = new Option<Boolean>("Pick Axe", true);
    private final Option<Boolean> Axe = new Option<Boolean>("Axe", true);
    private final Option<Boolean> Ores = new Option<Boolean>("Ores", true);
    private final Option<Boolean> Sticks = new Option<Boolean>("Sticks", true);
    private final Option<Boolean> Buckets = new Option<Boolean>("Buckets", true);
    private final Option<Boolean> Shovel = new Option<Boolean>("Shovel", true);
    private final Option<Boolean> GoldenApple = new Option<Boolean>("GoldenApple", true);
    private final Option<Boolean> Head = new Option<Boolean>("Head", true);
    private final Numbers<Double> arrows = new Numbers<Double>("Arrows", 128.0, 64.0, 512.0, 64.0, this.Bow::get);
    private final Numbers<Double> pickAxeSlot = new Numbers<Double>("Pickaxe Slot", 7.0, 1.0, 9.0, 1.0, this.PickAxe::get);
    private final Numbers<Double> axeSlot = new Numbers<Double>("Axe Slot", 8.0, 1.0, 9.0, 1.0, this.Axe::get);
    private final Numbers<Double> shovelSlot = new Numbers<Double>("Shovel Slot", 9.0, 1.0, 9.0, 1.0);
    private final Numbers<Double> swordSlot = new Numbers<Double>("Sword Slot", 1.0, 1.0, 9.0, 1.0, this.Sword::get);
    private final Numbers<Double> bowSlot = new Numbers<Double>("Bow Slot", 2.0, 1.0, 9.0, 1.0, this.Bow::get);
    private final Numbers<Double> headSlot = new Numbers<Double>("Head Slot", 3.0, 1.0, 9.0, 1.0, this.Head::get);
    private final Numbers<Double> gappleSlot = new Numbers<Double>("Golden Apple Slot", 3.0, 1.0, 9.0, 1.0, this.GoldenApple::get);
    private final Option<Boolean> auto_disable = new Option<Boolean>("AutoDisable", false);
    private final Option<Boolean> open_inv = new Option<Boolean>("Open Inv", true);
    public final Option<Boolean> onlyNoMove = new Option<Boolean>("OnlyNoMove", false);
    private final Timer timer = new Timer();
    private final List<Block> blacklistedBlocks2 = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.wooden_slab, Blocks.wooden_slab, Blocks.chest, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.skull, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.tnt, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.trapped_chest, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence, Blocks.activator_rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.redstone_torch, Blocks.acacia_stairs, Blocks.birch_stairs, Blocks.brick_stairs, Blocks.dark_oak_stairs, Blocks.jungle_stairs, Blocks.nether_brick_stairs, Blocks.oak_stairs, Blocks.quartz_stairs, Blocks.red_sandstone_stairs, Blocks.sandstone_stairs, Blocks.spruce_stairs, Blocks.stone_brick_stairs, Blocks.stone_stairs, Blocks.wooden_slab, Blocks.double_wooden_slab, Blocks.stone_slab, Blocks.double_stone_slab, Blocks.stone_slab2, Blocks.double_stone_slab2, Blocks.web, Blocks.gravel, Blocks.daylight_detector_inverted, Blocks.daylight_detector, Blocks.soul_sand, Blocks.piston, Blocks.piston_extension, Blocks.piston_head, Blocks.sticky_piston, Blocks.iron_trapdoor, Blocks.ender_chest, Blocks.end_portal, Blocks.end_portal_frame, Blocks.standing_banner, Blocks.wall_banner, Blocks.deadbush, Blocks.slime_block, Blocks.acacia_fence_gate, Blocks.birch_fence_gate, Blocks.dark_oak_fence_gate, Blocks.jungle_fence_gate, Blocks.spruce_fence_gate, Blocks.oak_fence_gate);

    public InvCleaner() {
        super("InventoryManager", new String[]{"InvCleaner"}, ModuleType.Player);
        this.addValues(this.blocks, this.delay, this.arrows, this.pickAxeSlot, this.axeSlot, this.shovelSlot, this.swordSlot, this.bowSlot, this.headSlot, this.gappleSlot, this.Sword, this.Bow, this.PickAxe, this.Axe, this.Shovel, this.GoldenApple, this.Head, this.inventoryCleaner, this.open_inv, this.onlyNoMove, this.auto_disable);
    }

    private boolean isBestSword(ItemStack stack) {
        float damage = this.getDamage(stack);
        for (int i = 9; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack is = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (!(this.getDamage(is) > damage) || !(is.getItem() instanceof ItemSword)) continue;
            return false;
        }
        return stack.getItem() instanceof ItemSword;
    }

    private boolean isHead(ItemStack stack) {
        return stack.getItem() instanceof ItemSkull && stack.getDisplayName().contains("Head") && !stack.getDisplayName().equalsIgnoreCase("Wither Skeleton Skull") && !stack.getDisplayName().equalsIgnoreCase("Zombie Head") && !stack.getDisplayName().equalsIgnoreCase("Creeper Head") && !stack.getDisplayName().equalsIgnoreCase("Skeleton Skull");
    }

    private boolean isGoldenApple(ItemStack stack) {
        return stack.getItem() instanceof ItemAppleGold;
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (((Boolean)this.auto_disable.getValue()).booleanValue()) {
            if (!Minecraft.thePlayer.isEntityAlive()) {
                ClientNotification.sendClientMessage("InventoryManager", "Auto disable because player not alive or death", 4000L, ClientNotification.Type.WARNING);
                this.setEnabled(false);
                return;
            }
            if (Minecraft.thePlayer.ticksExisted <= 1) {
                ClientNotification.sendClientMessage("InventoryManager", "Auto disable because World change", 4000L, ClientNotification.Type.WARNING);
                this.setEnabled(false);
            }
        }
    }

    @EventHandler
    public void onPreUpdate(MotionUpdateEvent event) {
        if (((Boolean)this.onlyNoMove.get()).booleanValue()) {
            if (Minecraft.thePlayer.moving()) {
                return;
            }
        }
        if (event.getState().equals((Object)MotionUpdateEvent.State.PRE)) {
            if (!(InvCleaner.mc.currentScreen instanceof GuiInventory) && (Boolean)this.open_inv.get() != false || Client.instance.getModuleManager().getModuleByClass(AutoArmor.class).isEnabled() && AutoArmor.isWorking() || ChestStealer.isStealing()) {
                return;
            }
            if (InvCleaner.mc.currentScreen == null || InvCleaner.mc.currentScreen instanceof GuiInventory || InvCleaner.mc.currentScreen instanceof GuiChat) {
                ItemStack stack;
                int slotIndex;
                int swordSlot = (int)((Double)this.swordSlot.get() - 1.0);
                int pickAxeSlot = (int)((Double)this.pickAxeSlot.get() - 1.0);
                int bowSlot = (int)((Double)this.bowSlot.get() - 1.0);
                int shovelSlot = (int)((Double)this.shovelSlot.get() - 1.0);
                int axeSlot = (int)((Double)this.axeSlot.get() - 1.0);
                int headSlot = (int)((Double)this.headSlot.get() - 1.0);
                int gappleSlot = (int)((Double)this.gappleSlot.get() - 1.0);
                boolean pickAxe = (Boolean)this.PickAxe.get();
                boolean shovel = (Boolean)this.Shovel.get();
                boolean axe = (Boolean)this.Axe.get();
                boolean sword = (Boolean)this.Sword.get();
                boolean bow = (Boolean)this.Bow.get();
                boolean head = (Boolean)this.Head.get();
                boolean gapple = (Boolean)this.GoldenApple.get();
                int tickDelay = ((Double)this.delay.get()).intValue() * 50;
                for (slotIndex = 9; slotIndex < 45; ++slotIndex) {
                    stack = Minecraft.thePlayer.getSlotFromPlayerContainer(slotIndex).getStack();
                    if (stack == null || !this.timer.check(tickDelay)) continue;
                    if (this.isBestSword(stack) && sword && this.shouldSwap(swordSlot)[0]) {
                        Minecraft.thePlayer.swap(slotIndex, swordSlot);
                        this.timer.reset();
                        continue;
                    }
                    if (this.isBestPickaxe(stack) && pickAxe && this.shouldSwap(pickAxeSlot)[2]) {
                        Minecraft.thePlayer.swap(slotIndex, pickAxeSlot);
                        this.timer.reset();
                        continue;
                    }
                    if (this.isBestAxe(stack) && axe && this.shouldSwap(axeSlot)[1]) {
                        Minecraft.thePlayer.swap(slotIndex, axeSlot);
                        this.timer.reset();
                        continue;
                    }
                    if (this.isBestBow(stack) && bow && this.shouldSwap(bowSlot)[5] && !stack.getDisplayName().toLowerCase().contains("kit selector")) {
                        Minecraft.thePlayer.swap(slotIndex, bowSlot);
                        this.timer.reset();
                        continue;
                    }
                    if (this.isHead(stack) && head && this.shouldSwap(headSlot)[4]) {
                        Minecraft.thePlayer.swap(slotIndex, headSlot);
                        this.timer.reset();
                        continue;
                    }
                    if (this.isBestShovel(stack) && shovel && this.shouldSwap(shovelSlot)[3]) {
                        Minecraft.thePlayer.swap(slotIndex, shovelSlot);
                        this.timer.reset();
                        continue;
                    }
                    if (!this.isGoldenApple(stack) || !gapple || !this.shouldSwap(gappleSlot)[6]) continue;
                    Minecraft.thePlayer.swap(slotIndex, gappleSlot);
                    this.timer.reset();
                }
                for (slotIndex = 9; slotIndex < 45; ++slotIndex) {
                    if (!Minecraft.thePlayer.getSlotFromPlayerContainer(slotIndex).getHasStack()) continue;
                    stack = Minecraft.thePlayer.getSlotFromPlayerContainer(slotIndex).getStack();
                    if (stack == null || !this.shouldDrop(stack) || !((Boolean)this.inventoryCleaner.get()).booleanValue() || !this.timer.delay(tickDelay)) continue;
                    Minecraft.thePlayer.drop(slotIndex);
                    this.timer.reset();
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    private boolean[] shouldSwap(int slot) {
        v0 = new boolean[7];
        if (!Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getHasStack()) ** GOTO lbl-1000
        if (!this.isBestSword(Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getStack())) lbl-1000:
        // 2 sources

        {
            v1 = true;
        } else {
            v1 = false;
        }
        v0[0] = v1;
        if (!Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getHasStack()) ** GOTO lbl-1000
        if (!this.isBestAxe(Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getStack())) lbl-1000:
        // 2 sources

        {
            v2 = true;
        } else {
            v2 = false;
        }
        v0[1] = v2;
        if (!Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getHasStack()) ** GOTO lbl-1000
        if (!this.isBestPickaxe(Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getStack())) lbl-1000:
        // 2 sources

        {
            v3 = true;
        } else {
            v3 = false;
        }
        v0[2] = v3;
        if (!Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getHasStack()) ** GOTO lbl-1000
        if (!this.isBestShovel(Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getStack())) lbl-1000:
        // 2 sources

        {
            v4 = true;
        } else {
            v4 = false;
        }
        v0[3] = v4;
        if (!Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getHasStack()) ** GOTO lbl-1000
        if (!this.isHead(Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getStack())) lbl-1000:
        // 2 sources

        {
            v5 = true;
        } else {
            v5 = false;
        }
        v0[4] = v5;
        if (!Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getHasStack()) ** GOTO lbl-1000
        if (!this.isBestBow(Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getStack())) lbl-1000:
        // 2 sources

        {
            v6 = true;
        } else {
            v6 = false;
        }
        v0[5] = v6;
        if (!Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getHasStack()) ** GOTO lbl-1000
        if (!this.isGoldenApple(Minecraft.thePlayer.getSlotFromPlayerContainer(slot + 36).getStack())) lbl-1000:
        // 2 sources

        {
            v7 = true;
        } else {
            v7 = false;
        }
        v0[6] = v7;
        return v0;
    }

    public boolean isWorking() {
        return !this.timer.check((float)((Double)this.delay.get() * 150.0));
    }

    private float getDamage(ItemStack stack) {
        float damage = 0.0f;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            damage += ((ItemTool)item).getDamage();
        } else if (item instanceof ItemSword) {
            damage += ((ItemSword)item).getAttackDamage();
        }
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }

    private int getBlocksCounter() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack stack = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            Item item = stack.getItem();
            if (!(stack.getItem() instanceof ItemBlock) || this.blacklistedBlocks2.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += stack.stackSize;
        }
        return blockCount;
    }

    private int getArrowsCounter() {
        int arrowCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack is = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (is.getItem() != Items.arrow) continue;
            arrowCount += is.stackSize;
        }
        return arrowCount;
    }

    private int getIronIngotsCounter() {
        int count = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack stack = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (stack.getItem() != Items.iron_ingot) continue;
            count += stack.stackSize;
        }
        return count;
    }

    private int getCoalCounter() {
        int count = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack stack = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (stack.getItem() != Items.coal) continue;
            count += stack.stackSize;
        }
        return count;
    }

    private int getSwordsCounter() {
        int count = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack stack = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (!(stack.getItem() instanceof ItemSword) || !this.isBestSword(stack)) continue;
            count += stack.stackSize;
        }
        return count;
    }

    private int getBowsCounter() {
        int count = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack stack = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (!(stack.getItem() instanceof ItemBow) || !this.isBestBow(stack)) continue;
            count += stack.stackSize;
        }
        return count;
    }

    private int getPickaxexCounter() {
        int count = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack stack = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (!(stack.getItem() instanceof ItemPickaxe) || !this.isBestPickaxe(stack)) continue;
            count += stack.stackSize;
        }
        return count;
    }

    private int getAxesCounter() {
        int count = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack stack = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (!(stack.getItem() instanceof ItemAxe) || !this.isBestAxe(stack)) continue;
            count += stack.stackSize;
        }
        return count;
    }

    private int getHeadsCounter() {
        int count = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack stack = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (!(stack.getItem() instanceof ItemSkull) || !this.isBestShovel(stack)) continue;
            count += stack.stackSize;
        }
        return count;
    }

    private int getShovelsCounter() {
        int count = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack stack = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (!(stack.getItem() instanceof ItemSpade) || !this.isBestShovel(stack)) continue;
            count += stack.stackSize;
        }
        return count;
    }

    private boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack slotStack = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (!(this.getToolEffect(slotStack) > value) || !(slotStack.getItem() instanceof ItemPickaxe)) continue;
            return false;
        }
        return true;
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

    private boolean shouldDrop(ItemStack stack) {
        int idFromItem;
        String displayName;
        Item item;
        block20: {
            block10: {
                boolean sword;
                block19: {
                    int swordSlot;
                    block18: {
                        boolean pickAxe;
                        block17: {
                            int pickAxeSlot;
                            block16: {
                                boolean axe;
                                block15: {
                                    int axeSlot;
                                    block14: {
                                        boolean head;
                                        block13: {
                                            int headSlot;
                                            block12: {
                                                boolean bow;
                                                block11: {
                                                    int bowSlot;
                                                    block9: {
                                                        boolean shovel;
                                                        block8: {
                                                            item = stack.getItem();
                                                            displayName = stack.getDisplayName();
                                                            idFromItem = Item.getIdFromItem(item);
                                                            if (idFromItem == 58 || displayName.toLowerCase().contains((Object)((Object)EnumChatFormatting.OBFUSCATED) + "||") || displayName.contains((Object)((Object)EnumChatFormatting.GREEN) + "Game Menu " + (Object)((Object)EnumChatFormatting.GRAY) + "(Right Click)") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.AQUA) + "" + (Object)((Object)EnumChatFormatting.BOLD) + "Spectator Settings " + (Object)((Object)EnumChatFormatting.GRAY) + "(Right Click)") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.AQUA) + "" + (Object)((Object)EnumChatFormatting.BOLD) + "Play Again " + (Object)((Object)EnumChatFormatting.GRAY) + "(Right Click)") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GREEN) + "" + (Object)((Object)EnumChatFormatting.BOLD) + "Teleporter " + (Object)((Object)EnumChatFormatting.GRAY) + "(Right Click)") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GREEN) + "SkyWars Challenges " + (Object)((Object)EnumChatFormatting.GRAY) + "(Right Click)") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GREEN) + "Collectibles " + (Object)((Object)EnumChatFormatting.GRAY) + "(Right Click)") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GREEN) + "Kit Selector " + (Object)((Object)EnumChatFormatting.GRAY) + "(Right Click)") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GREEN) + "Kill Effect Selector " + (Object)((Object)EnumChatFormatting.GRAY) + "(Right Click)") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.WHITE) + "Players: " + (Object)((Object)EnumChatFormatting.RED) + "Hidden " + (Object)((Object)EnumChatFormatting.GRAY) + "(Right Click)") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GREEN) + "Shop " + (Object)((Object)EnumChatFormatting.GRAY) + "(Right Click)") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.WHITE) + "Players: " + (Object)((Object)EnumChatFormatting.RED) + "Visible " + (Object)((Object)EnumChatFormatting.GRAY) + "(Right Click)") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GOLD) + "Excalibur") || displayName.equalsIgnoreCase("aDragon Sword") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GREEN) + "Cornucopia") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.RED) + "Bloodlust") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.RED) + "Artemis' Bow") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GREEN) + "Miner's Blessing") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GOLD) + "Axe of Perun") || displayName.equalsIgnoreCase((Object)((Object)EnumChatFormatting.GOLD) + "Cornucopia") || idFromItem == 116 || idFromItem == 145 || (idFromItem == 15 || idFromItem == 14) && ((Boolean)this.Ores.get()).booleanValue() || displayName.equalsIgnoreCase("\u00a7aAnd\u00faril") || idFromItem == 259 || idFromItem == 46) {
                                                                return false;
                                                            }
                                                            pickAxe = (Boolean)this.PickAxe.get();
                                                            shovel = (Boolean)this.Shovel.get();
                                                            axe = (Boolean)this.Axe.get();
                                                            sword = (Boolean)this.Sword.get();
                                                            bow = (Boolean)this.Bow.get();
                                                            head = (Boolean)this.Head.get();
                                                            swordSlot = (int)((Double)this.swordSlot.get() - 1.0);
                                                            pickAxeSlot = (int)((Double)this.pickAxeSlot.get() - 1.0);
                                                            bowSlot = (int)((Double)this.bowSlot.get() - 1.0);
                                                            int shovelSlot = (int)((Double)this.shovelSlot.get() - 1.0);
                                                            axeSlot = (int)((Double)this.axeSlot.get() - 1.0);
                                                            headSlot = (int)((Double)this.headSlot.get() - 1.0);
                                                            if (this.isBestShovel(stack) && this.getShovelsCounter() < 2) break block8;
                                                            if (!(stack.getItem() instanceof ItemSpade)) break block9;
                                                            if (stack != Minecraft.thePlayer.inventory.getStackInSlot(shovelSlot)) break block9;
                                                        }
                                                        if (shovel) break block10;
                                                    }
                                                    if (this.isBestBow(stack) && this.getBowsCounter() < 2) break block11;
                                                    if (!(stack.getItem() instanceof ItemBow)) break block12;
                                                    if (stack != Minecraft.thePlayer.inventory.getStackInSlot(bowSlot)) break block12;
                                                }
                                                if (bow) break block10;
                                            }
                                            if (this.isHead(stack) && this.getHeadsCounter() < 2) break block13;
                                            if (!(stack.getItem() instanceof ItemSkull)) break block14;
                                            if (stack != Minecraft.thePlayer.inventory.getStackInSlot(headSlot)) break block14;
                                        }
                                        if (head) break block10;
                                    }
                                    if (this.isBestAxe(stack) && this.getAxesCounter() < 2) break block15;
                                    if (!(stack.getItem() instanceof ItemAxe)) break block16;
                                    if (stack != Minecraft.thePlayer.inventory.getStackInSlot(axeSlot)) break block16;
                                }
                                if (axe) break block10;
                            }
                            if (this.isBestPickaxe(stack) && this.getPickaxexCounter() < 2) break block17;
                            if (!(stack.getItem() instanceof ItemPickaxe)) break block18;
                            if (stack != Minecraft.thePlayer.inventory.getStackInSlot(pickAxeSlot)) break block18;
                        }
                        if (pickAxe) break block10;
                    }
                    if (this.isBestSword(stack) && this.getSwordsCounter() < 2) break block19;
                    if (!(stack.getItem() instanceof ItemSword)) break block20;
                    if (stack != Minecraft.thePlayer.inventory.getStackInSlot(swordSlot)) break block20;
                }
                if (!sword) break block20;
            }
            return false;
        }
        if (item instanceof ItemArmor) {
            for (int type = 1; type < 5; ++type) {
                if (Minecraft.thePlayer.getSlotFromPlayerContainer(4 + type).getHasStack()) {
                    ItemStack slotStack = Minecraft.thePlayer.getSlotFromPlayerContainer(4 + type).getStack();
                    if (this.isBestArmor(slotStack, type)) continue;
                }
                if (!this.isBestArmor(stack, type)) continue;
                return false;
            }
        }
        if (item instanceof ItemBlock && ((double)this.getBlocksCounter() > (Double)this.blocks.get() || this.blacklistedBlocks2.contains(((ItemBlock)item).getBlock())) || item instanceof ItemPotion && this.isBadPotion(stack) || item instanceof ItemFood && !(item instanceof ItemAppleGold) && item != Items.bread && item != Items.pumpkin_pie && item != Items.baked_potato && item != Items.cooked_chicken && item != Items.carrot && item != Items.apple && item != Items.beef && item != Items.cooked_beef && item != Items.porkchop && item != Items.cooked_porkchop && item != Items.mushroom_stew && item != Items.cooked_fish && item != Items.melon || item instanceof ItemHoe || item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemArmor) {
            return true;
        }
        String unlocalizedName = item.getUnlocalizedName();
        return (Boolean)this.Sticks.get() == false && unlocalizedName.contains("stick") || unlocalizedName.contains("egg") || this.getIronIngotsCounter() > 64 && item == Items.iron_ingot || this.getCoalCounter() > 64 && item == Items.coal || unlocalizedName.contains("string") || unlocalizedName.contains("flint") || unlocalizedName.contains("compass") || unlocalizedName.contains("dyePowder") || unlocalizedName.contains("feather") || unlocalizedName.contains("chest") && !displayName.toLowerCase().contains("collect") || unlocalizedName.contains("snow") || unlocalizedName.contains("torch") || unlocalizedName.contains("seeds") || unlocalizedName.contains("leather") || unlocalizedName.contains("reeds") || unlocalizedName.contains("record") || unlocalizedName.contains("snowball") || item instanceof ItemGlassBottle || item instanceof ItemSlab || idFromItem == 113 || idFromItem == 106 || idFromItem == 325 || idFromItem == 326 && (Boolean)this.Buckets.get() == false || idFromItem == 327 || idFromItem == 111 || idFromItem == 85 || idFromItem == 188 || idFromItem == 189 || idFromItem == 190 || idFromItem == 191 || idFromItem == 401 || idFromItem == 192 || idFromItem == 81 || idFromItem == 32 || unlocalizedName.contains("gravel") || unlocalizedName.contains("flower") || unlocalizedName.contains("tallgrass") || item instanceof ItemBow || item == Items.arrow && (double)this.getArrowsCounter() > ((Boolean)this.Bow.get() != false ? (Double)this.arrows.get() : 0.0) || idFromItem == 175 || idFromItem == 340 || idFromItem == 339 || idFromItem == 160 || idFromItem == 101 || idFromItem == 102 || idFromItem == 321 || idFromItem == 323 || idFromItem == 389 || idFromItem == 416 || idFromItem == 171 || idFromItem == 139 || idFromItem == 23 || idFromItem == 25 || idFromItem == 69 || idFromItem == 70 || idFromItem == 72 || idFromItem == 77 || idFromItem == 96 || idFromItem == 107 || idFromItem == 123 || idFromItem == 131 || idFromItem == 143 || idFromItem == 147 || idFromItem == 148 || idFromItem == 151 || idFromItem == 152 || idFromItem == 154 || idFromItem == 158 || idFromItem == 167 || idFromItem == 403 || idFromItem == 183 || idFromItem == 184 || idFromItem == 185 || idFromItem == 186 || idFromItem == 187 || idFromItem == 331 || idFromItem == 356 || idFromItem == 404 || idFromItem == 27 || idFromItem == 28 || idFromItem == 66 || idFromItem == 76 || idFromItem == 157 || idFromItem == 328 || idFromItem == 342 || idFromItem == 343 || idFromItem == 398 || idFromItem == 407 || idFromItem == 408 || idFromItem == 138 || idFromItem == 352 || idFromItem == 385 || idFromItem == 386 || idFromItem == 395 || idFromItem == 402 || idFromItem == 418 || idFromItem == 419 || idFromItem == 281 || idFromItem == 289 || idFromItem == 337 || idFromItem == 336 || idFromItem == 348 || idFromItem == 353 || idFromItem == 369 || idFromItem == 372 || idFromItem == 405 || idFromItem == 406 || idFromItem == 409 || idFromItem == 410 || idFromItem == 415 || idFromItem == 370 || idFromItem == 376 || idFromItem == 377 || idFromItem == 378 || idFromItem == 379 || idFromItem == 380 || idFromItem == 382 || idFromItem == 414 || idFromItem == 346 || idFromItem == 347 || idFromItem == 420 || idFromItem == 397 || idFromItem == 421 || idFromItem == 341 || unlocalizedName.contains("sapling") || unlocalizedName.contains("stairs") || unlocalizedName.contains("door") || unlocalizedName.contains("monster_egg") || unlocalizedName.contains("sand") || unlocalizedName.contains("piston");
    }

    private boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack is = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (!(this.getToolEffect(is) > value) || !(is.getItem() instanceof ItemSpade)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack is = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (!(this.getToolEffect(is) > value) || !(is.getItem() instanceof ItemAxe) || this.isBestSword(stack)) continue;
            return false;
        }
        return true;
    }

    private float getToolEffect(ItemStack stack) {
        float value;
        Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool)item;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else {
            return 1.0f;
        }
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
        return value;
    }

    private float getBowEffect(ItemStack stack) {
        return 1 + EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack) + EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) + EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack);
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            return potion.getEffects(stack) == null || this.isBadPotionEffect(stack, potion);
        }
        return false;
    }

    public boolean isBadPotionEffect(ItemStack stack, ItemPotion pot) {
        for (PotionEffect effect : pot.getEffects(stack)) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            if (!potion.isBadEffect()) continue;
            return true;
        }
        return false;
    }

    private boolean isBestBow(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemBow)) {
            return false;
        }
        float value = this.getBowEffect(stack);
        for (int i = 9; i < 45; ++i) {
            if (!Minecraft.thePlayer.getSlotFromPlayerContainer(i).getHasStack()) continue;
            ItemStack slotStack = Minecraft.thePlayer.getSlotFromPlayerContainer(i).getStack();
            if (!(this.getBowEffect(slotStack) > value) || !(slotStack.getItem() instanceof ItemBow)) continue;
            return false;
        }
        return true;
    }

    @Override
    public void onDisable() {
        this.timer.reset();
    }
}

