/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package heaven.main.module.modules.render;

import com.mojang.authlib.GameProfile;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class FreeCam
extends Module {
    private EntityOtherPlayerMP freecamEntity;

    public FreeCam() {
        super("FreeCam", ModuleType.Render);
    }

    @Override
    public void onDisable() {
        Minecraft.thePlayer.setPositionAndRotation(this.freecamEntity.posX, this.freecamEntity.posY, this.freecamEntity.posZ, this.freecamEntity.rotationYaw, this.freecamEntity.rotationPitch);
        Minecraft.theWorld.removeEntityFromWorld(this.freecamEntity.getEntityId());
        FreeCam.mc.renderGlobal.loadRenderers();
        Minecraft.thePlayer.noClip = false;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        if (Minecraft.thePlayer != null) {
            this.freecamEntity = new EntityOtherPlayerMP(Minecraft.theWorld, new GameProfile(new UUID(69L, 96L), Minecraft.thePlayer.getName()));
            this.freecamEntity.inventory = Minecraft.thePlayer.inventory;
            this.freecamEntity.inventoryContainer = Minecraft.thePlayer.inventoryContainer;
            this.freecamEntity.setPositionAndRotation(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch);
            this.freecamEntity.rotationYawHead = Minecraft.thePlayer.rotationYawHead;
            Minecraft.theWorld.addEntityToWorld(this.freecamEntity.getEntityId(), this.freecamEntity);
            FreeCam.mc.renderGlobal.loadRenderers();
            super.onEnable();
        }
    }
}

