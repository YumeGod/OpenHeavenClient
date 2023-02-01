/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventMove;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.movement.Fly;
import heaven.main.module.modules.movement.Longjump;
import heaven.main.module.modules.world.Scaffold;
import heaven.main.utils.MoveUtils;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.io.Serializable;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class AntiVoid
extends Module {
    private final String[] fallmode = new String[]{"Packet", "Watchdog", "Bounce", "Teleport", "Flag"};
    private final Mode<String> mode = new Mode("Mode", this.fallmode, this.fallmode[0]);
    private final Numbers<Double> fallDistance = new Numbers<Double>("FallDistance", 10.0, 1.0, 20.0, 1.0);
    private double prevX;
    private double prevY;
    private double prevZ;

    public AntiVoid() {
        super("AntiFall", ModuleType.Movement);
        this.addValues(this.mode, this.fallDistance);
    }

    @Override
    public void onDisable() {
        this.prevX = 0.0;
        this.prevY = 0.0;
        this.prevZ = 0.0;
    }

    @EventHandler
    private void onMove(EventMove e) {
        boolean isInVoid;
        this.setSuffix((Serializable)this.mode.get());
        EntityPlayerSP player = Minecraft.thePlayer;
        boolean bl = isInVoid = !this.isBlockUnder();
        if (!this.moduleCheck()) {
            return;
        }
        if (!isInVoid && (double)player.fallDistance < 1.0 && player.onGround) {
            this.prevX = player.prevPosX;
            this.prevY = player.prevPosY;
            this.prevZ = player.prevPosZ;
        }
        if (isInVoid) {
            if (this.mode.is("Teleport") && this.fallDistanceCheck()) {
                player.setPositionAndUpdate(this.prevX, this.prevY, this.prevZ);
            }
            if (this.mode.is("Watchdog") && this.fallDistanceCheck()) {
                if (Minecraft.thePlayer.ticksExisted % 2 == 0) {
                    EventMove.setX(e.getX() + Math.max((double)MoveUtils.getSpeed(), 0.2 + Math.random() / 100.0));
                    EventMove.setZ(e.getZ() + Math.max((double)MoveUtils.getSpeed(), Math.random() / 100.0));
                } else {
                    EventMove.setX(e.getX() - Math.max((double)MoveUtils.getSpeed(), 0.2 + Math.random() / 100.0));
                    EventMove.setZ(e.getZ() - Math.max((double)MoveUtils.getSpeed(), Math.random() / 100.0));
                }
            }
            if (this.mode.is("Flag") && this.fallDistanceCheck()) {
                player.motionY += 0.1;
                player.fallDistance = 0.0f;
            }
            if (this.mode.is("Bounce") && !player.onGround && !player.isCollidedVertically && player.fallDistance > 4.0f && player.prevPosY < this.prevY) {
                player.motionY += 0.23;
            }
            if (this.mode.is("Packet") && this.fallDistanceCheck()) {
                this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY + (double)player.fallDistance, player.posZ, false));
            }
        }
    }

    private boolean fallDistanceCheck() {
        EntityPlayerSP player = Minecraft.thePlayer;
        if (!player.onGround && !player.isCollidedVertically) {
            return (double)player.fallDistance > (Double)this.fallDistance.get();
        }
        return false;
    }

    private boolean moduleCheck() {
        EntityPlayerSP player = Minecraft.thePlayer;
        if (player.posY > 0.0) {
            return !this.isEnabled((Class<? extends Module>)Scaffold.class);
        }
        return !this.isEnabled((Class<? extends Module>)Fly.class) && !this.isEnabled((Class<? extends Module>)Longjump.class);
    }

    private boolean isBlockUnder() {
        for (int i = (int)(Minecraft.thePlayer.posY - 1.0); i > 0; --i) {
            BlockPos pos = new BlockPos(Minecraft.thePlayer.posX, (double)i, Minecraft.thePlayer.posZ);
            if (Minecraft.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) continue;
            return true;
        }
        return false;
    }

    public boolean isInVoid() {
        for (int i = 0; i <= 128; ++i) {
            if (!MoveUtils.isOnGround(i)) continue;
            return false;
        }
        return true;
    }
}

