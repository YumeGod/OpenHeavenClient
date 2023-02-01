/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package heaven.main.module.modules.render;

import heaven.main.event.EventHandler;
import heaven.main.event.events.rendering.EventRender3D;
import heaven.main.event.events.world.EventPreUpdate;
import heaven.main.module.Module;
import heaven.main.module.ModuleType;
import heaven.main.module.modules.render.AStarNode;
import heaven.main.ui.gui.clickgui.RenderUtil;
import heaven.main.utils.render.color.Colors;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class Path
extends Module {
    public EntityPlayer player;
    private final ArrayList<AStarNode> openList = new ArrayList();
    private final ArrayList<AStarNode> closedList = new ArrayList();
    private final ArrayList<AStarNode> path = new ArrayList();
    private boolean startNextThread = true;

    public Path() {
        super("Path", new String[]{"path"}, ModuleType.Render);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        this.setEntity();
        if (this.player != null && this.startNextThread) {
            this.setEntity();
            this.startNextThread = false;
            this.openList.clear();
            this.closedList.clear();
            this.path.clear();
            Runnable run = this::astar;
            new Thread(run).start();
        }
    }

    @EventHandler
    public void onRender(EventRender3D event) {
        if (this.path.size() > 2) {
            double x = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * (double)Path.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            double y = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * (double)Path.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            double z = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * (double)Path.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            double playerX = Minecraft.thePlayer.lastTickPosX + (Minecraft.thePlayer.posX - Minecraft.thePlayer.lastTickPosX) * (double)Path.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            double playerY = Minecraft.thePlayer.lastTickPosY + (Minecraft.thePlayer.posY - Minecraft.thePlayer.lastTickPosY) * (double)Path.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            double playerZ = Minecraft.thePlayer.lastTickPosZ + (Minecraft.thePlayer.posZ - Minecraft.thePlayer.lastTickPosZ) * (double)Path.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)2.85f);
            RenderUtil.color(Colors.WHITE.c);
            GL11.glLoadIdentity();
            boolean bobbing = Path.mc.gameSettings.viewBobbing;
            Path.mc.gameSettings.viewBobbing = false;
            Path.mc.entityRenderer.orientCamera(Path.mc.timer.renderPartialTicks);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)x, (double)(y + (double)this.player.getEyeHeight()), (double)z);
            GL11.glVertex3d((double)x, (double)y, (double)z);
            for (AStarNode o : this.path) {
                GL11.glVertex3d((double)(o.getX() - RenderManager.renderPosX), (double)(this.player.posY - RenderManager.renderPosY), (double)(o.getZ() - RenderManager.renderPosZ));
            }
            GL11.glVertex3d((double)playerX, (double)playerY, (double)playerZ);
            GL11.glEnd();
            Path.mc.gameSettings.viewBobbing = bobbing;
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
    }

    private void astar() {
        this.openList.clear();
        this.closedList.clear();
        this.path.clear();
        double pX = (int)Minecraft.thePlayer.posX;
        double pZ = (int)Minecraft.thePlayer.posZ;
        double eX = (int)this.player.posX;
        double eZ = (int)this.player.posZ;
        AStarNode startNode = new AStarNode(pX, pZ);
        this.openList.add(startNode);
        if (pX != eX || pZ != eZ) {
            long start = System.currentTimeMillis();
            while (!this.openList.isEmpty()) {
                AStarNode newNode;
                int nextNode = -1;
                int distance = Integer.MAX_VALUE;
                int size = this.openList.size();
                for (int node = 0; node < size; ++node) {
                    newNode = this.openList.get(node);
                    newNode.setHeuristic(Path.getHeuristic(eX, eZ, newNode.getX(), newNode.getZ()));
                    if (nextNode != -1 && !(newNode.getHeuristic() < (double)distance)) continue;
                    nextNode = node;
                    distance = (int)newNode.getHeuristic();
                }
                AStarNode var23 = this.openList.get(nextNode);
                this.closedList.add(var23);
                this.openList.remove(nextNode);
                newNode = this.closedList.get(this.closedList.size() - 1);
                AStarNode lastNode = null;
                block2: for (double pathPoint = newNode.getX() - 1.0; pathPoint <= newNode.getX() + 1.0; pathPoint += 1.0) {
                    for (double z = newNode.getZ() - 1.0; z <= newNode.getZ() + 1.0; z += 1.0) {
                        if (this.isObsctacle(pathPoint, z) || this.isInClosedList(pathPoint, z)) continue;
                        AStarNode neighbourNode = new AStarNode(pathPoint, z);
                        neighbourNode.setParent(newNode);
                        this.openList.add(neighbourNode);
                        if (pathPoint != eX || z != eZ) continue;
                        lastNode = neighbourNode;
                        break block2;
                    }
                }
                if (lastNode != null) {
                    AStarNode var24 = lastNode;
                    this.path.add(lastNode);
                    while ((var24 = var24.getParent()) != null) {
                        this.path.add(var24);
                    }
                    break;
                }
                if (System.currentTimeMillis() - 1000L <= start) continue;
                break;
            }
        }
        this.startNextThread = true;
    }

    private boolean isInClosedList(double x, double z) {
        AStarNode node;
        Iterator<AStarNode> var6 = this.closedList.iterator();
        do {
            if (var6.hasNext()) continue;
            return false;
        } while ((node = var6.next()).getX() != x || node.getZ() != z);
        return true;
    }

    private static int getHeuristic(double x1, double z1, double x2, double z2) {
        return (int)(Math.abs(x2 - x1) + Math.abs(z2 - z1));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isObsctacle(double x, double z) {
        BlockPos pos = new BlockPos(x, (double)((int)this.player.posY), z);
        if (!(Minecraft.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) return true;
        if (!(Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockAir)) return false;
        if (!(Minecraft.theWorld.getBlockState(pos.add(0, -2, 0)).getBlock() instanceof BlockAir)) return false;
        return true;
    }

    public void setEntity() {
        EntityPlayer newPlayer = null;
        for (EntityPlayer player : Minecraft.theWorld.playerEntities) {
            if (Minecraft.thePlayer == player || player.isInvisible() || player.isDead) continue;
            if (newPlayer == null) {
                newPlayer = player;
                continue;
            }
            if (!(Minecraft.thePlayer.getDistanceToEntity(player) < Minecraft.thePlayer.getDistanceToEntity(newPlayer))) continue;
            newPlayer = player;
        }
        this.player = newPlayer;
    }
}

