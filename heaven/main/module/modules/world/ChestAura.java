/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.event.events.world.EventTurn;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.render.Rotate;
import heaven.main.ui.RenderRotate;
import heaven.main.utils.timer.TimeHelper;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class ChestAura
extends Module {
    private final ArrayList<BlockPos> pos = new ArrayList();
    private final TimeHelper timer = new TimeHelper();
    public static BlockPos lastBlock;
    private BlockPos blockCorner;
    private final Numbers<Double> delay = new Numbers<Double>("Delay", 100.0, 0.0, 1000.0, 10.0);
    private final Numbers<Double> reach = new Numbers<Double>("Reach", 4.0, 1.0, 7.0, 0.1);
    private final Option<Boolean> rayTrace = new Option<Boolean>("RayTrace", true);
    private final Option<Boolean> once = new Option<Boolean>("Once", true);
    private final Option<Boolean> swing = new Option<Boolean>("Swing", true);
    private final Option<Boolean> fakeswing = new Option<Boolean>("FakeSwing", true);
    private final ArrayList<BlockPos> opened = new ArrayList();

    public ChestAura() {
        super("ChestAura", ModuleType.World);
        this.addValues(this.delay, this.reach, this.rayTrace, this.once, this.swing, this.fakeswing);
    }

    @EventHandler
    public void onTurn(EventTurn e) {
        this.opened.clear();
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        this.getNextChest();
        if (lastBlock != null) {
            if (Minecraft.thePlayer.getDistance(lastBlock.getX(), lastBlock.getY(), lastBlock.getZ()) > (Double)this.reach.getValue()) {
                lastBlock = null;
            }
            if (ChestAura.mc.currentScreen instanceof GuiChest) {
                this.pos.add(lastBlock);
                lastBlock = null;
            }
        }
        if (lastBlock == null) {
            this.blockCorner = null;
        }
        if (ChestAura.mc.currentScreen == null && ChestAura.isAllowed()) {
            BlockPos chest;
            BlockPos blockPos = chest = lastBlock != null ? lastBlock : this.getNextChest();
            if (((Boolean)this.once.getValue()).booleanValue() && this.opened.contains(chest)) {
                return;
            }
            if (chest != null && this.blockCorner != null) {
                float[] rot = ChestAura.getRotationsNeededBlock(chest.getX(), chest.getY(), chest.getZ());
                EventPreUpdate.setYaw(rot[0]);
                EventPreUpdate.setPitch(rot[1]);
                if (Client.instance.getModuleManager().getModuleByClass(Rotate.class).isEnabled() && !Rotate.chest.isCurrentMode("Off")) {
                    new RenderRotate(rot[0], rot[1]);
                }
                if (this.timer.isDelayComplete(((Double)this.delay.getValue()).intValue())) {
                    if (((Boolean)this.swing.getValue()).booleanValue()) {
                        Minecraft.thePlayer.swingItem();
                        if (((Boolean)this.fakeswing.getValue()).booleanValue()) {
                            Minecraft.thePlayer.swingItem();
                            Minecraft.thePlayer.swingItem();
                        }
                    } else {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    }
                    Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem(), chest, EnumFacing.DOWN, new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ));
                    this.timer.reset();
                    lastBlock = chest;
                    this.opened.add(chest);
                }
            }
        }
    }

    private static boolean isAllowed() {
        return !Client.instance.getModuleManager().getModuleByClass(KillAura.class).isEnabled() || KillAura.target != null;
    }

    private BlockPos getNextChest() {
        BlockPos chestPos;
        Iterator<BlockPos> positions = BlockPos.getAllInBox(Minecraft.thePlayer.getPosition().subtract(new Vec3i((Double)this.reach.getValue(), (Double)this.reach.getValue(), (Double)this.reach.getValue())), Minecraft.thePlayer.getPosition().add(new Vec3i((Double)this.reach.getValue(), (Double)this.reach.getValue(), (Double)this.reach.getValue()))).iterator();
        while ((chestPos = positions.next()) != null) {
            BlockPos corner;
            if (!(Minecraft.theWorld.getBlockState(chestPos.add(0, 1, 0)).getBlock() instanceof BlockAir)) continue;
            if (!(Minecraft.theWorld.getBlockState(chestPos).getBlock() instanceof BlockChest) || this.pos.contains(chestPos)) continue;
            BlockPos blockPos = !((Boolean)this.rayTrace.getValue()).booleanValue() ? chestPos : (corner = this.getBlockCorner(new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ), chestPos));
            if (corner == null) continue;
            this.blockCorner = corner;
            return chestPos;
        }
        return null;
    }

    public BlockPos getBlockCorner(BlockPos start, BlockPos end) {
        for (int x = 0; x <= 1; ++x) {
            for (int y = 0; y <= 1; ++y) {
                for (int z = 0; z <= 1; ++z) {
                    BlockPos pos = new BlockPos(end.getX() + x, end.getY() + y, end.getZ() + z);
                    if (ChestAura.isBlockBetween(start, pos)) continue;
                    return pos;
                }
            }
        }
        return null;
    }

    private static float[] getRotationsNeededBlock(double x, double y, double z) {
        double diffX = x + 0.5 - Minecraft.thePlayer.posX;
        double diffZ = z + 0.5 - Minecraft.thePlayer.posZ;
        double diffY = y + 0.5 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        float[] fArray = new float[2];
        fArray[0] = Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw);
        fArray[1] = Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch);
        return fArray;
    }

    private static boolean isBlockBetween(BlockPos start, BlockPos end) {
        int startX = start.getX();
        int startY = start.getY();
        int startZ = start.getZ();
        int endX = end.getX();
        int endY = end.getY();
        int endZ = end.getZ();
        double diffX = endX - startX;
        double diffY = endY - startY;
        double diffZ = endZ - startZ;
        double x = startX;
        double y = startY;
        double z = startZ;
        int STEPS = (int)Math.max(Math.abs(diffX), Math.max(Math.abs(diffY), Math.abs(diffZ))) << 2;
        for (int i = 0; i < STEPS - 1; ++i) {
            if ((x += diffX / (double)STEPS) == (double)endX && (y += diffY / (double)STEPS) == (double)endY && (z += diffZ / (double)STEPS) == (double)endZ) continue;
            BlockPos pos = new BlockPos(x, y, z);
            Block block = Minecraft.theWorld.getBlockState(pos).getBlock();
            if (block.getMaterial() == Material.air || block.getMaterial() == Material.water || block instanceof BlockVine || block instanceof BlockLadder) continue;
            return true;
        }
        return false;
    }
}

