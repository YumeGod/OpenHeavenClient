/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  org.lwjgl.input.Keyboard
 */
package heaven.main.module;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import heaven.main.Client;
import heaven.main.event.events.module.EventDisable;
import heaven.main.event.events.module.EventEnable;
import heaven.main.management.EventManager;
import heaven.main.module.CommandModule;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.HUD;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.ui.gui.hud.notification.ModNotification;
import heaven.main.ui.sound.SoundManger;
import heaven.main.utils.chat.Helper;
import heaven.main.value.Mode;
import heaven.main.value.Value;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class Module {
    public final String name;
    private String suffix;
    private final String[] alias;
    public boolean enabled;
    public boolean enabledOnStartup;
    private int key;
    public final List<Value> values;
    public final ModuleType type;
    protected boolean removed;
    public static final Minecraft mc = Minecraft.getMinecraft();
    private float yanim;
    public float animx;
    public boolean arraylistremove = true;
    private int curX;
    private String cusname;
    public float offsetX;
    public float offsetY;
    private final Cache<Class<? extends Module>, Module> moduleCache = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.MINUTES).build();

    public Module(String name, String[] alias, ModuleType type) {
        this.name = name;
        this.cusname = null;
        this.alias = alias;
        this.type = type;
        this.suffix = "";
        this.key = 0;
        this.removed = false;
        this.enabled = false;
        this.values = new ArrayList<Value>();
    }

    public Module(String name, ModuleType type) {
        this.name = name;
        this.cusname = null;
        this.alias = new String[]{name};
        this.type = type;
        this.suffix = "";
        this.key = 0;
        this.removed = false;
        this.enabled = false;
        this.values = new ArrayList<Value>();
    }

    private String getBreakName(boolean breakValue) {
        if (!breakValue) {
            return this.cusname != null ? this.cusname : this.name;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String thename = this.cusname != null ? this.cusname : this.name;
        for (int i = 0; i < thename.length(); ++i) {
            if (i + 2 < thename.length() && i > 1 && Character.isUpperCase(thename.toCharArray()[i + 2]) && Character.isUpperCase(thename.toCharArray()[i])) {
                stringBuilder.append(" ").append(thename.toCharArray()[i]);
                continue;
            }
            if (i + 1 < thename.length()) {
                if (!Character.isUpperCase(thename.toCharArray()[i + 1])) {
                    stringBuilder.append(Character.isUpperCase(thename.toCharArray()[i]) && i > 0 ? " " + thename.toCharArray()[i] : Character.valueOf(thename.toCharArray()[i]));
                    continue;
                }
                stringBuilder.append(thename.toCharArray()[i]);
                continue;
            }
            stringBuilder.append(thename.toCharArray()[i]);
        }
        return stringBuilder.toString();
    }

    public String getCustomName() {
        return this.cusname;
    }

    public String getName() {
        return this.name;
    }

    public String getHUDName() {
        return this.getBreakName((Boolean)HUD.breakname.getValue());
    }

    public String[] getAlias() {
        return this.alias;
    }

    public ModuleType getType() {
        return this.type;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isEnabled(Class<? extends Module> ... modules) {
        for (Class<? extends Module> mClass : modules) {
            Module module = this.getModule(mClass);
            if (!module.enabled) continue;
            return true;
        }
        return false;
    }

    public boolean isEnabled(Class<? extends Module> cls) {
        for (Module m : Client.instance.getModuleManager().modules) {
            if (m.getClass() != cls) continue;
            return m.enabled;
        }
        return false;
    }

    public boolean wasRemoved() {
        return this.removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(Serializable obj) {
        String suffix = obj.toString();
        if (suffix.isEmpty()) {
            this.suffix = suffix;
        } else if (((Boolean)HUD.tags.getValue()).booleanValue()) {
            if (HUD.tagsmode.isCurrentMode("Binds")) {
                this.suffix = String.format((Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + "\u00a77\u00a7f%s", new Object[]{(Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY)}) + Keyboard.getKeyName((int)this.key);
            }
            if (HUD.tagsmode.isCurrentMode("Simple")) {
                this.suffix = String.format("\u00a77\u00a7f%s", (Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + suffix);
            }
            if (HUD.tagsmode.isCurrentMode("Box")) {
                this.suffix = String.format((Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + "[\u00a7f%s]", (Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + suffix);
            }
            if (HUD.tagsmode.isCurrentMode("Brackets")) {
                this.suffix = String.format((Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + "(\u00a7f%s)", (Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + suffix);
            }
            if (HUD.tagsmode.isCurrentMode("XML")) {
                this.suffix = String.format((Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + "<\u00a7f%s>", (Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + suffix);
            }
            if (HUD.tagsmode.isCurrentMode("VerticalBar")) {
                this.suffix = String.format((Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + "| \u00a7f%s", (Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + suffix);
            }
            if (HUD.tagsmode.isCurrentMode("Hyphen")) {
                this.suffix = String.format((Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + "- \u00a7f%s", (Object)((Object)((Boolean)HUD.rainbow.getValue() == false ? EnumChatFormatting.GRAY : ((Boolean)HUD.tagswhite.getValue() != false ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY))) + suffix);
            }
        } else {
            this.suffix = "";
        }
    }

    public void setEnabledWithoutNotification(boolean enabled) {
        if (enabled) {
            this.enabled = true;
            this.onEnable();
            EventManager.register(this);
            EventManager.call(new EventEnable(this));
        } else {
            EventManager.call(new EventDisable(this));
            EventManager.unregister(this);
            this.onDisable();
            this.enabled = false;
        }
    }

    public void setEnabled(boolean enabled) {
        if (((Boolean)HUD.sound.getValue()).booleanValue()) {
            if (Minecraft.thePlayer != null) {
                if (enabled) {
                    new SoundManger().jelloEnableSound();
                } else {
                    new SoundManger().jelloDisableSound();
                }
            }
        }
        if (enabled) {
            this.enabled = true;
            this.onEnable();
            EventManager.register(this);
            EventManager.call(new EventEnable(this));
            if (this.name.contains("ClickGui")) {
                ClientNotification.sendClientMessage("ClickGui", this.name + " \u00a77Enabled", 1600L, ClientNotification.Type.INFO);
            } else if (((Boolean)HUD.modnotifications.getValue()).booleanValue()) {
                ModNotification.sendClientMessage(this.name + " Enabled", 1600L, ModNotification.Type.Enabled);
            }
        } else {
            if (this.name.contains("ClickGui")) {
                ClientNotification.sendClientMessage("ClickGui", this.name + " \u00a77Disabled", 1600L, ClientNotification.Type.INFO);
            } else if (((Boolean)HUD.modnotifications.getValue()).booleanValue()) {
                ModNotification.sendClientMessage(this.name + " Disabled", 1600L, ModNotification.Type.Disabled);
            }
            EventManager.call(new EventDisable(this));
            EventManager.unregister(this);
            this.onDisable();
            this.enabled = false;
        }
    }

    protected void addValues(Value ... values) {
        Collections.addAll(this.values, values);
    }

    protected void values(Value ... values) {
        Collections.addAll(this.values, values);
    }

    public List<Value> getValues() {
        return this.values;
    }

    public boolean isMoving() {
        return Minecraft.thePlayer.moving();
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    /*
     * Unable to fully structure code
     */
    public void makeCommand() {
        if (!this.values.isEmpty()) {
            options = new StringBuilder();
            other = new StringBuilder();
            for (Value v : this.values) {
                if (v instanceof Mode) continue;
                if (options.length() == 0) {
                    options.append(v.getName());
                    continue;
                }
                options.append(String.format(", %s", new Object[]{v.getName()}));
            }
            var4 = this.values.iterator();
            block1: while (true) {
                if (!var4.hasNext()) {
                    Client.instance.getCommandManager().add(new CommandModule(this, this.name, this.alias));
                    return;
                }
                v = var4.next();
                if (!(v instanceof Mode)) continue;
                mode = (Mode)v;
                modes = mode.getModes();
                length = modes.length;
                i = 0;
                while (true) {
                    if (i < length) ** break;
                    continue block1;
                    e = modes[i];
                    if (other.length() == 0) {
                        other.append(e.toLowerCase());
                    } else {
                        other.append(String.format(", %s", new Object[]{e.toLowerCase()}));
                    }
                    ++i;
                }
                break;
            }
        }
    }

    public <Module extends Module> Module getModule(Class<? extends Module> moduleClass) {
        try {
            return (Module)((Module)this.moduleCache.get(moduleClass, () -> Client.instance.getModuleManager().getModuleByClass(moduleClass)));
        }
        catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkModule(Class<? extends Module> ... modules) {
        for (Class<? extends Module> mClass : modules) {
            Module module = this.getModule(mClass);
            if (!module.enabled) continue;
            module.toggle();
            ClientNotification.sendClientMessage("Warning", module.name + " was disabled to prevent flags/errors", 2000L, ClientNotification.Type.WARNING);
        }
    }

    public void checkModule(Class<? extends Module> moduleClass) {
        Module module = Client.instance.getModuleManager().getModuleByClass(moduleClass);
        if (module.enabled) {
            ClientNotification.sendClientMessage("Warning", module.name + " was disabled to prevent flags/errors", 2000L, ClientNotification.Type.WARNING);
            module.toggle();
        }
    }

    public boolean toggle() {
        if (this.enabled) {
            this.setEnabled(false);
            return false;
        }
        this.setEnabled(true);
        return true;
    }

    public float getYAnim() {
        return this.offsetY;
    }

    public void setYAnim(float anim) {
        this.offsetY = anim;
    }

    public void setX(int X) {
        this.curX = X;
    }

    public int getX() {
        return this.curX;
    }

    public void setAnimx(float aanimx) {
        this.animx = aanimx;
    }

    public float getAnimx() {
        return this.animx;
    }

    public boolean wasArrayRemoved() {
        return this.arraylistremove;
    }

    public void setArrayRemoved(boolean arraylistremove) {
        this.arraylistremove = arraylistremove;
    }

    public void setCustomName(String name) {
        this.cusname = name;
    }

    public void sendPacket(Packet<?> packet) {
        if (Minecraft.thePlayer != null) {
            mc.getNetHandler().getNetworkManager().sendPacket(packet);
        }
    }

    public void sendPacketNoEvent(Packet<?> packet) {
        if (Minecraft.thePlayer != null) {
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
        }
    }

    public void msg(String messgae) {
        Helper.sendMessage(messgae);
    }

    public void msg(String messgae, boolean prefix) {
        if (prefix) {
            Helper.sendMessage(messgae);
        } else {
            Helper.sendMessageWithoutPrefix(messgae);
        }
    }

    public void sendPacketNoEventDelayed(Packet packet, long delay) {
        try {
            new Thread(() -> {
                try {
                    Thread.sleep(delay);
                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void renderInfo(String title, String msg, long time) {
        ClientNotification.sendClientMessage(title, msg, time, ClientNotification.Type.INFO);
    }

    public void renderInfo(String title, String msg, long time, ClientNotification.Type type) {
        ClientNotification.sendClientMessage(title, msg, time, type);
    }
}

