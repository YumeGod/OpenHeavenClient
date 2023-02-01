/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.render.Rotate;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.ui.RenderRotate;
import heaven.main.utils.timer.Timer;
import heaven.main.value.Mode;
import java.io.Serializable;
import net.minecraft.client.Minecraft;

public class AntiAim
extends Module {
    float[] lastAngles;
    public float rotationPitch;
    private boolean fake;
    private boolean fake1;
    public byte var4 = (byte)-1;
    public float pitchDown;
    public float lastMeme;
    public float reverse;
    public float sutter;
    private float lastP;
    Minecraft var10000;
    final Timer fakeJitter = new Timer();
    private final String[] YAW = new String[]{"Reverse", "Jitter", "Lisp", "SpinSlow", "SpinFast", "Sideways", "FakeJitter", "FakeHead", "Freestanding"};
    private final String[] PITCH = new String[]{"Normal", "HalfDown", "Zero", "Up", "Stutter", "Reverse", "Meme"};
    private final Mode<String> AAYAW = new Mode("AAYAW", this.YAW, this.YAW[0]);
    private final Mode<String> AAPITCH = new Mode("AAPITCH", this.PITCH, this.PITCH[0]);

    public AntiAim() {
        super("AntiAim", new String[]{"aa"}, ModuleType.Player);
        this.addValues(this.AAYAW, this.AAPITCH);
    }

    public void updateAngles(float yaw, float pitch) {
        if (Client.instance.getModuleManager().getModuleByClass(Rotate.class).isEnabled()) {
            new RenderRotate(yaw, pitch, true);
        }
        if (AntiAim.mc.gameSettings.thirdPersonView != 0) {
            Minecraft.thePlayer.rotationYawHead = yaw;
            Minecraft.thePlayer.renderYawOffset = yaw;
        }
    }

    @Override
    public void onDisable() {
        this.fake1 = true;
        this.lastAngles = null;
        this.rotationPitch = 0.0f;
        Minecraft.thePlayer.renderYawOffset = Minecraft.thePlayer.rotationYaw;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        this.fake1 = true;
        this.lastAngles = null;
        this.rotationPitch = 0.0f;
        super.onEnable();
    }

    @EventHandler
    public void onEvent(EventPreUpdate event) {
        this.setSuffix((Serializable)((Object)((String)this.AAYAW.getValue() + " - " + (String)this.AAPITCH.getValue())));
        if (AntiAim.mc.gameSettings.keyBindAttack.isKeyDown() || AntiAim.mc.gameSettings.keyBindDrop.isKeyDown() || AntiAim.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            return;
        }
        Scaffold Scaffold2 = (Scaffold)Client.instance.getModuleManager().getModuleByClass(Scaffold.class);
        if (this.getModule(KillAura.class).getTarget() == null && !Scaffold2.isEnabled()) {
            if (this.lastAngles == null) {
                float[] var10001 = new float[2];
                var10001[0] = Minecraft.thePlayer.rotationYaw;
                var10001[1] = Minecraft.thePlayer.rotationPitch;
                this.lastAngles = var10001;
            }
            boolean bl = this.fake = !this.fake;
            if (this.AAYAW.isCurrentMode("Jitter")) {
                this.var4 = 0;
            }
            if (this.AAYAW.isCurrentMode("SpinFast")) {
                this.var4 = (byte)7;
            }
            if (this.AAYAW.isCurrentMode("SpinSlow")) {
                this.var4 = (byte)8;
            }
            if (this.AAYAW.isCurrentMode("Freestanding")) {
                this.var4 = (byte)6;
            }
            if (this.AAYAW.isCurrentMode("Reverse")) {
                this.var4 = (byte)2;
            }
            if (this.AAYAW.isCurrentMode("FakeJitter")) {
                this.var4 = (byte)4;
            }
            if (this.AAYAW.isCurrentMode("Lisp")) {
                this.var4 = 1;
            }
            if (this.AAYAW.isCurrentMode("Sideways")) {
                this.var4 = (byte)3;
            }
            if (this.AAYAW.isCurrentMode("FakeHead")) {
                this.var4 = (byte)5;
            }
            switch (this.var4) {
                case 0: {
                    this.pitchDown = this.lastAngles[0] + 90.0f;
                    EventPreUpdate.setYaw(this.pitchDown);
                    this.lastAngles = new float[]{this.pitchDown, this.lastAngles[1]};
                    this.updateAngles(this.pitchDown, this.lastAngles[1]);
                    this.var10000 = mc;
                    Minecraft.thePlayer.renderYawOffset = this.pitchDown;
                    Minecraft.thePlayer.prevRenderYawOffset = this.pitchDown;
                    break;
                }
                case 1: {
                    this.lastMeme = this.lastAngles[0] + 150000.0f;
                    this.lastAngles = new float[]{this.lastMeme, this.lastAngles[1]};
                    EventPreUpdate.setYaw(this.lastMeme);
                    this.updateAngles(this.lastMeme, this.lastAngles[1]);
                    break;
                }
                case 2: {
                    this.var10000 = mc;
                    this.reverse = Minecraft.thePlayer.rotationYaw + 180.0f;
                    this.lastAngles = new float[]{this.reverse, this.lastAngles[1]};
                    EventPreUpdate.setYaw(this.reverse);
                    this.updateAngles(this.reverse, this.lastAngles[1]);
                    break;
                }
                case 3: {
                    this.var10000 = mc;
                    this.sutter = Minecraft.thePlayer.rotationYaw - 90.0f;
                    this.lastAngles = new float[]{this.sutter, this.lastAngles[1]};
                    EventPreUpdate.setYaw(this.sutter);
                    this.updateAngles(this.sutter, this.lastAngles[1]);
                    break;
                }
                case 4: {
                    if (this.fakeJitter.delay(350.0)) {
                        this.fake1 = !this.fake1;
                        this.fakeJitter.reset();
                    }
                    this.var10000 = mc;
                    float yawRight = Minecraft.thePlayer.rotationYaw + (float)(this.fake1 ? 90 : -90);
                    this.lastAngles = new float[]{yawRight, this.lastAngles[1]};
                    EventPreUpdate.setYaw(yawRight);
                    this.updateAngles(yawRight, this.lastAngles[1]);
                    break;
                }
                case 5: {
                    if (this.fakeJitter.delay(1100.0)) {
                        this.fake1 = !this.fake1;
                        this.fakeJitter.reset();
                    }
                    this.var10000 = mc;
                    float yawFakeHead = Minecraft.thePlayer.rotationYaw + (float)(this.fake1 ? 90 : -90);
                    if (this.fake1) {
                        this.fake1 = false;
                    }
                    this.lastAngles = new float[]{yawFakeHead, this.lastAngles[1]};
                    EventPreUpdate.setYaw(yawFakeHead);
                    this.updateAngles(yawFakeHead, this.lastAngles[1]);
                    break;
                }
                case 6: {
                    this.var10000 = mc;
                    float freestandHead = (float)((double)(Minecraft.thePlayer.rotationYaw + 5.0f) + Math.random() * 175.0);
                    this.lastAngles = new float[]{freestandHead, this.lastAngles[1]};
                    EventPreUpdate.setYaw(freestandHead);
                    this.updateAngles(freestandHead, this.lastAngles[1]);
                    break;
                }
                case 7: {
                    float yawSpinFast = this.lastAngles[0] + 45.0f;
                    this.lastAngles = new float[]{yawSpinFast, this.lastAngles[1]};
                    EventPreUpdate.setYaw(yawSpinFast);
                    this.updateAngles(yawSpinFast, this.lastAngles[1]);
                    break;
                }
                case 8: {
                    float yawSpinSlow = this.lastAngles[0] + 10.0f;
                    this.lastAngles = new float[]{yawSpinSlow, this.lastAngles[1]};
                    EventPreUpdate.setYaw(yawSpinSlow);
                    this.updateAngles(yawSpinSlow, this.lastAngles[1]);
                    break;
                }
            }
            if (this.AAPITCH.isCurrentMode("Normal")) {
                this.var4 = (byte)2;
            }
            if (this.AAPITCH.isCurrentMode("Reverse")) {
                this.var4 = (byte)3;
            }
            if (this.AAPITCH.isCurrentMode("Stutter")) {
                this.var4 = (byte)4;
            }
            if (this.AAPITCH.isCurrentMode("Up")) {
                this.var4 = (byte)5;
            }
            if (this.AAPITCH.isCurrentMode("Meme")) {
                this.var4 = 1;
            }
            if (this.AAPITCH.isCurrentMode("Zero")) {
                this.var4 = (byte)6;
            }
            if (this.AAPITCH.isCurrentMode("HalfDown")) {
                this.var4 = 0;
            }
            switch (this.var4) {
                case 0: {
                    this.pitchDown = 90.0f;
                    this.lastAngles = new float[]{this.lastAngles[0], this.pitchDown};
                    EventPreUpdate.setPitch(this.pitchDown);
                    this.updateAngles(this.lastAngles[0], this.pitchDown);
                    break;
                }
                case 1: {
                    this.lastMeme = this.lastAngles[1];
                    this.lastMeme += 10.0f;
                    if (this.lastMeme > 90.0f) {
                        this.lastMeme = -90.0f;
                    }
                    this.lastAngles = new float[]{this.lastAngles[0], this.lastMeme};
                    EventPreUpdate.setPitch(this.lastMeme);
                    this.updateAngles(this.lastAngles[0], this.lastMeme);
                    break;
                }
                case 2: {
                    this.updateAngles(this.lastAngles[0], Minecraft.thePlayer.rotationPitch);
                    break;
                }
                case 3: {
                    this.var10000 = mc;
                    this.reverse = Minecraft.thePlayer.rotationPitch + 180.0f;
                    this.lastAngles = new float[]{this.lastAngles[0], this.reverse};
                    EventPreUpdate.setPitch(this.reverse);
                    this.updateAngles(this.lastAngles[0], this.reverse);
                    break;
                }
                case 4: {
                    this.sutter = this.fake ? 90.0f : -45.0f;
                    EventPreUpdate.setPitch(this.sutter);
                    this.lastAngles = new float[]{this.lastAngles[0], this.sutter};
                    this.updateAngles(this.lastAngles[0], this.sutter);
                    break;
                }
                case 5: {
                    this.lastAngles = new float[]{this.lastAngles[0], -90.0f};
                    EventPreUpdate.setPitch(-90.0f);
                    this.updateAngles(this.lastAngles[0], -90.0f);
                    break;
                }
                case 6: {
                    this.lastAngles = new float[]{this.lastAngles[0], -179.0f};
                    EventPreUpdate.setPitch(-180.0f);
                    this.updateAngles(this.lastAngles[0], -179.0f);
                }
            }
        }
    }
}

