/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.clickgui.astolfoclickgui.asClickgui;
import heaven.main.ui.gui.clickgui.flux.FluxGui;
import heaven.main.ui.gui.clickgui.nl.ClickUI;
import heaven.main.ui.gui.clickgui.novogui.ClickyUI;
import heaven.main.ui.gui.clickgui.powerx.PXClickGui;
import heaven.main.value.Mode;
import heaven.main.value.Option;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClickGui
extends Module {
    public static List<Module> memoriseML = new CopyOnWriteArrayList<Module>();
    public static ModuleType memoriseCatecory;
    public static float startX;
    public static float startY;
    public static ModuleType currentModuleType;
    public static int tempWheel;
    private final Mode<String> mode = new Mode("Mode", new String[]{"Box", "List", "Astolfo", "Novoline", "PowerX", "NL"}, "List");
    public static final Option<Boolean> Streamer;
    public static final Option<Boolean> Blur;
    public static final Option<Boolean> Visitable;
    public static int memoriseX;
    public static int memoriseY;
    public static int memoriseWheel;

    public ClickGui() {
        super("ClickGui", new String[]{"clickgui"}, ModuleType.Render);
        if (this.getKey() == 0) {
            this.setKey(54);
        }
        this.addValues(this.mode, Streamer, Visitable);
    }

    @Override
    public void onEnable() {
        this.setEnabledWithoutNotification(false);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (this.mode.is("NL")) {
            mc.displayGuiScreen(new ClickUI());
            ClickUI.setX((int)startX);
            ClickUI.setY((int)startY);
            ClickUI.setWheel(tempWheel);
            if (currentModuleType != null) {
                ClickUI.setCategory(currentModuleType);
            }
        }
        if (this.mode.isCurrentMode("List")) {
            mc.displayGuiScreen(new heaven.main.ui.gui.clickgui.listgui.ClickyUI());
        }
        if (this.mode.is("PowerX")) {
            mc.displayGuiScreen(new PXClickGui());
        }
        if (this.mode.is("Novoline")) {
            mc.displayGuiScreen(new ClickyUI());
        }
        if (this.mode.isCurrentMode("Astolfo")) {
            mc.displayGuiScreen(new asClickgui());
        }
        if (this.mode.isCurrentMode("Box")) {
            mc.displayGuiScreen(new FluxGui());
            FluxGui.setX(memoriseX);
            FluxGui.setY(memoriseY);
            FluxGui.setWheel(memoriseWheel);
            FluxGui.setInSetting(memoriseML);
            if (memoriseCatecory != null) {
                FluxGui.setCategory(memoriseCatecory);
            }
        }
    }

    static {
        Streamer = new Option<Boolean>("Streamer", true);
        Blur = new Option<Boolean>("Blur", false);
        Visitable = new Option<Boolean>("ValueVisitable", true);
        memoriseX = 30;
        memoriseY = 30;
    }
}

