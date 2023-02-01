/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.gui.hud.lefthand;

import heaven.main.module.modules.render.Animations;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;

public class LeftHand {
    public static boolean use(Entity entity) {
        boolean flag = (Boolean)Animations.LeftHand.getValue();
        if (entity == null) {
            return flag;
        }
        if (!((Boolean)Animations.LeftHand.getValue()).booleanValue()) {
            return flag;
        }
        if (!(entity instanceof AbstractClientPlayer)) {
            return flag;
        }
        AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entity;
        return abstractclientplayer.inventory == null ? flag : (abstractclientplayer.inventory.getCurrentItem() == null ? flag : (abstractclientplayer.inventory.getCurrentItem().getItem() == null ? flag : abstractclientplayer.inventory.getCurrentItem().getItem() instanceof ItemBow != flag));
    }

    public static boolean use(ItemStack itemToRender) {
        boolean flag = (Boolean)Animations.LeftHand.getValue();
        return (itemToRender == null || itemToRender.getItem() == null || !(itemToRender.getItem() instanceof ItemMap)) && ((Boolean)Animations.LeftHand.getValue() == false ? flag : (itemToRender == null ? flag : (itemToRender.getItem() == null ? flag : itemToRender.getItem() instanceof ItemBow != flag)));
    }
}

