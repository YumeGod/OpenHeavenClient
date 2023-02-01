/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastBow
extends Module {
    private final Option<Boolean> onlyground = new Option<Boolean>("OnlyGround", false);

    public FastBow() {
        super("FastBow", ModuleType.Combat);
        this.addValues(this.onlyground);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static boolean canConsume() {
        if (Minecraft.thePlayer.inventory.getCurrentItem() == null) return false;
        if (!(Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow)) return false;
        return true;
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        if (((Boolean)this.onlyground.getValue()).booleanValue()) {
            if (!Minecraft.thePlayer.onGround) {
                return;
            }
        }
        if (FastBow.canConsume() && FastBow.mc.gameSettings.keyBindUseItem.pressed) {
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
            for (int i = 0; i < 20; ++i) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-9, Minecraft.thePlayer.posZ, true));
            }
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    @EventHandler
    public void onRecieve(EventPacketReceive event) {
        if (event.getPacket() instanceof S18PacketEntityTeleport) {
            S18PacketEntityTeleport packet = (S18PacketEntityTeleport)event.getPacket();
            if (Minecraft.thePlayer != null) {
                packet.yaw = (byte)Minecraft.thePlayer.rotationYaw;
            }
            assert (Minecraft.thePlayer != null);
            packet.pitch = (byte)Minecraft.thePlayer.rotationPitch;
        }
    }
}

