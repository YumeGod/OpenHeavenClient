/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.CreativeCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiContainerCreative
extends InventoryEffectRenderer {
    private static final ResourceLocation creativeInventoryTabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    static InventoryBasic field_147060_v = new InventoryBasic("tmp", true, 45);
    private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasClicking;
    private GuiTextField searchField;
    private List<Slot> field_147063_B;
    private Slot field_147064_C;
    private boolean field_147057_D;
    private CreativeCrafting field_147059_E;

    public GuiContainerCreative(EntityPlayer p_i1088_1_) {
        super(new ContainerCreative(p_i1088_1_));
        p_i1088_1_.openContainer = this.inventorySlots;
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
    }

    @Override
    public void updateScreen() {
        if (!Minecraft.playerController.isInCreativeMode()) {
            this.mc.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
        }
        this.updateActivePotionEffects();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType) {
        this.field_147057_D = true;
        boolean flag = clickType == 1;
        int n = clickType = slotId == -999 && clickType == 0 ? 4 : clickType;
        if (slotIn == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && clickType != 5) {
            InventoryPlayer inventoryplayer1 = Minecraft.thePlayer.inventory;
            if (inventoryplayer1.getItemStack() == null) return;
            if (clickedButton == 0) {
                Minecraft.thePlayer.dropPlayerItemWithRandomChoice(inventoryplayer1.getItemStack(), true);
                Minecraft.playerController.sendPacketDropItem(inventoryplayer1.getItemStack());
                inventoryplayer1.setItemStack(null);
            }
            if (clickedButton != 1) return;
            ItemStack itemstack5 = inventoryplayer1.getItemStack().splitStack(1);
            Minecraft.thePlayer.dropPlayerItemWithRandomChoice(itemstack5, true);
            Minecraft.playerController.sendPacketDropItem(itemstack5);
            if (inventoryplayer1.getItemStack().stackSize != 0) return;
            inventoryplayer1.setItemStack(null);
            return;
        }
        if (slotIn == this.field_147064_C && flag) {
            int j = 0;
            while (j < Minecraft.thePlayer.inventoryContainer.getInventory().size()) {
                Minecraft.playerController.sendSlotPacket(null, j);
                ++j;
            }
            return;
        }
        if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) {
            if (slotIn == this.field_147064_C) {
                Minecraft.thePlayer.inventory.setItemStack(null);
                return;
            }
            if (clickType == 4 && slotIn != null && slotIn.getHasStack()) {
                ItemStack itemstack = slotIn.decrStackSize(clickedButton == 0 ? 1 : slotIn.getStack().getMaxStackSize());
                Minecraft.thePlayer.dropPlayerItemWithRandomChoice(itemstack, true);
                Minecraft.playerController.sendPacketDropItem(itemstack);
                return;
            }
            if (clickType == 4) {
                if (Minecraft.thePlayer.inventory.getItemStack() != null) {
                    Minecraft.thePlayer.dropPlayerItemWithRandomChoice(Minecraft.thePlayer.inventory.getItemStack(), true);
                    Minecraft.playerController.sendPacketDropItem(Minecraft.thePlayer.inventory.getItemStack());
                    Minecraft.thePlayer.inventory.setItemStack(null);
                    return;
                }
            }
            Minecraft.thePlayer.inventoryContainer.slotClick(slotIn == null ? slotId : ((CreativeSlot)slotIn).slot.slotNumber, clickedButton, clickType, Minecraft.thePlayer);
            Minecraft.thePlayer.inventoryContainer.detectAndSendChanges();
            return;
        }
        if (clickType != 5 && slotIn.inventory == field_147060_v) {
            InventoryPlayer inventoryplayer = Minecraft.thePlayer.inventory;
            ItemStack itemstack1 = inventoryplayer.getItemStack();
            ItemStack itemstack2 = slotIn.getStack();
            if (clickType == 2) {
                if (itemstack2 == null) return;
                if (clickedButton < 0) return;
                if (clickedButton >= 9) return;
                ItemStack itemstack7 = itemstack2.copy();
                itemstack7.stackSize = itemstack7.getMaxStackSize();
                Minecraft.thePlayer.inventory.setInventorySlotContents(clickedButton, itemstack7);
                Minecraft.thePlayer.inventoryContainer.detectAndSendChanges();
                return;
            }
            if (clickType == 3) {
                if (inventoryplayer.getItemStack() != null) return;
                if (!slotIn.getHasStack()) return;
                ItemStack itemstack6 = slotIn.getStack().copy();
                itemstack6.stackSize = itemstack6.getMaxStackSize();
                inventoryplayer.setItemStack(itemstack6);
                return;
            }
            if (clickType == 4) {
                if (itemstack2 == null) return;
                ItemStack itemstack3 = itemstack2.copy();
                itemstack3.stackSize = clickedButton == 0 ? 1 : itemstack3.getMaxStackSize();
                Minecraft.thePlayer.dropPlayerItemWithRandomChoice(itemstack3, true);
                Minecraft.playerController.sendPacketDropItem(itemstack3);
                return;
            }
            if (itemstack1 != null && itemstack2 != null && itemstack1.isItemEqual(itemstack2)) {
                if (clickedButton == 0) {
                    if (flag) {
                        itemstack1.stackSize = itemstack1.getMaxStackSize();
                        return;
                    }
                    if (itemstack1.stackSize >= itemstack1.getMaxStackSize()) return;
                    ++itemstack1.stackSize;
                    return;
                }
                if (itemstack1.stackSize <= 1) {
                    inventoryplayer.setItemStack(null);
                    return;
                }
                --itemstack1.stackSize;
                return;
            }
            if (itemstack2 != null && itemstack1 == null) {
                inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack2));
                itemstack1 = inventoryplayer.getItemStack();
                if (!flag) return;
                itemstack1.stackSize = itemstack1.getMaxStackSize();
                return;
            }
            inventoryplayer.setItemStack(null);
            return;
        }
        this.inventorySlots.slotClick(slotIn == null ? slotId : slotIn.slotNumber, clickedButton, clickType, Minecraft.thePlayer);
        if (Container.getDragEvent(clickedButton) != 2) {
            if (slotIn == null) return;
            ItemStack itemstack4 = this.inventorySlots.getSlot(slotIn.slotNumber).getStack();
            Minecraft.playerController.sendSlotPacket(itemstack4, slotIn.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
            return;
        }
        int i = 0;
        while (i < 9) {
            Minecraft.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + i).getStack(), 36 + i);
            ++i;
        }
    }

    @Override
    protected void updateActivePotionEffects() {
        int i = this.guiLeft;
        super.updateActivePotionEffects();
        if (this.searchField != null && this.guiLeft != i) {
            this.searchField.xPosition = this.guiLeft + 82;
        }
    }

    @Override
    public void initGui() {
        if (Minecraft.playerController.isInCreativeMode()) {
            super.initGui();
            this.buttonList.clear();
            Keyboard.enableRepeatEvents((boolean)true);
            this.searchField = new GuiTextField(0, this.fontRendererObj, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRendererObj.FONT_HEIGHT);
            this.searchField.setMaxStringLength(15);
            this.searchField.setEnableBackgroundDrawing(false);
            this.searchField.setVisible(false);
            this.searchField.setTextColor(0xFFFFFF);
            int i = selectedTabIndex;
            selectedTabIndex = -1;
            this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[i]);
            this.field_147059_E = new CreativeCrafting(this.mc);
            Minecraft.thePlayer.inventoryContainer.onCraftGuiOpened(this.field_147059_E);
        } else {
            this.mc.displayGuiScreen(new GuiInventory(Minecraft.thePlayer));
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (Minecraft.thePlayer != null) {
            if (Minecraft.thePlayer.inventory != null) {
                Minecraft.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_147059_E);
            }
        }
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex()) {
            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
                this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
            } else {
                super.keyTyped(typedChar, keyCode);
            }
        } else {
            if (this.field_147057_D) {
                this.field_147057_D = false;
                this.searchField.setText("");
            }
            if (!this.checkHotbarKeys(keyCode)) {
                if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
                    this.updateCreativeSearch();
                } else {
                    super.keyTyped(typedChar, keyCode);
                }
            }
        }
    }

    private void updateCreativeSearch() {
        ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        guicontainercreative$containercreative.itemList.clear();
        for (Item item : Item.itemRegistry) {
            if (item == null || item.getCreativeTab() == null) continue;
            item.getSubItems(item, null, guicontainercreative$containercreative.itemList);
        }
        for (Enchantment enchantment : Enchantment.enchantmentsBookList) {
            if (enchantment == null || enchantment.type == null) continue;
            Items.enchanted_book.getAll(enchantment, guicontainercreative$containercreative.itemList);
        }
        Iterator<ItemStack> iterator = guicontainercreative$containercreative.itemList.iterator();
        String s1 = this.searchField.getText().toLowerCase();
        while (iterator.hasNext()) {
            ItemStack itemstack = iterator.next();
            boolean flag = false;
            for (String s : itemstack.getTooltip(Minecraft.thePlayer, this.mc.gameSettings.advancedItemTooltips)) {
                if (!EnumChatFormatting.getTextWithoutFormattingCodes(s).toLowerCase().contains(s1)) continue;
                flag = true;
                break;
            }
            if (flag) continue;
            iterator.remove();
        }
        this.currentScroll = 0.0f;
        guicontainercreative$containercreative.scrollTo(0.0f);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
        if (creativetabs.drawInForegroundOfTab()) {
            GlStateManager.disableBlend();
            this.fontRendererObj.drawString(I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]), 8, 6, 0x404040);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            int i = mouseX - this.guiLeft;
            int j = mouseY - this.guiTop;
            for (CreativeTabs creativetabs : CreativeTabs.creativeTabArray) {
                if (!this.func_147049_a(creativetabs, i, j)) continue;
                return;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            int i = mouseX - this.guiLeft;
            int j = mouseY - this.guiTop;
            for (CreativeTabs creativetabs : CreativeTabs.creativeTabArray) {
                if (!this.func_147049_a(creativetabs, i, j)) continue;
                this.setCurrentCreativeTab(creativetabs);
                return;
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    private boolean needsScrollBars() {
        return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory() && ((ContainerCreative)this.inventorySlots).func_148328_e();
    }

    private void setCurrentCreativeTab(CreativeTabs p_147050_1_) {
        int i = selectedTabIndex;
        selectedTabIndex = p_147050_1_.getTabIndex();
        ContainerCreative guicontainercreative$containercreative = (ContainerCreative)this.inventorySlots;
        this.dragSplittingSlots.clear();
        guicontainercreative$containercreative.itemList.clear();
        p_147050_1_.displayAllReleventItems(guicontainercreative$containercreative.itemList);
        if (p_147050_1_ == CreativeTabs.tabInventory) {
            Container container = Minecraft.thePlayer.inventoryContainer;
            if (this.field_147063_B == null) {
                this.field_147063_B = guicontainercreative$containercreative.inventorySlots;
            }
            guicontainercreative$containercreative.inventorySlots = Lists.newArrayList();
            for (int j = 0; j < container.inventorySlots.size(); ++j) {
                CreativeSlot slot = new CreativeSlot(container.inventorySlots.get(j), j);
                guicontainercreative$containercreative.inventorySlots.add(slot);
                if (j >= 5 && j < 9) {
                    int j1 = j - 5;
                    int k1 = j1 / 2;
                    int l1 = j1 % 2;
                    slot.xDisplayPosition = 9 + k1 * 54;
                    slot.yDisplayPosition = 6 + l1 * 27;
                    continue;
                }
                if (j >= 0 && j < 5) {
                    slot.yDisplayPosition = -2000;
                    slot.xDisplayPosition = -2000;
                    continue;
                }
                if (j >= container.inventorySlots.size()) continue;
                int k = j - 9;
                int l = k % 9;
                int i1 = k / 9;
                slot.xDisplayPosition = 9 + l * 18;
                slot.yDisplayPosition = j >= 36 ? 112 : 54 + i1 * 18;
            }
            this.field_147064_C = new Slot(field_147060_v, 0, 173, 112);
            guicontainercreative$containercreative.inventorySlots.add(this.field_147064_C);
        } else if (i == CreativeTabs.tabInventory.getTabIndex()) {
            guicontainercreative$containercreative.inventorySlots = this.field_147063_B;
            this.field_147063_B = null;
        }
        if (this.searchField != null) {
            if (p_147050_1_ == CreativeTabs.tabAllSearch) {
                this.searchField.setVisible(true);
                this.searchField.setCanLoseFocus(false);
                this.searchField.setFocused(true);
                this.searchField.setText("");
                this.updateCreativeSearch();
            } else {
                this.searchField.setVisible(false);
                this.searchField.setCanLoseFocus(true);
                this.searchField.setFocused(false);
            }
        }
        this.currentScroll = 0.0f;
        guicontainercreative$containercreative.scrollTo(0.0f);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0 && this.needsScrollBars()) {
            int j = ((ContainerCreative)this.inventorySlots).itemList.size() / 9 - 5;
            if (i > 0) {
                i = 1;
            }
            if (i < 0) {
                i = -1;
            }
            this.currentScroll = (float)((double)this.currentScroll - (double)i / (double)j);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean flag = Mouse.isButtonDown((int)0);
        int i = this.guiLeft;
        int j = this.guiTop;
        int k = i + 175;
        int l = j + 18;
        int i1 = k + 14;
        int j1 = l + 112;
        if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i1 && mouseY < j1) {
            this.isScrolling = this.needsScrollBars();
        }
        if (!flag) {
            this.isScrolling = false;
        }
        this.wasClicking = flag;
        if (this.isScrolling) {
            this.currentScroll = ((float)(mouseY - l) - 7.5f) / ((float)(j1 - l) - 15.0f);
            this.currentScroll = MathHelper.clamp_float(this.currentScroll, 0.0f, 1.0f);
            ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (CreativeTabs creativetabs : CreativeTabs.creativeTabArray) {
            if (this.renderCreativeInventoryHoveringText(creativetabs, mouseX, mouseY)) break;
        }
        if (this.field_147064_C != null && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex() && this.isPointInRegion(this.field_147064_C.xDisplayPosition, this.field_147064_C.yDisplayPosition, 16, 16, mouseX, mouseY)) {
            this.drawCreativeTabHoveringText(I18n.format("inventory.binSlot", new Object[0]), mouseX, mouseY);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
    }

    @Override
    protected void renderToolTip(ItemStack stack, int x, int y) {
        if (selectedTabIndex == CreativeTabs.tabAllSearch.getTabIndex()) {
            Map<Integer, Integer> map;
            List<String> list = stack.getTooltip(Minecraft.thePlayer, this.mc.gameSettings.advancedItemTooltips);
            CreativeTabs creativetabs = stack.getItem().getCreativeTab();
            if (creativetabs == null && stack.getItem() == Items.enchanted_book && (map = EnchantmentHelper.getEnchantments(stack)).size() == 1) {
                Enchantment enchantment = Enchantment.getEnchantmentById(map.keySet().iterator().next());
                for (CreativeTabs creativetabs1 : CreativeTabs.creativeTabArray) {
                    if (!creativetabs1.hasRelevantEnchantmentType(enchantment.type)) continue;
                    creativetabs = creativetabs1;
                    break;
                }
            }
            if (creativetabs != null) {
                list.add(1, String.valueOf((Object)EnumChatFormatting.BOLD) + (Object)((Object)EnumChatFormatting.BLUE) + I18n.format(creativetabs.getTranslatedTabLabel(), new Object[0]));
            }
            for (int i = 0; i < list.size(); ++i) {
                if (i == 0) {
                    list.set(i, (Object)((Object)stack.getRarity().rarityColor) + list.get(i));
                    continue;
                }
                list.set(i, (Object)((Object)EnumChatFormatting.GRAY) + list.get(i));
            }
            this.drawHoveringText(list, x, y);
        } else {
            super.renderToolTip(stack, x, y);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableGUIStandardItemLighting();
        CreativeTabs creativetabs = CreativeTabs.creativeTabArray[selectedTabIndex];
        for (CreativeTabs creativetabs1 : CreativeTabs.creativeTabArray) {
            GuiContainerCreative guiContainerCreative = this;
            guiContainerCreative.mc.getTextureManager().bindTexture(creativeInventoryTabs);
            if (creativetabs1.getTabIndex() == selectedTabIndex) continue;
            this.func_147051_a(creativetabs1);
        }
        GuiContainerCreative guiContainerCreative = this;
        guiContainerCreative.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + creativetabs.getBackgroundImageName()));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.searchField.drawTextBox();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        int i = this.guiLeft + 175;
        int j = this.guiTop + 18;
        int k = j + 112;
        GuiContainerCreative guiContainerCreative2 = this;
        guiContainerCreative2.mc.getTextureManager().bindTexture(creativeInventoryTabs);
        if (creativetabs.shouldHidePlayerInventory()) {
            this.drawTexturedModalRect(i, j + (int)((float)(k - j - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
        }
        this.func_147051_a(creativetabs);
        if (creativetabs == CreativeTabs.tabInventory) {
            GuiInventory.drawEntityOnScreen(this.guiLeft + 43, this.guiTop + 45, 20, this.guiLeft + 43 - mouseX, this.guiTop + 45 - 30 - mouseY, Minecraft.thePlayer);
        }
    }

    protected boolean func_147049_a(CreativeTabs p_147049_1_, int p_147049_2_, int p_147049_3_) {
        int i = p_147049_1_.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (i == 5) {
            j = this.xSize - 28 + 2;
        } else if (i > 0) {
            j += i;
        }
        k = p_147049_1_.isTabInFirstRow() ? (k -= 32) : (k += this.ySize);
        return p_147049_2_ >= j && p_147049_2_ <= j + 28 && p_147049_3_ >= k && p_147049_3_ <= k + 32;
    }

    protected boolean renderCreativeInventoryHoveringText(CreativeTabs p_147052_1_, int p_147052_2_, int p_147052_3_) {
        int i = p_147052_1_.getTabColumn();
        int j = 28 * i;
        int k = 0;
        if (i == 5) {
            j = this.xSize - 28 + 2;
        } else if (i > 0) {
            j += i;
        }
        k = p_147052_1_.isTabInFirstRow() ? (k -= 32) : (k += this.ySize);
        if (this.isPointInRegion(j + 3, k + 3, 23, 27, p_147052_2_, p_147052_3_)) {
            this.drawCreativeTabHoveringText(I18n.format(p_147052_1_.getTranslatedTabLabel(), new Object[0]), p_147052_2_, p_147052_3_);
            return true;
        }
        return false;
    }

    protected void func_147051_a(CreativeTabs p_147051_1_) {
        boolean flag = p_147051_1_.getTabIndex() == selectedTabIndex;
        boolean flag1 = p_147051_1_.isTabInFirstRow();
        int i = p_147051_1_.getTabColumn();
        int j = i * 28;
        int k = 0;
        int l = this.guiLeft + 28 * i;
        int i1 = this.guiTop;
        int j1 = 32;
        if (flag) {
            k += 32;
        }
        if (i == 5) {
            l = this.guiLeft + this.xSize - 28;
        } else if (i > 0) {
            l += i;
        }
        if (flag1) {
            i1 -= 28;
        } else {
            k += 64;
            i1 += this.ySize - 4;
        }
        GlStateManager.disableLighting();
        this.drawTexturedModalRect(l, i1, j, k, 28, j1);
        zLevel = 100.0f;
        this.itemRender.zLevel = 100.0f;
        i1 = i1 + 8 + (flag1 ? 1 : -1);
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        ItemStack itemstack = p_147051_1_.getIconItemStack();
        this.itemRender.renderItemAndEffectIntoGUI(itemstack, l += 6, i1);
        this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, l, i1);
        GlStateManager.disableLighting();
        this.itemRender.zLevel = 0.0f;
        zLevel = 0.0f;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiAchievements(this, Minecraft.thePlayer.getStatFileWriter()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, Minecraft.thePlayer.getStatFileWriter()));
        }
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    static class CreativeSlot
    extends Slot {
        final Slot slot;

        public CreativeSlot(Slot p_i46313_2_, int p_i46313_3_) {
            super(p_i46313_2_.inventory, p_i46313_3_, 0, 0);
            this.slot = p_i46313_2_;
        }

        @Override
        public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
            this.slot.onPickupFromSlot(playerIn, stack);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return this.slot.isItemValid(stack);
        }

        @Override
        public ItemStack getStack() {
            return this.slot.getStack();
        }

        @Override
        public boolean getHasStack() {
            return this.slot.getHasStack();
        }

        @Override
        public void putStack(ItemStack stack) {
            this.slot.putStack(stack);
        }

        @Override
        public void onSlotChanged() {
            this.slot.onSlotChanged();
        }

        @Override
        public int getSlotStackLimit() {
            return this.slot.getSlotStackLimit();
        }

        @Override
        public int getItemStackLimit(ItemStack stack) {
            return this.slot.getItemStackLimit(stack);
        }

        @Override
        public String getSlotTexture() {
            return this.slot.getSlotTexture();
        }

        @Override
        public ItemStack decrStackSize(int amount) {
            return this.slot.decrStackSize(amount);
        }

        @Override
        public boolean isHere(IInventory inv, int slotIn) {
            return this.slot.isHere(inv, slotIn);
        }
    }

    static class ContainerCreative
    extends Container {
        public List<ItemStack> itemList = Lists.newArrayList();

        public ContainerCreative(EntityPlayer p_i1086_1_) {
            InventoryPlayer inventoryplayer = p_i1086_1_.inventory;
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlotToContainer(new Slot(field_147060_v, i * 9 + j, 9 + j * 18, 18 + i * 18));
                }
            }
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventoryplayer, k, 9 + k * 18, 112));
            }
            this.scrollTo(0.0f);
        }

        @Override
        public boolean canInteractWith(EntityPlayer playerIn) {
            return true;
        }

        public void scrollTo(float p_148329_1_) {
            int i = (this.itemList.size() + 9 - 1) / 9 - 5;
            int j = (int)((double)(p_148329_1_ * (float)i) + 0.5);
            if (j < 0) {
                j = 0;
            }
            for (int k = 0; k < 5; ++k) {
                for (int l = 0; l < 9; ++l) {
                    int i1 = l + (k + j) * 9;
                    if (i1 >= 0 && i1 < this.itemList.size()) {
                        field_147060_v.setInventorySlotContents(l + k * 9, this.itemList.get(i1));
                        continue;
                    }
                    field_147060_v.setInventorySlotContents(l + k * 9, null);
                }
            }
        }

        public boolean func_148328_e() {
            return this.itemList.size() > 45;
        }

        @Override
        protected void retrySlotClick(int slotId, int clickedButton, boolean mode, EntityPlayer playerIn) {
        }

        @Override
        public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
            Slot slot;
            if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size() && (slot = (Slot)this.inventorySlots.get(index)) != null && slot.getHasStack()) {
                slot.putStack(null);
            }
            return null;
        }

        @Override
        public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
            return slotIn.yDisplayPosition > 90;
        }

        @Override
        public boolean canDragIntoSlot(Slot p_94531_1_) {
            return p_94531_1_.inventory instanceof InventoryPlayer || p_94531_1_.yDisplayPosition > 90 && p_94531_1_.xDisplayPosition <= 162;
        }
    }
}

