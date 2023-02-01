/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.Rotate;
import heaven.main.ui.RenderRotate;
import heaven.main.utils.math.RotationUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class AntiFireball
extends Module {
    private final Option<Boolean> swing = new Option<Boolean>("Swing", true);
    private final Option<Boolean> rot = new Option<Boolean>("Rotations", true);
    private final Numbers<Double> range = new Numbers<Double>("Range", 4.2, 1.0, 6.0, 0.1);
    boolean canSee;

    public AntiFireball() {
        super("AntiFireball", new String[]{"afb"}, ModuleType.Combat);
        this.addValues(this.rot, this.swing, this.range);
    }

    @Override
    public void onDisable() {
        this.canSee = false;
        super.onDisable();
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        this.setSuffix((Serializable)this.range.getValue());
        for (Entity entity : Minecraft.theWorld.loadedEntityList) {
            if (entity instanceof EntityFireball) {
                double distance = Minecraft.thePlayer.getDistanceToEntity(entity);
                if (!(distance <= (Double)this.range.getValue())) continue;
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                if (((Boolean)this.swing.getValue()).booleanValue()) {
                    Minecraft.thePlayer.swingItem();
                }
                if (!((Boolean)this.rot.getValue()).booleanValue()) continue;
                float[] rotation = RotationUtil.getRotations(entity);
                EventPreUpdate.setYaw(rotation[0]);
                EventPreUpdate.setPitch(rotation[1]);
                if (!Client.instance.getModuleManager().getModuleByClass(Rotate.class).isEnabled() || Rotate.fire.isCurrentMode("Off") || !this.canSee) continue;
                new RenderRotate(rotation[0], rotation[1]);
                continue;
            }
            this.canSee = false;
        }
    }
}

