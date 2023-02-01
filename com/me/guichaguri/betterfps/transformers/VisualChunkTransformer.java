/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.transformers;

import com.me.guichaguri.betterfps.ASMUtils;
import com.me.guichaguri.betterfps.BetterFps;
import com.me.guichaguri.betterfps.IClassTransformer;
import com.me.guichaguri.betterfps.tweaker.Naming;
import java.util.ArrayList;
import java.util.HashMap;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.FrameNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.LocalVariableNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

public class VisualChunkTransformer
implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return bytes;
        }
        if (Naming.C_WorldServer.is(name)) {
            ClassNode node = ASMUtils.readClass(bytes, 0);
            for (MethodNode m : node.methods) {
                if (!Naming.M_updateBlocks.is(m.name, m.desc)) continue;
                BetterFps.log.info("PATCH TICK +++++++++++++++++++++++++ " + node.name);
                this.patchTick(m, "thunder");
            }
            return ASMUtils.writeClass(node, 3);
        }
        if (Naming.C_WorldClient.is(name)) {
            ClassNode node = ASMUtils.readClass(bytes, 0);
            for (MethodNode m : node.methods) {
                if (!Naming.M_updateBlocks.is(m.name, m.desc)) continue;
                BetterFps.log.info("PATCH TICK +++++++++++++++++++++++++ " + node.name);
            }
            return ASMUtils.writeClass(node, 3);
        }
        if (Naming.C_World.is(name)) {
            ClassNode node = ASMUtils.readClass(bytes, 0);
            for (MethodNode m : node.methods) {
                if (!Naming.M_setActivePlayerChunksAndCheckLight.is(m.name, m.desc)) continue;
                this.patchTickableCheck(m);
            }
            return ASMUtils.writeClass(node, 3);
        }
        if (Naming.C_ChunkCoordIntPair.is(name)) {
            ClassNode node = ASMUtils.readClass(bytes, 0);
            this.patchChunk(node);
            return ASMUtils.writeClass(node, 3);
        }
        return bytes;
    }

    private void patchChunk(ClassNode node) {
        node.fields.add(new FieldNode(1, "isTickable", "Z", null, null));
        MethodNode m = new MethodNode(1, "<init>", "(IIZ)V", null, null);
        m.instructions = new InsnList();
        LabelNode l1 = new LabelNode();
        m.instructions.add(l1);
        m.instructions.add(new VarInsnNode(25, 0));
        m.instructions.add(new VarInsnNode(21, 1));
        m.instructions.add(new VarInsnNode(21, 2));
        m.instructions.add(new MethodInsnNode(183, node.name, "<init>", "(II)V", false));
        m.instructions.add(new VarInsnNode(25, 0));
        m.instructions.add(new VarInsnNode(21, 3));
        m.instructions.add(new FieldInsnNode(181, node.name, "isTickable", "Z"));
        LabelNode l2 = new LabelNode();
        m.instructions.add(l2);
        m.instructions.add(new InsnNode(177));
        m.localVariables.clear();
        m.localVariables.add(new LocalVariableNode("this", "L" + node.name + ";", null, l1, l2, 0));
        m.localVariables.add(new LocalVariableNode("f1", "I", null, l1, l2, 1));
        m.localVariables.add(new LocalVariableNode("f2", "I", null, l1, l2, 2));
        m.localVariables.add(new LocalVariableNode("f3", "Z", null, l1, l2, 3));
        node.methods.add(m);
    }

    private void patchTickableCheck(MethodNode method) {
        InsnList list = new InsnList();
        for (AbstractInsnNode node : method.instructions.toArray()) {
            if (node instanceof MethodInsnNode) {
                MethodInsnNode m = (MethodInsnNode)node;
                if (Naming.C_ChunkCoordIntPair.isASM(m.owner) && Naming.M_Constructor.is(m.name)) {
                    BetterFps.log.info("Patching tickable chunks check...");
                    list.add(new VarInsnNode(21, 6));
                    list.add(new VarInsnNode(21, 7));
                    list.add(new MethodInsnNode(184, "me/guichaguri/betterfps/BetterFps", "isTickable", "(II)Z", false));
                    m.desc = "(IIZ)V";
                }
            }
            list.add(node);
        }
        method.instructions.clear();
        method.instructions.add(list);
    }

    private HashMap<FrameNode, LabelNode> getFrames(HashMap<FrameNode, LabelNode> hm, AbstractInsnNode[] list, FrameNode after) {
        FrameNode frame = null;
        LabelNode lastLabel = null;
        for (AbstractInsnNode node : list) {
            if (node instanceof FrameNode) {
                if (node.getOpcode() != 3) {
                    if (after != null && after != node) continue;
                    after = null;
                    if (frame == null) {
                        frame = (FrameNode)node;
                        continue;
                    }
                    this.getFrames(hm, list, (FrameNode)node);
                    continue;
                }
                hm.put(frame, lastLabel);
                frame = null;
                continue;
            }
            if (!(node instanceof LabelNode)) continue;
            lastLabel = (LabelNode)node;
        }
        return hm;
    }

    private void patchTick(MethodNode method, String afterLdcStr) {
        String coordName = null;
        ArrayList<Integer> lvs = new ArrayList<Integer>();
        for (LocalVariableNode node : method.localVariables) {
            String c;
            int s = node.desc.length();
            if (s <= 2 || !Naming.C_ChunkCoordIntPair.isASM(c = node.desc.substring(1, s - 1))) continue;
            coordName = c;
            lvs.add(node.index);
            BetterFps.log.info("PATCH TICK ----------------------- " + node.index + "  - " + method.name);
        }
        if (lvs.isEmpty()) {
            return;
        }
        InsnList list = new InsnList();
        boolean afterLdc = afterLdcStr != null;
        AbstractInsnNode[] instList = method.instructions.toArray();
        HashMap<FrameNode, LabelNode> frames = this.getFrames(new HashMap<FrameNode, LabelNode>(), instList, null);
        VarInsnNode lastVar = null;
        FrameNode frame = null;
        for (AbstractInsnNode node : instList) {
            list.add(node);
            if (afterLdc && node instanceof LdcInsnNode) {
                if (!afterLdcStr.equals(((LdcInsnNode)node).cst) || lastVar == null) continue;
                BetterFps.log.info((Object)frames.size());
                this.addTickCheck(list, lastVar, coordName, frames.get(frame));
                continue;
            }
            if (node.getOpcode() == 58 && node instanceof VarInsnNode) {
                VarInsnNode var = (VarInsnNode)node;
                if (!lvs.contains(var.var)) continue;
                if (afterLdc) {
                    lastVar = var;
                    continue;
                }
                BetterFps.log.info((Object)frames.size());
                this.addTickCheck(list, var, coordName, frames.get(frame));
                continue;
            }
            if (!(node instanceof FrameNode)) continue;
            frame = node.getOpcode() == 3 ? null : (FrameNode)node;
        }
        method.instructions.clear();
        method.instructions.add(list);
    }

    private void addTickCheck(InsnList list, VarInsnNode var, String coordName, LabelNode loop) {
        list.add(new VarInsnNode(25, var.var));
        list.add(new FieldInsnNode(180, coordName, "isTickable", "Z"));
        LabelNode l1 = new LabelNode();
        list.add(new JumpInsnNode(153, l1));
        LabelNode l2 = new LabelNode();
        list.add(l2);
        if (loop != null) {
            BetterFps.log.info("----------- LOOP: " + loop.getOpcode());
            list.add(new JumpInsnNode(167, loop));
        } else {
            BetterFps.log.info("----------- RETURN ");
            list.add(new InsnNode(177));
        }
        list.add(l1);
        list.add(new FrameNode(1, 1, new Object[]{coordName}, 0, null));
    }
}

