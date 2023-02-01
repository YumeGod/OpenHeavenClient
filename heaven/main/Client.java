/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.Display
 */
package heaven.main;

import com.me.guichaguri.betterfps.BetterFpsClient;
import heaven.heavenalpha.AAL;
import heaven.heavenalpha.antileak.AntiLeak;
import heaven.heavenalpha.antileak.TitleCheck;
import heaven.heavenalpha.web.HWIDUtil;
import heaven.heavenalpha.web.WebUtils;
import heaven.main.management.CommandManager;
import heaven.main.management.ConfigManager;
import heaven.main.management.EventManager;
import heaven.main.management.FileManager;
import heaven.main.management.FriendManager;
import heaven.main.management.ModuleManager;
import heaven.main.module.Module;
import heaven.main.module.modules.misc.Spammer;
import heaven.main.module.modules.render.NameProtect;
import heaven.main.module.modules.render.info.CombatManager;
import heaven.main.ui.font.FontLoaders;
import heaven.main.ui.font.fontRenderer.FontManager;
import heaven.main.ui.gui.alt.AltFileSaver;
import heaven.main.ui.gui.guimainmenu.mainmenu.ClientMainMenu;
import heaven.main.ui.gui.hud.tabgui.SideTabGui;
import heaven.main.ui.gui.hud.tabgui.TabGUI;
import heaven.main.value.Value;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Client {
    public static final Client instance = new Client();
    public final String name = "Heaven";
    public String hudname = "Heaven";
    public String prefix;
    public final String lastPrefix = this.prefix = "-";
    public final String lastName = this.hudname;
    public String version;
    public DynamicTexture headTexture;
    private CommandManager commandmanager;
    public final FontManager fontManager = new FontManager();
    private boolean pretend;
    public final TabGUI tabGUI = new TabGUI();
    public final SideTabGui sideTabGui = new SideTabGui();
    private EventManager eventManager;
    public FriendManager friendManager;
    public CombatManager combatManager = new CombatManager();
    public ConfigManager configManager = new ConfigManager();
    public FontManager FontManager = new FontManager();
    public FontLoaders FontLoaders = new FontLoaders();
    private ModuleManager modulemanager;
    public String User;
    public String Users;
    public String Pass;
    public static boolean Beta = true;
    public static String strHWID;
    private boolean LeakBoolean;
    public String nowTitle;

    public Client() {
        Minecraft.getMinecraft();
        this.User = Minecraft.User;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    public final void initiate() throws IOException {
        System.out.println("Start Beta");
        strHWID = AAL.otherSeverNotGitee ? "https://gitee.com/bili_shiqi/heavenclienthwid/blob/master/hwid" : "https://gitee.com/bili_shiqi/heavenclienthwid/blob/master/hwid";
        this.version = "1.2";
        try {
            for (int i = 0; i < 5; ++i) {
                System.out.println("[DEBUG] Your HWID is not beta" + HWIDUtil.getClientHWID(!Beta));
            }
        }
        catch (Throwable e) {
            System.out.println("catch (Throwable e)");
            e.printStackTrace();
        }
        this.betaCheckAndTryPing();
        this.eventManager = new EventManager();
        this.commandmanager = new CommandManager();
        this.commandmanager.init();
        this.friendManager = new FriendManager();
        this.friendManager.init();
        this.modulemanager = new ModuleManager();
        Client.instance.modulemanager.init();
        BetterFpsClient.start(Minecraft.getMinecraft());
        AltFileSaver.readAlts();
        FileManager.init();
        EventManager.register(this.tabGUI, this);
        EventManager.register(this.sideTabGui, this);
        this.tabGUI.init();
        this.sideTabGui.init();
        new TitleCheck().titleCheck();
    }

    public ModuleManager getModuleManager() {
        return this.modulemanager;
    }

    public CommandManager getCommandManager() {
        return this.commandmanager;
    }

    /*
     * WARNING - void declaration
     */
    public void saveConfig() {
        void var4_11;
        AltFileSaver.saveAlts();
        StringBuilder values = new StringBuilder();
        for (Module module : Client.instance.modulemanager.getModules()) {
            for (Value v : module.getValues()) {
                values.append(String.format("%s:%s:%s%s", module.getName(), v.getName(), v.getValue(), System.lineSeparator()));
            }
        }
        FileManager.save("Values.txt", values.toString(), false);
        StringBuilder enabled = new StringBuilder();
        for (Module module : Client.instance.modulemanager.getModules()) {
            if (!module.isEnabled()) continue;
            enabled.append(String.format("%s%s", module.getName(), System.lineSeparator()));
        }
        FileManager.save("Enabled.txt", enabled.toString(), false);
        new File(FileManager.dir + "/CustomName.txt").delete();
        StringBuilder stringBuilder = new StringBuilder();
        for (Module m2 : Client.instance.modulemanager.getModules()) {
            if (m2.getCustomName() == null) continue;
            stringBuilder.append(String.format("%s:%s%s", m2.getName(), m2.getCustomName(), System.lineSeparator()));
        }
        FileManager.save("CustomName.txt", stringBuilder.toString(), false);
        String string = "";
        for (Module m : Client.instance.modulemanager.getModules()) {
            String string2 = (String)var4_11 + String.format("%s:%s%s", m.getName(), Keyboard.getKeyName((int)m.getKey()), System.lineSeparator());
        }
        FileManager.save("Binds.txt", (String)var4_11, false);
        String spammerMessage = Spammer.bindmessage != null ? "Version<1.0.0>" + Spammer.bindmessage : "";
        FileManager.save("Spammer.txt", spammerMessage, false);
        String nameProtected = NameProtect.name != null ? "Version<1.0.0>" + NameProtect.name : "Version<1.0.0>User";
        FileManager.save("Name.txt", nameProtected, false);
        StringBuilder Hiddens = new StringBuilder();
        for (Module m2 : Client.instance.modulemanager.getModules()) {
            if (!m2.wasRemoved()) continue;
            Hiddens.append(m2.getName()).append(System.lineSeparator());
        }
        FileManager.save("Hidden.txt", Hiddens.toString(), false);
        String shadercfg = ClientMainMenu.useShader ? "true" : "false";
        FileManager.save("Shader.txt", shadercfg, false);
    }

    public final void startClient() throws NoSuchFieldException {
        new AntiLeak().startAntiLeakModule();
        System.out.println("new AntiLeak().startAntiLeakModule(); finished");
        this.LeakBoolean = false;
        System.out.println("LeakBoolean = false; finished");
        List<String> shadercfg = FileManager.read("Shader.txt");
        for (String string : shadercfg) {
            ClientMainMenu.useShader = string.toLowerCase().contains("true");
        }
        List<String> autoDuelName = FileManager.read("AutoDuelName.txt");
        for (String string : autoDuelName) {
            if (string.contains("Version<Version>")) continue;
            return;
        }
        List<String> list = FileManager.read("Spammer.txt");
        for (String b : list) {
            if (!b.contains("Version<Version>")) {
                return;
            }
            Spammer.bindmessage = b.substring(21);
        }
        List<String> list2 = FileManager.read("Name.txt");
        for (String b : list2) {
            if (!b.contains("Version<Version>")) {
                return;
            }
            NameProtect.name = b.substring(21);
        }
    }

    private void crashClient() {
        this.windowMessage(null, "Crash Client Report", "Leak is true");
        new AntiLeak().publicResetAllCheck();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Runtime.getRuntime().gc();
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

    public boolean isPretend() {
        return this.pretend;
    }

    private String getWebInfo(String url) {
        return WebUtils.get(url);
    }

    private boolean titleCheck() {
        String[] info = new String[]{"M", "i", "n", "e", "c", "r", "a", "f", "t"};
        String indexCore = info[0] + info[1] + info[2] + info[3] + info[4] + info[5] + info[6] + info[7] + info[8];
        String[] numberInfo = new String[]{"1", ".", "8", ".", "9"};
        String numberIndexCore = numberInfo[0] + numberInfo[1] + numberInfo[2] + numberInfo[3] + numberInfo[4];
        return Display.getTitle().equals(indexCore + " " + numberIndexCore);
    }

    public final void tryPingIP(String ip) {
    }

    private void betaCheckAndTryPing() {
        if (Beta) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.nowTitle = "Heaven BETA UserDate - " + df.format(new Date());
            Display.setTitle((String)("Heaven BETA UserDate - " + df.format(new Date())));
        }
    }
}

