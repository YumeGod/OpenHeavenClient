/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.management;

import heaven.main.event.Event;
import heaven.main.event.EventCancel;
import heaven.main.event.EventHandler;
import heaven.main.management.Manager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager
implements Manager {
    private static final Map<Class<? extends Event>, List<MethodData>> REGISTRY_MAP = new HashMap<Class<? extends Event>, List<MethodData>>();
    private static final Map<Class<? extends Event>, List<MethodData>> ALL_TIME_MAP = new HashMap<Class<? extends Event>, List<MethodData>>();

    public static void runAllTime(Object ... objects) {
        for (Object object : objects) {
            for (Method method : object.getClass().getDeclaredMethods()) {
                Class<?> indexClass;
                if (method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventCancel.class)) continue;
                final MethodData data = new MethodData(object, method, method.getAnnotation(EventCancel.class).priority());
                if (!data.getTarget().isAccessible()) {
                    data.getTarget().setAccessible(true);
                }
                if (ALL_TIME_MAP.containsKey(indexClass = method.getParameterTypes()[0])) {
                    if (ALL_TIME_MAP.get(indexClass).contains(data)) continue;
                    ALL_TIME_MAP.get(indexClass).add(data);
                    continue;
                }
                ALL_TIME_MAP.put(indexClass, (List<MethodData>)new CopyOnWriteArrayList<MethodData>(){
                    private static final long serialVersionUID = 666L;
                    {
                        this.add(data);
                    }
                });
            }
        }
    }

    public static void register(Object ... objects) {
        for (Object object : objects) {
            for (Method method : object.getClass().getDeclaredMethods()) {
                if (EventManager.isMethodBad(method)) continue;
                EventManager.register_method(method, object);
            }
        }
    }

    public static void unregister(Object ... objects) {
        for (Object object : objects) {
            for (List<MethodData> dataList : REGISTRY_MAP.values()) {
                dataList.removeIf(data -> data.getSource().equals(object));
            }
            EventManager.cleanMap(true);
        }
    }

    private static void register_method(Method method, Object object) {
        Class<?> indexClass = method.getParameterTypes()[0];
        final MethodData data = new MethodData(object, method, method.getAnnotation(EventHandler.class).value());
        if (!data.getTarget().isAccessible()) {
            data.getTarget().setAccessible(true);
        }
        if (REGISTRY_MAP.containsKey(indexClass)) {
            if (!REGISTRY_MAP.get(indexClass).contains(data)) {
                REGISTRY_MAP.get(indexClass).add(data);
            }
        } else {
            REGISTRY_MAP.put(indexClass, (List<MethodData>)new CopyOnWriteArrayList<MethodData>(){
                private static final long serialVersionUID = 666L;
                {
                    this.add(data);
                }
            });
        }
    }

    public static void removeEntry(Class<? extends Event> indexClass) {
        Iterator<Map.Entry<Class<? extends Event>, List<MethodData>>> mapIterator = REGISTRY_MAP.entrySet().iterator();
        while (mapIterator.hasNext()) {
            if (!mapIterator.next().getKey().equals(indexClass)) continue;
            mapIterator.remove();
            break;
        }
    }

    public static void cleanMap(boolean onlyEmptyEntries) {
        Iterator<Map.Entry<Class<? extends Event>, List<MethodData>>> mapIterator = REGISTRY_MAP.entrySet().iterator();
        while (mapIterator.hasNext()) {
            if (onlyEmptyEntries && !mapIterator.next().getValue().isEmpty()) continue;
            mapIterator.remove();
        }
    }

    private static boolean isMethodBad(Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventHandler.class);
    }

    public static Event call(Event event) {
        List<MethodData> dataAllList;
        List<MethodData> dataList = REGISTRY_MAP.get(event.getClass());
        if (dataList != null) {
            for (MethodData data : dataList) {
                EventManager.invoke(data, event);
            }
        }
        if ((dataAllList = ALL_TIME_MAP.get(event.getClass())) != null) {
            for (MethodData data : dataAllList) {
                EventManager.invoke(data, event);
            }
            return event;
        }
        return null;
    }

    private static void invoke(MethodData data, Event argument) {
        try {
            data.getTarget().invoke(data.getSource(), argument);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            // empty catch block
        }
    }

    @Override
    public void init() {
    }

    private static final class MethodData {
        private final Object source;
        private final Method target;
        private final byte priority;

        public MethodData(Object source, Method target, byte priority) {
            this.source = source;
            this.target = target;
            this.priority = priority;
        }

        public Object getSource() {
            return this.source;
        }

        public Method getTarget() {
            return this.target;
        }

        public byte getPriority() {
            return this.priority;
        }
    }
}

