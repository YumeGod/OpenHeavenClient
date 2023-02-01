/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.display.DisplayChestGuiEvent;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;

public class WindowChestHack
extends Module {
    private final Option<Boolean> zoomHack = new Option<Boolean>("ZoomHack", true);
    private final Option<Boolean> silent = new Option<Boolean>("SilentGui", true);
    private final Numbers<Double> delay = new Numbers<Double>("Delay", 200.0, 0.0, 1000.0, 1.0, () -> (Boolean)this.zoomHack.get() == false);
    private final TimerUtil timer = new TimerUtil();

    public WindowChestHack() {
        super("WindowChestStealer", new String[]{"chesthack"}, ModuleType.World);
        this.addValues(this.delay, this.zoomHack, this.silent);
    }

    @EventHandler
    public void onDisplayGuiChest(DisplayChestGuiEvent event) {
        block3: {
            block2: {
                if (((Boolean)this.silent.get()).booleanValue() && event.getString().equals("minecraft:chest")) break block2;
                if (!(Minecraft.thePlayer.openContainer instanceof ContainerChest)) break block3;
            }
            mc.displayGuiScreen(WindowChestHack.mc.previousScreen instanceof GuiInventory ? null : WindowChestHack.mc.previousScreen);
            ScaledResolution sr = new ScaledResolution(mc);
            Client.instance.FontLoaders.Comfortaa18.drawCenteredStringWithShadow("Stealing chest...", sr.getScaledWidth_double() / 2.0, sr.getScaledHeight_double() / 2.0, new Color(200, 200, 200).getRGB());
        }
    }

    @EventHandler
    private void onUpdate(EventTick event) {
        if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest)Minecraft.thePlayer.openContainer;
            for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
                if (container.getLowerChestInventory().getStackInSlot(i) == null || !this.timer.hasReached((Double)this.delay.getValue()) && !((Boolean)this.zoomHack.get()).booleanValue()) continue;
                Minecraft.playerController.windowClick(container.windowId, i, 0, 1, Minecraft.thePlayer);
                this.timer.reset();
            }
            if (this.isEmpty()) {
                Minecraft.thePlayer.closeScreen();
            }
        }
    }

    private boolean isEmpty() {
        if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest)Minecraft.thePlayer.openContainer;
            for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
                ItemStack itemStack = container.getLowerChestInventory().getStackInSlot(i);
                if (itemStack == null || itemStack.getItem() == null) continue;
                return false;
            }
        }
        return true;
    }
}

