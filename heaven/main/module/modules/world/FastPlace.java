/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.world;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.math.MathUtil;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;

public class FastPlace
extends Module {
    private final Numbers<Double> maxdelay = new Numbers<Double>("MaxPlaceDelay", 1.0, 0.0, 3.0, 1.0);
    private final Numbers<Double> mindelay = new Numbers<Double>("MinPlaceDelay", 1.0, 0.0, 3.0, 1.0);
    private final Option<Boolean> onlyground = new Option<Boolean>("OnlyGround", false);
    private final Option<Boolean> onlyair = new Option<Boolean>("OnlyAir", false);
    private final Option<Boolean> Spectatorcheck = new Option<Boolean>("SpectatorCheck", true);
    private final List<Block> blackList = Arrays.asList(Blocks.air, Blocks.water, Blocks.torch, Blocks.redstone_torch, Blocks.ladder, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.web, Blocks.redstone_torch, Blocks.brewing_stand, Blocks.waterlily, Blocks.farmland, Blocks.sand, Blocks.beacon, Blocks.double_plant, Blocks.noteblock, Blocks.hopper, Blocks.dispenser, Blocks.dropper);

    public FastPlace() {
        super("FastPlace", ModuleType.World);
        this.addValues(this.maxdelay, this.mindelay, this.Spectatorcheck, this.onlyground, this.onlyair);
    }

    @EventHandler
    private void onTick(EventTick e) {
        if (((Boolean)this.Spectatorcheck.get()).booleanValue()) {
            if (Minecraft.thePlayer.isSpectator()) {
                return;
            }
        }
        if (((Boolean)this.onlyair.getValue()).booleanValue()) {
            if (Minecraft.thePlayer.onGround) {
                return;
            }
        } else if (((Boolean)this.onlyground.get()).booleanValue()) {
            if (!Minecraft.thePlayer.onGround) {
                return;
            }
        }
        if (Minecraft.thePlayer.getHeldItem() != null) {
            if (Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                if (!this.blackList.contains(((ItemBlock)Minecraft.thePlayer.getHeldItem().getItem()).getBlock())) {
                    FastPlace.mc.rightClickDelayTimer = (int)MathUtil.randomNumber(((Double)this.maxdelay.getValue()).intValue(), ((Double)this.mindelay.getValue()).intValue());
                }
            }
        }
    }
}

