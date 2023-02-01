/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.misc.EventChat;
import heaven.main.event.events.world.EventPostUpdate;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.event.events.world.EventTurn;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.render.Rotate;
import heaven.main.ui.RenderRotate;
import heaven.main.utils.timer.TimeHelper;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class BedFucker
extends Module {
    private final String[] breakingMode = new String[]{"Vanilla", "Hypixel"};
    public final Mode<String> mode = new Mode("Mode", this.breakingMode, this.breakingMode[0]);
    private final Numbers<Double> range = new Numbers<Double>("Range", 3.0, 1.0, 6.0, 1.0);
    private final Numbers<Double> delay = new Numbers<Double>("Delay", 100.0, 100.0, 1000.0, 100.0);
    private final Option<Boolean> swing = new Option<Boolean>("NoSwing", false);
    private final Option<Boolean> noHit = new Option<Boolean>("OnlyNoHits", false);
    public static BlockPos self;
    public static BlockPos ready;
    public static BlockPos fucking;
    private final TimeHelper timer = new TimeHelper();

    public BedFucker() {
        super("BedFucker", new String[]{"fucker"}, ModuleType.World);
        this.addValues(this.mode, this.range, this.delay, this.swing, this.noHit);
    }

    @Override
    public void onEnable() {
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        ready = null;
        fucking = null;
    }

    @EventHandler
    public void onTurn(EventTurn e) {
        if (self != null) {
            self = null;
        }
    }

    @EventHandler
    public void onChat(EventChat e) {
        if (e.getMessage().contains("You can't destroy your own bed!") && ready != null) {
            self = ready;
        }
    }

    @EventHandler
    private void onPreUpdate(EventPreUpdate e) {
        int reach;
        this.setSuffix((Serializable)this.mode.getValue());
        if (((Boolean)this.noHit.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(KillAura.class).isEnabled() && this.getModule(KillAura.class).getTarget() != null) {
            return;
        }
        for (int y = reach = ((Double)this.range.getValue()).intValue(); y >= -reach; --y) {
            for (int x = -reach; x <= reach; ++x) {
                for (int z = -reach; z <= reach; ++z) {
                    boolean confirm = x != 0 || z != 0;
                    if (Minecraft.thePlayer.isSneaking()) {
                        boolean bl = confirm = !confirm;
                    }
                    if (!confirm) continue;
                    BlockPos pos = new BlockPos(Minecraft.thePlayer.posX + (double)x, Minecraft.thePlayer.posY + (double)y, Minecraft.thePlayer.posZ + (double)z);
                    if (BedFucker.getFacingDirection(pos) == null) continue;
                    if (!BedFucker.blockChecks(Minecraft.theWorld.getBlockState(pos).getBlock())) continue;
                    if (!(Minecraft.thePlayer.getDistance(Minecraft.thePlayer.posX + (double)x, Minecraft.thePlayer.posY + (double)y, Minecraft.thePlayer.posZ + (double)z) < (double)Minecraft.playerController.getBlockReachDistance() - 0.5)) continue;
                    float[] rotations = BedFucker.getBlockRotations(Minecraft.thePlayer.posX + (double)x, Minecraft.thePlayer.posY + (double)y, Minecraft.thePlayer.posZ + (double)z);
                    EventPreUpdate.setYaw(rotations[0]);
                    EventPreUpdate.setPitch(rotations[1]);
                    if (Client.instance.getModuleManager().getModuleByClass(Rotate.class).isEnabled() && !Rotate.fucker.isCurrentMode("Off")) {
                        new RenderRotate(rotations[0]);
                    }
                    fucking = pos;
                    ready = pos;
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPostUpdate(EventPostUpdate e2) {
        if (((Boolean)this.noHit.getValue()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(KillAura.class).isEnabled() && this.getModule(KillAura.class).getTarget() != null) {
            return;
        }
        if (!this.timer.isDelayComplete(((Double)this.delay.getValue()).longValue())) {
            return;
        }
        if (!BedFucker.blockChecks(Minecraft.theWorld.getBlockState(fucking).getBlock())) {
            this.timer.reset();
        }
        if (fucking != null) {
            if (fucking == self) {
                fucking = null;
                return;
            }
            if (Minecraft.playerController.blockHitDelay > 1) {
                Minecraft.playerController.blockHitDelay = 1;
            }
            if (!((Boolean)this.swing.getValue()).booleanValue()) {
                Minecraft.thePlayer.swingItem();
            } else {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            }
            EnumFacing direction = BedFucker.getFacingDirection(fucking);
            if (direction != null) {
                Minecraft.playerController.onPlayerDamageBlock(fucking, direction);
            }
            fucking = null;
        }
    }

    private static boolean blockChecks(Block block) {
        return block == Blocks.bed;
    }

    private static float[] getBlockRotations(double x, double y, double z) {
        double var4 = x - Minecraft.thePlayer.posX + 0.5;
        double var5 = z - Minecraft.thePlayer.posZ + 0.5;
        double var6 = y - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() - 1.0);
        double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        float var8 = (float)(Math.atan2(var5, var4) * 180.0 / Math.PI) - 90.0f;
        return new float[]{var8, (float)(-(Math.atan2(var6, var7) * 180.0 / Math.PI))};
    }

    private static EnumFacing getFacingDirection(BlockPos pos) {
        EnumFacing direction = null;
        if (!Minecraft.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.UP;
        } else if (!Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.DOWN;
        } else if (!Minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.EAST;
        } else if (!Minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.WEST;
        } else if (!Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.SOUTH;
        } else if (!Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.NORTH;
        }
        MovingObjectPosition rayResult = Minecraft.theWorld.rayTraceBlocks(new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ), new Vec3((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5));
        return rayResult != null && rayResult.getBlockPos() == pos ? rayResult.sideHit : direction;
    }
}

