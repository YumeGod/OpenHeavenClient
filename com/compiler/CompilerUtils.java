/*
 * Decompiled with CFR 0.152.
 */
package com.compiler;

import com.compiler.CachedCompiler;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.util.Arrays;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import sun.misc.Unsafe;

public enum CompilerUtils {

    public static final CachedCompiler CACHED_COMPILER = new CachedCompiler(null, null);
    private static final Method DEFINE_CLASS_METHOD;
    private static final Charset UTF_8;
    static JavaCompiler s_compiler;
    static StandardJavaFileManager s_standardJavaFileManager;

    static void setAccessible(AccessibleObject ao, boolean accessible) {
        if (System.getSecurityManager() == null) {
            ao.setAccessible(accessible);
        } else {
            AccessController.doPrivileged(() -> {
                ao.setAccessible(accessible);
                return null;
            });
        }
    }

    private static void reset() {
        s_compiler = ToolProvider.getSystemJavaCompiler();
        if (s_compiler == null) {
            try {
                Class<?> javacTool = Class.forName("com.sun.tools.javac.api.JavacTool");
                Method create = javacTool.getMethod("create", new Class[0]);
                s_compiler = (JavaCompiler)create.invoke(null, new Object[0]);
            }
            catch (Exception e) {
                throw new AssertionError((Object)e);
            }
        }
    }

    public static Class loadFromJava(String className, String javaCode) throws ClassNotFoundException {
        return CACHED_COMPILER.loadFromJava(Thread.currentThread().getContextClassLoader(), className, javaCode);
    }

    public static void defineClass(String className, byte[] bytes) {
        CompilerUtils.defineClass(Thread.currentThread().getContextClassLoader(), className, bytes);
    }

    public static Class defineClass(ClassLoader classLoader, String className, byte[] bytes) {
        try {
            return (Class)DEFINE_CLASS_METHOD.invoke(classLoader, className, bytes, 0, bytes.length);
        }
        catch (IllegalAccessException e) {
            throw new AssertionError((Object)e);
        }
        catch (InvocationTargetException e) {
            throw new AssertionError((Object)e.getCause());
        }
    }

    private static byte[] readBytes(File file) {
        if (!file.exists()) {
            return null;
        }
        long len = file.length();
        if (len > Runtime.getRuntime().totalMemory() / 10L) {
            throw new IllegalStateException("Attempted to read large file " + file + " was " + len + " bytes.");
        }
        byte[] bytes = new byte[(int)len];
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(bytes);
        }
        catch (IOException e) {
            CompilerUtils.close(dis);
            throw new IllegalStateException("Unable to read file " + file, e);
        }
        return bytes;
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean writeText(File file, String text) {
        return CompilerUtils.writeBytes(file, CompilerUtils.encodeUTF8(text));
    }

    private static byte[] encodeUTF8(String text) {
        try {
            return text.getBytes(UTF_8.name());
        }
        catch (UnsupportedEncodingException e) {
            throw new AssertionError((Object)e);
        }
    }

    public static boolean writeBytes(File file, byte[] bytes) {
        File parentDir = file.getParentFile();
        if (!parentDir.isDirectory() && !parentDir.mkdirs()) {
            throw new IllegalStateException("Unable to create directory " + parentDir);
        }
        File bak = null;
        if (file.exists()) {
            byte[] bytes2 = CompilerUtils.readBytes(file);
            if (Arrays.equals(bytes, bytes2)) {
                return false;
            }
            bak = new File(parentDir, file.getName() + ".bak");
            file.renameTo(bak);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
        }
        catch (IOException e) {
            CompilerUtils.close(fos);
            file.delete();
            if (bak != null) {
                bak.renameTo(file);
            }
            throw new IllegalStateException("Unable to write " + file, e);
        }
        return true;
    }

    static {
        UTF_8 = StandardCharsets.UTF_8;
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            CompilerUtils.setAccessible(theUnsafe, true);
            Unsafe u = (Unsafe)theUnsafe.get(null);
            DEFINE_CLASS_METHOD = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
            try {
                Field f = AccessibleObject.class.getDeclaredField("override");
                long offset = u.objectFieldOffset(f);
                u.putBoolean((Object)DEFINE_CLASS_METHOD, offset, true);
            }
            catch (NoSuchFieldException e) {
                CompilerUtils.setAccessible(DEFINE_CLASS_METHOD, true);
            }
        }
        catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException e) {
            throw new AssertionError((Object)e);
        }
        CompilerUtils.reset();
    }
}

