/*
 * Decompiled with CFR 0.152.
 */
package com.compiler;

import com.compiler.CompilerUtils;
import com.compiler.JavaSourceFromString;
import com.compiler.MyJavaFileManager;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

public class CachedCompiler
implements Closeable {
    private static final PrintWriter DEFAULT_WRITER = new PrintWriter(System.err);
    private final Map<ClassLoader, Map<String, Class>> loadedClassesMap = Collections.synchronizedMap(new WeakHashMap());
    private final Map<ClassLoader, MyJavaFileManager> fileManagerMap = Collections.synchronizedMap(new WeakHashMap());
    private final File sourceDir;
    private final File classDir;
    private final ConcurrentMap<String, JavaFileObject> javaFileObjects = new ConcurrentHashMap<String, JavaFileObject>();

    public CachedCompiler(File sourceDir, File classDir) {
        this.sourceDir = sourceDir;
        this.classDir = classDir;
    }

    @Override
    public void close() {
        try {
            for (MyJavaFileManager fileManager : this.fileManagerMap.values()) {
                fileManager.close();
            }
        }
        catch (IOException e) {
            throw new AssertionError((Object)e);
        }
    }

    public Class loadFromJava(String className, String javaCode) throws ClassNotFoundException {
        return this.loadFromJava(this.getClass().getClassLoader(), className, javaCode, DEFAULT_WRITER);
    }

    public Class loadFromJava(ClassLoader classLoader, String className, String javaCode) throws ClassNotFoundException {
        return this.loadFromJava(classLoader, className, javaCode, DEFAULT_WRITER);
    }

    Map<String, byte[]> compileFromJava(String className, String javaCode, MyJavaFileManager fileManager) {
        return this.compileFromJava(className, javaCode, DEFAULT_WRITER, fileManager);
    }

    Map<String, byte[]> compileFromJava(String className, String javaCode, final PrintWriter writer, MyJavaFileManager fileManager) {
        Iterable<Object> compilationUnits;
        if (this.sourceDir != null) {
            String filename = className.replaceAll("\\.", '\\' + File.separator) + ".java";
            File file = new File(this.sourceDir, filename);
            CompilerUtils.writeText(file, javaCode);
            if (CompilerUtils.s_standardJavaFileManager == null) {
                CompilerUtils.s_standardJavaFileManager = CompilerUtils.s_compiler.getStandardFileManager(null, null, null);
            }
            compilationUnits = CompilerUtils.s_standardJavaFileManager.getJavaFileObjects(file);
        } else {
            this.javaFileObjects.put(className, new JavaSourceFromString(className, javaCode));
            compilationUnits = new ArrayList(this.javaFileObjects.values());
        }
        List<String> options = Arrays.asList("-g", "-nowarn");
        boolean ok = CompilerUtils.s_compiler.getTask(writer, fileManager, (DiagnosticListener<? super JavaFileObject>)new DiagnosticListener<JavaFileObject>(){

            @Override
            public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
                if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                    writer.println(diagnostic);
                }
            }
        }, options, null, compilationUnits).call();
        if (!ok) {
            if (this.sourceDir == null) {
                this.javaFileObjects.remove(className);
            }
            return Collections.emptyMap();
        }
        return fileManager.getAllBuffers();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Class loadFromJava(ClassLoader classLoader, String className, String javaCode, PrintWriter writer) throws ClassNotFoundException {
        PrintWriter printWriter;
        Map<String, Class> loadedClasses;
        Class<?> clazz = null;
        Map<ClassLoader, Map<String, Class>> map = this.loadedClassesMap;
        synchronized (map) {
            loadedClasses = this.loadedClassesMap.get(classLoader);
            if (loadedClasses == null) {
                loadedClasses = new LinkedHashMap<String, Class>();
                this.loadedClassesMap.put(classLoader, loadedClasses);
            } else {
                clazz = loadedClasses.get(className);
            }
        }
        PrintWriter printWriter2 = printWriter = writer == null ? DEFAULT_WRITER : writer;
        if (clazz != null) {
            return clazz;
        }
        MyJavaFileManager fileManager = this.fileManagerMap.get(classLoader);
        if (fileManager == null) {
            StandardJavaFileManager standardJavaFileManager = CompilerUtils.s_compiler.getStandardFileManager(null, null, null);
            fileManager = new MyJavaFileManager(standardJavaFileManager);
            this.fileManagerMap.put(classLoader, fileManager);
        }
        Map<String, byte[]> compiled = this.compileFromJava(className, javaCode, printWriter, fileManager);
        for (Map.Entry<String, byte[]> entry : compiled.entrySet()) {
            String filename;
            boolean changed2;
            String className2 = entry.getKey();
            Map<ClassLoader, Map<String, Class>> map2 = this.loadedClassesMap;
            synchronized (map2) {
                if (loadedClasses.containsKey(className2)) {
                    continue;
                }
            }
            byte[] bytes = entry.getValue();
            if (this.classDir != null && (changed2 = CompilerUtils.writeBytes(new File(this.classDir, filename = className2.replaceAll("\\.", '\\' + File.separator) + ".class"), bytes))) {
                System.out.println("Updated " + className2 + "in " + this.classDir);
            }
            String string = className2.intern();
            synchronized (string) {
                Map<ClassLoader, Map<String, Class>> changed2 = this.loadedClassesMap;
                synchronized (changed2) {
                    if (loadedClasses.containsKey(className2)) {
                        continue;
                    }
                }
                Class clazz2 = CompilerUtils.defineClass(classLoader, className2, bytes);
                Map<ClassLoader, Map<String, Class>> map3 = this.loadedClassesMap;
                synchronized (map3) {
                    loadedClasses.put(className2, clazz2);
                }
            }
        }
        Map<ClassLoader, Map<String, Class>> map4 = this.loadedClassesMap;
        synchronized (map4) {
            clazz = classLoader.loadClass(className);
            loadedClasses.put(className, clazz);
        }
        return clazz;
    }
}

