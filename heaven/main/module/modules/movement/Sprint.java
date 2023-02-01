/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.movement.Step;
import heaven.main.ui.RenderRotate;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.PlayerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;

public class Sprint
extends Module {
    private final String[] SprintMode = new String[]{"Simple", "AllDirection"};
    public final Mode<String> mode = new Mode("Mode", this.SprintMode, this.SprintMode[0]);
    private final Option<Boolean> sneak = new Option<Boolean>("SneakSprint", true);
    private final Option<Boolean> itemUsing = new Option<Boolean>("UseItemSprint", true);
    private final Option<Boolean> keepair = new Option<Boolean>("KeepAir", false);
    private final Option<Boolean> rot = new Option<Boolean>("Rotations", false);

    public Sprint() {
        super("Sprint", new String[]{"run"}, ModuleType.Movement);
        this.addValues(this.mode, this.sneak, this.itemUsing, this.keepair, this.rot);
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        block12: {
            block11: {
                if (((Boolean)this.keepair.get()).booleanValue()) {
                    if (Minecraft.thePlayer.onGround && Sprint.mc.gameSettings.keyBindBack.isKeyDown()) {
                        Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.3E-6, Minecraft.thePlayer.posZ);
                    }
                }
                if (((Boolean)this.rot.get()).booleanValue() && this.getModule(KillAura.class).getTarget() == null && this.isMoving()) {
                    EventPreUpdate.setYaw((float)Math.toDegrees(MoveUtils.getDirection()));
                    new RenderRotate(event.getYaw(), event.getPitch(), true);
                }
                if (!((Boolean)this.sneak.getValue()).booleanValue()) {
                    if (Minecraft.thePlayer.isSneaking()) {
                        return;
                    }
                }
                if (!((Boolean)this.itemUsing.getValue()).booleanValue()) {
                    if (Minecraft.thePlayer.isUsingItem()) {
                        return;
                    }
                }
                if (!PlayerUtil.isMoving2()) break block11;
                if (Minecraft.thePlayer.isCollidedHorizontally && !Client.instance.getModuleManager().getModuleByClass(Step.class).isEnabled()) break block11;
                if (Minecraft.thePlayer.getFoodStats().getFoodLevel() > 6) break block12;
            }
            Minecraft.thePlayer.setSprinting(false);
            return;
        }
        if (this.mode.isCurrentMode("AllDirection") || MovementInput.moveForward >= 0.8f) {
            Minecraft.thePlayer.setSprinting(true);
        }
    }
}

