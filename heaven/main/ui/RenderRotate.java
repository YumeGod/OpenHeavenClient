/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui;

import heaven.main.Client;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.render.FPSBoost;
import heaven.main.module.modules.render.Rotate;
import heaven.main.module.modules.world.BedFucker;
import heaven.main.module.modules.world.ChestAura;
import heaven.main.module.modules.world.Scaffold;
import net.minecraft.client.Minecraft;

public class RenderRotate {
    private final Minecraft mc = Minecraft.getMinecraft();

    private boolean shouldRotate(String mode) {
        if (Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled() && Rotate.scaffold.is(mode)) {
            return true;
        }
        if (Client.instance.getModuleManager().getModuleByClass(KillAura.class).isEnabled() && Rotate.ka.is(mode) && KillAura.target != null) {
            return true;
        }
        if (Client.instance.getModuleManager().getModuleByClass(ChestAura.class).isEnabled() && Rotate.chest.is(mode) && ChestAura.lastBlock != null) {
            return true;
        }
        return Client.instance.getModuleManager().getModuleByClass(BedFucker.class).isEnabled() && Rotate.fucker.is(mode) && BedFucker.fucking != null && BedFucker.ready != null && BedFucker.self != null;
    }

    private boolean fpsBoostBooleanCheck() {
        if (Client.instance.getModuleManager().getModuleByClass(FPSBoost.class).isEnabled() && ((Boolean)FPSBoost.lazyStrategy.get()).booleanValue()) {
            return this.mc.gameSettings.thirdPersonView == 0;
        }
        return false;
    }

    public RenderRotate(float yaw) {
        if (this.shouldRotate("Body")) {
            Minecraft.thePlayer.renderYawOffset = yaw;
            Minecraft.thePlayer.rotationYawHead = yaw;
        }
        if (this.shouldRotate("Head")) {
            Minecraft.thePlayer.rotationYawHead = yaw;
        }
        if (this.shouldRotate("Bitch")) {
            Minecraft.thePlayer.renderYawOffset = Minecraft.thePlayer.rotationYaw + Rotate.yaw;
            Minecraft.thePlayer.rotationYawHead = Minecraft.thePlayer.rotationYaw + Rotate.yaw;
        }
    }

    public RenderRotate(float yaw, float pitch, boolean noIo) {
        if (this.fpsBoostBooleanCheck()) {
            return;
        }
        Minecraft.thePlayer.renderYawOffset = yaw;
        Minecraft.thePlayer.rotationYawHead = yaw;
        Minecraft.thePlayer.rotationPitchHead = pitch;
    }

    public RenderRotate(float yaw, float pitch) {
        if (this.fpsBoostBooleanCheck()) {
            return;
        }
        if (this.shouldRotate("Body")) {
            Minecraft.thePlayer.renderYawOffset = yaw;
            Minecraft.thePlayer.rotationYawHead = yaw;
            Minecraft.thePlayer.rotationPitchHead = pitch;
        }
        if (this.shouldRotate("Head")) {
            Minecraft.thePlayer.rotationYawHead = yaw;
            Minecraft.thePlayer.rotationPitchHead = pitch;
        }
        if (this.shouldRotate("Bitch")) {
            Minecraft.thePlayer.renderYawOffset = Minecraft.thePlayer.rotationYaw + Rotate.yaw;
            Minecraft.thePlayer.rotationYawHead = Minecraft.thePlayer.rotationYaw + Rotate.yaw;
            Minecraft.thePlayer.rotationPitchHead = pitch;
        }
    }
}

