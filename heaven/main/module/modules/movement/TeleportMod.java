/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventMove;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.vec.pathfinding.CustomVec3;
import heaven.main.utils.vec.pathfinding.PathfindingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class TeleportMod
extends Module {
    public static float x;
    public static float y;
    public static float z;
    public static boolean isTPPlayer;
    public static String playername;
    private CustomVec3 target;
    boolean tp;

    public TeleportMod() {
        super("TeleportMod", new String[]{"teleport"}, ModuleType.Movement);
        this.setRemoved(true);
    }

    @Override
    public void onEnable() {
        if (Minecraft.thePlayer == null) {
            return;
        }
        this.tp = true;
        Minecraft.thePlayer.stepHeight = 0.0f;
        Minecraft.thePlayer.motionX = 0.0;
        Minecraft.thePlayer.motionZ = 0.0;
    }

    @Override
    public void onDisable() {
        TeleportMod.mc.timer.timerSpeed = 1.0f;
        Minecraft.thePlayer.stepHeight = 0.625f;
        Minecraft.thePlayer.motionX = 0.0;
        Minecraft.thePlayer.motionZ = 0.0;
        isTPPlayer = false;
        playername = null;
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (isTPPlayer) {
            if (Minecraft.theWorld.getPlayerEntityByName(playername) == null) {
                this.setEnabled(false);
                return;
            }
            x = (float)Minecraft.theWorld.getPlayerEntityByName((String)TeleportMod.playername).posX;
            y = (float)Minecraft.theWorld.getPlayerEntityByName((String)TeleportMod.playername).posY + 3.0f;
            z = (float)Minecraft.theWorld.getPlayerEntityByName((String)TeleportMod.playername).posZ;
        }
        if (this.tp) {
            double lastY = Minecraft.thePlayer.posY;
            double downY = 0.0;
            for (CustomVec3 vec3 : PathfindingUtils.computePath(new CustomVec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ), new CustomVec3(x, y, z))) {
                if (vec3.getY() < lastY) {
                    downY += lastY - vec3.getY();
                }
                if (downY > 2.5) {
                    downY = 0.0;
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), true));
                } else {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), false));
                }
                lastY = vec3.getY();
            }
            Minecraft.thePlayer.setPosition(x, y, z);
            this.setEnabled(false);
        }
    }

    @EventHandler
    public void onMove(EventMove event) {
        MoveUtils.setSpeed(0.0);
        Minecraft.thePlayer.motionY = 0.0;
        event.y = 0.0;
    }

    @EventHandler
    public final void onSendPacket(EventPacketSend event) {
        if (!this.tp && event.getPacket() instanceof C03PacketPlayer) {
            event.setCancelled(true);
        }
    }
}

