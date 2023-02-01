/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.transformers.cloner;

import com.me.guichaguri.betterfps.ASMUtils;
import com.me.guichaguri.betterfps.BetterFps;
import com.me.guichaguri.betterfps.BetterFpsConfig;
import com.me.guichaguri.betterfps.BetterFpsHelper;
import com.me.guichaguri.betterfps.IClassTransformer;
import com.me.guichaguri.betterfps.transformers.cloner.CopyBoolCondition;
import com.me.guichaguri.betterfps.transformers.cloner.CopyCondition;
import com.me.guichaguri.betterfps.transformers.cloner.CopyMode;
import com.me.guichaguri.betterfps.transformers.cloner.Named;
import com.me.guichaguri.betterfps.tweaker.Naming;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.LocalVariableNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.TypeInsnNode;

public class ClonerTransformer
implements IClassTransformer {
    private static final List<Clone> clones = new ArrayList<Clone>();

    public static void add(String clazz, Naming target) {
        clones.add(new Clone(clazz.replaceAll("\\.", "/"), target));
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return bytes;
        }
        ArrayList<Clone> foundClones = null;
        for (Clone c : clones) {
            if (!c.target.is(name)) continue;
            if (foundClones == null) {
                foundClones = new ArrayList<Clone>();
            }
            foundClones.add(c);
        }
        if (foundClones != null) {
            BetterFps.log.info("Found " + foundClones.size() + " class patches for " + name);
            return this.patchClones(foundClones, bytes);
        }
        return bytes;
    }

    public byte[] patchClones(List<Clone> clones, byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        boolean patched = false;
        for (Clone c : clones) {
            try {
                boolean canCopy;
                Naming name;
                CopyMode.Mode mode;
                boolean canCopy2;
                ClassReader reader;
                if (BetterFpsHelper.LOC == null) {
                    reader = new ClassReader(c.clonePath);
                } else {
                    JarFile jar = new JarFile(BetterFpsHelper.LOC);
                    ZipEntry e = jar.getEntry(c.clonePath + ".class");
                    reader = new ClassReader(jar.getInputStream(e));
                    jar.close();
                }
                ClassNode cloneClass = new ClassNode();
                reader.accept(cloneClass, 0);
                if (cloneClass.visibleAnnotations != null && !(canCopy2 = this.canCopy(cloneClass.visibleAnnotations))) continue;
                for (FieldNode field : cloneClass.fields) {
                    mode = CopyMode.Mode.REPLACE;
                    name = null;
                    if (field.visibleAnnotations != null) {
                        canCopy = this.canCopy(field.visibleAnnotations);
                        if (!canCopy) continue;
                        mode = this.getCopyMode(field.visibleAnnotations);
                        name = this.getNaming(field.visibleAnnotations);
                    }
                    this.cloneField(field, classNode, mode, name);
                    patched = true;
                }
                for (MethodNode method : cloneClass.methods) {
                    mode = CopyMode.Mode.REPLACE;
                    name = null;
                    if (method.visibleAnnotations != null) {
                        canCopy = this.canCopy(method.visibleAnnotations);
                        if (!canCopy) continue;
                        mode = this.getCopyMode(method.visibleAnnotations);
                        name = this.getNaming(method.visibleAnnotations);
                    }
                    this.cloneMethod(method, classNode, cloneClass, mode, name);
                    patched = true;
                }
            }
            catch (Exception ex) {
                BetterFps.log.error("Could not patch with " + c.clonePath + ": " + ex);
                ex.printStackTrace();
            }
        }
        if (!patched) {
            return bytes;
        }
        ClassWriter writer = new ClassWriter(3);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private CopyMode.Mode getCopyMode(List<AnnotationNode> annotations) {
        for (AnnotationNode node : annotations) {
            CopyMode.Mode n;
            if (!node.desc.equals(Type.getDescriptor(CopyMode.class)) || (n = ASMUtils.getAnnotationValue(node, "value", CopyMode.Mode.class)) == null) continue;
            return n;
        }
        return CopyMode.Mode.REPLACE;
    }

    private Naming getNaming(List<AnnotationNode> annotations) {
        for (AnnotationNode node : annotations) {
            Naming n;
            if (!node.desc.equals(Type.getDescriptor(Named.class)) || (n = ASMUtils.getAnnotationValue(node, "value", Naming.class)) == null) continue;
            return n;
        }
        return null;
    }

    private boolean canCopy(List<AnnotationNode> annotations) {
        boolean canCopy = false;
        int conditions = 0;
        for (AnnotationNode node : annotations) {
            Object value;
            String key;
            if (node.desc.equals(Type.getDescriptor(CopyCondition.class))) {
                key = ASMUtils.getAnnotationValue(node, "key");
                value = ASMUtils.getAnnotationValue(node, "value");
                canCopy = canCopy || BetterFpsConfig.getValue(key).equals(value);
                ++conditions;
                continue;
            }
            if (!node.desc.equals(Type.getDescriptor(CopyBoolCondition.class))) continue;
            key = ASMUtils.getAnnotationValue(node, "key");
            value = ASMUtils.getAnnotationValue(node, "value", Boolean.class);
            if (value == null) {
                value = true;
            }
            canCopy = canCopy || ((Boolean)BetterFpsConfig.getValue(key)).booleanValue() == ((Boolean)value).booleanValue();
            ++conditions;
        }
        return conditions > 0 ? canCopy : true;
    }

    private void cloneField(FieldNode e, ClassNode node, CopyMode.Mode mode, Naming name) {
        if (mode == CopyMode.Mode.IGNORE) {
            return;
        }
        for (int i = 0; i < node.fields.size(); ++i) {
            FieldNode field = node.fields.get(i);
            boolean b = false;
            if (name != null && name.is(field.name, field.desc)) {
                b = true;
                e.name = field.name;
                e.desc = field.desc;
            } else if (field.name.equals(e.name) && field.desc.equals(e.desc)) {
                b = true;
            }
            if (!b) continue;
            if (mode == CopyMode.Mode.ADD_IF_NOT_EXISTS) {
                return;
            }
            node.fields.remove(field);
            break;
        }
        node.fields.add(e);
    }

    private boolean cloneMethod(MethodNode e, ClassNode node, ClassNode original, CopyMode.Mode mode, Naming name) {
        if (mode == CopyMode.Mode.IGNORE) {
            return false;
        }
        MethodNode originalMethod = null;
        for (int i = 0; i < node.methods.size(); ++i) {
            MethodNode method = node.methods.get(i);
            boolean b = false;
            if (name != null && name.is(method.name) && method.desc.equals(e.desc)) {
                b = true;
                e.name = method.name;
                e.desc = method.desc;
            } else if (method.name.equals(e.name) && method.desc.equals(e.desc)) {
                b = true;
            }
            if (!b) continue;
            if (mode == CopyMode.Mode.ADD_IF_NOT_EXISTS) {
                return false;
            }
            if (mode == CopyMode.Mode.PREPEND) {
                this.replaceOcurrences(e, node, original, null);
                method.instructions = ASMUtils.prependNodeList(method.instructions, e.instructions);
                return true;
            }
            if (mode == CopyMode.Mode.APPEND) {
                this.replaceOcurrences(e, node, original, null);
                method.instructions = ASMUtils.appendNodeList(method.instructions, e.instructions);
                return true;
            }
            originalMethod = method;
            node.methods.remove(method);
            break;
        }
        this.replaceOcurrences(e, node, original, originalMethod);
        node.methods.add(e);
        return true;
    }

    private void replaceOcurrences(MethodNode method, ClassNode classNode, ClassNode original, MethodNode originalMethod) {
        String superName;
        String originalDesc = "L" + original.name + ";";
        String classDesc = "L" + classNode.name + ";";
        ListIterator<AbstractInsnNode> nodes = method.instructions.iterator();
        TypeInsnNode lastType = null;
        boolean hasSuper = false;
        String string = superName = originalMethod == null ? null : originalMethod.name;
        block0: while (nodes.hasNext()) {
            AbstractInsnNode node = (AbstractInsnNode)nodes.next();
            if (node instanceof FieldInsnNode) {
                FieldInsnNode f = (FieldInsnNode)node;
                if (f.owner.equals(original.name)) {
                    f.owner = classNode.name;
                    continue;
                }
                for (Clone c : clones) {
                    if (!f.owner.equals(c.clonePath)) continue;
                    f.owner = lastType.desc;
                    continue block0;
                }
                continue;
            }
            if (node instanceof MethodInsnNode) {
                MethodInsnNode m = (MethodInsnNode)node;
                if (originalMethod != null && m.getOpcode() == 183 && m.owner.equals(classNode.name) && m.name.equals(superName) && m.desc.equals(originalMethod.desc)) {
                    if (!hasSuper) {
                        originalMethod.name = originalMethod.name + "_BF_repl";
                        classNode.methods.add(originalMethod);
                        hasSuper = true;
                    }
                    m.setOpcode(182);
                    m.name = originalMethod.name;
                }
                if (m.owner.equals(original.name)) {
                    m.owner = classNode.name;
                    continue;
                }
                for (Clone c : clones) {
                    if (!m.owner.equals(c.clonePath)) continue;
                    m.owner = lastType.desc;
                    continue block0;
                }
                continue;
            }
            if (!(node instanceof TypeInsnNode)) continue;
            TypeInsnNode t = (TypeInsnNode)node;
            if (t.desc.equals(original.name)) {
                t.desc = classNode.name;
                continue;
            }
            for (Clone c : clones) {
                if (!t.desc.equals(c.clonePath)) continue;
                nodes.remove();
                continue block0;
            }
            lastType = t;
        }
        for (LocalVariableNode var : method.localVariables) {
            if (var.desc != originalDesc) continue;
            var.desc = classDesc;
        }
    }

    static {
        BetterFpsConfig config = BetterFpsConfig.getConfig();
        if (config.fastBeacon) {
            ClonerTransformer.add("com.me.guichaguri.betterfps.clones.tileentity.BeaconLogic", Naming.C_TileEntityBeacon);
        }
        if (config.fastHopper) {
            ClonerTransformer.add("com.me.guichaguri.betterfps.clones.tileentity.HopperLogic", Naming.C_TileEntityHopper);
            ClonerTransformer.add("com.me.guichaguri.betterfps.clones.block.HopperBlock", Naming.C_BlockHopper);
        }
        ClonerTransformer.add("com.me.guichaguri.betterfps.clones.client.ModelBoxLogic", Naming.C_ModelBox);
        ClonerTransformer.add("com.me.guichaguri.betterfps.clones.client.EntityRenderLogic", Naming.C_EntityRenderer);
        ClonerTransformer.add("com.me.guichaguri.betterfps.clones.client.GuiOptionsLogic", Naming.C_GuiOptions);
    }

    public static class Clone {
        public final String clonePath;
        public final Naming target;

        public Clone(String clonePath, Naming target) {
            this.clonePath = clonePath;
            this.target = target;
        }
    }
}

