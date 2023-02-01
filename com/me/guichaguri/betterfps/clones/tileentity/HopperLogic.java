/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.clones.tileentity;

import com.me.guichaguri.betterfps.transformers.cloner.CopyMode;
import com.me.guichaguri.betterfps.transformers.cloner.Named;
import com.me.guichaguri.betterfps.tweaker.Naming;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class HopperLogic
extends TileEntityHopper {
    public IInventory topInventory = null;
    public int topBlockUpdate = 1;
    public boolean canPickupDrops = true;
    public boolean isOnTransferCooldown = false;

    @Named(value=Naming.M_captureDroppedItems)
    public static boolean captureDroppedItems(IHopper hopper) {
        block7: {
            HopperLogic hopperTE;
            block6: {
                IInventory iinventory;
                hopperTE = hopper.getClass() == TileEntityHopper.class ? (HopperLogic)hopper : null;
                IInventory iInventory = iinventory = hopperTE == null ? HopperLogic.getHopperInventory(hopper) : hopperTE.topInventory;
                if (iinventory == null) break block6;
                EnumFacing enumfacing = EnumFacing.DOWN;
                if (HopperLogic.isInventoryEmpty(iinventory, enumfacing)) {
                    return false;
                }
                if (iinventory instanceof ISidedInventory) {
                    ISidedInventory isidedinventory = (ISidedInventory)iinventory;
                    int[] aint = isidedinventory.getSlotsForFace(enumfacing);
                    for (int i = 0; i < aint.length; ++i) {
                        if (!HopperLogic.pullItemFromSlot(hopper, iinventory, aint[i], enumfacing)) continue;
                        return true;
                    }
                } else {
                    int j = iinventory.getSizeInventory();
                    for (int k = 0; k < j; ++k) {
                        if (!HopperLogic.pullItemFromSlot(hopper, iinventory, k, enumfacing)) continue;
                        return true;
                    }
                }
                break block7;
            }
            if (hopperTE != null && !hopperTE.canPickupDrops) break block7;
            for (EntityItem entityitem : HopperLogic.func_181556_a(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0, hopper.getZPos())) {
                if (!HopperLogic.putDropInInventoryAllSlots(hopper, entityitem)) continue;
                return true;
            }
        }
        return false;
    }

    @CopyMode(value=CopyMode.Mode.IGNORE)
    public HopperLogic() {
    }

    @Override
    public void update() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            --this.transferCooldown;
            boolean bl = this.isOnTransferCooldown = this.transferCooldown > 0;
            if (!this.isOnTransferCooldown) {
                this.setTransferCooldown(2);
                this.updateHopper();
                if (this.topBlockUpdate-- <= 0) {
                    this.checkBlockOnTop();
                    this.topBlockUpdate = 120;
                }
            }
        }
    }

    @Override
    public boolean isOnTransferCooldown() {
        return this.isOnTransferCooldown;
    }

    public void checkBlockOnTop() {
        BlockPos topPos = new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
        this.canPickupDrops = !this.worldObj.getBlockState(topPos).getBlock().isOpaqueCube();
        this.topInventory = HopperLogic.getHopperInventory(this);
    }
}

