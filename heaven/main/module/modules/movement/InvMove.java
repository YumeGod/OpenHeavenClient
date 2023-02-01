/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package heaven.main.module.modules.movement;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPacket;
import heaven.main.event.events.world.EventPacketSend;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.PlayerUtil;
import heaven.main.value.Mode;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

public class InvMove
extends Module {
    public static final Mode<String> mode = new Mode("Mode", new String[]{"Key", "KeepMove"}, "Key");
    public Option<Boolean> Carry = new Option<Boolean>("Carry", false);
    public Option<Boolean> Cancel = new Option<Boolean>("Cancel", false);
    public Option<Boolean> noopenpacket = new Option<Boolean>("NoOpenPacket", false);
    public Option<Boolean> fakePacket = new Option<Boolean>("FakePacket", false);

    public InvMove() {
        super("InvMove", ModuleType.Movement);
        this.addValues(mode, this.Carry, this.Cancel, this.noopenpacket, this.fakePacket);
    }

    @EventHandler
    public void onPacket(EventPacketSend event) {
        Packet packet = event.getPacket();
        if (((Boolean)this.fakePacket.get()).booleanValue()) {
            C16PacketClientStatus packetClientStatus;
            Packet p = event.getPacket();
            if (p instanceof C0DPacketCloseWindow) {
                event.setCancelled(true);
            }
            if (p instanceof C0EPacketClickWindow) {
                event.setCancelled(true);
                this.sendPacketNoEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                this.sendPacketNoEvent(event.getPacket());
                this.sendPacketNoEvent(new C0DPacketCloseWindow(Minecraft.thePlayer.inventoryContainer.windowId));
            }
            if (p instanceof C16PacketClientStatus && (packetClientStatus = (C16PacketClientStatus)event.getPacket()).getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                event.setCancelled(true);
            }
        }
        if (((Boolean)this.Carry.getValue()).booleanValue() && packet instanceof C0DPacketCloseWindow) {
            event.setCancelled(true);
        }
        if (((Boolean)this.noopenpacket.get()).booleanValue()) {
            if (event.getPacket() instanceof S2DPacketOpenWindow) {
                event.cancel();
            }
            if (event.getPacket() instanceof S2EPacketCloseWindow) {
                event.cancel();
            }
        }
    }

    @EventHandler
    public void onMove(EventPacket ep) {
        if (((Boolean)this.Cancel.getValue()).booleanValue() && InvMove.mc.currentScreen instanceof GuiInventory) {
            if (!PlayerUtil.isMoving2()) {
                return;
            }
            if (ep.isOutgoing() && ep.getPacket() instanceof C0EPacketClickWindow) {
                ep.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        if (mode.is("Key") && !(InvMove.mc.currentScreen instanceof GuiChat)) {
            KeyBinding[] key;
            KeyBinding[] array = key = new KeyBinding[]{InvMove.mc.gameSettings.keyBindForward, InvMove.mc.gameSettings.keyBindBack, InvMove.mc.gameSettings.keyBindLeft, InvMove.mc.gameSettings.keyBindRight, InvMove.mc.gameSettings.keyBindSprint, InvMove.mc.gameSettings.keyBindJump};
            int length = key.length;
            for (int i = 0; i < length; ++i) {
                KeyBinding b = array[i];
                KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown((int)b.getKeyCode()));
            }
        }
    }
}

