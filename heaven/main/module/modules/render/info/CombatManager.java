/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render.info;

import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventAttack;
import heaven.main.event.events.world.EventPacketSend;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.server.S45PacketTitle;

public class CombatManager {
    private int killCounts;
    private int second;
    private int minute;
    private int hour;
    private int totalPlayed;
    private int win;
    private long startTime;

    @EventHandler
    private void onSendPacket(EventAttack e) {
        Entity syncEntity = e.getEntity();
        if (syncEntity != null && syncEntity.isDead) {
            ++this.killCounts;
        }
    }

    @EventHandler
    private void onSendPacket(EventPacketSend e) {
        Packet packet = e.getPacket();
        if (packet instanceof C00Handshake) {
            this.startTime = System.currentTimeMillis();
            this.second = 0;
            this.minute = 0;
            this.hour = 0;
        }
        if (packet instanceof S45PacketTitle) {
            S45PacketTitle packetTitle = (S45PacketTitle)packet;
            String title = packetTitle.getMessage().getFormattedText();
            if (title.startsWith("\u00a76\u00a7l") && title.endsWith("\u00a7r") || title.startsWith("\u00a7c\u00a7lYOU") && title.endsWith("\u00a7r") || title.startsWith("\u00a7c\u00a7lGame") && title.endsWith("\u00a7r") || title.startsWith("\u00a7c\u00a7lWITH") && title.endsWith("\u00a7r") || title.startsWith("\u00a7c\u00a7lYARR") && title.endsWith("\u00a7r")) {
                ++this.totalPlayed;
            }
            if (title.startsWith("\u00a76\u00a7l") && title.endsWith("\u00a7r")) {
                ++this.win;
            }
        }
    }

    public int getKillCounts() {
        return this.killCounts;
    }

    public int getSecond() {
        return this.second;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getHour() {
        return this.hour;
    }

    public int getTotalPlayed() {
        return this.totalPlayed;
    }

    public int getWin() {
        return this.win;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return this.startTime;
    }
}

