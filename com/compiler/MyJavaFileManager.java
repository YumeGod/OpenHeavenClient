/*
 * Decompiled with CFR 0.152.
 */
package com.compiler;

import com.compiler.CloseableByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import sun.misc.Unsafe;

class MyJavaFileManager
implements JavaFileManager {
    private static final Unsafe unsafe;
    private static final long OVERRIDE_OFFSET;
    private final StandardJavaFileManager fileManager;
    final Map<String, CloseableByteArrayOutputStream> buffers = Collections.synchronizedMap(new LinkedHashMap());

    MyJavaFileManager(StandardJavaFileManager fileManager) {
        this.fileManager = fileManager;
    }

    public Iterable<Set<JavaFileManager.Location>> listLocationsForModules(JavaFileManager.Location location) {
        return (Iterable)this.invokeNamedMethodIfAvailable(location, "listLocationsForModules");
    }

    public String inferModuleName(JavaFileManager.Location location) {
        return (String)this.invokeNamedMethodIfAvailable(location, "inferModuleName");
    }

    @Override
    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return this.fileManager.getClassLoader(location);
    }

    @Override
    public Iterable<JavaFileObject> list(JavaFileManager.Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
        return this.fileManager.list(location, packageName, kinds, recurse);
    }

    @Override
    public String inferBinaryName(JavaFileManager.Location location, JavaFileObject file) {
        return this.fileManager.inferBinaryName(location, file);
    }

    @Override
    public boolean isSameFile(FileObject a, FileObject b) {
        return this.fileManager.isSameFile(a, b);
    }

    @Override
    public boolean handleOption(String current, Iterator<String> remaining) {
        return this.fileManager.handleOption(current, remaining);
    }

    @Override
    public boolean hasLocation(JavaFileManager.Location location) {
        return this.fileManager.hasLocation(location);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public JavaFileObject getJavaFileForInput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind) throws IOException {
        if (location == StandardLocation.CLASS_OUTPUT) {
            byte[] bytes;
            boolean success;
            Map<String, CloseableByteArrayOutputStream> map = this.buffers;
            synchronized (map) {
                success = this.buffers.containsKey(className) && kind == JavaFileObject.Kind.CLASS;
                bytes = this.buffers.get(className).toByteArray();
            }
            if (success) {
                return new SimpleJavaFileObject(URI.create(className), kind){

                    @Override
                    public InputStream openInputStream() {
                        return new ByteArrayInputStream(bytes);
                    }
                };
            }
        }
        return this.fileManager.getJavaFileForInput(location, className, kind);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, final String className, JavaFileObject.Kind kind, FileObject sibling) {
        return new SimpleJavaFileObject(URI.create(className), kind){

            @Override
            public OutputStream openOutputStream() {
                CloseableByteArrayOutputStream baos = new CloseableByteArrayOutputStream();
                MyJavaFileManager.this.buffers.putIfAbsent(className, baos);
                return baos;
            }
        };
    }

    @Override
    public FileObject getFileForInput(JavaFileManager.Location location, String packageName, String relativeName) throws IOException {
        return this.fileManager.getFileForInput(location, packageName, relativeName);
    }

    @Override
    public FileObject getFileForOutput(JavaFileManager.Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
        return this.fileManager.getFileForOutput(location, packageName, relativeName, sibling);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws IOException {
        this.fileManager.close();
    }

    @Override
    public int isSupportedOption(String option) {
        return this.fileManager.isSupportedOption(option);
    }

    public void clearBuffers() {
        this.buffers.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Map<String, byte[]> getAllBuffers() {
        LinkedHashMap<String, byte[]> ret = new LinkedHashMap<String, byte[]>(this.buffers.size() << 1);
        LinkedHashMap<String, CloseableByteArrayOutputStream> compiledClasses = new LinkedHashMap<String, CloseableByteArrayOutputStream>(ret.size());
        Map<String, CloseableByteArrayOutputStream> map = this.buffers;
        synchronized (map) {
            compiledClasses.putAll(this.buffers);
        }
        for (Map.Entry entry : compiledClasses.entrySet()) {
            try {
                ((CloseableByteArrayOutputStream)entry.getValue()).closeFuture().get(30L, TimeUnit.SECONDS);
            }
            catch (InterruptedException t) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted while waiting for compilation result [class=" + (String)entry.getKey() + "]");
                break;
            }
            catch (ExecutionException | TimeoutException t) {
                t.printStackTrace();
                System.out.println("Failed to wait for compilation result [class=" + (String)entry.getKey() + "]");
                continue;
            }
            byte[] value = ((CloseableByteArrayOutputStream)entry.getValue()).toByteArray();
            ret.put((String)entry.getKey(), value);
        }
        return ret;
    }

    private <T> T invokeNamedMethodIfAvailable(JavaFileManager.Location location, String name) {
        Method[] methods;
        for (Method method : methods = this.fileManager.getClass().getDeclaredMethods()) {
            if (!method.getName().equals(name) || method.getParameterTypes().length != 1 || method.getParameterTypes()[0] != JavaFileManager.Location.class) continue;
            try {
                if (OVERRIDE_OFFSET == 0L) {
                    method.setAccessible(true);
                } else {
                    unsafe.putBoolean((Object)method, OVERRIDE_OFFSET, true);
                }
                return (T)method.invoke(this.fileManager, location);
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                throw new UnsupportedOperationException("Unable to invoke method " + name);
            }
        }
        throw new UnsupportedOperationException("Unable to find method " + name);
    }

    static {
        long offset;
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe)theUnsafe.get(null);
        }
        catch (Exception ex) {
            throw new AssertionError((Object)ex);
        }
        try {
            Field f = AccessibleObject.class.getDeclaredField("override");
            offset = unsafe.objectFieldOffset(f);
        }
        catch (NoSuchFieldException e) {
            offset = 0L;
        }
        OVERRIDE_OFFSET = offset;
    }
}

