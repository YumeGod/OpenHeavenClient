/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.misc;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.AntiBot;
import heaven.main.ui.gui.hud.notification.ClientNotification;
import heaven.main.value.Option;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class HackerDetect
extends Module {
    private final ArrayList<EntityPlayer> hackers = new ArrayList();
    private final Option<Boolean> speed = new Option<Boolean>("Speed", true);
    private final Option<Boolean> flys = new Option<Boolean>("Fly/LongJump", true);
    private final Option<Boolean> step = new Option<Boolean>("Step", false);
    private final Option<Boolean> velocity = new Option<Boolean>("Velocity", false);
    private final Option<Boolean> noslow = new Option<Boolean>("NoSlow", false);
    private final Option<Boolean> nofalldmg = new Option<Boolean>("NoFall", false);

    public HackerDetect() {
        super("HackerDetect", ModuleType.Misc);
        this.addValues(this.speed, this.flys, this.step, this.velocity, this.noslow, this.nofalldmg);
    }

    @EventHandler
    public final void onPreUpdate(EventPreUpdate event) {
        if (Minecraft.thePlayer.ticksExisted <= 105) {
            this.hackers.clear();
            return;
        }
        for (EntityPlayer player : Minecraft.theWorld.playerEntities) {
            if (player == Minecraft.thePlayer || player.ticksExisted < 105 || this.hackers.contains(player) || AntiBot.isBot(player) || player.capabilities.isFlying || player.capabilities.isCreativeMode) continue;
            double playerSpeed = this.getBPS(player);
            if (((Boolean)this.noslow.getValue()).booleanValue() && (player.isUsingItem() || player.isBlocking()) && player.onGround && playerSpeed >= 6.5) {
                ClientNotification.sendClientMessage("HackerDetect", player.getName() + " as using NoSlow", 3000L, ClientNotification.Type.WARNING);
                this.hackers.add(player);
            }
            if (((Boolean)this.speed.getValue()).booleanValue() && player.isSprinting() && (player.moveForward < 0.0f || player.moveForward == 0.0f && player.moveStrafing != 0.0f)) {
                ClientNotification.sendClientMessage("HackerDetect", player.getName() + " as using Speed Hacks", 3000L, ClientNotification.Type.WARNING);
                this.hackers.add(player);
            }
            if (((Boolean)this.flys.getValue()).booleanValue()) {
                if (!Minecraft.theWorld.getCollidingBoundingBoxes(player, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, player.motionY, 0.0)).isEmpty() && player.motionY > 0.0 && playerSpeed > 10.0) {
                    ClientNotification.sendClientMessage("HackerDetect", player.getName() + " as using Fly or LongJump", 3000L, ClientNotification.Type.WARNING);
                    this.hackers.add(player);
                }
            }
            if (((Boolean)this.step.getValue()).booleanValue()) {
                double lastY;
                double yDiff;
                double y = Math.abs((int)player.posY);
                double d = yDiff = y > (lastY = (double)Math.abs((int)player.lastTickPosY)) ? y - lastY : lastY - y;
                if (yDiff > 0.0) {
                    if (Minecraft.thePlayer.onGround && player.motionY == -0.0784000015258789) {
                        ClientNotification.sendClientMessage("HackerDetect", player.getName() + " as using Step Hacks", 3000L, ClientNotification.Type.WARNING);
                        this.hackers.add(player);
                    }
                }
            }
            if (((Boolean)this.velocity.getValue()).booleanValue() && player.hurtTime >= 5 && player.hurtTime <= 8) {
                if (Minecraft.thePlayer.onGround && player.motionY == -0.0784000015258789 && player.motionX == 0.0 && player.motionZ == 0.0) {
                    ClientNotification.sendClientMessage("HackerDetect", player.getName() + " as using Velocity Hacks", 3000L, ClientNotification.Type.WARNING);
                    this.hackers.add(player);
                }
            }
            if (!((Boolean)this.nofalldmg.getValue()).booleanValue() || player.fallDistance != 0.0f || player.motionY >= -0.08 || this.InsideBlock(player) || player.onGround) continue;
            ClientNotification.sendClientMessage("HackerDetect", player.getName() + " as using NoFall Hacks", 3000L, ClientNotification.Type.WARNING);
            this.hackers.add(player);
        }
    }

    public double getBPS(Entity entityIn) {
        double bps = this.getLastDist(entityIn) * 20.0;
        return (double)((int)bps) + bps - (double)((int)bps);
    }

    public double getLastDist(Entity entIn) {
        double xDist = entIn.posX - entIn.prevPosX;
        double zDist = entIn.posZ - entIn.prevPosZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public boolean InsideBlock(EntityPlayer player) {
        for (int x = MathHelper.floor_double(player.getEntityBoundingBox().minX); x < MathHelper.floor_double(player.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(player.getEntityBoundingBox().minY); y < MathHelper.floor_double(player.getEntityBoundingBox().maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(player.getEntityBoundingBox().minZ); z < MathHelper.floor_double(player.getEntityBoundingBox().maxZ) + 1; ++z) {
                    Block block = Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block == null || block instanceof BlockAir) continue;
                    AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.theWorld, new BlockPos(x, y, z), Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)));
                    if (block instanceof BlockHopper) {
                        boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                    }
                    if (boundingBox == null || !player.getEntityBoundingBox().intersectsWith(boundingBox)) continue;
                    return true;
                }
            }
        }
        return false;
    }
}

