/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.More.PacketEvent;
import heaven.main.event.events.rendering.EventRender2D;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.event.events.world.WorldLoadEvent;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.value.Numbers;
import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class PlayerList
extends Module {
    public final Numbers<Double> xv = new Numbers<Double>("X", 50.0, 0.0, 1000.0, 10.0);
    public final Numbers<Double> yv = new Numbers<Double>("Y", 50.0, 0.0, 1000.0, 10.0);
    public PlayerListObject king;
    public List<PlayerListObject> players = new CopyOnWriteArrayList<PlayerListObject>();
    private final String[] messages = new String[]{"was shot by", "took the L to", "was filled full of lead by", "was crushed into moon dust by", "was killed by ", "was thrown into the void by ", "be sent to Davy Jones' locker by", "was turned to dust by", "was thrown off a cliff by ", "was deleted by ", "was purified by ", "was turned into space dust by", "was given the cold shoulder by", "was socked by", "was oinked by"};

    public PlayerList() {
        super("PlayerList", ModuleType.Render);
        this.addValues(this.xv, this.yv);
    }

    @Override
    public void onDisable() {
        this.players.clear();
        this.king = null;
    }

    @EventHandler
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            boolean flag = false;
            String message = ((S02PacketChat)event.getPacket()).getChatComponent().getUnformattedText();
            if (Minecraft.thePlayer == null) {
                return;
            }
            String playerName = null;
            try {
                for (String s : this.messages) {
                    if (!message.contains(s)) continue;
                    playerName = message.split(s)[1];
                    flag = true;
                }
                if (!flag) {
                    String test;
                    if (message.contains("\ufffd\ufffd\ufffd\ufffd! \ufffd\ufffd ") && message.contains(" \ufffd\ufffd\u0271")) {
                        test = message.split("] ")[1];
                        playerName = test.split(" \ufffd\ufffd\u0271")[0];
                    }
                    if (message.contains("\u0271\ufffd\ufffd\ufffd\ufffd")) {
                        playerName = message.split("\\[")[0];
                    }
                    if (message.contains("\ufffd\ufffd\u0271\ufffd\ufffd")) {
                        test = message.split("\ufffd\ufffd\u0271\ufffd\u07e3\ufffd ")[1];
                        playerName = test.split("\\[")[0];
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (playerName == null || playerName.isEmpty()) {
                return;
            }
            if (!this.players.isEmpty()) {
                for (PlayerListObject player : this.players) {
                    if (!player.name.equalsIgnoreCase(playerName)) continue;
                    ++player.kills;
                    break;
                }
            } else {
                this.players.add(new PlayerListObject(playerName, 1));
            }
        }
    }

    @EventHandler
    public void onWorld(WorldLoadEvent event) {
        if (Minecraft.thePlayer != null) {
            if (!Minecraft.thePlayer.isDead) {
                this.players.clear();
            }
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (Minecraft.thePlayer.ticksExisted < 1) {
            this.players.clear();
            this.king = null;
        }
    }

    @EventHandler
    public void onRender2D(EventRender2D e) {
        if (Minecraft.thePlayer.isDead) {
            this.players.clear();
            this.king = null;
        }
        float textY = ((Double)this.xv.get()).floatValue();
        float x = ((Double)this.xv.get()).floatValue();
        float y = ((Double)this.yv.get()).floatValue();
        RenderUtil.drawRect(x, y, x + (float)Minecraft.fontRendererObj.getStringWidth("PlayerList") + 84.0f, y + (float)Minecraft.fontRendererObj.FONT_HEIGHT + 3.0f, new Color(21, 19, 23, 255).getRGB());
        Minecraft.fontRendererObj.drawString("Player List", (int)x + 3, (int)y + 2, new Color(255, 255, 255).getRGB());
        this.players.sort((o1, o2) -> o2.kills - o1.kills);
        for (PlayerListObject player : this.players) {
            String killString;
            if (player == this.players.get(0)) {
                this.king = player;
            }
            RenderUtil.drawRect(x, textY + (float)Minecraft.fontRendererObj.FONT_HEIGHT + 3.0f, x + (float)Minecraft.fontRendererObj.getStringWidth("PlayerList") + 84.0f, textY + (float)Minecraft.fontRendererObj.FONT_HEIGHT + 13.0f, new Color(30, 30, 35, 170).getRGB());
            if (player == this.king) {
                RenderUtil.drawImage(new ResourceLocation("client/PlayerList.png"), (int)(x + 1.0f), (int)y + 11, 10, 10);
                Minecraft.fontRendererObj.drawString((Object)((Object)EnumChatFormatting.YELLOW) + player.name, (int)(x + (float)(player == this.king ? 14 : 3)), (int)((float)Minecraft.fontRendererObj.FONT_HEIGHT + 2.0f + textY) + 1, -1);
            } else {
                Minecraft.fontRendererObj.drawString(player.name, (int)(x + 3.0f), (int)((float)Minecraft.fontRendererObj.FONT_HEIGHT + 2.0f + textY) + 2, -1);
            }
            switch (player.kills) {
                case -1: 
                case 0: 
                case 1: {
                    killString = "kill";
                    break;
                }
                default: {
                    killString = "kills";
                }
            }
            Minecraft.fontRendererObj.drawString(player.kills + " " + killString, (int)(x + (float)Minecraft.fontRendererObj.getStringWidth("PlayerList") + 83.0f - (float)Minecraft.fontRendererObj.getStringWidth(player.kills + killString) - 2.0f - (float)(killString.equalsIgnoreCase("kill") ? 3 : 1)) - 2, (int)((float)(Minecraft.fontRendererObj.FONT_HEIGHT + 3) + textY), -1);
            textY += 10.0f;
        }
    }

    private static class PlayerListObject {
        public String name;
        public int kills;

        public PlayerListObject(String name, int kills) {
            this.name = name;
            this.kills = kills;
        }
    }
}

