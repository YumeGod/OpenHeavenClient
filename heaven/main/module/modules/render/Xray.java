/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventBlockRenderSide;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.render.RenderUtils;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class Xray
extends Module {
    private final Numbers<Double> range = new Numbers<Double>("Range", 50.0, 10.0, 100.0, 10.0);
    public final Option<Boolean> hypixelUhc = new Option<Boolean>("HypixelUHC", false);
    private final ArrayList<XrayBlock> list = new ArrayList();

    public Xray() {
        super("Xray", new String[0], ModuleType.Render);
        this.addValues(this.range, this.hypixelUhc);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Xray.mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Xray.mc.renderGlobal.loadRenderers();
        this.list.clear();
    }

    @EventHandler
    public void onRender3D(EventRender3D event) {
        if (!((Boolean)this.hypixelUhc.getValue()).booleanValue()) {
            return;
        }
        for (XrayBlock found : this.list) {
            RenderUtils.drawBlockBox(found.blockPos, Xray.getOreColor(found.block), true);
        }
    }

    private static Color getOreColor(Block block) {
        if (block == Blocks.diamond_ore) {
            return new Color(0, 255, 255, 165);
        }
        if (block == Blocks.coal_ore) {
            return new Color(80, 80, 80, 165);
        }
        if (block == Blocks.emerald_ore) {
            return new Color(0, 255, 0, 165);
        }
        if (block == Blocks.gold_ore) {
            return new Color(255, 255, 0, 165);
        }
        if (block == Blocks.iron_ore) {
            return new Color(255, 150, 100, 165);
        }
        if (block == Blocks.lapis_ore) {
            return new Color(0, 0, 255, 165);
        }
        if (block == Blocks.lit_redstone_ore || block == Blocks.redstone_ore) {
            return new Color(255, 0, 0, 165);
        }
        if (block == Blocks.quartz_ore) {
            return new Color(255, 255, 255, 165);
        }
        return null;
    }

    @EventHandler
    public void onBlockRenderSide(EventBlockRenderSide e) {
        block6: {
            block7: {
                if (!((Boolean)this.hypixelUhc.getValue()).booleanValue()) {
                    return;
                }
                if (!(e.getSide() == EnumFacing.DOWN && e.minY > 0.0 || e.getSide() == EnumFacing.UP && e.maxY < 1.0 || e.getSide() == EnumFacing.NORTH && e.minZ > 0.0 || e.getSide() == EnumFacing.SOUTH && e.maxZ < 1.0 || e.getSide() == EnumFacing.WEST && e.minX > 0.0 || e.getSide() == EnumFacing.EAST && e.maxX < 1.0) && e.getWorld().getBlockState(e.pos).getBlock().isOpaqueCube()) break block6;
                if (Minecraft.theWorld.getBlockState(e.getPos().offset(e.getSide(), -1)).getBlock() instanceof BlockOre) break block7;
                if (!(Minecraft.theWorld.getBlockState(e.getPos().offset(e.getSide(), -1)).getBlock() instanceof BlockRedstoneOre)) break block6;
            }
            float xDiff = (float)(Minecraft.thePlayer.posX - (double)e.pos.getX());
            float yDiff = 0.0f;
            float zDiff = (float)(Minecraft.thePlayer.posZ - (double)e.pos.getZ());
            float range = MathHelper.sqrt_float(xDiff * xDiff + 0.0f + zDiff * zDiff);
            if ((double)range > (Double)this.range.getValue()) {
                return;
            }
            XrayBlock x = new XrayBlock(new BlockPos(Math.round(e.pos.offset(e.getSide(), -1).getX()), Math.round(e.pos.offset(e.getSide(), -1).getY()), Math.round(e.pos.offset(e.getSide(), -1).getZ())), Minecraft.theWorld.getBlockState(e.pos.offset(e.getSide(), -1)).getBlock());
            if (!this.list.contains(x)) {
                this.list.add(x);
            }
        }
    }

    static class XrayBlock {
        public BlockPos blockPos;
        public Block block;

        public XrayBlock(BlockPos blockPos, Block block) {
            this.blockPos = blockPos;
            this.block = block;
        }
    }
}

