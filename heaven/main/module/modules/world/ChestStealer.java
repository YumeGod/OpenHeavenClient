/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.More.PacketEvent;
import heaven.main.event.events.display.DisplayChestGuiEvent;
import heaven.main.event.events.drakApi.MotionUpdateEvent;
import heaven.main.event.events.key.KeyPressEvent;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.timer.Timer;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;

public class ChestStealer
extends Module {
    private final String[] list = new String[]{"mode", "delivery", "menu", "selector", "game", "gui", "server", "inventory", "play", "teleporter", "shop", "melee", "armor", "block", "castle", "mini", "warp", "teleport", "user", "team", "tool", "sure", "trade", "cancel", "accept", "soul", "book", "recipe", "profile", "tele", "port", "map", "kit", "select", "lobby", "vault", "lock", "anticheat", "travel", "settings", "user", "preference", "compass", "cake", "wars", "buy", "upgrade", "ranged", "potions", "utility"};
    private final List<Integer> containerSlots = new CopyOnWriteArrayList<Integer>();
    private final List<Integer> chestIds = new CopyOnWriteArrayList<Integer>();
    private final TimerUtil timer = new TimerUtil();
    private final Timer timerAura = new Timer();
    private static boolean isStealing;
    private boolean slotsFilled;
    private int containerSize;
    private int windowID;
    public final Numbers<Double> maxdelay = new Numbers<Double>("MaxDelay", 150.0, 0.0, 1000.0, 10.0);
    public final Numbers<Double> mindelay = new Numbers<Double>("MinDelay", 150.0, 0.0, 1000.0, 10.0);
    public final Option<Boolean> aura = new Option<Boolean>("Aura", false);
    public final Numbers<Double> aura_range = new Numbers<Double>("Aura Range", 4.0, 1.0, 5.0, 0.5);
    private final Option<Boolean> silent = new Option<Boolean>("Silent", false);
    private final Option<Boolean> silentText = new Option<Boolean>("SilentText", false);
    private final Option<Boolean> extra_packet = new Option<Boolean>("Extra Packet", true);
    private final Option<Boolean> auto_disable = new Option<Boolean>("AutoDisable", false);
    public final Option<Boolean> onlyNoMove = new Option<Boolean>("OnlyNoMove", false);
    public final Option<Boolean> ignore = new Option<Boolean>("Ignore", true);

    public ChestStealer() {
        super("ChestStealer", new String[]{"cs"}, ModuleType.World);
        this.addValues(this.maxdelay, this.mindelay, this.silent, this.silentText, this.ignore, this.extra_packet, this.aura, this.aura_range, this.onlyNoMove, this.auto_disable);
    }

    @Override
    public void onDisable() {
        this.slotsFilled = false;
        isStealing = false;
        this.containerSlots.clear();
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (((Boolean)this.onlyNoMove.get()).booleanValue()) {
            if (Minecraft.thePlayer.moving()) {
                return;
            }
        }
        if (event.getState().equals((Object)PacketEvent.State.INCOMING)) {
            Packet<INetHandlerPlayClient> packet;
            if (event.getPacket() instanceof S2DPacketOpenWindow) {
                packet = (S2DPacketOpenWindow)event.getPacket();
                for (String blacklisted : this.list) {
                    if (!((S2DPacketOpenWindow)packet).getWindowTitle().getUnformattedText().toLowerCase().contains(blacklisted)) continue;
                    isStealing = false;
                    return;
                }
                isStealing = ((S2DPacketOpenWindow)packet).getGuiId().equals("minecraft:chest");
                if (isStealing) {
                    this.containerSize = ((S2DPacketOpenWindow)packet).getSlotCount();
                    this.windowID = ((S2DPacketOpenWindow)packet).getWindowId();
                    this.containerSlots.clear();
                    this.slotsFilled = false;
                }
            }
            if (isStealing && event.getPacket() instanceof S30PacketWindowItems && ((S30PacketWindowItems)(packet = (S30PacketWindowItems)event.getPacket())).getWindowID() == this.windowID && !this.slotsFilled) {
                for (int i = 0; i < this.containerSize; ++i) {
                    ItemStack stack = ((S30PacketWindowItems)packet).getItemStacks()[i];
                    if (stack == null || ((Boolean)this.ignore.get()).booleanValue() && (!this.isNotBad(stack) || !this.checkArmor(stack, (S30PacketWindowItems)packet) || !this.checkTool(stack, (S30PacketWindowItems)packet) || !this.checkSword(stack, (S30PacketWindowItems)packet) || !this.checkBow(stack, (S30PacketWindowItems)packet))) continue;
                    this.containerSlots.add(i);
                }
                this.slotsFilled = true;
            }
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (((Boolean)this.auto_disable.getValue()).booleanValue()) {
            if (!Minecraft.thePlayer.isEntityAlive()) {
                ClientNotification.sendClientMessage("Stealer", "Auto disable because player not alive or death", 4000L, ClientNotification.Type.WARNING);
                this.setEnabled(false);
                return;
            }
            if (Minecraft.thePlayer.ticksExisted <= 1) {
                ClientNotification.sendClientMessage("Stealer", "Auto disable because World change ", 4000L, ClientNotification.Type.WARNING);
                this.setEnabled(false);
            }
        }
    }

    private List<TileEntityChest> tileEntityChestList() {
        return Minecraft.theWorld.getLoadedTileEntityList().stream().filter(te -> te instanceof TileEntityChest).map(te -> (TileEntityChest)te).filter(te -> Minecraft.thePlayer.getDistance(te.getPos()) <= (Double)this.aura_range.get()).sorted(Comparator.comparing(o -> Minecraft.thePlayer.getDistance(((TileEntityChest)o).getPos())).reversed()).collect(Collectors.toList());
    }

    @EventHandler
    public void onMotion(MotionUpdateEvent event) {
        if (((Boolean)this.onlyNoMove.get()).booleanValue()) {
            if (Minecraft.thePlayer.moving()) {
                return;
            }
        }
        if (((Boolean)this.aura.get()).booleanValue() && event.getState().equals((Object)MotionUpdateEvent.State.PRE)) {
            if (!isStealing) {
                for (TileEntityChest chest : this.tileEntityChestList()) {
                    int id = Integer.parseInt(StringUtils.digitString(chest.toString().replace("net.minecraft.tileentity.TileEntityChest@", "")));
                    if (this.chestIds.contains(id) || !this.timerAura.delay(300.0)) continue;
                    Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem(), chest.getPos(), Block.getFacingDirection(chest.getPos()), MathHelper.getVec3(chest.getPos()));
                    this.chestIds.add(id);
                    this.timerAura.reset();
                }
            } else {
                this.timerAura.reset();
            }
        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (((Boolean)this.onlyNoMove.get()).booleanValue()) {
            if (Minecraft.thePlayer.moving()) {
                return;
            }
        }
        if (isStealing) {
            if (!this.containerSlots.isEmpty()) {
                Collections.reverse(this.containerSlots);
                for (int count = 0; count < this.containerSlots.size(); ++count) {
                    if (!((Boolean)this.extra_packet.get()).booleanValue() && !this.timer.hasReached(MathUtil.randomNumber((Double)this.maxdelay.get(), (Double)this.mindelay.get()))) continue;
                    Minecraft.playerController.windowClick(this.windowID, this.containerSlots.get(count), 0, 1, Minecraft.thePlayer);
                    this.containerSlots.remove(this.containerSlots.get(count));
                    this.timer.reset();
                }
            } else {
                Minecraft.thePlayer.closeScreen(((Boolean)this.silent.get()).booleanValue() ? (ChestStealer.mc.currentScreen instanceof GuiInventory ? null : ChestStealer.mc.currentScreen) : null, this.windowID);
                isStealing = false;
            }
        }
    }

    @EventHandler
    public void onDisplayGuiChest(DisplayChestGuiEvent event) {
        if (((Boolean)this.onlyNoMove.get()).booleanValue()) {
            if (Minecraft.thePlayer.moving()) {
                return;
            }
        }
        if (((Boolean)this.silent.get()).booleanValue() && event.getString().equals("minecraft:chest")) {
            if (!((Boolean)this.silentText.get()).booleanValue()) {
                ScaledResolution sr = new ScaledResolution(mc);
                Client.instance.FontLoaders.Comfortaa18.drawCenteredStringWithShadow("Stealing chest...", sr.getScaledWidth_double() / 2.0, sr.getScaledHeight_double() / 2.0, new Color(200, 200, 200).getRGB());
            }
            mc.displayGuiScreen(ChestStealer.mc.previousScreen instanceof GuiInventory ? null : ChestStealer.mc.previousScreen);
        }
    }

    @EventHandler
    public void onKeyPress(KeyPressEvent event) {
        if (((Boolean)this.onlyNoMove.get()).booleanValue()) {
            if (Minecraft.thePlayer.moving()) {
                return;
            }
        }
        if (((Boolean)this.silent.get()).booleanValue() && isStealing && (event.getKey() == -100 || event.getKey() == -99)) {
            event.setCancelled(true);
        }
    }

    private boolean checkArmor(ItemStack stack, S30PacketWindowItems packet) {
        if (stack.getItem().getUnlocalizedName().contains("helmet")) {
            return stack.equals(this.bestArmor(packet.getItemStacks(), Armor.HELMET)) && (this.bestArmor(Armor.HELMET) == null || this.getProtection(this.bestArmor(packet.getItemStacks(), Armor.HELMET)) > this.getProtection(this.bestArmor(Armor.HELMET)));
        }
        if (stack.getItem().getUnlocalizedName().contains("chestplate")) {
            return stack.equals(this.bestArmor(packet.getItemStacks(), Armor.CHESTPLATE)) && (this.bestArmor(Armor.CHESTPLATE) == null || this.getProtection(this.bestArmor(packet.getItemStacks(), Armor.CHESTPLATE)) > this.getProtection(this.bestArmor(Armor.CHESTPLATE)));
        }
        if (stack.getItem().getUnlocalizedName().contains("boots")) {
            return stack.equals(this.bestArmor(packet.getItemStacks(), Armor.BOOTS)) && (this.bestArmor(Armor.BOOTS) == null || this.getProtection(this.bestArmor(packet.getItemStacks(), Armor.BOOTS)) > this.getProtection(this.bestArmor(Armor.BOOTS)));
        }
        if (stack.getItem().getUnlocalizedName().contains("leggings")) {
            return stack.equals(this.bestArmor(packet.getItemStacks(), Armor.LEGGINS)) && (this.bestArmor(Armor.LEGGINS) == null || this.getProtection(this.bestArmor(packet.getItemStacks(), Armor.LEGGINS)) > this.getProtection(this.bestArmor(Armor.LEGGINS)));
        }
        return true;
    }

    private boolean checkTool(ItemStack stack, S30PacketWindowItems packet) {
        if (stack.getItem() instanceof ItemPickaxe) {
            return stack.equals(this.bestTool(packet.getItemStacks(), Tool.PICKAXE)) && (this.bestTool(this.getInventory(), Tool.PICKAXE) == null || this.getEfficiency(this.bestTool(packet.getItemStacks(), Tool.PICKAXE)) > this.getEfficiency(this.bestTool(this.getInventory(), Tool.PICKAXE)));
        }
        if (stack.getItem() instanceof ItemAxe) {
            return stack.equals(this.bestTool(packet.getItemStacks(), Tool.AXE)) && (this.bestTool(this.getInventory(), Tool.AXE) == null || this.getEfficiency(this.bestTool(packet.getItemStacks(), Tool.AXE)) > this.getEfficiency(this.bestTool(this.getInventory(), Tool.AXE)));
        }
        if (stack.getItem() instanceof ItemSpade) {
            return stack.equals(this.bestTool(packet.getItemStacks(), Tool.SHOVEL)) && (this.bestTool(this.getInventory(), Tool.SHOVEL) == null || this.getEfficiency(this.bestTool(packet.getItemStacks(), Tool.SHOVEL)) > this.getEfficiency(this.bestTool(this.getInventory(), Tool.SHOVEL)));
        }
        return true;
    }

    private boolean checkSword(ItemStack stack, S30PacketWindowItems packet) {
        if (stack.getItem() instanceof ItemSword) {
            return stack.equals(this.bestSword(packet.getItemStacks())) && (this.bestSword(this.getInventory()) == null || this.getDamage(this.bestSword(packet.getItemStacks())) > this.getDamage(this.bestSword(this.getInventory())));
        }
        return true;
    }

    private boolean checkBow(ItemStack stack, S30PacketWindowItems packet) {
        if (stack.getItem() instanceof ItemBow) {
            return stack.equals(this.bestBow(packet.getItemStacks())) && (this.bestBow(this.getInventory()) == null || this.getPower(this.bestBow(packet.getItemStacks())) > this.getPower(this.bestBow(this.getInventory())));
        }
        return true;
    }

    private boolean isNotBad(ItemStack item) {
        ItemStack stack = null;
        float lastDamage = -1.0f;
        for (int i = 9; i < 45; ++i) {
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is1 = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (!(is1.getItem() instanceof ItemSword) || !(item.getItem() instanceof ItemSword) || !(lastDamage < this.getDamage(is1))) continue;
            lastDamage = this.getDamage(is1);
            stack = is1;
        }
        if (stack != null && stack.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
            float currentDamage = this.getDamage(stack);
            float itemDamage = this.getDamage(item);
            if (itemDamage > currentDamage) {
                return true;
            }
        }
        return item == null || !item.getItem().getUnlocalizedName().contains("stick") && (!item.getItem().getUnlocalizedName().contains("egg") || item.getItem().getUnlocalizedName().contains("leg")) && !item.getItem().getUnlocalizedName().contains("string") && !item.getItem().getUnlocalizedName().contains("compass") && !item.getItem().getUnlocalizedName().contains("feather") && !item.getItem().getUnlocalizedName().contains("bucket") && !item.getItem().getUnlocalizedName().contains("snow") && !item.getItem().getUnlocalizedName().contains("fish") && !item.getItem().getUnlocalizedName().contains("enchant") && !item.getItem().getUnlocalizedName().contains("exp") && !item.getItem().getUnlocalizedName().contains("shears") && !item.getItem().getUnlocalizedName().contains("anvil") && !item.getItem().getUnlocalizedName().contains("torch") && !item.getItem().getUnlocalizedName().contains("seeds") && !item.getItem().getUnlocalizedName().contains("leather") && !(item.getItem() instanceof ItemGlassBottle) && !item.getItem().getUnlocalizedName().contains("piston") && (!item.getItem().getUnlocalizedName().contains("potion") || !this.isBadPotion(item));
    }

    public boolean isBadPotionEffect(ItemStack stack, ItemPotion pot) {
        for (PotionEffect effect : pot.getEffects(stack)) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            if (!potion.isBadEffect()) continue;
            return true;
        }
        return false;
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                return this.isBadPotionEffect(stack, potion);
            }
        }
        return false;
    }

    private ItemStack[] getInventory() {
        return Minecraft.thePlayer.inventory.mainInventory;
    }

    private ItemStack[] getArmorInventory() {
        return Minecraft.thePlayer.inventory.armorInventory;
    }

    public ItemStack bestArmor(Armor armor) {
        if (this.hasArmor(this.getInventory(), armor) && this.hasArmor(this.getArmorInventory(), armor)) {
            return (ItemStack)Stream.of((ItemStack)Arrays.stream(this.getInventory()).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(this.armorType(armor))).sorted((s1, s2) -> Float.compare(this.getProtection((ItemStack)s2), this.getProtection((ItemStack)s1))).collect(Collectors.toList()).stream().findFirst().get(), (ItemStack)Arrays.stream(this.getArmorInventory()).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(this.armorType(armor))).sorted((s1, s2) -> Float.compare(this.getProtection((ItemStack)s2), this.getProtection((ItemStack)s1))).collect(Collectors.toList()).stream().findFirst().get()).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(this.armorType(armor))).sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage())).sorted((s1, s2) -> Float.compare(this.getProtection((ItemStack)s2), this.getProtection((ItemStack)s1))).collect(Collectors.toList()).stream().findFirst().get();
        }
        if (this.hasArmor(this.getInventory(), armor)) {
            return (ItemStack)Arrays.stream(this.getInventory()).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(this.armorType(armor))).sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage())).sorted((s1, s2) -> Float.compare(this.getProtection((ItemStack)s2), this.getProtection((ItemStack)s1))).collect(Collectors.toList()).stream().findFirst().get();
        }
        if (this.hasArmor(this.getArmorInventory(), armor)) {
            return (ItemStack)Arrays.stream(this.getArmorInventory()).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(this.armorType(armor))).sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage())).sorted((s1, s2) -> Float.compare(this.getProtection((ItemStack)s2), this.getProtection((ItemStack)s1))).collect(Collectors.toList()).stream().findFirst().get();
        }
        return null;
    }

    public ItemStack bestArmor(ItemStack[] container, Armor armor) {
        if (this.hasArmor(container, armor)) {
            return (ItemStack)Arrays.stream(container).filter(s -> s != null && s.getItem().getUnlocalizedName().contains(this.armorType(armor))).sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage())).sorted((s1, s2) -> Float.compare(this.getProtection((ItemStack)s2), this.getProtection((ItemStack)s1))).sorted((s1, s2) -> Integer.compare(s2.getItemDamage(), s1.getItemDamage())).collect(Collectors.toList()).stream().findFirst().get();
        }
        return null;
    }

    public ItemStack bestTool(ItemStack[] container, Tool tool) {
        if (this.hasTool(container, tool)) {
            if (tool.equals((Object)Tool.PICKAXE)) {
                return Stream.of(container).filter(s -> s != null && s.getItem() instanceof ItemPickaxe).sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage())).min((s1, s2) -> Float.compare(this.getEfficiency((ItemStack)s2), this.getEfficiency((ItemStack)s1))).get();
            }
            if (tool.equals((Object)Tool.SHOVEL)) {
                return Stream.of(container).filter(s -> s != null && s.getItem() instanceof ItemSpade).sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage())).min((s1, s2) -> Float.compare(this.getEfficiency((ItemStack)s2), this.getEfficiency((ItemStack)s1))).get();
            }
            if (tool.equals((Object)Tool.AXE)) {
                return Stream.of(container).filter(s -> s != null && s.getItem() instanceof ItemAxe).sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage())).min((s1, s2) -> Float.compare(this.getEfficiency((ItemStack)s2), this.getEfficiency((ItemStack)s1))).get();
            }
        }
        return null;
    }

    public ItemStack bestSword(ItemStack[] container) {
        if (this.hasSword(container)) {
            return (ItemStack)Stream.of(container).filter(s -> s != null && s.getItem() instanceof ItemSword).sorted((s1, s2) -> Integer.compare(s2.getMaxDamage() - s2.getItemDamage(), s1.getMaxDamage() - s1.getItemDamage())).sorted((s1, s2) -> Float.compare(this.getDamage((ItemStack)s2), this.getDamage((ItemStack)s1))).collect(Collectors.toList()).stream().findFirst().get();
        }
        return null;
    }

    public ItemStack bestBow(ItemStack[] container) {
        if (this.hasBow(container)) {
            return (ItemStack)Stream.of(container).filter(s -> s != null && s.getItem() instanceof ItemBow).sorted((s1, s2) -> Float.compare(this.getPower((ItemStack)s2), this.getPower((ItemStack)s1))).collect(Collectors.toList()).stream().findFirst().get();
        }
        return null;
    }

    private int containerContainsArmor(ItemStack[] container, Armor armor) {
        for (int i = 0; i < container.length; ++i) {
            if (container[i] == null || !container[i].getItem().getUnlocalizedName().contains(this.armorType(armor))) continue;
            return i;
        }
        return -1;
    }

    private int containerContainsTool(ItemStack[] container, Tool tool) {
        for (int i = 0; i < container.length; ++i) {
            if (container[i] == null || !container[i].getItem().getUnlocalizedName().contains(this.toolType(tool))) continue;
            return i;
        }
        return -1;
    }

    private int containerContainsSword(ItemStack[] container) {
        for (int i = 0; i < container.length; ++i) {
            if (container[i] == null || !(container[i].getItem() instanceof ItemSword)) continue;
            return i;
        }
        return -1;
    }

    private int containerContainsBow(ItemStack[] container) {
        for (int i = 0; i < container.length; ++i) {
            if (container[i] == null || !(container[i].getItem() instanceof ItemBow)) continue;
            return i;
        }
        return -1;
    }

    public boolean hasArmor(ItemStack[] container, Armor armor) {
        int i = this.containerContainsArmor(container, armor);
        return i >= 0;
    }

    public boolean hasTool(ItemStack[] container, Tool tool) {
        int i = this.containerContainsTool(container, tool);
        return i >= 0;
    }

    public boolean hasSword(ItemStack[] container) {
        int i = this.containerContainsSword(container);
        return i >= 0;
    }

    public boolean hasBow(ItemStack[] container) {
        int i = this.containerContainsBow(container);
        return i >= 0;
    }

    public String armorType(Armor armor) {
        if (armor.equals((Object)Armor.LEGGINS)) {
            return "leggings";
        }
        if (armor.equals((Object)Armor.CHESTPLATE)) {
            return "chestplate";
        }
        if (armor.equals((Object)Armor.BOOTS)) {
            return "boots";
        }
        if (armor.equals((Object)Armor.HELMET)) {
            return "helmet";
        }
        return "";
    }

    public String toolType(Tool tool) {
        if (tool.equals((Object)Tool.AXE)) {
            return "hatchet";
        }
        if (tool.equals((Object)Tool.PICKAXE)) {
            return "pickaxe";
        }
        if (tool.equals((Object)Tool.SHOVEL)) {
            return "shovel";
        }
        return "";
    }

    private float getPower(ItemStack stack) {
        return 1 + EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack) + EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) + EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack);
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

    private float getEfficiency(ItemStack stack) {
        float value;
        Item item = stack.getItem();
        ItemTool tool = (ItemTool)item;
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        String name = item.getUnlocalizedName();
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 6.0f;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 6.0f;
            }
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 6.0f;
            }
        } else {
            return 1.0f;
        }
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
        return value;
    }

    public List<Integer> getChestIds() {
        return this.chestIds;
    }

    public static boolean isStealing() {
        return isStealing;
    }

    public static enum Tool {
        PICKAXE,
        SHOVEL,
        AXE;

    }

    public static enum Armor {
        CHESTPLATE,
        LEGGINS,
        HELMET,
        BOOTS;

    }
}

