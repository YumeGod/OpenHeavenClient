/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.combat;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.management.FriendManager;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.combat.AntiBot;
import heaven.main.module.modules.misc.Teams;
import heaven.main.utils.render.MoonRender;
import heaven.main.utils.render.gl.GLUtils;
import heaven.main.value.Mode;
import heaven.main.value.Numbers;
import heaven.main.value.Option;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class AimBow
extends Module {
    private EntityLivingBase target;
    private final List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
    private final Option<Boolean> players = new Option<Boolean>("Players", true);
    private final Option<Boolean> animals = new Option<Boolean>("Animals", false);
    private final Option<Boolean> mobs = new Option<Boolean>("Mobs", false);
    private final Option<Boolean> invisibles = new Option<Boolean>("Invisibles", false);
    private final Mode<String> mode = new Mode("Mode", new String[]{"Fov", "Distance", "Health", "Cycle", "Armor"}, "Health");
    public final Numbers<Double> range = new Numbers<Double>("Range", 70.0, 1.0, 150.0, 0.1);
    public final Option<Boolean> passives = new Option<Boolean>("Passives", false);

    public AimBow() {
        super("AimBow", ModuleType.Combat);
        this.addValues(this.range, this.players, this.animals, this.mobs, this.passives, this.invisibles, this.mode);
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        if (Minecraft.thePlayer.getHeldItem() != null && Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemBow && Mouse.isButtonDown((int)1)) {
            this.target = this.getBestTarget(event.getYaw());
            if (this.target == null) {
                return;
            }
            float pitch = (float)(-Math.toDegrees(AimBow.getLaunchAngle(this.target, 3.0, 0.05f)));
            if (Double.isNaN(pitch)) {
                return;
            }
            Vec3 pos = this.predictPos(this.target, 11.0f);
            double difX = pos.xCoord - Minecraft.thePlayer.posX;
            double difZ = pos.zCoord - Minecraft.thePlayer.posZ;
            float yaw = (float)(Math.atan2(difZ, difX) * 180.0 / Math.PI) - 90.0f;
            EventPreUpdate.setYaw(yaw);
            EventPreUpdate.setPitch(pitch);
            return;
        }
        this.target = null;
    }

    @EventHandler
    public void onRender3D(EventRender3D event) {
        if (Minecraft.theWorld == null || this.target == null) {
            return;
        }
        Vec3 pos = this.predictPos(this.target, 11.0f);
        AimBow.drawEntityESP(pos.xCoord - mc.getRenderManager().getRenderPosX(), pos.yCoord + 0.55 - mc.getRenderManager().getRenderPosY(), pos.zCoord - mc.getRenderManager().getRenderPosZ(), 0.4, 0.5, new Color(14890790));
    }

    private static void drawEntityESP(double x, double y, double z, double height, double width, Color color) {
        GL11.glPushMatrix();
        GLUtils.setGLCap(3042, true);
        GLUtils.setGLCap(3553, false);
        GLUtils.setGLCap(2896, false);
        GLUtils.setGLCap(2929, false);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.8f);
        GL11.glBlendFunc((int)770, (int)771);
        GLUtils.setGLCap(2848, true);
        GL11.glDepthMask((boolean)true);
        MoonRender.BB(new AxisAlignedBB(x - width + 0.25, y + 0.1, z - width + 0.25, x + width - 0.25, y + height + 0.25, z + width - 0.25), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        MoonRender.OutlinedBB(new AxisAlignedBB(x - width + 0.25, y + 0.1, z - width + 0.25, x + width - 0.25, y + height + 0.25, z + width - 0.25), 1.0f, color.getRGB());
        GLUtils.revertAllCaps();
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private static Vec3 lerp(Vec3 pos, Vec3 prev, float time) {
        double x = pos.xCoord + (pos.xCoord - prev.xCoord) * (double)time;
        double y = pos.yCoord + (pos.yCoord - prev.yCoord) * (double)time;
        double z = pos.zCoord + (pos.zCoord - prev.zCoord) * (double)time;
        return new Vec3(x, y, z);
    }

    public Vec3 predictPos(Entity entity, float time) {
        return AimBow.lerp(new Vec3(entity.posX, entity.posY, entity.posZ), new Vec3(entity.prevPosX, entity.prevPosY, entity.prevPosZ), time);
    }

    private static float getLaunchAngle(EntityLivingBase targetEntity, double v, double g) {
        double yDif = targetEntity.posY + (double)(targetEntity.getEyeHeight() / 2.0f) - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double xDif = targetEntity.posX - Minecraft.thePlayer.posX;
        double zDif = targetEntity.posZ - Minecraft.thePlayer.posZ;
        double xCoord = Math.sqrt(xDif * xDif + zDif * zDif);
        return AimBow.theta(v, g, xCoord, yDif);
    }

    private static float theta(double v, double g, double x, double y) {
        double yv = 2.0 * y * (v * v);
        double gx = g * (x * x);
        double g2 = g * (gx + yv);
        double insqrt = v * v * v * v - g2;
        double sqrt = Math.sqrt(insqrt);
        double numerator = v * v + sqrt;
        double numerator2 = v * v - sqrt;
        double atan1 = Math.atan2(numerator, g * x);
        double atan2 = Math.atan2(numerator2, g * x);
        return (float)Math.min(atan1, atan2);
    }

    private EntityLivingBase getBestTarget(float yaw) {
        this.targets.clear();
        for (Entity e : Minecraft.theWorld.loadedEntityList) {
            EntityLivingBase ent;
            if (!(e instanceof EntityLivingBase) || !this.isValid(ent = (EntityLivingBase)e, (Double)this.range.getValue())) continue;
            this.targets.add(ent);
        }
        if (this.targets.isEmpty()) {
            return null;
        }
        this.sortTargets(yaw);
        return this.targets.get(0);
    }

    private void sortTargets(float yaw) {
        switch (this.mode.getModeAsString()) {
            case "DISTANCE": {
                this.targets.sort(Comparator.comparingDouble(Minecraft.thePlayer::getDistanceSqToEntity));
                break;
            }
            case "HEALTH": {
                this.targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            }
            case "FOV": {
                this.targets.sort(Comparator.comparingDouble(AimBow::yawDist));
                break;
            }
            case "CYCLE": {
                this.targets.sort(Comparator.comparingDouble(player -> AimBow.yawDistCycle(player, yaw)));
                break;
            }
            case "ARMOR": {
                this.targets.sort(Comparator.comparingDouble(AimBow::getArmorVal));
            }
        }
    }

    private boolean isValid(EntityLivingBase ent, double reach) {
        boolean b = ent == null ? false : (ent == Minecraft.thePlayer ? false : (ent instanceof EntityPlayer && !((Boolean)this.players.getValue()).booleanValue() ? false : ((ent instanceof EntityAnimal || ent instanceof EntitySquid) && !((Boolean)this.animals.getValue()).booleanValue() ? false : ((ent instanceof EntityMob || ent instanceof EntityVillager || ent instanceof EntityBat) && !((Boolean)this.mobs.getValue()).booleanValue() ? false : ((double)Minecraft.thePlayer.getDistanceToEntity(ent) > reach + 0.4 ? false : (ent instanceof EntityPlayer && FriendManager.isFriend(ent.getName()) ? false : (!ent.isDead && ent.getHealth() > 0.0f ? (ent.isInvisible() && !((Boolean)this.invisibles.getValue()).booleanValue() ? false : (!AntiBot.isBot(ent) ? false : !Minecraft.thePlayer.isDead && (!(ent instanceof EntityPlayer) || !Teams.isOnSameTeam(ent)))) : false)))))));
        return b;
    }

    private static double getArmorVal(EntityLivingBase ent) {
        if (ent instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)ent;
            double armorstrength = 0.0;
            for (int index = 3; index >= 0; --index) {
                ItemStack stack = player.inventory.armorInventory[index];
                if (stack == null) continue;
                armorstrength += AimBow.getArmorStrength(stack);
            }
            return armorstrength;
        }
        return 0.0;
    }

    private static double getArmorStrength(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) {
            return -1.0;
        }
        float damageReduction = ((ItemArmor)itemStack.getItem()).damageReduceAmount;
        Map<Integer, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = enchantments.get(Enchantment.protection.effectId);
            damageReduction += (float)Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }

    private static double yawDist(EntityLivingBase e) {
        Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(Minecraft.thePlayer.getPositionVector().addVector(0.0, Minecraft.thePlayer.getEyeHeight(), 0.0));
        double d = Math.abs((double)Minecraft.thePlayer.rotationYaw - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0)) % 360.0;
        return d > 180.0 ? 360.0 - d : d;
    }

    private static double yawDistCycle(EntityLivingBase e, float yaw) {
        Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(Minecraft.thePlayer.getPositionVector().addVector(0.0, Minecraft.thePlayer.getEyeHeight(), 0.0));
        return Math.abs((double)yaw - Math.atan2(difference.zCoord, difference.xCoord)) % 90.0;
    }
}

