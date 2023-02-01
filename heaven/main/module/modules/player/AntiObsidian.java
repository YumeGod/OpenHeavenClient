/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventTick;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class AntiObsidian
extends Module {
    public AntiObsidian() {
        super("AntiObsidian", new String[]{"antitrap"}, ModuleType.Player);
    }

    @EventHandler
    public void onTick(EventTick e) {
        BlockPos downpos;
        BlockPos sand = new BlockPos(new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 3.0, Minecraft.thePlayer.posZ));
        Block sandblock = Minecraft.theWorld.getBlockState(sand).getBlock();
        BlockPos forge = new BlockPos(new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 2.0, Minecraft.thePlayer.posZ));
        Block forgeblock = Minecraft.theWorld.getBlockState(forge).getBlock();
        BlockPos obsidianpos = new BlockPos(new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0, Minecraft.thePlayer.posZ));
        Block obsidianblock = Minecraft.theWorld.getBlockState(obsidianpos).getBlock();
        if (obsidianblock == Block.getBlockById(49)) {
            this.bestTool(AntiObsidian.mc.objectMouseOver.getBlockPos().getX(), AntiObsidian.mc.objectMouseOver.getBlockPos().getY(), AntiObsidian.mc.objectMouseOver.getBlockPos().getZ());
            downpos = new BlockPos(new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0, Minecraft.thePlayer.posZ));
            Minecraft.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
        }
        if (forgeblock == Block.getBlockById(61)) {
            this.bestTool(AntiObsidian.mc.objectMouseOver.getBlockPos().getX(), AntiObsidian.mc.objectMouseOver.getBlockPos().getY(), AntiObsidian.mc.objectMouseOver.getBlockPos().getZ());
            downpos = new BlockPos(new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 1.0, Minecraft.thePlayer.posZ));
            Minecraft.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
        }
        if (sandblock == Block.getBlockById(12) || sandblock == Block.getBlockById(13)) {
            this.bestTool(AntiObsidian.mc.objectMouseOver.getBlockPos().getX(), AntiObsidian.mc.objectMouseOver.getBlockPos().getY(), AntiObsidian.mc.objectMouseOver.getBlockPos().getZ());
            downpos = new BlockPos(new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 3.0, Minecraft.thePlayer.posZ));
            Minecraft.playerController.onPlayerDamageBlock(downpos, EnumFacing.UP);
        }
    }

    public void bestTool(int x, int y, int z) {
        int blockId = Block.getIdFromBlock(Minecraft.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock());
        int bestSlot = 0;
        float f = -1.0f;
        for (int i1 = 36; i1 < 45; ++i1) {
            try {
                ItemStack curSlot = Minecraft.thePlayer.inventoryContainer.getSlot(i1).getStack();
                if (!(curSlot.getItem() instanceof ItemTool) && !(curSlot.getItem() instanceof ItemSword) && !(curSlot.getItem() instanceof ItemShears) || !(curSlot.getStrVsBlock(Block.getBlockById(blockId)) > f)) continue;
                bestSlot = i1 - 36;
                f = curSlot.getStrVsBlock(Block.getBlockById(blockId));
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (f != -1.0f) {
            Minecraft.thePlayer.inventory.currentItem = bestSlot;
            Minecraft.playerController.updateController();
        }
    }
}

