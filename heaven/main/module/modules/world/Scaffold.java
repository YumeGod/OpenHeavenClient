/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ThreadLocalRandom
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.world;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.drakApi.MotionUpdateEvent;
import heaven.main.event.events.misc.LoopEvent;
import heaven.main.event.events.move.StrafeEvent;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventJump;
import heaven.main.event.events.world.EventMove;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.module.modules.movement.FastFall;
import heaven.main.module.modules.movement.Longjump;
import heaven.main.module.modules.movement.Speed;
import heaven.main.module.modules.render.HUD;
import heaven.main.module.modules.render.Rotate;
import heaven.main.module.modules.world.GameSpeed;
import heaven.main.ui.RenderRotate;
import heaven.main.ui.font.CFontRenderer;
import heaven.main.utils.BlockUtils;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.PlayerUtil;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.math.RotationUtil;
import heaven.main.utils.render.RenderUtil;
import heaven.main.utils.render.RenderUtils;
import heaven.main.utils.render.color.Colors;
import heaven.main.utils.skid.RotationUtils;
import heaven.main.utils.timer.TimeHelper;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import io.netty.util.internal.ThreadLocalRandom;
import java.awt.Color;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Scaffold
extends Module {
    private final String[] rots = new String[]{"None", "Watchdog", "AAC", "AAC2", "NCP"};
    private final String[] scmode = new String[]{"Normal", "SlowSpeed"};
    private final String[] towermodes = new String[]{"NCP", "AAC3.6.4"};
    private final String[] placeTiming1 = new String[]{"Pre", "Post", "Motion"};
    private final Mode<String> scaffoldmode = new Mode("Mode", this.scmode, this.scmode[0]);
    private final Mode<String> placeTiming = new Mode("PlaceTiming", this.placeTiming1, this.placeTiming1[0]);
    private static final Option<Boolean> tower = new Option<Boolean>("Tower", true);
    private static final Option<Boolean> towerMove = new Option<Boolean>("TowerMove", true, tower::get);
    private final Mode<String> towermode = new Mode("TowerMode", this.towermodes, this.towermodes[0], tower::get);
    public final Mode<String> rot = new Mode("Rotations", this.rots, this.rots[0]);
    private final Numbers<Double> maxblockdelay = new Numbers<Double>("MaxPlaceDelay", 0.0, 0.0, 500.0, 0.1);
    private final Numbers<Double> minblockdelay = new Numbers<Double>("MinPlaceDelay", 0.0, 0.0, 500.0, 0.1);
    private final Numbers<Double> minturnspeed = new Numbers<Double>("MinTurnSpeed", 120.0, 0.0, 180.0, 0.1);
    private final Numbers<Double> maxturnspeed = new Numbers<Double>("MaxTurnSpeed", 180.0, 0.0, 180.0, 0.1);
    private final Option<Boolean> timerboost = new Option<Boolean>("TimerBoost", false);
    private final Numbers<Double> timermax = new Numbers<Double>("TimerMax", 0.98, 0.4, 5.0, 0.01, this.timerboost::get);
    private final Numbers<Double> timermin = new Numbers<Double>("TimerMin", 1.17, 0.4, 5.0, 0.01, this.timerboost::get);
    private final Numbers<Double> timertick = new Numbers<Double>("TimerTick", 77.2, 0.0, 200.0, 1.0, this.timerboost::get);
    private final Option<Boolean> silent = new Option<Boolean>("AutoBlock", true);
    private final Option<Boolean> sprint = new Option<Boolean>("Sprint", true);
    private final Mode<String> sprintMode = new Mode("SprintMode", new String[]{"Normal", "Legit", "SwitchSprint", "SwitchSprint2"}, "Normal", this.sprint::get);
    private final Option<Boolean> moveControl = new Option<Boolean>("MoveControl", false);
    private final Option<Boolean> swing = new Option<Boolean>("Swing", true);
    private final Option<Boolean> pick = new Option<Boolean>("PickBlock", true);
    public static final Option<Boolean> down = new Option<Boolean>("DownPlace", false);
    private final Mode<String> rayCast = new Mode("RayCast", new String[]{"None", "Normal"}, "None");
    private final Option<Boolean> sameY = new Option<Boolean>("SameY", false);
    private final String[] sameyModes = new String[]{"Normal", "OnlySpeed", "AutoJump"};
    private final Mode<String> sameYMode = new Mode("SameYs", this.sameyModes, this.sameyModes[0], this.sameY::get);
    public static final Option<Boolean> StopSpeed = new Option<Boolean>("StopSpeed", false);
    private final Option<Boolean> combatspoof = new Option<Boolean>("CombatSpoof", false);
    public static final Option<Boolean> safe = new Option<Boolean>("SafeWalk", false);
    private final Option<Boolean> Counter = new Option<Boolean>("RenderCount", true);
    private final Option<Boolean> displays = new Option<Boolean>("RangeDisplay", false);
    private final Option<Boolean> mark = new Option<Boolean>("Mark", false);
    private double y;
    private final TimerUtil placedelay = new TimerUtil();
    private final TimeHelper isScaffolding = new TimeHelper();
    private int theSlot;
    private static float lastYaw;
    private static float lastPitch;
    private float blockPitch;
    private final List<Block> invalid;
    private final List<Block> validBlocks2 = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava);
    private final List<Block> blacklist = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.wooden_slab, Blocks.wooden_slab, Blocks.chest, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.skull, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.tnt, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.trapped_chest, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence, Blocks.activator_rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.redstone_torch, Blocks.acacia_stairs, Blocks.birch_stairs, Blocks.brick_stairs, Blocks.dark_oak_stairs, Blocks.jungle_stairs, Blocks.nether_brick_stairs, Blocks.oak_stairs, Blocks.quartz_stairs, Blocks.red_sandstone_stairs, Blocks.sandstone_stairs, Blocks.spruce_stairs, Blocks.stone_brick_stairs, Blocks.stone_stairs, Blocks.wooden_slab, Blocks.double_wooden_slab, Blocks.stone_slab, Blocks.double_stone_slab, Blocks.stone_slab2, Blocks.double_stone_slab2, Blocks.web, Blocks.gravel, Blocks.daylight_detector_inverted, Blocks.daylight_detector, Blocks.soul_sand, Blocks.piston, Blocks.piston_extension, Blocks.piston_head, Blocks.sticky_piston, Blocks.iron_trapdoor, Blocks.ender_chest, Blocks.end_portal, Blocks.end_portal_frame, Blocks.standing_banner, Blocks.wall_banner, Blocks.deadbush, Blocks.slime_block, Blocks.acacia_fence_gate, Blocks.birch_fence_gate, Blocks.dark_oak_fence_gate, Blocks.jungle_fence_gate, Blocks.spruce_fence_gate, Blocks.oak_fence_gate);
    private double xDist;
    int towertick;
    boolean isTower;
    private int oldBlockUse;
    private int offGroundTicks;
    private int keepRotateTicks;
    public Vec3 hitVec;

    public Scaffold() {
        super("Scaffold", ModuleType.World);
        this.addValues(this.scaffoldmode, this.towermode, this.rot, this.placeTiming, this.sameYMode, this.maxblockdelay, this.minblockdelay, this.maxturnspeed, this.minturnspeed, this.silent, this.swing, this.sprint, this.sprintMode, tower, towerMove, down, this.sameY, this.pick, this.rayCast, this.moveControl, StopSpeed, this.combatspoof, this.timerboost, this.timermax, this.timermin, this.timertick, safe, this.Counter, this.displays, this.mark);
        this.invalid = Arrays.asList(Blocks.anvil, Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.skull, Blocks.trapped_chest, Blocks.flowing_lava, Blocks.chest, Blocks.enchanting_table, Blocks.ender_chest, Blocks.crafting_table);
        this.theSlot = -1;
    }

    @Override
    public void onEnable() {
        this.keepRotateTicks = 0;
        this.isScaffolding.reset();
        if (!((Boolean)this.silent.get()).booleanValue()) {
            this.oldBlockUse = Minecraft.thePlayer.inventory.currentItem;
        }
        this.y = Minecraft.thePlayer.posY;
        lastYaw = Minecraft.thePlayer.rotationYaw;
        lastPitch = Minecraft.thePlayer.rotationPitch;
        this.blockPitch = 80.0f;
        Scaffold.mc.timer.timerSpeed = 1.0f;
    }

    @Override
    public void onDisable() {
        if (this.theSlot != Minecraft.thePlayer.inventory.currentItem) {
            mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
        }
        if (!((Boolean)this.silent.get()).booleanValue()) {
            Minecraft.thePlayer.inventory.currentItem = this.oldBlockUse;
        }
        if (!Minecraft.thePlayer.movementInput.sneak) {
            this.sendPacketNoEvent(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        this.isScaffolding.reset();
        lastYaw = Minecraft.thePlayer.rotationYaw;
        lastPitch = Minecraft.thePlayer.rotationPitch;
        this.blockPitch = Minecraft.thePlayer.rotationPitch;
        Scaffold.mc.timer.timerSpeed = 1.0f;
    }

    @EventHandler
    private void render(EventRender2D e) {
        if (((Boolean)this.Counter.get()).booleanValue() && Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled()) {
            String var10001 = Integer.toString(this.getBlockCount()) + (Object)((Object)EnumChatFormatting.GRAY) + " Blocks";
            RenderItem ir = new RenderItem(mc.getTextureManager(), Scaffold.mc.modelManager);
            CFontRenderer font = Client.instance.FontLoaders.Comfortaa18;
            int sr = ScaledResolution.getScaledWidth() / 2 + 1;
            font.drawStringWithShadow(var10001, (float)sr - (float)font.getStringWidth(Integer.toString(this.getBlockCount())) / 2.0f, ScaledResolution.getScaledHeight() / 2 + 12, -1);
            RenderHelper.enableGUIStandardItemLighting();
            ir.renderItemIntoGUI(Minecraft.thePlayer.inventory.getStackInSlot(this.theSlot), sr - font.getStringHeight() - 15, ScaledResolution.getScaledHeight() / 2 + 7);
            RenderHelper.disableStandardItemLighting();
        }
    }

    @EventHandler
    public void onStrafe(StrafeEvent event) {
        if (((Boolean)this.moveControl.get()).booleanValue()) {
            event.setCancelled(true);
            Scaffold.silentRotationStrafe(event, lastYaw);
        }
    }

    @EventHandler
    public void onJump(EventJump e) {
        if (this.sameYMode.is("AutoJump") && this.isEnabled(Speed.class, Longjump.class) && this.isMoving()) {
            e.cancelEvent();
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        block22: {
            block23: {
                this.setSuffix((Serializable)this.scaffoldmode.get());
                if (((Boolean)this.combatspoof.get()).booleanValue()) {
                    KillAura.target = null;
                }
                if (!((Boolean)this.timerboost.get()).booleanValue()) break block22;
                if (!Client.instance.getModuleManager().getModuleByClass(GameSpeed.class).isEnabled()) break block23;
                if (Client.instance.getModuleManager().getModuleByClass(FastFall.class).isEnabled()) break block22;
                if (!(Minecraft.thePlayer.fallDistance > 1.0f)) break block22;
            }
            if (this.xDist > 200.0) {
                this.xDist = 0.0;
            }
            if (!MoveUtils.isMovingKeyBindingActive()) ** GOTO lbl-1000
            v0 = this.xDist;
            this.xDist = v0 + 1.0;
            if (v0 > (double)((Double)this.timertick.get()).intValue() && MoveUtils.isMovingKeyBindingActive() && ((Double)this.timermax.get()).floatValue() > ((Double)this.timermin.get()).floatValue()) {
                Scaffold.mc.timer.timerSpeed = (float)(((Double)this.timermax.get()).floatValue() > ((Double)this.timermin.get()).floatValue() ? ThreadLocalRandom.current().nextDouble((double)((Double)this.timermin.get()).floatValue(), (double)((Double)this.timermax.get()).floatValue()) : (double)((Double)this.timermin.get()).floatValue() - Math.random() / 10.0);
            } else if (!Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                Scaffold.mc.timer.timerSpeed = 1.0f;
            }
            if ((double)Minecraft.thePlayer.fallDistance > 3.0) {
                Scaffold.mc.timer.timerSpeed = 1.0f;
            }
        }
        this.doRotate(event);
        if (Minecraft.thePlayer.moving()) {
            if (this.sprintMode.is("Normal") || this.sprintMode.is("SwitchSprint") || this.sprintMode.is("SwitchSprint2")) {
                Minecraft.thePlayer.setSprinting((Boolean)this.sprint.get());
            } else if (this.sprintMode.is("Legit")) {
                if (Math.abs(MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationYaw) - MathHelper.wrapAngleTo180_float(Scaffold.lastYaw)) > 90.0f) {
                    Scaffold.mc.gameSettings.keyBindSprint.pressed = false;
                    Minecraft.thePlayer.setSprinting(false);
                }
            }
        }
        this.moveBlock();
        if (((Boolean)Scaffold.tower.get()).booleanValue()) {
            blockPos = new BlockPos(Minecraft.thePlayer.posX, this.y - 1.0, Minecraft.thePlayer.posZ);
            blockData = this.getBlockData(blockPos);
            if (blockData != null) {
                if (this.towermode.is("NCP")) {
                    this.TowerCore();
                    if (!Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                        if (!Minecraft.thePlayer.moving() && this.towertick != 0) {
                            this.towertick = 0;
                        }
                    }
                }
                if (this.towermode.is("AAC3.6.4") && Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                    if (!this.isMoving()) {
                        MoveUtils.stop();
                    }
                    if (Minecraft.thePlayer.ticksExisted % 4 == 1) {
                        Minecraft.thePlayer.motionY = 0.4195464;
                        Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX - 0.035, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
                    } else if (Minecraft.thePlayer.ticksExisted % 4 == 0) {
                        Minecraft.thePlayer.motionY = -0.5;
                        Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX + 0.035, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
                    }
                }
            }
        }
        if (this.CanDownPut()) {
            Scaffold.mc.gameSettings.keyBindSneak.pressed = false;
        }
    }

    public double[] getExpandCoords(double y) {
        BlockPos underPos = new BlockPos(Minecraft.thePlayer.posX, y, Minecraft.thePlayer.posZ);
        Block underBlock = Minecraft.theWorld.getBlockState(underPos).getBlock();
        MovementInput movementInput = Minecraft.thePlayer.movementInput;
        float forward = movementInput.getMoveForward();
        float strafe = movementInput.getMoveStrafe();
        float yaw = Minecraft.thePlayer.rotationYaw;
        double xCalc = -999.0;
        double zCalc = -999.0;
        double dist = 0.0;
        double expandDist = 0.0;
        while (!this.isAirBlock(underBlock)) {
            xCalc = Minecraft.thePlayer.posX;
            zCalc = Minecraft.thePlayer.posZ;
            if ((dist += 1.0) > expandDist) {
                dist = expandDist;
            }
            xCalc += ((double)forward * 0.45 * MathHelper.cos(Math.toRadians(yaw + 90.0f)) + (double)strafe * 0.45 * MathHelper.sin(Math.toRadians(yaw + 90.0f))) * dist;
            zCalc += ((double)forward * 0.45 * MathHelper.sin(Math.toRadians(yaw + 90.0f)) - (double)strafe * 0.45 * MathHelper.cos(Math.toRadians(yaw + 90.0f))) * dist;
            if (dist == expandDist) break;
            underPos = new BlockPos(xCalc, y, zCalc);
            underBlock = Minecraft.theWorld.getBlockState(underPos).getBlock();
        }
        return new double[]{xCalc, zCalc};
    }

    @EventHandler
    public void onMove(EventMove e) {
        if (this.scaffoldmode.is("SlowSpeed") && !Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
            if (!Minecraft.thePlayer.isSneaking()) {
                MoveUtils.setSpeed(e, 0.22);
            }
        }
    }

    private void doRotate(EventPreUpdate e) {
        BlockData blockData;
        float[] rotations;
        BlockData lastBlockCache;
        BlockPos blockPos;
        if (this.rot.is("Watchdog")) {
            blockPos = new BlockPos(Minecraft.thePlayer.posX, this.y - 1.0, Minecraft.thePlayer.posZ);
            lastBlockCache = this.getBlockData(blockPos);
            if (lastBlockCache != null) {
                rotations = RotationUtils.getFacingRotations2(lastBlockCache.getPosition().getX(), lastBlockCache.getPosition().getY(), lastBlockCache.getPosition().getZ());
                if (((Double)this.minturnspeed.get()).intValue() == 180 && ((Double)this.maxturnspeed.get()).intValue() == 180) {
                    lastYaw = rotations[0];
                    lastPitch = 81.0f;
                    EventPreUpdate.setYaw(rotations[0]);
                    EventPreUpdate.setPitch(81.0f);
                } else {
                    lastYaw = RotationUtil.getRotateForScaffold(rotations[0], 81.0f, lastYaw, lastPitch, ((Double)this.minturnspeed.get()).intValue(), ((Double)this.maxturnspeed.get()).intValue())[0];
                    lastPitch = RotationUtil.getRotateForScaffold(rotations[0], 81.0f, lastYaw, lastPitch, ((Double)this.minturnspeed.get()).intValue(), ((Double)this.maxturnspeed.get()).intValue())[1];
                    EventPreUpdate.setYaw(lastYaw);
                    EventPreUpdate.setPitch(lastPitch);
                }
            } else {
                lastYaw = this.getRotation(((Double)this.minturnspeed.get()).floatValue(), ((Double)this.maxturnspeed.get()).floatValue())[0];
                lastPitch = this.getRotation(((Double)this.minturnspeed.get()).floatValue(), ((Double)this.maxturnspeed.get()).floatValue())[1];
                EventPreUpdate.setYaw(lastYaw);
                EventPreUpdate.setPitch(lastPitch);
            }
        }
        if (this.rot.is("Keep")) {
            blockPos = new BlockPos(Minecraft.thePlayer.posX, this.y - 1.0, Minecraft.thePlayer.posZ);
            lastBlockCache = this.getBlockData(blockPos);
            if (lastBlockCache != null) {
                rotations = RotationUtil.getRotations(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, Minecraft.thePlayer.getEyeHeight(), lastBlockCache.pos, lastBlockCache.face);
                this.keepRotateTicks = this.keepRotateTicks <= 15 ? ++this.keepRotateTicks : 15;
                if (BlockUtils.lookingAtBlock(lastBlockCache.pos, this.keepRotateTicks >= 15 ? lastYaw : rotations[0], 81.0f, lastBlockCache.getFacing(), false)) {
                    lastYaw = RotationUtil.getRotateForScaffold(rotations[0], 81.0f, lastYaw, lastPitch, ((Double)this.minturnspeed.get()).intValue(), ((Double)this.maxturnspeed.get()).intValue())[0];
                    lastPitch = RotationUtil.getRotateForScaffold(rotations[0], 81.0f, lastYaw, lastPitch, ((Double)this.minturnspeed.get()).intValue(), ((Double)this.maxturnspeed.get()).intValue())[1];
                } else {
                    lastYaw = RotationUtil.getRotateForScaffold(rotations[0], 81.0f, lastYaw, lastPitch, 0.0f, ((Double)HUD.ez.get()).floatValue())[0];
                    lastPitch = RotationUtil.getRotateForScaffold(rotations[0], 81.0f, lastYaw, lastPitch, 0.0f, ((Double)HUD.ez.get()).floatValue())[1];
                }
            } else {
                lastYaw = this.getRotation(((Double)this.minturnspeed.get()).floatValue(), ((Double)this.maxturnspeed.get()).floatValue())[0];
                lastPitch = this.getRotation(((Double)this.minturnspeed.get()).floatValue(), ((Double)this.maxturnspeed.get()).floatValue())[1];
            }
            EventPreUpdate.setYaw(lastYaw);
            EventPreUpdate.setPitch(lastPitch);
        }
        if (this.rot.is("AAC2")) {
            blockPos = new BlockPos(Minecraft.thePlayer.posX, this.y - 1.0, Minecraft.thePlayer.posZ);
            blockData = this.getBlockData(blockPos);
            if (blockData != null) {
                double yDif = 1.0;
                Block block = Minecraft.theWorld.getBlockState(Objects.requireNonNull(blockData).pos.offset(blockData.face)).getBlock();
                if (this.isMoving() && (!this.invalid.contains(block) || this.isBlockUnder(yDif))) {
                    return;
                }
                RotationUtil.Rotation rotation = RotationUtil.toRotation(blockData.hitVec, false);
                if (((Double)this.minturnspeed.get()).intValue() == 180 && ((Double)this.maxturnspeed.get()).intValue() == 180) {
                    lastYaw = rotation.getYaw();
                    lastPitch = rotation.getPitch();
                } else {
                    lastYaw = RotationUtil.getRotateForScaffold(rotation.getYaw(), rotation.getPitch(), lastYaw, lastPitch, ((Double)this.minturnspeed.get()).intValue(), ((Double)this.maxturnspeed.get()).intValue())[0];
                    lastPitch = RotationUtil.getRotateForScaffold(rotation.getYaw(), rotation.getPitch(), lastYaw, lastPitch, ((Double)this.minturnspeed.get()).intValue(), ((Double)this.maxturnspeed.get()).intValue())[1];
                }
            } else {
                lastYaw = this.getRotation(((Double)this.minturnspeed.get()).floatValue(), ((Double)this.maxturnspeed.get()).floatValue())[0];
                lastPitch = this.getRotation(((Double)this.minturnspeed.get()).floatValue(), ((Double)this.maxturnspeed.get()).floatValue())[1];
            }
            EventPreUpdate.setYaw(lastYaw);
            EventPreUpdate.setPitch(lastPitch);
        }
        if (this.rot.is("AAC")) {
            lastYaw = this.getRotation(((Double)this.minturnspeed.get()).floatValue(), ((Double)this.maxturnspeed.get()).floatValue())[0];
            lastPitch = this.getRotation(((Double)this.minturnspeed.get()).floatValue(), ((Double)this.maxturnspeed.get()).floatValue())[1];
            EventPreUpdate.setYaw(lastYaw);
            EventPreUpdate.setPitch(lastPitch);
        }
        if (this.rot.is("NCP")) {
            blockPos = new BlockPos(Minecraft.thePlayer.posX, this.y - 1.0, Minecraft.thePlayer.posZ);
            blockData = this.getBlockData(blockPos);
            if (blockData != null) {
                float[] rotations2 = RotationUtil.getRotations(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, Minecraft.thePlayer.getEyeHeight(), Objects.requireNonNull(blockData).pos, blockData.face);
                float[] newrots = RotationUtil.getRotateForScaffold(rotations2[0], rotations2[1], lastYaw, lastPitch, ((Double)this.minturnspeed.get()).floatValue(), ((Double)this.maxturnspeed.get()).floatValue());
                lastYaw = newrots[0];
                lastPitch = newrots[1];
            } else {
                lastYaw = this.getRotation(((Double)this.minturnspeed.get()).floatValue(), ((Double)this.maxturnspeed.get()).floatValue())[0];
                lastPitch = this.getRotation(((Double)this.minturnspeed.get()).floatValue(), ((Double)this.maxturnspeed.get()).floatValue())[1];
            }
            EventPreUpdate.setYaw(lastYaw);
            EventPreUpdate.setPitch(lastPitch);
        }
        if (Client.instance.getModuleManager().getModuleByClass(Rotate.class).isEnabled() && !Rotate.scaffold.is("Off")) {
            new RenderRotate(e.getYaw(), e.getPitch());
        }
    }

    private boolean isBlockUnder(double yOffset) {
        EntityPlayerSP player = Minecraft.thePlayer;
        return !this.invalid.contains(Minecraft.theWorld.getBlockState(new BlockPos(player.posX, player.posY - yOffset, player.posZ)).getBlock());
    }

    public double getDelay() {
        return MathUtil.randomNumber((Double)this.maxblockdelay.get(), (Double)this.minblockdelay.get());
    }

    @EventHandler
    private void onLoop(LoopEvent event) {
        for (int i = 0; i < 8; ++i) {
            if (Minecraft.thePlayer.inventory.mainInventory[i] == null) continue;
            if (Minecraft.thePlayer.inventory.mainInventory[i].stackSize > 0) continue;
            Minecraft.thePlayer.inventory.mainInventory[i] = null;
        }
    }

    private boolean sameYCheck() {
        if (((Boolean)this.sameY.get()).booleanValue()) {
            if (((Boolean)down.get()).booleanValue() && Keyboard.isKeyDown((int)42)) {
                return false;
            }
            if (this.sameYMode.is("Normal") || this.sameYMode.is("AutoJump")) {
                return true;
            }
            if (this.sameYMode.is("OnlySpeed")) {
                return this.isEnabled((Class<? extends Module>)Speed.class);
            }
        }
        return false;
    }

    @EventHandler
    public void onMotionUpdate(MotionUpdateEvent event) {
        block29: {
            boolean b;
            block31: {
                BlockData blockData;
                block30: {
                    double xPos;
                    boolean air;
                    if (this.placeTiming.is("Pre") && event.getState() == MotionUpdateEvent.State.POST) {
                        return;
                    }
                    if (this.placeTiming.is("Post") && event.getState() == MotionUpdateEvent.State.PRE) {
                        return;
                    }
                    if (!this.placedelay.hasReached(this.getDelay())) {
                        return;
                    }
                    this.placedelay.reset();
                    if (this.sameYCheck()) {
                        if (this.sameYMode.is("AutoJump")) {
                            if (Minecraft.thePlayer.onGround && this.isMoving() && MoveUtils.isMovingKeyBindingActive()) {
                                Minecraft.thePlayer.jump();
                            }
                        }
                        if ((double)Minecraft.thePlayer.fallDistance > 1.2 + (double)MoveUtils.getJumpEffect() || !PlayerUtil.isMoving2()) {
                            this.y = Minecraft.thePlayer.posY;
                        }
                    } else {
                        this.y = Minecraft.thePlayer.posY;
                    }
                    double d = (air = this.isAirBlock(Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer.posX, this.y, Minecraft.thePlayer.posZ)).getBlock())) ? Minecraft.thePlayer.posX : (xPos = this.getExpandCoords(this.y)[0]);
                    double zPos = air ? Minecraft.thePlayer.posZ : this.getExpandCoords(this.y)[1];
                    BlockPos blockPos = new BlockPos(xPos, this.y - 1.0, zPos);
                    Block getBlock = Minecraft.theWorld.getBlockState(blockPos).getBlock();
                    blockData = this.getBlockData(blockPos);
                    double x = Minecraft.thePlayer.posX;
                    double y = Minecraft.thePlayer.posY - 1.0;
                    double z = Minecraft.thePlayer.posZ;
                    BlockPos blockBelow = new BlockPos(x, y, z);
                    for (double posY = Minecraft.thePlayer.posY - 1.0; posY > 0.0; posY -= 1.0) {
                        BlockData newData = this.getBlockData(new BlockPos((double)blockBelow.getX(), posY, (double)blockBelow.getZ()));
                        if (newData == null) continue;
                        if (!(Minecraft.thePlayer.posY - posY <= 3.0)) continue;
                        blockData = newData;
                        break;
                    }
                    ItemStack itemStack = new ItemStack(Item.getItemById(261));
                    this.theSlot = -1;
                    for (int i = 36; i < 45; ++i) {
                        this.theSlot = i - 36;
                        if (Container.canAddItemToSlot(Minecraft.thePlayer.inventoryContainer.getSlot(i), itemStack, true)) continue;
                        if (!(Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock)) continue;
                        if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack() == null) continue;
                        if (!this.isValid(Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) continue;
                        if (Minecraft.thePlayer.inventoryContainer.getSlot((int)i).getStack().stackSize != 0) break;
                    }
                    if (blockData == null) break block29;
                    if (((Boolean)down.get()).booleanValue()) {
                        if (Minecraft.thePlayer.isSneaking() || Keyboard.isKeyDown((int)42)) {
                            if (Minecraft.thePlayer.onGround) {
                                blockData.setFacing(EnumFacing.DOWN);
                            }
                        }
                    }
                    if (((Boolean)this.pick.get()).booleanValue()) {
                        int blockCount = 0;
                        for (int i = 0; i < 9; ++i) {
                            ItemStack itemStacke = Minecraft.thePlayer.inventory.getStackInSlot(i);
                            if (itemStacke == null) continue;
                            int stackSize = itemStacke.stackSize;
                            if (!this.isValidItem(itemStacke.getItem()) || stackSize <= blockCount) continue;
                            blockCount = stackSize;
                            this.theSlot = i;
                        }
                    }
                    if (!this.isAirBlock(getBlock)) break block29;
                    b = Minecraft.thePlayer.inventory.currentItem != this.theSlot && (Boolean)this.silent.get() != false;
                    boolean b2 = false;
                    if (!((Boolean)this.silent.get()).booleanValue()) {
                        Minecraft.thePlayer.inventory.currentItem = this.theSlot;
                    }
                    if (b) {
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.theSlot));
                        b2 = true;
                    }
                    if (b2) break block30;
                    if (Minecraft.thePlayer.inventory.currentItem != this.theSlot) break block31;
                }
                if (((Boolean)this.swing.get()).booleanValue()) {
                    Minecraft.thePlayer.swingItem();
                } else {
                    Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                }
                if (!this.rayCast.is("None")) {
                    if (BlockUtils.lookingAtBlock(blockData.pos, lastYaw, lastPitch, blockData.getFacing(), this.rayCast.is("Strict"))) {
                        Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventory.getStackInSlot(this.theSlot), blockData.pos, blockData.face, this.getVec3(blockData.pos, blockData.face));
                    }
                } else {
                    Minecraft.playerController.onPlayerRightClick(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.inventory.getStackInSlot(this.theSlot), blockData.pos, blockData.face, this.getVec3(blockData.pos, blockData.face));
                }
                if (this.sprintMode.is("SwitchSprint")) {
                    Minecraft.thePlayer.setSprinting(false);
                }
                if (this.sprintMode.is("SwitchSprint2")) {
                    if (Minecraft.thePlayer.onGround) {
                        Minecraft.thePlayer.setSprinting(false);
                    }
                }
            }
            if (b) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
            }
        }
    }

    private Vec3 getVec3(BlockPos blockPos, EnumFacing enumFacing) {
        double n = (double)blockPos.getX() + 0.5;
        double n2 = (double)blockPos.getY() + 0.5;
        double n3 = (double)blockPos.getZ() + 0.5;
        double n4 = n + (double)enumFacing.getFrontOffsetX() / 2.0;
        double n5 = n3 + (double)enumFacing.getFrontOffsetZ() / 2.0;
        double n6 = n2 + (double)enumFacing.getFrontOffsetY() / 2.0;
        if (enumFacing == EnumFacing.UP || enumFacing == EnumFacing.DOWN) {
            n4 += Math.random() * 0.6 - 0.3;
            n5 += Math.random() * 0.6 - 0.3;
        } else {
            n6 += Math.random() * 0.6 - 0.3;
        }
        if (enumFacing == EnumFacing.WEST || enumFacing == EnumFacing.EAST) {
            n5 += Math.random() * 0.6 - 0.3;
        }
        if (enumFacing == EnumFacing.SOUTH || enumFacing == EnumFacing.NORTH) {
            n4 += Math.random() * 0.6 - 0.3;
        }
        return new Vec3(n4, n6, n5);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean CanDownPut() {
        if ((Boolean)down.get() == false) return false;
        if (!Scaffold.mc.gameSettings.keyBindSneak.isPressed()) return false;
        if (!Minecraft.thePlayer.onGround) return false;
        return true;
    }

    private boolean isValidItem(Item item) {
        if (item instanceof ItemBlock) {
            ItemBlock iBlock = (ItemBlock)item;
            Block block = iBlock.getBlock();
            return !this.blacklist.contains(block);
        }
        return false;
    }

    private boolean isValid(Item item) {
        if (!(item instanceof ItemBlock)) {
            return false;
        }
        ItemBlock itemBlock = (ItemBlock)item;
        Block block = itemBlock.getBlock();
        return !this.blacklist.contains(block);
    }

    public boolean isAirBlock(Block block) {
        if (block.getMaterial().isReplaceable()) {
            return !(block instanceof BlockSnow) || !(block.getBlockBoundsMaxY() > 0.125);
        }
        return false;
    }

    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || this.blacklist.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    public float[] getRotation(float minturnspeed, float maxturnspeed) {
        if (Scaffold.mc.gameSettings.keyBindForward.isKeyDown()) {
            MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
            if ((double)MovementInput.moveStrafe == 0.0) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 180.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
            if (Scaffold.mc.gameSettings.keyBindLeft.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 225.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
            if (Scaffold.mc.gameSettings.keyBindRight.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 135.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
        } else if (Scaffold.mc.gameSettings.keyBindBack.isKeyDown()) {
            MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
            if ((double)MovementInput.moveStrafe == 0.0) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
            if (Scaffold.mc.gameSettings.keyBindLeft.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 315.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
            if (Scaffold.mc.gameSettings.keyBindRight.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 45.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
        } else {
            if (Scaffold.mc.gameSettings.keyBindLeft.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 270.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
            if (Scaffold.mc.gameSettings.keyBindRight.isKeyDown()) {
                return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 90.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
            }
        }
        return RotationUtil.getRotateForScaffold(Minecraft.thePlayer.rotationYaw - 180.0f, this.blockPitch, lastYaw, lastPitch, minturnspeed, maxturnspeed);
    }

    private void TowerCore() {
        if (((Boolean)tower.get()).booleanValue() && (!PlayerUtil.isMoving2() || ((Boolean)towerMove.get()).booleanValue())) {
            this.isTower = true;
            BlockPos blockPos0 = new BlockPos(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0, Minecraft.thePlayer.posZ);
            Block getBlock0 = Minecraft.theWorld.getBlockState(blockPos0).getBlock();
            BlockData blockData0 = this.getBlockData(blockPos0);
            if (!Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                if (PlayerUtil.isMoving2()) {
                    if (this.sameYCheck()) {
                        return;
                    }
                    if (MoveUtils.isOnGround(0.76) && !MoveUtils.isOnGround(0.75)) {
                        if (Minecraft.thePlayer.motionY > 0.23) {
                            if (Minecraft.thePlayer.motionY < 0.25) {
                                Minecraft.thePlayer.motionY = (double)Math.round(Minecraft.thePlayer.posY) - Minecraft.thePlayer.posY;
                            }
                        }
                    }
                    if (!MoveUtils.isOnGround(1.0E-4)) {
                        if (Minecraft.thePlayer.motionY > 0.1) {
                            if (Minecraft.thePlayer.posY >= (double)Math.round(Minecraft.thePlayer.posY) - 1.0E-4) {
                                if (Minecraft.thePlayer.posY <= (double)Math.round(Minecraft.thePlayer.posY) + 1.0E-4) {
                                    Minecraft.thePlayer.motionY = 0.0;
                                }
                            }
                        }
                    }
                }
                return;
            }
            if (PlayerUtil.isMoving2()) {
                if (this.sameYCheck()) {
                    return;
                }
                if (MoveUtils.isOnGround(0.76) && !MoveUtils.isOnGround(0.75)) {
                    if (Minecraft.thePlayer.motionY > 0.23) {
                        if (Minecraft.thePlayer.motionY < 0.25) {
                            Minecraft.thePlayer.motionY = (double)Math.round(Minecraft.thePlayer.posY) - Minecraft.thePlayer.posY;
                        }
                    }
                }
                if (MoveUtils.isOnGround(1.0E-4)) {
                    Minecraft.thePlayer.motionY = Scaffold.mc.gameSettings.keyBindForward.isKeyDown() ? (double)0.42f : (double)0.38f;
                    Minecraft.thePlayer.motionX *= 0.95;
                    Minecraft.thePlayer.motionZ *= 0.95;
                } else if (!MoveUtils.isOnGround(1.0E-4)) {
                    if (Minecraft.thePlayer.posY >= (double)Math.round(Minecraft.thePlayer.posY) - 1.0E-4) {
                        if (Minecraft.thePlayer.posY <= (double)Math.round(Minecraft.thePlayer.posY) + 1.0E-4) {
                            Minecraft.thePlayer.motionY = 0.0;
                        }
                    }
                }
            } else {
                Minecraft.thePlayer.motionX = 0.0;
                Minecraft.thePlayer.motionZ = 0.0;
                Minecraft.thePlayer.jumpMovementFactor = 0.0f;
                if (this.isAirBlock(getBlock0) && blockData0 != null) {
                    Minecraft.thePlayer.setPosition(Minecraft.thePlayer.prevPosX, Minecraft.thePlayer.posY, Minecraft.thePlayer.prevPosZ);
                    Minecraft.thePlayer.motionY = 0.4196f;
                }
            }
        } else {
            this.isTower = false;
        }
    }

    private int moveHot() {
        int slot = -1;
        for (int i = 36; i < 45; ++i) {
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            Item item = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
            ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (!(item instanceof ItemBlock) || !this.isValid(item) || is.stackSize <= slot) continue;
            slot = i;
            return slot;
        }
        return slot;
    }

    private boolean hotBlockError() {
        int i = 36;
        while (i < 45) {
            try {
                ItemStack slotStack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (slotStack == null || slotStack.getItem() == null || !(slotStack.getItem() instanceof ItemBlock) || !this.isValid(slotStack.getItem())) {
                    ++i;
                    continue;
                }
                return true;
            }
            catch (Exception exception) {
            }
        }
        return false;
    }

    private void moveBlock() {
        Item item;
        int i;
        int slot = -1;
        int size = 0;
        this.getBlockCount();
        for (i = 9; i < 36; ++i) {
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            item = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
            ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (!(item instanceof ItemBlock) || !this.isValid(item) || is.stackSize <= size) continue;
            size = is.stackSize;
            slot = i;
        }
        for (i = 36; i < 45; ++i) {
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            item = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
            if (!(item instanceof ItemBlock)) continue;
            this.isValid(item);
        }
        ItemStack is = new ItemStack(Item.getItemById(261));
        int bestInvSlot = slot;
        int bestHotbarSlot = this.moveHot();
        if (bestHotbarSlot > 0 && bestInvSlot > 0) {
            if (Minecraft.thePlayer.inventoryContainer.getSlot(bestInvSlot).getHasStack()) {
                if (Minecraft.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getHasStack()) {
                    Minecraft.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getStack();
                    Minecraft.thePlayer.inventoryContainer.getSlot(bestInvSlot).getStack();
                }
            }
        }
        if (this.hotBlockError()) {
            for (int a = 36; a < 45; ++a) {
                if (Minecraft.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
                    continue;
                }
                break;
            }
        } else {
            for (int i2 = 9; i2 < 36; ++i2) {
                if (!Minecraft.thePlayer.inventoryContainer.getSlot(i2).getHasStack()) continue;
                Item item2 = Minecraft.thePlayer.inventoryContainer.getSlot(i2).getStack().getItem();
                int count = 0;
                if (!(item2 instanceof ItemBlock) || !this.isValid(item2)) continue;
                for (int a = 36; a < 45; ++a) {
                    if (!Container.canAddItemToSlot(Minecraft.thePlayer.inventoryContainer.getSlot(a), is, true)) continue;
                    this.swap(i2, a - 36);
                    ++count;
                    break;
                }
                if (count != 0) break;
                this.swap(i2, 7);
                break;
            }
        }
    }

    private void swap(int slot, int hotbarNum) {
        Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Minecraft.thePlayer);
    }

    private boolean isNotBlockPosAir(BlockPos blockPos) {
        Block block = Minecraft.theWorld.getBlockState(blockPos).getBlock();
        return !this.invalid.contains(block);
    }

    private BlockData getBlockData(BlockPos down) {
        BlockData blockData = null;
        for (int n = 0; n < 2; ++n) {
            if (this.isNotBlockPosAir(down.add(0, 0, 1))) {
                blockData = new BlockData(down.add(0, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (this.isNotBlockPosAir(down.add(0, 0, -1))) {
                blockData = new BlockData(down.add(0, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (this.isNotBlockPosAir(down.add(1, 0, 0))) {
                blockData = new BlockData(down.add(1, 0, 0), EnumFacing.WEST);
                break;
            }
            if (this.isNotBlockPosAir(down.add(-1, 0, 0))) {
                blockData = new BlockData(down.add(-1, 0, 0), EnumFacing.EAST);
                break;
            }
            if (this.isNotBlockPosAir(down.add(0, -1, 0))) {
                blockData = new BlockData(down.add(0, -1, 0), EnumFacing.UP);
                break;
            }
            if (this.isNotBlockPosAir(down.add(1, 0, 1))) {
                blockData = new BlockData(down.add(1, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (this.isNotBlockPosAir(down.add(-1, 0, -1))) {
                blockData = new BlockData(down.add(-1, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (this.isNotBlockPosAir(down.add(1, 0, 1))) {
                blockData = new BlockData(down.add(1, 0, 1), EnumFacing.WEST);
                break;
            }
            if (this.isNotBlockPosAir(down.add(-1, 0, -1))) {
                blockData = new BlockData(down.add(-1, 0, -1), EnumFacing.EAST);
                break;
            }
            if (this.isNotBlockPosAir(down.add(-1, 0, 1))) {
                blockData = new BlockData(down.add(-1, 0, 1), EnumFacing.NORTH);
                break;
            }
            if (this.isNotBlockPosAir(down.add(1, 0, -1))) {
                blockData = new BlockData(down.add(1, 0, -1), EnumFacing.SOUTH);
                break;
            }
            if (this.isNotBlockPosAir(down.add(1, 0, -1))) {
                blockData = new BlockData(down.add(1, 0, -1), EnumFacing.WEST);
                break;
            }
            if (this.isNotBlockPosAir(down.add(-1, 0, 1))) {
                blockData = new BlockData(down.add(-1, 0, 1), EnumFacing.EAST);
                break;
            }
            down = down.down();
        }
        return blockData;
    }

    public Vec3 getVec3ForRotUse(BlockPos pos, EnumFacing facing) {
        Vec3 vector = new Vec3(pos);
        double random = 0.0;
        if (facing == EnumFacing.NORTH) {
            vector.xCoord = Minecraft.thePlayer.posX + random * 0.01;
        } else if (facing == EnumFacing.SOUTH) {
            vector.xCoord = Minecraft.thePlayer.posX + random * 0.01;
            vector.zCoord += 1.0;
        } else if (facing == EnumFacing.WEST) {
            vector.zCoord = Minecraft.thePlayer.posZ + random * 0.01;
        } else if (facing == EnumFacing.EAST) {
            vector.zCoord = Minecraft.thePlayer.posZ + random * 0.01;
            vector.xCoord += 1.0;
        }
        if (facing == EnumFacing.UP) {
            vector.xCoord += random;
            vector.zCoord += random;
            vector.yCoord += 1.0;
        } else {
            vector.yCoord += random;
        }
        return vector;
    }

    @EventHandler
    public void on3D(EventRender3D e) {
        if (((Boolean)this.mark.get()).booleanValue()) {
            int i = 0;
            while ((double)i < 1.1) {
                BlockPos blockPos = new BlockPos(Minecraft.thePlayer.posX + (double)(Minecraft.thePlayer.getHorizontalFacing() == EnumFacing.WEST ? -i : (Minecraft.thePlayer.getHorizontalFacing() == EnumFacing.EAST ? i : 0)), Minecraft.thePlayer.posY - (Minecraft.thePlayer.posY == (double)((int)Minecraft.thePlayer.posY) + 0.5 ? 0.0 : 1.0), Minecraft.thePlayer.posZ + (double)(Minecraft.thePlayer.getHorizontalFacing() == EnumFacing.NORTH ? -i : (Minecraft.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH ? i : 0)));
                if (Minecraft.theWorld.getBlockState(blockPos).getBlock().isReplaceable(Minecraft.theWorld, blockPos) && this.getBlockData(blockPos) != null) {
                    RenderUtils.drawBlockBox(blockPos, new Color(255, 255, 255), true);
                    break;
                }
                ++i;
            }
        }
        if (((Boolean)this.displays.get()).booleanValue()) {
            Color color = new Color(Colors.BLACK.c);
            Color color2 = new Color(Colors.ORANGE.c);
            double x = Minecraft.thePlayer.lastTickPosX + (Minecraft.thePlayer.posX - Minecraft.thePlayer.lastTickPosX) * (double)Scaffold.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            double y = Minecraft.thePlayer.lastTickPosY + (Minecraft.thePlayer.posY - Minecraft.thePlayer.lastTickPosY) * (double)Scaffold.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            double z = Minecraft.thePlayer.lastTickPosZ + (Minecraft.thePlayer.posZ - Minecraft.thePlayer.lastTickPosZ) * (double)Scaffold.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            double x2 = Minecraft.thePlayer.lastTickPosX + (Minecraft.thePlayer.posX - Minecraft.thePlayer.lastTickPosX) * (double)Scaffold.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            double z2 = Minecraft.thePlayer.lastTickPosZ + (Minecraft.thePlayer.posZ - Minecraft.thePlayer.lastTickPosZ) * (double)Scaffold.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            x -= 0.65;
            z -= 0.65;
            x2 -= 0.5;
            z2 -= 0.5;
            y += (double)Minecraft.thePlayer.getEyeHeight() + 0.35 - (Minecraft.thePlayer.isSneaking() ? 0.25 : 0.0);
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1.0f);
            GL11.glLineWidth((float)2.0f);
            RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y - 2.0, z, x + 1.3, y - 2.0, z + 1.3));
            RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x2, y - 2.0, z2, x2 + 1.0, y - 2.0, z2 + 1.0));
            GL11.glColor4f((float)((float)color2.getRed() / 255.0f), (float)((float)color2.getGreen() / 255.0f), (float)((float)color2.getBlue() / 255.0f), (float)1.0f);
            RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y - 2.0, z, x + 1.3, y - 2.0, z + 1.3));
            GL11.glDisable((int)2848);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
    }

    public static void silentRotationStrafe(StrafeEvent event, float yaw) {
        float f;
        int dif = (int)((MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationYaw - yaw - 23.5f - 135.0f) + 180.0f) / 45.0f);
        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();
        float calcForward = 0.0f;
        float calcStrafe = 0.0f;
        switch (dif) {
            case 0: {
                calcForward = forward;
                calcStrafe = strafe;
                break;
            }
            case 1: {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            }
            case 2: {
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            }
            case 3: {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            }
            case 4: {
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            }
            case 5: {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            }
            case 6: {
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            }
            case 7: {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
            }
        }
        if (calcForward > 1.0f || calcForward < 0.9f && calcForward > 0.3f || calcForward < -1.0f || calcForward > -0.9f && calcForward < -0.3f) {
            calcForward *= 0.5f;
        }
        if (calcStrafe > 1.0f || calcStrafe < 0.9f && calcStrafe > 0.3f || calcStrafe < -1.0f || calcStrafe > -0.9f && calcStrafe < -0.3f) {
            calcStrafe *= 0.5f;
        }
        float d = calcStrafe * calcStrafe + calcForward * calcForward;
        if (f >= 1.0E-4f) {
            float f2;
            d = MathHelper.sqrt_float(d);
            if (f2 < 1.0f) {
                d = 1.0f;
            }
            d = friction / d;
            float yawSin = MathHelper.sin((float)((double)yaw * Math.PI / 180.0));
            float yawCos = MathHelper.cos((float)((double)yaw * Math.PI / 180.0));
            Minecraft.thePlayer.motionX += (double)((calcStrafe *= d) * yawCos - (calcForward *= d) * yawSin);
            Minecraft.thePlayer.motionZ += (double)(calcForward * yawCos + calcStrafe * yawSin);
        }
    }

    private class BlockData {
        final BlockPos pos;
        EnumFacing face;
        public Vec3 hitVec;

        public BlockData(BlockPos position, EnumFacing facing) {
            this.pos = position;
            this.face = facing;
            this.hitVec = Scaffold.this.getVec3ForRotUse(this.pos, this.face);
        }

        public EnumFacing setFacing(EnumFacing set) {
            this.face = set;
            return this.face;
        }

        public EnumFacing getFacing() {
            return this.face;
        }

        public BlockPos getPosition() {
            return this.pos;
        }
    }
}

