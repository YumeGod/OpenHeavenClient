/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.transformers;

import com.me.guichaguri.betterfps.IClassTransformer;
import com.me.guichaguri.betterfps.tweaker.Naming;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class WeakerTransformer
implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return bytes;
    }

    public byte[] patchWeakKeys(byte[] bytes, Naming[] fieldsToWeak) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 4);
        boolean patch = false;
        block0: for (FieldNode field : classNode.fields) {
            for (Naming f : fieldsToWeak) {
                if (!f.is(field.name, field.desc)) continue;
                String oldDesc = field.desc;
                field.desc = "Ljava/lang/ref/WeakReference;";
                field.signature = "Ljava/lang/ref/WeakReference<" + oldDesc + ">;";
                patch = true;
                continue block0;
            }
        }
        for (MethodNode method : classNode.methods) {
            InsnList newList = new InsnList();
            AbstractInsnNode n = null;
            block3: for (AbstractInsnNode node : method.instructions.toArray()) {
                if (n != null) {
                    newList.add(n);
                }
                n = node;
                if (!(node instanceof FieldInsnNode)) continue;
                FieldInsnNode fNode = (FieldInsnNode)node;
                if (!fNode.owner.equals(classNode.name)) continue;
                for (Naming f : fieldsToWeak) {
                    if (!f.is(fNode.name, fNode.desc)) continue;
                    fNode.desc = "Ljava/lang/ref/WeakReference;";
                    if (fNode.getOpcode() == 181) {
                        newList.add(new MethodInsnNode(183, "java/lang/ref/WeakReference", "<init>", "(Ljava/lang/Object;)V", false));
                        newList.add(fNode);
                        n = null;
                    } else if (fNode.getOpcode() == 180) {
                        newList.add(fNode);
                        newList.add(new MethodInsnNode(182, "java/lang/ref/WeakReference", "get", "()Ljava/lang/Object;", false));
                        n = null;
                    }
                    patch = true;
                    continue block3;
                }
            }
            if (n != null) {
                newList.add(n);
            }
            method.instructions.clear();
            method.instructions.add(newList);
        }
        if (patch) {
            ClassWriter writer = new ClassWriter(3);
            classNode.accept(writer);
            return writer.toByteArray();
        }
        return bytes;
    }
}

