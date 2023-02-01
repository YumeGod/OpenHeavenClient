/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.utils.file.FileUtils
 *  org.lwjgl.input.Mouse
 */
package heaven.main.ui.gui.alt;

import com.utils.file.FileUtils;
import heaven.main.Client;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.ui.gui.alt.Alt;
import heaven.main.ui.gui.alt.AltEnum;
import heaven.main.ui.gui.alt.AltManagerRightClickMenu;
import heaven.main.ui.gui.alt.GuiAltLogin;
import heaven.main.ui.gui.alt.RightClickMenu;
import heaven.main.ui.gui.clickgui.CSGOClickGui;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.login.AltLoginThread;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Mouse;

public class GuiAltManager
extends GuiScreen {
    private static final CopyOnWriteArrayList<Alt> alts = new CopyOnWriteArrayList();
    private final GuiScreen screen;
    private float wheel;
    private static int start;
    private static float listHeight;
    private static float selectedAltY;
    public static Alt selectedAlt;
    public static AltLoginThread altLoginThread;
    private boolean leftMouseDown;
    private boolean rightMouseDown;
    private String status = "Waiting for alt login...";
    public static RightClickMenu displayingMenu;

    public GuiAltManager(GuiScreen screen) {
        this.screen = screen;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, 100, height - 24, 75, 20, "Add Alt"));
        this.buttonList.add(new GuiButton(1, 177, height - 24, 75, 20, "Login Alt"));
        this.buttonList.add(new GuiButton(2, 254, height - 24, 75, 20, "Back"));
        this.buttonList.add(new GuiButton(3, 20, height - 24, 75, 20, "Import"));
        displayingMenu = null;
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiAltLogin(this, AltEnum.ADD));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(new GuiAltLogin(this, AltEnum.LOGIN));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(this.screen);
                break;
            }
            case 3: {
                new Thread(() -> {
                    try {
                        for (String s : FileUtils.readLine((InputStream)new FileInputStream(FileUtils.browseFile((int)0, (String)"Choose a file")))) {
                            String a = s.split(":")[0];
                            String p = s.split(":")[1];
                            String u = s.split(":")[2];
                            alts.add(new Alt(a, p, u));
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }, "Import alts Thread").start();
                System.gc();
            }
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if (Mouse.hasWheel()) {
            int cacheWheel = Mouse.getDWheel();
            if (cacheWheel < 0) {
                if (listHeight < (float)(alts.size() * 50)) {
                    this.wheel = 50.0f;
                }
                if (start < alts.size()) {
                    ++start;
                }
                if (listHeight < 0.0f) {
                    listHeight = 0.0f;
                }
            } else if (cacheWheel > 0) {
                this.wheel = -50.0f;
                if (listHeight < 0.0f) {
                    listHeight = 0.0f;
                }
                if (start > 0) {
                    --start;
                }
            }
            if (cacheWheel != 0) {
                displayingMenu = null;
            }
        }
        if (this.wheel < 0.0f) {
            if (listHeight > 0.0f) {
                listHeight -= (float)((int)(-((double)this.wheel / 16.0)));
            }
            this.wheel += 1.0f;
        } else if (this.wheel > 0.0f) {
            if (listHeight < (float)(alts.size() * 50)) {
                listHeight += (float)((int)((double)this.wheel / 16.0));
            }
            this.wheel -= 1.0f;
        }
        RenderUtil.drawRect(20.0f, 2.0f, 150.0f, 48.0f, new Color(0, 0, 0, 150).getRGB());
        this.mc.session.loadHead();
        RenderUtil.drawImage(this.mc.session.getHead(), 22.0, 9.0, 37.0, 37.0, -1);
        CFontRenderer cfr_ignored_0 = Client.instance.FontLoaders.Comfortaa22;
        Client.instance.FontLoaders.Comfortaa22.drawString("Name:", 68.0f, 18 - 0, -1);
        CFontRenderer cfr_ignored_1 = Client.instance.FontLoaders.Comfortaa22;
        Client.instance.FontLoaders.Comfortaa22.drawString(this.mc.session.getProfile().getName(), 68.0f, 33 - 0, -1);
        CFontRenderer cfr_ignored_2 = Client.instance.FontLoaders.Comfortaa22;
        Client.instance.FontLoaders.Comfortaa22.drawString(this.status, 160.0f, 32 - 0, -1);
        Client.instance.FontLoaders.Comfortaa16.drawString(alts.size() + "alts", 21.0f, height - 50, new Color(88688455, true).getRGB());
        RenderUtil.drawRect(20.0f, 50.0f, width - 20, height - 50, new Color(0, 0, 0, 150).getRGB());
        if (alts.size() >= 11) {
            RenderUtil.drawRect(width - 24, 51.0f + listHeight / 50.0f, width - 22, (float)(height - 51) - (-listHeight / 50.0f + (float)alts.size()), new Color(50, 50, 50).getRGB());
        }
        RenderUtil.startGlScissor(20, 52, width - 20, height - 104);
        if (selectedAlt != null) {
            RenderUtil.drawBorderedRect(22.0, selectedAltY + 2.0f, width - 26, selectedAltY + 34.0f, 1.0f, -1, new Color(0, 0, 0, 0).getRGB());
        }
        float altY = 50.0f - listHeight;
        for (Alt alt : alts) {
            alt.loadHead();
            RenderUtil.drawRect(0.0f, 0.0f, 0.0f, 0.0f, 0);
            RenderUtil.drawImage(alt.getHead(), 23.0, altY + 3.0f, 30.0, 30.0, -1);
            Client.instance.FontLoaders.Comfortaa17.drawString("ID: " + (Object)((Object)EnumChatFormatting.GRAY) + alt.getAccount(), 60.0f, (int)(altY + 2.0f + 4.0f), -1);
            Client.instance.FontLoaders.Comfortaa17.drawString("Password: " + (alt.getPassword().equals("NULL_PASSWORD") ? (Object)((Object)EnumChatFormatting.RED) + "\u79bb\u7ebf\u8d26\u6237" : (Object)((Object)EnumChatFormatting.GRAY) + alt.getPassword().replaceAll(".", "*")), 60.0f, (int)(altY + 12.0f + 4.0f), -1);
            Client.instance.FontLoaders.Comfortaa17.drawString("Player Name: " + (Object)((Object)EnumChatFormatting.GRAY) + alt.getUserName(), 60.0f, (int)(altY + 22.0f + 4.0f), -1);
            if (CSGOClickGui.isHovered(22.0f, altY + 2.0f + 4.0f, width - 22, altY + 34.0f, mouseX, mouseY)) {
                boolean isIn = CSGOClickGui.isHovered(20.0f, 50.0f, width - 20, height - 50, mouseX, mouseY);
                if (isIn) {
                    RenderUtil.drawBorderedRect(22.0, altY + 2.0f, width - 26, altY + 34.0f, 1.0f, new Color(0, 0, 0).getRGB(), new Color(0, 0, 0, 100).getRGB());
                }
                if (Mouse.isButtonDown((int)0) && !this.leftMouseDown && isIn) {
                    if (alt != selectedAlt) {
                        selectedAlt = alt;
                    } else {
                        String cachePassword = alt.getPassword().equals("NULL_PASSWORD") ? "" : alt.getPassword();
                        altLoginThread = new AltLoginThread(alt.getAccount(), cachePassword);
                        altLoginThread.start();
                    }
                    this.leftMouseDown = true;
                }
                if (Mouse.isButtonDown((int)1) && !this.rightMouseDown && isIn) {
                    displayingMenu = new AltManagerRightClickMenu(mouseX, mouseY, alt);
                    displayingMenu.onOpen();
                    this.rightMouseDown = true;
                }
            }
            if (alt == selectedAlt) {
                selectedAltY = altY;
            }
            altY += 40.0f;
        }
        RenderUtil.stopGlScissor();
        if (!Mouse.isButtonDown((int)0)) {
            this.leftMouseDown = false;
        }
        if (!Mouse.isButtonDown((int)1)) {
            this.rightMouseDown = false;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (displayingMenu != null) {
            displayingMenu.draw(mouseX, mouseY);
        }
        if (altLoginThread != null) {
            this.status = altLoginThread.getStatus();
        }
    }

    public static CopyOnWriteArrayList<Alt> getAlts() {
        return alts;
    }
}

