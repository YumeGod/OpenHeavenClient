/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 */
package heaven.main.ui.gui.login;

import heaven.heavenalpha.AAL;
import heaven.heavenalpha.antileak.AntiLeak;
import heaven.heavenalpha.base64.Base64Obf;
import heaven.heavenalpha.web.HWIDUtil;
import heaven.heavenalpha.web.WebUtils;
import heaven.main.Client;
import heaven.main.ui.SplashProgress;
import heaven.main.ui.gui.clickgui.AnimationUtil;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.ui.gui.login.GuiPasswordField;
import heaven.main.ui.gui.login.GuiUserField;
import heaven.main.ui.gui.login.GuiWelcome;
import heaven.main.ui.simplecore.SimpleRender;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GuiLogin
extends GuiScreen {
    public static boolean login;
    public static boolean checkDev;
    private int alpha = 255;
    public static boolean i;
    public static boolean j;
    private static GuiPasswordField password;
    private static GuiUserField username;
    private static boolean crack;
    static final int allX = 160;
    float cleanX;
    float cleanSize;
    float cleanSize2;
    private final Base64.Encoder encoder = Base64.getEncoder();
    static final Base64.Decoder decoder;

    public Color fade(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        String name;
        if (i) {
            if (this.alpha < 255) {
                this.alpha += 5;
            }
        } else if (SplashProgress.PROGRESS == 11 && this.alpha > 0) {
            this.alpha -= 5;
        }
        RenderUtil.drawImage(new ResourceLocation("client/guimainmenu/mainmenu.png"), 0, 0, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight());
        int loginX = -60;
        int loginY = -20;
        RenderUtil.drawRect(width / 2 - 180 - -60, height / 2 - 115 - -20, width / 2 + 180 + -60, height / 2 + 115 + -20, new Color(39, 38, 55, 86).getRGB());
        if (var1 > width / 2 + 30 - 160 && var1 < width / 2 + 155 - 160 && var2 > height / 2 + 47 && var2 < height / 2 + 70) {
            this.cleanX = AnimationUtil.moveUD(this.cleanX, 10.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            this.cleanSize = AnimationUtil.moveUD(this.cleanSize, Mouse.isButtonDown((int)0) ? 10.0f : 0.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            this.cleanSize2 = AnimationUtil.moveUD(this.cleanSize2, 1.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        } else {
            this.cleanSize = AnimationUtil.moveUD(this.cleanSize, 0.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            this.cleanSize2 = AnimationUtil.moveUD(this.cleanSize2, 0.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
            this.cleanX = AnimationUtil.moveUD(this.cleanX, 0.0f, SimpleRender.processFPS(1000.0f, 0.013f), SimpleRender.processFPS(1000.0f, 0.011f));
        }
        if (!username.getText().isEmpty() && !password.getText().isEmpty() && Mouse.isButtonDown((int)0) && var1 > width / 2 + 30 - 160 && var1 < width / 2 + 155 - 160 && var2 > height / 2 + 47 && var2 < height / 2 + 70) {
            Client.instance.tryPingIP(AAL.otherSeverNotGitee ? "https://gitee.com/bili_shiqi/heavenclienthwid/blob/master/hwid" : "https://gitee.com/bili_shiqi/heavenclienthwid/blob/master/hwid");
            if (Client.Beta) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Client.instance.nowTitle = Client.instance.name + " BETA UserDate - " + df.format(new Date());
                Display.setTitle((String)(Client.instance.name + " BETA UserDate - " + df.format(new Date())));
            }
            if (username.getText().length() <= 2 || password.getText().length() <= 2) {
                this.windowMessage(null, "HWIDError", "ID: 002");
                Minecraft.getMinecraft().shutdown();
                this.crashClient();
            }
            if (username.getText().length() >= 200 || password.getText().length() >= 200) {
                this.windowMessage(null, "HWIDError", "ID: 999");
                Minecraft.getMinecraft().shutdown();
                this.crashClient();
            }
            try {
                Client.instance.headTexture = new DynamicTexture(ImageIO.read(new URL("https://q.qlogo.cn/headimg_dl?dst_uin=3203016039&spec=640&img_type=png")));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Client.instance.Users = username.getText();
            StringBuilder usernames = new StringBuilder(username.getText());
            StringBuilder passwords = new StringBuilder(password.getText());
            if (this.Login(String.valueOf(usernames), String.valueOf(passwords))) {
                i = true;
                j = true;
            } else {
                Client.instance.Users = null;
                i = true;
                j = false;
            }
        }
        RenderUtil.drawIcon((float)(width / 2 + 50 - 160) + this.cleanSize, (float)(height / 2 + 47) + this.cleanX / 10.0f + this.cleanSize2, 210, 24, new ResourceLocation("client/login.png"));
        RenderUtil.drawIcon((float)(width / 2 + 50 - 160) + this.cleanSize, (float)(height / 2 + 47) + this.cleanX / 10.0f + this.cleanSize2, 210, 24, new ResourceLocation("client/loginFont.png"));
        RenderUtil.smoothRender(1);
        float otherheight = 8.0f;
        RenderUtil.drawRoundRect(width / 2 + 60 - 160, (float)(height / 2 - 9) - otherheight, width / 2 + 250 - 160, (float)(height / 2 - 8) - otherheight, new Color(255, 255, 255).getRGB());
        RenderUtil.drawRoundRect(width / 2 + 60 - 160, (float)(height / 2 + 30) - otherheight, width / 2 + 250 - 160, (float)(height / 2 + 31) - otherheight, new Color(255, 255, 255).getRGB());
        RenderUtil.smoothRender(2);
        Client.instance.FontLoaders.regular20.drawString("L o g i n", width / 2 + 65 - 160, height / 2 + 54, new Color(255, 255, 255).getRGB());
        int y2offsets = 140;
        int x2offsets = 90;
        Client.instance.FontLoaders.regular18.drawString("Heaven Login", (float)(width / 2 - 135 + 90) - 28.5f + (float)(Client.instance.FontLoaders.bold30.getStringWidth("HeavenLogin") / 2), height / 2 + 50 - 140, new Color(255, 255, 255).getRGB());
        if (password.getText().isEmpty() && !password.isFocused()) {
            name = "Password";
            Client.instance.FontLoaders.novoicons24.drawString("L", width / 2 + 60 - 160, (float)(height / 2 + 22) - otherheight, new Color(255, 255, 255).getRGB());
            Client.instance.FontLoaders.regular18.drawString(Character.toUpperCase("Password".toLowerCase().charAt(0)) + "Password".toLowerCase().substring(1), width / 2 + 77 - 160, (float)(height / 2 + 20) - otherheight, new Color(255, 255, 255).getRGB());
        } else {
            Client.instance.FontLoaders.novoicons24.drawString("L", width / 2 + 60 - 160, (float)(height / 2 + 22) - otherheight, new Color(255, 255, 255).getRGB());
            StringBuilder xing = new StringBuilder();
            for (char ignored : password.getText().toCharArray()) {
                xing.append("* ");
                Client.instance.FontLoaders.regular20.drawString(String.valueOf(xing), width / 2 + 75 - 160, (float)(height / 2 + 20) - otherheight, new Color(255, 255, 255).getRGB());
            }
        }
        if (username.getText().isEmpty() && !username.isFocused()) {
            name = "Username";
            Client.instance.FontLoaders.NovICON24.drawString("d", width / 2 + 60 - 160, (float)(height / 2 - 19) - otherheight, new Color(255, 255, 255).getRGB());
            Client.instance.FontLoaders.regular18.drawString(Character.toUpperCase(name.toLowerCase().charAt(0)) + name.toLowerCase().substring(1), width / 2 + 77 - 160, (float)(height / 2 - 19) - otherheight, new Color(255, 255, 255).getRGB());
        } else {
            Client.instance.FontLoaders.NovICON24.drawString("d", width / 2 + 60 - 160, (float)(height / 2 - 19) - otherheight, new Color(255, 255, 255).getRGB());
            Client.instance.FontLoaders.regular18.drawString(username.getText(), width / 2 + 75 - 160, (float)(height / 2 - 19) - otherheight, new Color(255, 255, 255).getRGB());
        }
        super.drawScreen(var1, var2, var3);
        if (this.alpha >= 255 && i && j) {
            i = false;
            j = false;
            Minecraft.getMinecraft().displayGuiScreen(new GuiWelcome());
        }
        if (this.alpha >= 255 && i && !j) {
            i = false;
        }
        GuiLogin.drawRect(0, 0, RenderUtil.width(), RenderUtil.height(), new Color(0, 0, 0, this.alpha).getRGB());
    }

    @Override
    public void initGui() {
        Minecraft.getMinecraft();
        FontRenderer var1 = Minecraft.fontRendererObj;
        super.initGui();
        username = new GuiUserField(var1, width / 2 + 30 - 90 - 60, height / 2 - 30, 55, 20);
        password = new GuiPasswordField(var1, width / 2 + 30 - 90 - 60, height / 2 + 10, 55, 20);
    }

    @Override
    protected void keyTyped(char var1, int var2) {
        if (var1 == '\r') {
            Client.instance.tryPingIP(AAL.otherSeverNotGitee ? "https://gitee.com/bili_shiqi/heavenclienthwid/blob/master/hwid" : "https://gitee.com/bili_shiqi/heavenclienthwid/blob/master/hwid");
            if (Client.Beta) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Client.instance.nowTitle = Client.instance.name + " BETA UserDate - " + df.format(new Date());
                Display.setTitle((String)(Client.instance.name + " BETA UserDate - " + df.format(new Date())));
            }
            if (username.getText().length() <= 2 || password.getText().length() <= 2) {
                JOptionPane.showInputDialog(null, "Why use Cracked version", Client.instance.version);
                Minecraft.getMinecraft().shutdown();
            }
            try {
                Client.instance.headTexture = new DynamicTexture(ImageIO.read(new URL("https://q.qlogo.cn/headimg_dl?dst_uin=3203016039&spec=640&img_type=png")));
            }
            catch (IOException df) {
                // empty catch block
            }
            Client.instance.Users = username.getText();
            StringBuilder usernames = new StringBuilder(username.getText());
            StringBuilder passwords = new StringBuilder(password.getText());
            if (this.Login(String.valueOf(usernames), String.valueOf(passwords))) {
                i = true;
                j = true;
            } else {
                Client.instance.Users = null;
                i = true;
                j = false;
            }
        }
        if (var1 == '\t') {
            if (!username.isFocused()) {
                username.setFocused(true);
            } else {
                username.setFocused(true);
                password.setFocused(!username.isFocused());
            }
        }
        username.textboxKeyTyped(var1, var2);
        password.textboxKeyTyped(var1, var2);
    }

    @Override
    protected void mouseClicked(int var1, int var2, int var3) {
        try {
            super.mouseClicked(var1, var2, var3);
        }
        catch (IOException var5) {
            var5.printStackTrace();
        }
        username.mouseClicked(var1, var2, var3);
        password.mouseClicked(var1, var2, var3);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    @Override
    public void updateScreen() {
        password.updateCursorCounter();
    }

    public final void Logins() {
        try {
            crack = !this.get(Client.strHWID).contains(HWIDUtil.getClientHWID(!Client.Beta));
        }
        catch (Exception exception) {
            crack = true;
        }
    }

    private boolean Login(String usernames, String passwords) {
        Client.instance.User = usernames;
        Client.instance.Pass = passwords;
        try {
            this.Logins();
            System.out.println("token is " + this.get(Client.strHWID));
            if (this.get(Client.strHWID).contains(usernames + ":" + passwords + ":" + HWIDUtil.getClientHWID(!Client.Beta))) {
                try {
                    checkDev = this.get(Client.strHWID).contains(usernames + ":" + passwords + ":" + HWIDUtil.getClientHWID(!Client.Beta) + ":Dev");
                }
                catch (Exception e) {
                    checkDev = false;
                }
                if (checkDev) {
                    System.out.println("login successful you are a Dev");
                } else {
                    System.out.println("login successful");
                }
                return true;
            }
            JOptionPane.showInputDialog(null, "HWID don\u2019t have activation", HWIDUtil.getClientHWID(!Client.Beta));
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    private String encode(String text) {
        byte[] textByte = text.getBytes(StandardCharsets.UTF_8);
        return this.encoder.encodeToString(textByte);
    }

    public static String decode(String encodedText) {
        String text = new String(decoder.decode(encodedText), StandardCharsets.UTF_8);
        return text;
    }

    private void windowMessage(Component bool, String text, String text2) {
        if (bool == null) {
            if (text != null || text2 != null) {
                if (Objects.requireNonNull(text).isEmpty() || text2.isEmpty()) {
                    this.crashClient();
                }
                JOptionPane.showInputDialog(null, text, text2);
            } else {
                this.crashClient();
            }
        } else {
            this.crashClient();
            new AntiLeak().publicResetAllCheck();
        }
    }

    private void crashClient() {
        new AntiLeak().publicResetAllCheck();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Runtime.getRuntime().gc();
    }

    private String get(String url) {
        return WebUtils.get(Base64Obf.obfTurnIO(url));
    }

    static {
        decoder = Base64.getDecoder();
    }
}

