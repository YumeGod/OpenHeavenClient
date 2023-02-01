/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class PlayerUtil {
    public static final Minecraft mc = Minecraft.getMinecraft();

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isMoving2() {
        if (Minecraft.thePlayer.moveForward != 0.0f) return true;
        if (Minecraft.thePlayer.moveStrafing == 0.0f) return false;
        return true;
    }

    public static void blockHit(Entity en, boolean value) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack stack = Minecraft.thePlayer.getCurrentEquippedItem();
        if (Minecraft.thePlayer.getCurrentEquippedItem() != null && en != null && value && stack.getItem() instanceof ItemSword) {
            if ((double)Minecraft.thePlayer.swingProgress > 0.2) {
                KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
            }
        }
    }

    public static boolean isHoldingSword() {
        return Minecraft.thePlayer.getCurrentEquippedItem() != null && Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

    public static boolean MovementInput() {
        return PlayerUtil.mc.gameSettings.keyBindForward.pressed || PlayerUtil.mc.gameSettings.keyBindLeft.pressed || PlayerUtil.mc.gameSettings.keyBindRight.pressed || PlayerUtil.mc.gameSettings.keyBindBack.pressed;
    }

    public static String teamColor(ICommandSender player) {
        Matcher matcher = Pattern.compile("\u00a7(.).*\u00a7r").matcher(player.getDisplayName().getFormattedText());
        return matcher.find() ? matcher.group(1) : "f";
    }

    public static boolean inTeam(ICommandSender entity0, ICommandSender entity1) {
        String s = "\u00a7" + PlayerUtil.teamColor(entity0);
        return entity0.getDisplayName().getFormattedText().contains(s) && entity1.getDisplayName().getFormattedText().contains(s);
    }
}

