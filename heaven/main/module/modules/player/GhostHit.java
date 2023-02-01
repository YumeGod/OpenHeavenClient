/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacket;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.skid.PathFinder;
import heaven.main.utils.skid.VanillaTeleport;
import heaven.main.utils.vec.SigmaVec3;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class GhostHit
extends Module {
    private Vec3 oldPosition;

    public GhostHit() {
        super("GhostHit", ModuleType.Player);
    }

    @EventHandler
    public void onTick(EventTick e) {
        Minecraft.thePlayer.motionY = 0.0;
        if (!this.isMoving()) {
            Minecraft.thePlayer.motionZ = 0.0;
            Minecraft.thePlayer.motionX = 0.0;
        } else {
            MoveUtils.setSpeed(1.0);
        }
        if (GhostHit.mc.gameSettings.keyBindSneak.isKeyDown()) {
            Minecraft.thePlayer.motionY = -1.0;
        } else if (GhostHit.mc.gameSettings.keyBindJump.isKeyDown()) {
            Minecraft.thePlayer.motionY = 1.0;
        }
    }

    @EventHandler
    public void onPacket(EventPacket e) {
        C02PacketUseEntity packet;
        if (e.getPacket() instanceof C03PacketPlayer) {
            e.cancelEvent();
        }
        if (e.getPacket() instanceof C02PacketUseEntity && (packet = (C02PacketUseEntity)e.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK) {
            e.cancelEvent();
            SigmaVec3 topFrom = new SigmaVec3(this.oldPosition.xCoord, this.oldPosition.yCoord, this.oldPosition.zCoord);
            SigmaVec3 to = new SigmaVec3(packet.getEntityFromWorld((World)Minecraft.theWorld).posX, packet.getEntityFromWorld((World)Minecraft.theWorld).posY, packet.getEntityFromWorld((World)Minecraft.theWorld).posZ);
            List<SigmaVec3> path = PathFinder.computePath(topFrom, to);
            for (SigmaVec3 vec3 : path) {
                this.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(vec3.getX(), vec3.getY(), vec3.getZ(), EventPreUpdate.getYaw(), EventPreUpdate.getPitch(), true));
            }
            this.sendPacketNoEvent(new C02PacketUseEntity(packet.getEntityFromWorld(Minecraft.theWorld), C02PacketUseEntity.Action.ATTACK));
            Collections.reverse(path);
            for (SigmaVec3 vec3 : path) {
                this.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(vec3.getX(), vec3.getY(), vec3.getZ(), EventPreUpdate.getYaw(), EventPreUpdate.getPitch(), true));
            }
        }
    }

    @Override
    public void onEnable() {
        this.oldPosition = Minecraft.thePlayer.getPositionVector();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (this.oldPosition != null) {
            new VanillaTeleport(mc, this.oldPosition.xCoord, this.oldPosition.yCoord, this.oldPosition.zCoord).teleportToXYZ();
        }
        super.onDisable();
    }
}

