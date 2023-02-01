/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.combat;

import heaven.main.Client;
import heaven.main.event.EventHandler;
import heaven.main.event.events.world.EventPostUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot
extends Module {
    private static final Mode<String> modeValue = new Mode("Mode", new String[]{"Hypixel"}, "Hypixel");
    private static final Numbers<Double> livingTicksValue = new Numbers<Double>("Living Ticks", 80.0, 0.0, 100.0, 10.0);
    private static final ArrayList<Integer> ground = new ArrayList();

    public AntiBot() {
        super("AntiBot", new String[]{"AntiBot"}, ModuleType.Combat);
        this.addValues(modeValue, livingTicksValue);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ground.clear();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ground.clear();
    }

    @EventHandler
    void onUpdate(EventPostUpdate event) {
        this.setSuffix((Serializable)modeValue.getValue());
        if (Minecraft.thePlayer.ticksExisted <= 1) {
            ground.clear();
        }
        if (modeValue.is("Hypixel")) {
            for (EntityPlayer entity : Minecraft.theWorld.playerEntities) {
                this.removeHypixelBot(entity);
            }
        }
        ground.addAll(this.getLivingPlayers().stream().filter(entityPlayer -> entityPlayer.onGround && !ground.contains(entityPlayer.getEntityId())).map(Entity::getEntityId).collect(Collectors.toList()));
    }

    private ArrayList<EntityPlayer> getLivingPlayers() {
        return (ArrayList)Arrays.asList(Minecraft.theWorld.loadedEntityList.stream().filter(entity -> entity instanceof EntityPlayer).filter(entity -> entity != Minecraft.thePlayer).map(entity -> (EntityPlayer)entity).toArray(EntityPlayer[]::new));
    }

    private static boolean inTab(EntityLivingBase entity) {
        for (NetworkPlayerInfo item : mc.getNetHandler().getPlayerInfoMap()) {
            NetworkPlayerInfo playerInfo = item;
            if (playerInfo == null || playerInfo.getGameProfile() == null || !playerInfo.getGameProfile().getName().contains(entity.getName())) continue;
            return true;
        }
        return false;
    }

    private static boolean isMineplexNPC(EntityLivingBase entity) {
        String custom = entity.getCustomNameTag();
        if (entity instanceof EntityPlayer && !(entity instanceof EntityPlayerSP)) {
            return Minecraft.thePlayer.ticksExisted > 40 && custom.equals("");
        }
        return false;
    }

    private static boolean isHypixelNPC(EntityLivingBase entity) {
        String formatted = entity.getDisplayName().getFormattedText();
        if (!formatted.startsWith("\u00a7") && formatted.endsWith("\u00a7r")) {
            return true;
        }
        if (ground.contains(entity.getEntityId())) {
            return true;
        }
        return formatted.contains("\u00a78[NPC]");
    }

    private boolean removeHypixelBot(EntityLivingBase entity) {
        if (entity instanceof EntityWither && entity.isInvisible()) {
            return true;
        }
        if (!AntiBot.inTab(entity) && !AntiBot.isHypixelNPC(entity) && entity.isEntityAlive() && entity != Minecraft.thePlayer) {
            Minecraft.theWorld.removeEntity(entity);
            return true;
        }
        return false;
    }

    public static boolean isBot(EntityLivingBase entity) {
        if (!Client.instance.getModuleManager().getModuleByClass(AntiBot.class).isEnabled() || entity == Minecraft.thePlayer) {
            return false;
        }
        if ((modeValue.is("Living") || modeValue.is("Advanced")) && entity.ticksExisted > ((Number)livingTicksValue.getValue()).intValue()) {
            return true;
        }
        if (modeValue.is("Advanced") && !ground.contains(entity.getEntityId())) {
            return true;
        }
        if (modeValue.is("BrokenID") && entity.getEntityId() > 1000000) {
            return true;
        }
        if (modeValue.is("Tab") && !AntiBot.inTab(entity)) {
            return true;
        }
        if (modeValue.is("Hypixel") && AntiBot.isHypixelNPC(entity)) {
            return true;
        }
        return modeValue.is("Mineplex") && AntiBot.isMineplexNPC(entity);
    }
}

