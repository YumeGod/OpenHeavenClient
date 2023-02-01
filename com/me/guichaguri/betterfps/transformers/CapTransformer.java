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
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class CapTransformer
implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (Naming.C_PrimedTNT.is(name)) {
            return this.patchEntityUpdateCap(bytes, "TNT_TICKS", "MAX_TNT_TICKS");
        }
        return bytes;
    }

    private byte[] patchEntityUpdateCap(byte[] bytes, String fieldName, String maxFieldName) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 4);
        Iterator<MethodNode> methods = classNode.methods.iterator();
        boolean patch = false;
        while (methods.hasNext()) {
            MethodNode method = methods.next();
            if (!Naming.M_onUpdate.is(method.name, method.desc)) continue;
            BetterFps.log.info("Patching Entity Cap... (" + classNode.name + ")");
            InsnList list = new InsnList();
            boolean b = false;
            for (AbstractInsnNode node : method.instructions.toArray()) {
                if (!b && node instanceof LabelNode) {
                    list.add(new FieldInsnNode(178, "me/guichaguri/betterfps/BetterFps", fieldName, "I"));
                    list.add(new InsnNode(89));
                    list.add(new InsnNode(4));
                    list.add(new InsnNode(96));
                    list.add(new FieldInsnNode(179, "me/guichaguri/betterfps/BetterFps", fieldName, "I"));
                    list.add(new FieldInsnNode(178, "me/guichaguri/betterfps/BetterFps", maxFieldName, "I"));
                    list.add(new JumpInsnNode(164, (LabelNode)node));
                    list.add(new InsnNode(177));
                    list.add(node);
                    b = true;
                    continue;
                }
                list.add(node);
            }
            method.instructions.clear();
            method.instructions.add(list);
            patch = true;
        }
        if (patch) {
            ClassWriter writer = new ClassWriter(3);
            classNode.accept(writer);
            return writer.toByteArray();
        }
        return bytes;
    }
}

