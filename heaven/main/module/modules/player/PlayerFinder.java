/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.player;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EntityAddedEvent;
import heaven.main.event.events.world.EventPacketReceive;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.utils.chat.Helper;
import heaven.main.value.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class PlayerFinder
extends Module {
    public final Option Thunder2 = new Option<Boolean>("Thunder2", true);
    public final Option Thunder = new Option<Boolean>("Thunder", true);
    public final Option Player = new Option<Boolean>("Player", true);

    public PlayerFinder() {
        super("PlayerFinder", ModuleType.Player);
        this.addValues(this.Thunder2, this.Thunder, this.Player);
    }

    public boolean isNCPBot(Entity v1) {
        if (this.isEnabled()) {
            StringBuilder append;
            String formattedText = v1.getDisplayName().getFormattedText();
            return formattedText.equalsIgnoreCase((append = new StringBuilder().append(v1.getName())).append("\u00a7r").toString()) && v1.getDisplayName().getFormattedText().contains("NPC");
        }
        return false;
    }

    @EventHandler
    public void onPacketReceive(EventPacketReceive packetEvent) {
        S2CPacketSpawnGlobalEntity packetIn;
        if (((Boolean)this.Thunder2.getValue()).booleanValue() && packetEvent.getPacket() instanceof S2CPacketSpawnGlobalEntity && (packetIn = (S2CPacketSpawnGlobalEntity)packetEvent.getPacket()).func_149053_g() == 1) {
            int x = packetIn.func_149051_d() / 32;
            int y = packetIn.func_149050_e() / 32;
            int z = packetIn.func_149049_f() / 32;
            ChatComponentText ChatComponent = new ChatComponentText(String.format("%s%s", (Object)((Object)EnumChatFormatting.GREEN) + Client.instance.name + " > \u96f7\u58f0\u5b9a\u4f4d" + (Object)((Object)EnumChatFormatting.GRAY), (Object)((Object)EnumChatFormatting.GOLD) + " [" + x + "," + y + "," + z + "]"));
            ChatComponent.appendSibling(new ChatComponentText((Object)((Object)EnumChatFormatting.RED) + " [\u70b9\u6211tp\u5230\u8be5\u4f4d\u7f6e]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, new StringBuilder().insert(0, ".tp " + x + " " + y + " " + z).toString())).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("[\u70b9\u6211tp\u5230\u8be5\u4f4d\u7f6e]")))));
            Minecraft.thePlayer.addChatMessage(ChatComponent);
        }
    }

    @EventHandler
    public void leishen(EventPacketReceive event) {
        if (((Boolean)this.Thunder.getValue()).booleanValue()) {
            StringBuilder append = new StringBuilder().append((Object)EnumChatFormatting.DARK_GRAY).append("[").append((Object)EnumChatFormatting.DARK_AQUA);
            if (event.getPacket() instanceof S2CPacketSpawnGlobalEntity) {
                if (event.getPacket() != Minecraft.thePlayer && Minecraft.theWorld.getEntityByID(((S2CPacketSpawnGlobalEntity)event.getPacket()).func_149052_c()) instanceof EntityLightningBolt) {
                    Helper.sendMessageWithoutPrefix(append.append("Notifier").append((Object)EnumChatFormatting.DARK_GRAY).append("] ").append((Object)EnumChatFormatting.DARK_AQUA).append("Name: ").append((Object)EnumChatFormatting.WHITE).append(((S2CPacketSpawnGlobalEntity)event.getPacket()).func_149052_c()).append(" ").append((Object)EnumChatFormatting.DARK_AQUA).append("Coords: ").append((Object)EnumChatFormatting.AQUA).append("X: ").append((Object)EnumChatFormatting.WHITE).append(((S2CPacketSpawnGlobalEntity)event.getPacket()).func_149051_d()).append((Object)EnumChatFormatting.AQUA).append(" Y: ").append((Object)EnumChatFormatting.WHITE).append(((S2CPacketSpawnGlobalEntity)event.getPacket()).func_149050_e()).append((Object)EnumChatFormatting.AQUA).append(" Z: ").append((Object)EnumChatFormatting.WHITE).append(((S2CPacketSpawnGlobalEntity)event.getPacket()).func_149049_f()).toString());
                }
            }
        }
    }

    @EventHandler
    public void onEntityAdded(EntityAddedEvent v1) {
        if (((Boolean)this.Player.getValue()).booleanValue() && v1.getEntity() instanceof EntityPlayer && v1.getEntity() != Minecraft.thePlayer && !this.isNCPBot(v1.getEntity())) {
            String formattedText = v1.getEntity().getDisplayName().getFormattedText();
            if (!formattedText.contains("[NPC]")) {
                StringBuilder sb = new StringBuilder();
                Helper.sendMessage(sb.append("target detected: ").append(v1.getEntity().getName()).toString());
            }
            StringBuilder append = new StringBuilder().append((Object)EnumChatFormatting.DARK_GRAY).append("[").append((Object)EnumChatFormatting.DARK_AQUA);
            Helper.sendMessageWithoutPrefix(append.append("Notifier").append((Object)EnumChatFormatting.DARK_GRAY).append("] ").append((Object)EnumChatFormatting.DARK_AQUA).append("Name: ").append((Object)EnumChatFormatting.WHITE).append(v1.getEntity().getDisplayName().getFormattedText()).append(" ").append((Object)EnumChatFormatting.DARK_AQUA).append("Coords: ").append((Object)EnumChatFormatting.AQUA).append("X: ").append((Object)EnumChatFormatting.WHITE).append((int)v1.getEntity().posX).append((Object)EnumChatFormatting.AQUA).append(" Y: ").append((Object)EnumChatFormatting.WHITE).append((int)v1.getEntity().posY).append((Object)EnumChatFormatting.AQUA).append(" Z: ").append((Object)EnumChatFormatting.WHITE).append((int)v1.getEntity().posZ).toString());
        }
    }
}

