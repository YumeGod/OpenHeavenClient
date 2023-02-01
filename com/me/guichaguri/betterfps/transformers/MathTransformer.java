/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.transformers;

import com.me.guichaguri.betterfps.BetterFps;
import com.me.guichaguri.betterfps.BetterFpsConfig;
import com.me.guichaguri.betterfps.BetterFpsHelper;
import com.me.guichaguri.betterfps.IClassTransformer;
import com.me.guichaguri.betterfps.tweaker.Naming;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.InsnList;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class MathTransformer
implements IClassTransformer {
    @Override
    public byte[] transform(String name, String name2, byte[] bytes) {
        if (bytes == null) {
            return new byte[0];
        }
        if (Naming.C_MathHelper.is(name)) {
            try {
                return this.patchMath(bytes);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return bytes;
    }

    private byte[] patchMath(byte[] bytes) throws Exception {
        ClassReader reader;
        String algorithmClass;
        BetterFpsConfig config = BetterFpsConfig.getConfig();
        if (config == null) {
            BetterFpsHelper.loadConfig();
            config = BetterFpsConfig.getConfig();
        }
        if ((algorithmClass = BetterFpsHelper.helpers.get(config.algorithm)) == null) {
            BetterFps.log.error("The algorithm is invalid. We're going to use Vanilla Algorithm instead.");
            config.algorithm = "vanilla";
        }
        if (config.algorithm.equals("vanilla")) {
            BetterFps.log.info("Letting Minecraft use " + BetterFpsHelper.displayHelpers.get(config.algorithm));
            return bytes;
        }
        BetterFps.log.info("Patching Minecraft using " + BetterFpsHelper.displayHelpers.get(config.algorithm));
        if (BetterFpsHelper.LOC == null) {
            reader = new ClassReader("com.me.guichaguri.betterfps.math." + algorithmClass);
        } else {
            JarFile jar = new JarFile(BetterFpsHelper.LOC);
            ZipEntry e = jar.getEntry("me/guichaguri/betterfps/math/" + algorithmClass + ".class");
            reader = new ClassReader(jar.getInputStream(e));
            jar.close();
        }
        ClassNode mathnode = new ClassNode();
        reader.accept(mathnode, 0);
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        String className = classNode.name;
        String mathClass = mathnode.name;
        this.patchInit(classNode, mathnode, className, mathClass);
        Iterator<MethodNode> methods = classNode.methods.iterator();
        boolean patched = false;
        while (methods.hasNext()) {
            MethodNode method = methods.next();
            if (Naming.M_sin.is(method.name, method.desc)) {
                this.patchSin(method, mathnode, className, mathClass);
                patched = true;
                continue;
            }
            if (!Naming.M_cos.is(method.name, method.desc)) continue;
            this.patchCos(method, mathnode, className, mathClass);
            patched = true;
        }
        if (patched) {
            for (FieldNode field : classNode.fields) {
                if (!Naming.F_SIN_TABLE.is(field.name, field.desc)) continue;
                break;
            }
            ClassWriter writer = new ClassWriter(3);
            classNode.accept(writer);
            return writer.toByteArray();
        }
        return bytes;
    }

    private void patchInit(ClassNode classNode, ClassNode math, String name, String oldName) {
        classNode.fields.addAll(math.fields);
        MethodNode mathClinit = null;
        for (MethodNode methodNode : math.methods) {
            if (!methodNode.name.equals("<clinit>")) continue;
            mathClinit = methodNode;
            break;
        }
        if (mathClinit != null) {
            MethodNode clinit = null;
            for (MethodNode m : classNode.methods) {
                if (!m.name.equals("<clinit>")) continue;
                clinit = m;
                break;
            }
            if (clinit == null) {
                clinit = new MethodNode(8, "<clinit>", "()V", null, null);
            }
            InsnList insnList = new InsnList();
            for (AbstractInsnNode node : mathClinit.instructions.toArray()) {
                if (node instanceof FieldInsnNode) {
                    FieldInsnNode field = (FieldInsnNode)node;
                    if (field.owner.equals(oldName)) {
                        field.owner = name;
                    }
                } else if (node.getOpcode() == 177) continue;
                insnList.add(node);
            }
            insnList.add(clinit.instructions);
            clinit.instructions.clear();
            clinit.instructions.add(insnList);
            classNode.methods.remove(clinit);
            classNode.methods.add(clinit);
        }
    }

    private void patchSin(MethodNode method, ClassNode math, String name, String oldName) {
        method.instructions.clear();
        for (MethodNode original : math.methods) {
            if (!original.name.equals("sin")) continue;
            method.instructions.add(original.instructions);
            for (AbstractInsnNode node : method.instructions.toArray()) {
                if (!(node instanceof FieldInsnNode)) continue;
                FieldInsnNode field = (FieldInsnNode)node;
                if (!field.owner.equals(oldName)) continue;
                field.owner = name;
            }
        }
    }

    private void patchCos(MethodNode method, ClassNode math, String name, String oldName) {
        method.instructions.clear();
        for (MethodNode original : math.methods) {
            if (!original.name.equals("cos")) continue;
            method.instructions.add(original.instructions);
            for (AbstractInsnNode node : method.instructions.toArray()) {
                if (!(node instanceof FieldInsnNode)) continue;
                FieldInsnNode field = (FieldInsnNode)node;
                if (!field.owner.equals(oldName)) continue;
                field.owner = name;
            }
        }
    }
}

