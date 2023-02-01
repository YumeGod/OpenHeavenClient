/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.gson.JsonParseException
 *  io.netty.buffer.Unpooled
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiScreenBook
extends GuiScreen {
    private static final Logger logger = LogManager.getLogger();
    static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");
    private final EntityPlayer editingPlayer;
    private final ItemStack bookObj;
    private final boolean bookIsUnsigned;
    private boolean bookIsModified;
    private boolean bookGettingSigned;
    private int updateCount;
    private final int bookImageWidth = 192;
    private final int bookImageHeight = 192;
    private int bookTotalPages = 1;
    private int currPage;
    private NBTTagList bookPages;
    private String bookTitle = "";
    private List<IChatComponent> field_175386_A;
    private int field_175387_B = -1;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private GuiButton buttonDone;
    private GuiButton buttonSign;
    private GuiButton buttonFinalize;
    private GuiButton buttonCancel;

    public GuiScreenBook(EntityPlayer player, ItemStack book, boolean isUnsigned) {
        this.editingPlayer = player;
        this.bookObj = book;
        this.bookIsUnsigned = isUnsigned;
        if (book.hasTagCompound()) {
            NBTTagCompound nbttagcompound = book.getTagCompound();
            this.bookPages = nbttagcompound.getTagList("pages", 8);
            if (this.bookPages != null) {
                this.bookPages = (NBTTagList)this.bookPages.copy();
                this.bookTotalPages = this.bookPages.tagCount();
                if (this.bookTotalPages < 1) {
                    this.bookTotalPages = 1;
                }
            }
        }
        if (this.bookPages == null && isUnsigned) {
            this.bookPages = new NBTTagList();
            this.bookPages.appendTag(new NBTTagString(""));
            this.bookTotalPages = 1;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        ++this.updateCount;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents((boolean)true);
        if (this.bookIsUnsigned) {
            int n = width / 2 - 100;
            this.getClass();
            this.buttonSign = new GuiButton(3, n, 4 + 192, 98, 20, I18n.format("book.signButton", new Object[0]));
            this.buttonList.add(this.buttonSign);
            int n2 = width / 2 + 2;
            this.getClass();
            this.buttonDone = new GuiButton(0, n2, 4 + 192, 98, 20, I18n.format("gui.done", new Object[0]));
            this.buttonList.add(this.buttonDone);
            int n3 = width / 2 - 100;
            this.getClass();
            this.buttonFinalize = new GuiButton(5, n3, 4 + 192, 98, 20, I18n.format("book.finalizeButton", new Object[0]));
            this.buttonList.add(this.buttonFinalize);
            int n4 = width / 2 + 2;
            this.getClass();
            this.buttonCancel = new GuiButton(4, n4, 4 + 192, 98, 20, I18n.format("gui.cancel", new Object[0]));
            this.buttonList.add(this.buttonCancel);
        } else {
            int n = width / 2 - 100;
            this.getClass();
            this.buttonDone = new GuiButton(0, n, 4 + 192, 200, 20, I18n.format("gui.done", new Object[0]));
            this.buttonList.add(this.buttonDone);
        }
        this.getClass();
        int i = (width - 192) / 2;
        int j = 2;
        this.buttonNextPage = new NextPageButton(1, i + 120, j + 154, true);
        this.buttonList.add(this.buttonNextPage);
        this.buttonPreviousPage = new NextPageButton(2, i + 38, j + 154, false);
        this.buttonList.add(this.buttonPreviousPage);
        this.updateButtons();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    private void updateButtons() {
        this.buttonNextPage.visible = !this.bookGettingSigned && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned);
        this.buttonPreviousPage.visible = !this.bookGettingSigned && this.currPage > 0;
        boolean bl = this.buttonDone.visible = !this.bookIsUnsigned || !this.bookGettingSigned;
        if (this.bookIsUnsigned) {
            this.buttonSign.visible = !this.bookGettingSigned;
            this.buttonCancel.visible = this.bookGettingSigned;
            this.buttonFinalize.visible = this.bookGettingSigned;
            this.buttonFinalize.enabled = !this.bookTitle.trim().isEmpty();
        }
    }

    private void sendBookToServer(boolean publish) {
        if (this.bookIsUnsigned && this.bookIsModified && this.bookPages != null) {
            String s;
            while (this.bookPages.tagCount() > 1 && (s = this.bookPages.getStringTagAt(this.bookPages.tagCount() - 1)).isEmpty()) {
                this.bookPages.removeTag(this.bookPages.tagCount() - 1);
            }
            if (this.bookObj.hasTagCompound()) {
                NBTTagCompound nbttagcompound = this.bookObj.getTagCompound();
                nbttagcompound.setTag("pages", this.bookPages);
            } else {
                this.bookObj.setTagInfo("pages", this.bookPages);
            }
            String s2 = "MC|BEdit";
            if (publish) {
                s2 = "MC|BSign";
                this.bookObj.setTagInfo("author", new NBTTagString(this.editingPlayer.getName()));
                this.bookObj.setTagInfo("title", new NBTTagString(this.bookTitle.trim()));
                for (int i = 0; i < this.bookPages.tagCount(); ++i) {
                    String s1 = this.bookPages.getStringTagAt(i);
                    ChatComponentText ichatcomponent = new ChatComponentText(s1);
                    s1 = IChatComponent.Serializer.componentToJson(ichatcomponent);
                    this.bookPages.set(i, new NBTTagString(s1));
                }
                this.bookObj.setItem(Items.written_book);
            }
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeItemStackToBuffer(this.bookObj);
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(s2, packetbuffer));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.mc.displayGuiScreen(null);
                this.sendBookToServer(false);
            } else if (button.id == 3 && this.bookIsUnsigned) {
                this.bookGettingSigned = true;
            } else if (button.id == 1) {
                if (this.currPage < this.bookTotalPages - 1) {
                    ++this.currPage;
                } else if (this.bookIsUnsigned) {
                    this.addNewPage();
                    if (this.currPage < this.bookTotalPages - 1) {
                        ++this.currPage;
                    }
                }
            } else if (button.id == 2) {
                if (this.currPage > 0) {
                    --this.currPage;
                }
            } else if (button.id == 5 && this.bookGettingSigned) {
                this.sendBookToServer(true);
                this.mc.displayGuiScreen(null);
            } else if (button.id == 4 && this.bookGettingSigned) {
                this.bookGettingSigned = false;
            }
            this.updateButtons();
        }
    }

    private void addNewPage() {
        if (this.bookPages != null && this.bookPages.tagCount() < 50) {
            this.bookPages.appendTag(new NBTTagString(""));
            ++this.bookTotalPages;
            this.bookIsModified = true;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (this.bookIsUnsigned) {
            if (this.bookGettingSigned) {
                this.keyTypedInTitle(typedChar, keyCode);
            } else {
                this.keyTypedInBook(typedChar, keyCode);
            }
        }
    }

    private void keyTypedInBook(char typedChar, int keyCode) {
        if (GuiScreen.isKeyComboCtrlV(keyCode)) {
            this.pageInsertIntoCurrent(GuiScreen.getClipboardString());
        } else {
            switch (keyCode) {
                case 14: {
                    String s = this.pageGetCurrent();
                    if (!s.isEmpty()) {
                        this.pageSetCurrent(s.substring(0, s.length() - 1));
                    }
                    return;
                }
                case 28: 
                case 156: {
                    this.pageInsertIntoCurrent("\n");
                    return;
                }
            }
            if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                this.pageInsertIntoCurrent(Character.toString(typedChar));
            }
        }
    }

    private void keyTypedInTitle(char p_146460_1_, int p_146460_2_) throws IOException {
        switch (p_146460_2_) {
            case 14: {
                if (!this.bookTitle.isEmpty()) {
                    this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
                    this.updateButtons();
                }
                return;
            }
            case 28: 
            case 156: {
                if (!this.bookTitle.isEmpty()) {
                    this.sendBookToServer(true);
                    this.mc.displayGuiScreen(null);
                }
                return;
            }
        }
        if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(p_146460_1_)) {
            this.bookTitle = this.bookTitle + p_146460_1_;
            this.updateButtons();
            this.bookIsModified = true;
        }
    }

    private String pageGetCurrent() {
        return this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount() ? this.bookPages.getStringTagAt(this.currPage) : "";
    }

    private void pageSetCurrent(String p_146457_1_) {
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
            this.bookPages.set(this.currPage, new NBTTagString(p_146457_1_));
            this.bookIsModified = true;
        }
    }

    private void pageInsertIntoCurrent(String p_146459_1_) {
        String s = this.pageGetCurrent();
        String s1 = s + p_146459_1_;
        int i = this.fontRendererObj.splitStringWidth(s1 + (Object)((Object)EnumChatFormatting.BLACK) + "_", 118);
        if (i <= 128 && s1.length() < 256) {
            this.pageSetCurrent(s1);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GuiScreenBook guiScreenBook = this;
        guiScreenBook.mc.getTextureManager().bindTexture(bookGuiTextures);
        this.getClass();
        int i = (width - 192) / 2;
        int j = 2;
        this.getClass();
        this.getClass();
        this.drawTexturedModalRect(i, j, 0, 0, 192, 192);
        if (this.bookGettingSigned) {
            String s = this.bookTitle;
            if (this.bookIsUnsigned) {
                s = this.updateCount / 6 % 2 == 0 ? s + (Object)((Object)EnumChatFormatting.BLACK) + "_" : s + (Object)((Object)EnumChatFormatting.GRAY) + "_";
            }
            String s1 = I18n.format("book.editTitle", new Object[0]);
            int k = this.fontRendererObj.getStringWidth(s1);
            this.fontRendererObj.drawString(s1, i + 36 + (116 - k) / 2, j + 16 + 16, 0);
            int l = this.fontRendererObj.getStringWidth(s);
            this.fontRendererObj.drawString(s, i + 36 + (116 - l) / 2, j + 48, 0);
            String s2 = I18n.format("book.byAuthor", this.editingPlayer.getName());
            int i1 = this.fontRendererObj.getStringWidth(s2);
            this.fontRendererObj.drawString((Object)((Object)EnumChatFormatting.DARK_GRAY) + s2, i + 36 + (116 - i1) / 2, j + 48 + 10, 0);
            String s3 = I18n.format("book.finalizeWarning", new Object[0]);
            this.fontRendererObj.drawSplitString(s3, i + 36, j + 80, 116, 0);
        } else {
            String s4 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
            String s5 = "";
            if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
                s5 = this.bookPages.getStringTagAt(this.currPage);
            }
            if (this.bookIsUnsigned) {
                s5 = this.fontRendererObj.getBidiFlag() ? s5 + "_" : (this.updateCount / 6 % 2 == 0 ? s5 + (Object)((Object)EnumChatFormatting.BLACK) + "_" : s5 + (Object)((Object)EnumChatFormatting.GRAY) + "_");
            } else if (this.field_175387_B != this.currPage) {
                if (ItemEditableBook.validBookTagContents(this.bookObj.getTagCompound())) {
                    try {
                        IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s5);
                        this.field_175386_A = ichatcomponent != null ? GuiUtilRenderComponents.splitText(ichatcomponent, 116, this.fontRendererObj, true, true) : null;
                    }
                    catch (JsonParseException var13) {
                        this.field_175386_A = null;
                    }
                } else {
                    ChatComponentText chatcomponenttext = new ChatComponentText(EnumChatFormatting.DARK_RED.toString() + "* Invalid book tag *");
                    this.field_175386_A = Lists.newArrayList((Iterable)chatcomponenttext);
                }
                this.field_175387_B = this.currPage;
            }
            int j1 = this.fontRendererObj.getStringWidth(s4);
            this.getClass();
            this.fontRendererObj.drawString(s4, i - j1 + 192 - 44, j + 16, 0);
            if (this.field_175386_A == null) {
                this.fontRendererObj.drawSplitString(s5, i + 36, j + 16 + 16, 116, 0);
            } else {
                int k1 = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());
                for (int l1 = 0; l1 < k1; ++l1) {
                    IChatComponent ichatcomponent2 = this.field_175386_A.get(l1);
                    this.fontRendererObj.drawString(ichatcomponent2.getUnformattedText(), i + 36, j + 16 + 16 + l1 * this.fontRendererObj.FONT_HEIGHT, 0);
                }
                IChatComponent ichatcomponent1 = this.func_175385_b(mouseX, mouseY);
                if (ichatcomponent1 != null) {
                    this.handleComponentHover(ichatcomponent1, mouseX, mouseY);
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        IChatComponent ichatcomponent;
        if (mouseButton == 0 && this.handleComponentClick(ichatcomponent = this.func_175385_b(mouseX, mouseY))) {
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected boolean handleComponentClick(IChatComponent component) {
        ClickEvent clickevent;
        ClickEvent clickEvent = clickevent = component == null ? null : component.getChatStyle().getChatClickEvent();
        if (clickevent == null) {
            return false;
        }
        if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            String s = clickevent.getValue();
            try {
                int i = Integer.parseInt(s) - 1;
                if (i >= 0 && i < this.bookTotalPages && i != this.currPage) {
                    this.currPage = i;
                    this.updateButtons();
                    return true;
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            return false;
        }
        boolean flag = super.handleComponentClick(component);
        if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
            this.mc.displayGuiScreen(null);
        }
        return flag;
    }

    public IChatComponent func_175385_b(int p_175385_1_, int p_175385_2_) {
        if (this.field_175386_A == null) {
            return null;
        }
        this.getClass();
        int i = p_175385_1_ - (width - 192) / 2 - 36;
        int j = p_175385_2_ - 2 - 16 - 16;
        if (i >= 0 && j >= 0) {
            int k = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());
            if (i <= 116) {
                if (j < Minecraft.fontRendererObj.FONT_HEIGHT * k + k) {
                    int l = j / Minecraft.fontRendererObj.FONT_HEIGHT;
                    if (l >= 0 && l < this.field_175386_A.size()) {
                        IChatComponent ichatcomponent = this.field_175386_A.get(l);
                        int i1 = 0;
                        for (IChatComponent ichatcomponent1 : ichatcomponent) {
                            if (!(ichatcomponent1 instanceof ChatComponentText)) continue;
                            if ((i1 += Minecraft.fontRendererObj.getStringWidth(((ChatComponentText)ichatcomponent1).getChatComponentText_TextValue())) <= i) continue;
                            return ichatcomponent1;
                        }
                    }
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    static class NextPageButton
    extends GuiButton {
        private final boolean field_146151_o;

        public NextPageButton(int p_i46316_1_, int p_i46316_2_, int p_i46316_3_, boolean p_i46316_4_) {
            super(p_i46316_1_, p_i46316_2_, p_i46316_3_, 23, 13, "");
            this.field_146151_o = p_i46316_4_;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                mc.getTextureManager().bindTexture(bookGuiTextures);
                int i = 0;
                int j = 192;
                if (flag) {
                    i += 23;
                }
                if (!this.field_146151_o) {
                    j += 13;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, i, j, 23, 13);
            }
        }
    }
}

