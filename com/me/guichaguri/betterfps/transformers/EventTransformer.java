/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.transformers;

import com.me.guichaguri.betterfps.BetterFps;
import com.me.guichaguri.betterfps.IClassTransformer;
import com.me.guichaguri.betterfps.tweaker.Naming;
import java.util.Iterator;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

public class EventTransformer
implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return new byte[0];
        }
        try {
            if (Naming.C_Minecraft.is(name)) {
                return this.patchClientStart(bytes);
            }
            if (!Naming.C_KeyBinding.is(name)) {
                if (Naming.C_World.is(name)) {
                    return this.patchWorldTick(bytes);
                }
                if (Naming.C_ClientBrandRetriever.is(name)) {
                    return this.patchClientBrand(bytes);
                }
                if (Naming.C_WorldClient.is(name)) {
                    return this.patchClientWorldLoad(bytes);
                }
                if (Naming.C_DedicatedServer.is(name)) {
                    return this.patchServerStart(bytes);
                }
                if (Naming.C_IntegratedServer.is(name)) {
                    return this.patchServerStart(bytes);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    private byte[] patchClientStart(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 4);
        Iterator<MethodNode> methods = classNode.methods.iterator();
        boolean patch = false;
        while (methods.hasNext()) {
            MethodNode method = methods.next();
            if (!Naming.M_startGame.is(method.name, method.desc)) continue;
            BetterFps.log.info("Patching Game Start...");
            InsnList list = new InsnList();
            for (AbstractInsnNode node : method.instructions.toArray()) {
                if (node.getOpcode() == 177) {
                    list.add(new VarInsnNode(25, 0));
                    list.add(new MethodInsnNode(184, "me/guichaguri/betterfps/BetterFpsClient", "start", "(L" + classNode.name + ";)V", false));
                }
                list.add(node);
            }
            method.instructions.clear();
            method.instructions.add(list);
            patch = true;
        }
        if (!patch) {
            return bytes;
        }
        ClassWriter writer = new ClassWriter(1);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private byte[] patchKeyTick(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 4);
        Iterator<MethodNode> methods = classNode.methods.iterator();
        boolean patch = false;
        while (methods.hasNext()) {
            MethodNode method = methods.next();
            if (!Naming.M_onTick.is(method.name, method.desc)) continue;
            BetterFps.log.info("Patching SimpleWhiteKey Event...");
            InsnList list = new InsnList();
            for (AbstractInsnNode node : method.instructions.toArray()) {
                if (node.getOpcode() == 177) {
                    list.add(new VarInsnNode(21, 0));
                    list.add(new MethodInsnNode(184, "me/guichaguri/betterfps/BetterFpsClient", "keyEvent", "(I)V", false));
                }
                list.add(node);
            }
            method.instructions.clear();
            method.instructions.add(list);
            patch = true;
        }
        if (!patch) {
            return bytes;
        }
        ClassWriter writer = new ClassWriter(3);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private byte[] patchWorldTick(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 4);
        Iterator<MethodNode> methods = classNode.methods.iterator();
        boolean patch = false;
        while (methods.hasNext()) {
            MethodNode method = methods.next();
            if (!Naming.M_tick.is(method.name, method.desc)) continue;
            BetterFps.log.info("Patching World Event...");
            InsnList list = new InsnList();
            for (AbstractInsnNode node : method.instructions.toArray()) {
                if (node.getOpcode() == 177) {
                    list.add(new MethodInsnNode(184, "me/guichaguri/betterfps/BetterFps", "worldTick", "()V", false));
                }
                list.add(node);
            }
            method.instructions.clear();
            method.instructions.add(list);
            patch = true;
        }
        if (!patch) {
            return bytes;
        }
        ClassWriter writer = new ClassWriter(3);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public byte[] patchClientBrand(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 4);
        Iterator<MethodNode> methods = classNode.methods.iterator();
        boolean patch = false;
        while (methods.hasNext()) {
            MethodNode method = methods.next();
            if (!Naming.M_getClientModName.is(method.name, method.desc)) continue;
            BetterFps.log.info("Patching Client Brand...");
            InsnList list = new InsnList();
            for (AbstractInsnNode node : method.instructions.toArray()) {
                if (node instanceof LdcInsnNode) {
                    LdcInsnNode ldc = (LdcInsnNode)node;
                    if (ldc.cst instanceof String && ldc.cst.equals("vanilla")) {
                        ldc.cst = "BetterFps-1.2.1";
                    }
                }
                list.add(node);
            }
            method.instructions.clear();
            method.instructions.add(list);
            patch = true;
        }
        if (!patch) {
            return bytes;
        }
        ClassWriter writer = new ClassWriter(3);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public byte[] patchClientWorldLoad(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 4);
        boolean patch = false;
        for (MethodNode method : classNode.methods) {
            if (!Naming.M_Constructor.is(method.name)) continue;
            BetterFps.log.info("Patching World Client Event...");
            InsnList list = new InsnList();
            for (AbstractInsnNode node : method.instructions.toArray()) {
                if (node.getOpcode() == 177) {
                    list.add(new MethodInsnNode(184, "me/guichaguri/betterfps/BetterFpsClient", "worldLoad", "()V", false));
                }
                list.add(node);
            }
            method.instructions.clear();
            method.instructions.add(list);
            patch = true;
        }
        if (!patch) {
            return bytes;
        }
        ClassWriter writer = new ClassWriter(3);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public byte[] patchServerStart(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 4);
        boolean patch = false;
        for (MethodNode method : classNode.methods) {
            if (!Naming.M_startServer.is(method.name, method.desc)) continue;
            BetterFps.log.info("Patching Server Start Event...");
            InsnList list = new InsnList();
            list.add(new MethodInsnNode(184, "me/guichaguri/betterfps/BetterFps", "serverStart", "()V", false));
            for (AbstractInsnNode node : method.instructions.toArray()) {
                list.add(node);
            }
            method.instructions.clear();
            method.instructions.add(list);
            patch = true;
        }
        if (!patch) {
            return bytes;
        }
        ClassWriter writer = new ClassWriter(3);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}

