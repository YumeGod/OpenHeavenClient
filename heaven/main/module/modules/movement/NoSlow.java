/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.More.PacketEvent;
import heaven.main.event.events.drakApi.MotionUpdateEvent;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.KillAura;
import heaven.main.utils.MoveUtils;
import heaven.main.utils.math.MathUtil;
import heaven.main.utils.timer.TimerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.io.Serializable;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow
extends Module {
    private static final String[] noslowmode = new String[]{"Vanilla", "Watchdog", "ZQAT", "AAC4.4.2", "NCP", "Matrix", "Hawk", "Spartan"};
    public static final Mode<String> mode = new Mode("Mode", noslowmode, noslowmode[1]);
    public static final Numbers<Double> multiplier = new Numbers<Double>("Multiplier", 1.0, 0.0, 1.0, 0.01);
    private final Option<Boolean> rpg = new Option<Boolean>("NoRPGItemSlow", false);
    public static final Option<Boolean> antiSoulSand = new Option<Boolean>("AntiSoulSand", false);
    private final TimerUtil time = new TimerUtil();
    public static boolean Slowdown;
    public static boolean matrixShouldSlowdown;

    public NoSlow() {
        super("NoSlow", ModuleType.Movement);
        this.addValues(mode, multiplier, antiSoulSand, this.rpg);
    }

    @Override
    public void onDisable() {
        this.time.reset();
        super.onDisable();
    }

    @EventHandler
    public void onSend(PacketEvent e) {
        block5: {
            block6: {
                block7: {
                    if (!e.getState().equals((Object)PacketEvent.State.OUTGOING)) break block5;
                    if (!mode.is("Watchdog") || !(e.getPacket() instanceof S30PacketWindowItems)) break block6;
                    if (Minecraft.thePlayer.isUsingItem()) break block7;
                    if (!Minecraft.thePlayer.isBlocking()) break block6;
                }
                e.setCancelled(true);
            }
            if (mode.is("Vanilla") && !this.isUsingFood() && this.isEnabled((Class<? extends Module>)KillAura.class)) {
                C07PacketPlayerDigging packet;
                if (!Minecraft.thePlayer.isBlocking() && this.getModule(KillAura.class).checkSword() && e.getPacket() instanceof C07PacketPlayerDigging && (packet = (C07PacketPlayerDigging)e.getPacket()).getStatus().equals((Object)C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventHandler
    public void onBlock(MotionUpdateEvent event) {
        block25: {
            block26: {
                block24: {
                    if (NoSlow.mode.is("AAC4.4.2") || NoSlow.mode.is("Hawk")) {
                        if (!this.isMoving()) {
                            return;
                        }
                        if (event.getState().equals((Object)MotionUpdateEvent.State.PRE) && this.isUsingFood()) {
                            if (Minecraft.thePlayer.getItemInUseDuration() >= 1) {
                                this.sendPacket(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
                            }
                        }
                    }
                    if (!NoSlow.mode.is("Matrix")) break block24;
                    if (!this.isMoving()) {
                        return;
                    }
                    if (!event.getState().equals((Object)MotionUpdateEvent.State.PRE)) break block24;
                    if (!this.isUsingFood()) ** GOTO lbl-1000
                    if (Minecraft.thePlayer.getItemInUseDuration() >= 1) {
                        NoSlow.matrixShouldSlowdown = true;
                        this.sendPacket(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
                    } else if (NoSlow.matrixShouldSlowdown) {
                        NoSlow.matrixShouldSlowdown = false;
                    }
                }
                if (!NoSlow.mode.is("ZQAT")) break block25;
                if (!this.isMoving()) {
                    return;
                }
                if (!event.getState().equals((Object)MotionUpdateEvent.State.PRE)) break block26;
                if (!this.isUsingFood()) ** GOTO lbl-1000
                if (Minecraft.thePlayer.getItemInUseDuration() >= 1) {
                    NoSlow.matrixShouldSlowdown = true;
                    this.sendPacket(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
                } else if (NoSlow.matrixShouldSlowdown) {
                    NoSlow.matrixShouldSlowdown = false;
                }
            }
            if (event.getState().equals((Object)MotionUpdateEvent.State.POST)) {
                if (Minecraft.thePlayer.isBlocking()) {
                    NoSlow.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Minecraft.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                }
            }
        }
        if (NoSlow.mode.is("Watchdog")) {
            if (!this.isMoving()) {
                return;
            }
            if (event.getState().equals((Object)MotionUpdateEvent.State.PRE) && this.isUsingFood()) {
                if (Minecraft.thePlayer.getItemInUseDuration() >= 1) {
                    this.sendPacket(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
                }
            }
        }
        if (NoSlow.mode.is("NCP") && this.isMoving()) {
            if (Minecraft.thePlayer.getHeldItem() != null) {
                if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    if (Minecraft.thePlayer.isBlocking()) {
                        if (event.getState().equals((Object)MotionUpdateEvent.State.PRE)) {
                            this.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        } else {
                            this.sendPacket(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.getHeldItem()));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        block15: {
            block16: {
                block13: {
                    block14: {
                        this.setSuffix((Serializable)mode.getValue());
                        if (mode.is("Matrix")) {
                            if (Minecraft.thePlayer.getHeldItem() != null && this.isMoving()) {
                                if (Minecraft.thePlayer.isBlocking()) {
                                    if (Minecraft.thePlayer.ticksExisted % 7 == 0 && MoveUtils.isOnGround(0.42)) {
                                        this.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.UP));
                                    }
                                }
                            }
                        }
                        if (!mode.is("AAC4.4.2")) break block13;
                        if (Minecraft.thePlayer.getHeldItem() == null || !this.isMoving()) break block13;
                        if (!Minecraft.thePlayer.isBlocking()) break block13;
                        if (Minecraft.thePlayer.ticksExisted % 2 != 0) break block13;
                        if (MoveUtils.isOnGround(0.42)) break block14;
                        if (Minecraft.thePlayer.onGround) break block13;
                    }
                    this.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.UP));
                }
                if (!mode.is("Hawk")) break block15;
                if (Minecraft.thePlayer.getHeldItem() == null || !this.isMoving()) break block15;
                if (!Minecraft.thePlayer.isBlocking()) break block15;
                if ((double)Minecraft.thePlayer.ticksExisted % MathUtil.getRandom(2.0, 1.0) != 0.0) break block15;
                if (MoveUtils.isOnGround(0.42)) break block16;
                if (Minecraft.thePlayer.onGround) break block15;
            }
            this.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.UP));
        }
        if (((Boolean)this.rpg.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.getHeldItem() != null && this.isMoving()) {
                if (Minecraft.thePlayer.getHeldItem().getDisplayName().equals("\ufffd\ufffdf < \ufffd\ufffd2\ufffd\ufffdl\ufffd\ufffd\ufffd\ufffd\u00e9\u00ae\ufffd\ufffdf >")) {
                    MoveUtils.setSpeed(0.26);
                }
            }
        }
    }

    private boolean isUsingFood() {
        if (Minecraft.thePlayer.getItemInUse() == null) {
            return false;
        }
        Item usingItem = Minecraft.thePlayer.getItemInUse().getItem();
        return Minecraft.thePlayer.isUsingItem() && (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion);
    }
}

