/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.client.Minecraft;

public class Animations
extends Module {
    private final String[] renderMode = new String[]{"1.7", "Slide", "Exhibition", "Jigsaw", "Avatar", "Remix", "Sigma", "Astro", "Jello", "Jello2", "Rainy", "Target", "LiquidBounce", "Push", "Lucky", "Swank", "Swang", "Thinking", "Stitch", "Circle"};
    public final Mode<String> mode = new Mode("Mode", this.renderMode, this.renderMode[0]);
    public static final Numbers<Double> speed = new Numbers<Double>("SwingSpeed", 1.0, 0.1, 1.5, 0.1);
    public static final Numbers<Double> x = new Numbers<Double>("X", 0.0, -1.0, 1.0, 0.1);
    public static final Numbers<Double> y = new Numbers<Double>("Y", 0.0, -1.0, 1.0, 0.1);
    public static final Numbers<Double> z = new Numbers<Double>("Z", 0.0, -1.0, 1.0, 0.1);
    public static final Numbers<Double> swingx = new Numbers<Double>("SwingX", 0.0, -1.0, 1.0, 0.1);
    public static final Numbers<Double> swingy = new Numbers<Double>("SwingY", 0.0, -1.0, 1.0, 0.1);
    public static final Numbers<Double> swingz = new Numbers<Double>("SwingZ", 0.0, -1.0, 1.0, 0.1);
    public static final Option<Boolean> Smooth = new Option<Boolean>("SmoothSwing", false);
    public static final Option<Boolean> highHand = new Option<Boolean>("HighHand", false);
    public static final Option<Boolean> LeftHand = new Option<Boolean>("LeftHand", false);
    public static final Option<Boolean> betterThirdPersonBlock = new Option<Boolean>("BetterThirdPersonBlock", true);
    private final Option<Boolean> foodanim = new Option<Boolean>("FoodAnimations", false);
    private final Option<Boolean> resetAnimation = new Option<Boolean>("ResetBlockAnimations", false);
    private final Numbers<Double> resetMaxDelay = new Numbers<Double>("ResetMaxDelay", 300.0, 0.0, 2000.0, 5.0, this.resetAnimation::get);
    private final Numbers<Double> resetMinDelay = new Numbers<Double>("ResetMinDelay", 300.0, 0.0, 2000.0, 5.0, this.resetAnimation::get);
    private final TimerUtil AnimationsTimer = new TimerUtil();
    int foodticks;

    public Animations() {
        super("Animations", ModuleType.Render);
        this.addValues(this.mode, speed, x, y, z, swingx, swingy, swingz, Smooth, highHand, LeftHand, this.foodanim, betterThirdPersonBlock, this.resetAnimation, this.resetMaxDelay, this.resetMinDelay);
    }

    public double getDelay() {
        return MathUtil.randomNumber((Double)this.resetMaxDelay.getValue(), (Double)this.resetMinDelay.getValue());
    }

    @Override
    public void onEnable() {
        if (((Boolean)this.resetAnimation.get()).booleanValue()) {
            this.AnimationsTimer.reset();
        }
    }

    @Override
    public void onDisable() {
        if (((Boolean)this.resetAnimation.get()).booleanValue()) {
            this.AnimationsTimer.reset();
        }
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e2) {
        block6: {
            block7: {
                block8: {
                    this.setSuffix((Serializable)this.mode.getValue());
                    if (((Boolean)this.resetAnimation.get()).booleanValue()) {
                        if (Minecraft.thePlayer.isBlocking() && this.AnimationsTimer.hasReached(this.getDelay())) {
                            mc.getItemRenderer().resetEquippedProgress2();
                            this.AnimationsTimer.reset();
                        }
                    }
                    if (!((Boolean)this.foodanim.get()).booleanValue()) break block6;
                    ++this.foodticks;
                    if (this.foodticks != 4 && this.foodticks != 8 && this.foodticks != 12 && this.foodticks != 16 && this.foodticks != 20) break block7;
                    if (Minecraft.thePlayer.isUsingItem() && Animations.mc.gameSettings.keyBindAttack.isKeyDown()) break block8;
                    if (Minecraft.thePlayer.isEating() && Animations.mc.gameSettings.keyBindAttack.isKeyDown()) break block8;
                    if (!Minecraft.thePlayer.isBlocking() || !Animations.mc.gameSettings.keyBindAttack.isKeyDown()) break block7;
                }
                Minecraft.thePlayer.swingItem();
            }
            if (this.foodticks >= 20) {
                this.foodticks = 0;
            }
        }
    }
}

