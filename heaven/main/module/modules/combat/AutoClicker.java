/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package heaven.main.module.modules.combat;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.utils.PlayerUtil;
import heaven.main.utils.timer.TimerUtils;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class AutoClicker
extends Module {
    public final TimerUtils time = new TimerUtils();
    public final Numbers<Double> cpsmin = new Numbers<Double>("MinCPS", 8.0, 2.0, 20.0, 1.0);
    public final Numbers<Double> cpsmax = new Numbers<Double>("MaxCPS", 8.0, 2.0, 20.0, 1.0);
    protected final Random r = new Random();
    protected long lastMS = -1L;
    public final Option<Boolean> ab = new Option<Boolean>("BlockHit", true);
    public final Option<Boolean> BreakBlock = new Option<Boolean>("BreakBlock", true);
    public final Option<Boolean> InvClicker = new Option<Boolean>("Inventory", false);
    public int Click;
    public boolean Clicked;
    private double delay;
    private final TimerUtils time2 = new TimerUtils();
    private final TimerUtils time3 = new TimerUtils();

    public AutoClicker() {
        super("AutoClicker", new String[]{"AutoClicker"}, ModuleType.Combat);
        this.addValues(this.cpsmin, this.cpsmax, this.ab, this.InvClicker, this.BreakBlock);
    }

    private void delay() {
        float minCps = ((Double)this.cpsmin.getValue()).floatValue();
        float maxCps = ((Double)this.cpsmax.getValue()).floatValue();
        float minDelay = 1000.0f / minCps;
        float maxDelay = 1000.0f / maxCps;
        this.delay = (double)maxDelay + this.r.nextDouble() * (double)(minDelay - maxDelay);
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        boolean isblock;
        BlockPos bp = Minecraft.thePlayer.rayTrace(6.0, 0.0f).getBlockPos();
        boolean bl = isblock = Minecraft.theWorld.getBlockState(bp).getBlock() != Blocks.air && AutoClicker.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY;
        if (!((Boolean)this.BreakBlock.getValue()).booleanValue()) {
            isblock = false;
        }
        if (this.time2.delay(this.delay) && !this.time2.delay(this.delay + this.delay / 2.0)) {
            this.Clicked = true;
        }
        if (this.time2.delay(this.delay + this.delay - 1.0)) {
            this.Clicked = false;
            AutoClicker autoClicker = this;
            autoClicker.time2.reset();
        }
        if (!Client.instance.getModuleManager().getModuleByClass(KillAura.class).isEnabled() && Mouse.isButtonDown((int)0) && this.time.delay(this.delay) && AutoClicker.mc.currentScreen == null && !isblock) {
            PlayerUtil.blockHit(AutoClicker.mc.objectMouseOver.entityHit, (Boolean)this.ab.getValue());
            AutoClicker.mc.leftClickCounter = 0;
            mc.clickMouse();
            this.delay();
            AutoClicker autoClicker = this;
            autoClicker.time.reset();
        }
    }

    @EventHandler
    private void invClicks(EventPreUpdate event) {
        if (!Keyboard.isKeyDown((int)42)) {
            return;
        }
        if (AutoClicker.mc.currentScreen instanceof GuiContainer && ((Boolean)this.InvClicker.getValue()).booleanValue()) {
            float invClickDelay = 1000.0f / ((Double)this.cpsmax.getValue()).floatValue() + (float)this.r.nextInt(50);
            if (Mouse.isButtonDown((int)0)) {
                if (TimerUtils.delay(invClickDelay)) {
                    try {
                        AutoClicker.mc.currentScreen.InventoryClicks();
                        AutoClicker autoClicker = this;
                        autoClicker.time3.reset();
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }
    }
}

