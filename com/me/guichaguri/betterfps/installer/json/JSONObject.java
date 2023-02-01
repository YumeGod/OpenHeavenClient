/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.installer.json;

import com.me.guichaguri.betterfps.installer.json.JSONArray;
import com.me.guichaguri.betterfps.installer.json.JSONException;
import com.me.guichaguri.betterfps.installer.json.JSONString;
import com.me.guichaguri.betterfps.installer.json.JSONTokener;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class JSONObject {
    private final Map<String, Object> map = new HashMap<String, Object>();
    public static final Object NULL = new Null();

    public JSONObject() {
    }

    public JSONObject(JSONObject jo, String[] names) {
        this();
        for (int i = 0; i < names.length; ++i) {
            try {
                this.putOnce(names[i], jo.opt(names[i]));
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public JSONObject(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        block8: while (true) {
            char c = x.nextClean();
            switch (c) {
                case '\u0000': {
                    throw x.syntaxError("A JSONObject text must end with '}'");
                }
                case '}': {
                    return;
                }
            }
            x.back();
            String key = x.nextValue().toString();
            c = x.nextClean();
            if (c != ':') {
                throw x.syntaxError("Expected a ':' after a key");
            }
            this.putOnce(key, x.nextValue());
            switch (x.nextClean()) {
                case ',': 
                case ';': {
                    if (x.nextClean() == '}') {
                        return;
                    }
                    x.back();
                    continue block8;
                }
                case '}': {
                    return;
                }
            }
            break;
        }
        throw x.syntaxError("Expected a ',' or '}'");
    }

    public JSONObject(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value == null) continue;
                this.map.put(entry.getKey(), JSONObject.wrap(value));
            }
        }
    }

    public JSONObject(Object bean) {
        this();
        this.populateMap(bean);
    }

    public JSONObject(Object object, String[] names) {
        this();
        Class<?> c = object.getClass();
        for (int i = 0; i < names.length; ++i) {
            String name = names[i];
            try {
                this.putOpt(name, c.getField(name).get(object));
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public JSONObject(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    public JSONObject(String baseName, Locale locale) throws JSONException {
        this();
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, Thread.currentThread().getContextClassLoader());
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key == null) continue;
            String[] path = key.split("\\.");
            int last = path.length - 1;
            JSONObject target = this;
            for (int i = 0; i < last; ++i) {
                String segment = path[i];
                JSONObject nextTarget = target.optJSONObject(segment);
                if (nextTarget == null) {
                    nextTarget = new JSONObject();
                    target.put(segment, nextTarget);
                }
                target = nextTarget;
            }
            target.put(path[last], bundle.getString(key));
        }
    }

    public JSONObject accumulate(String key, Object value) throws JSONException {
        JSONObject.testValidity(value);
        Object object = this.opt(key);
        if (object == null) {
            this.put(key, value instanceof JSONArray ? new JSONArray().put(value) : value);
        } else if (object instanceof JSONArray) {
            ((JSONArray)object).put(value);
        } else {
            this.put(key, new JSONArray().put(object).put(value));
        }
        return this;
    }

    public JSONObject append(String key, Object value) throws JSONException {
        JSONObject.testValidity(value);
        Object object = this.opt(key);
        if (object == null) {
            this.put(key, new JSONArray().put(value));
        } else if (object instanceof JSONArray) {
            this.put(key, ((JSONArray)object).put(value));
        } else {
            throw new JSONException("JSONObject[" + key + "] is not a JSONArray.");
        }
        return this;
    }

    public static String doubleToString(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return "null";
        }
        String string = Double.toString(d);
        if (string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0) {
            while (!string.isEmpty() && string.charAt(string.length() - 1) == '0') {
                string = string.substring(0, string.length() - 1);
            }
            if (!string.isEmpty() && string.charAt(string.length() - 1) == '.') {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }

    public Object get(String key) throws JSONException {
        if (key == null) {
            throw new JSONException("Null key.");
        }
        Object object = this.opt(key);
        if (object == null) {
            throw new JSONException("JSONObject[" + JSONObject.quote(key) + "] not found.");
        }
        return object;
    }

    public boolean getBoolean(String key) throws JSONException {
        Object object = this.get(key);
        if (object.equals(Boolean.FALSE) || object instanceof String && ((String)object).equalsIgnoreCase("false")) {
            return false;
        }
        if (object.equals(Boolean.TRUE) || object instanceof String && ((String)object).equalsIgnoreCase("true")) {
            return true;
        }
        throw new JSONException("JSONObject[" + JSONObject.quote(key) + "] is not a Boolean.");
    }

    public double getDouble(String key) throws JSONException {
        Object object = this.get(key);
        try {
            return object instanceof Number ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONObject[" + JSONObject.quote(key) + "] is not a number.");
        }
    }

    public int getInt(String key) throws JSONException {
        Object object = this.get(key);
        try {
            return object instanceof Number ? ((Number)object).intValue() : Integer.parseInt((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONObject[" + JSONObject.quote(key) + "] is not an int.");
        }
    }

    public JSONArray getJSONArray(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof JSONArray) {
            return (JSONArray)object;
        }
        throw new JSONException("JSONObject[" + JSONObject.quote(key) + "] is not a JSONArray.");
    }

    public JSONObject getJSONObject(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof JSONObject) {
            return (JSONObject)object;
        }
        throw new JSONException("JSONObject[" + JSONObject.quote(key) + "] is not a JSONObject.");
    }

    public long getLong(String key) throws JSONException {
        Object object = this.get(key);
        try {
            return object instanceof Number ? ((Number)object).longValue() : Long.parseLong((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONObject[" + JSONObject.quote(key) + "] is not a long.");
        }
    }

    public static String[] getNames(JSONObject jo) {
        int length = jo.length();
        if (length == 0) {
            return null;
        }
        Iterator<String> iterator = jo.keys();
        String[] names = new String[length];
        int i = 0;
        while (iterator.hasNext()) {
            names[i] = iterator.next();
            ++i;
        }
        return names;
    }

    public static String[] getNames(Object object) {
        if (object == null) {
            return null;
        }
        Class<?> klass = object.getClass();
        Field[] fields = klass.getFields();
        int length = fields.length;
        if (length == 0) {
            return null;
        }
        String[] names = new String[length];
        for (int i = 0; i < length; ++i) {
            names[i] = fields[i].getName();
        }
        return names;
    }

    public String getString(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof String) {
            return (String)object;
        }
        throw new JSONException("JSONObject[" + JSONObject.quote(key) + "] not a string.");
    }

    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    public JSONObject increment(String key) throws JSONException {
        Object value = this.opt(key);
        if (value == null) {
            this.put(key, 1);
        } else if (value instanceof Integer) {
            this.put(key, (Integer)value + 1);
        } else if (value instanceof Long) {
            this.put(key, (Long)value + 1L);
        } else if (value instanceof Double) {
            this.put(key, (Double)value + 1.0);
        } else if (value instanceof Float) {
            this.put(key, ((Float)value).floatValue() + 1.0f);
        } else {
            throw new JSONException("Unable to increment [" + JSONObject.quote(key) + "].");
        }
        return this;
    }

    public boolean isNull(String key) {
        return NULL.equals(this.opt(key));
    }

    public Iterator<String> keys() {
        return this.keySet().iterator();
    }

    public Set<String> keySet() {
        return this.map.keySet();
    }

    public int length() {
        return this.map.size();
    }

    public JSONArray names() {
        JSONArray ja = new JSONArray();
        Iterator<String> keys = this.keys();
        while (keys.hasNext()) {
            ja.put(keys.next());
        }
        return ja.length() == 0 ? null : ja;
    }

    public static String numberToString(Number number) throws JSONException {
        if (number == null) {
            throw new JSONException("Null pointer");
        }
        JSONObject.testValidity(number);
        String string = number.toString();
        if (string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0) {
            while (!string.isEmpty() && string.charAt(string.length() - 1) == '0') {
                string = string.substring(0, string.length() - 1);
            }
            if (!string.isEmpty() && string.charAt(string.length() - 1) == '.') {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }

    public Object opt(String key) {
        return key == null ? null : this.map.get(key);
    }

    public boolean optBoolean(String key) {
        return this.optBoolean(key, false);
    }

    public boolean optBoolean(String key, boolean defaultValue) {
        try {
            return this.getBoolean(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public double optDouble(String key) {
        return this.optDouble(key, Double.NaN);
    }

    public double optDouble(String key, double defaultValue) {
        try {
            return this.getDouble(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public int optInt(String key) {
        return this.optInt(key, 0);
    }

    public int optInt(String key, int defaultValue) {
        try {
            return this.getInt(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public JSONArray optJSONArray(String key) {
        Object o = this.opt(key);
        return o instanceof JSONArray ? (JSONArray)o : null;
    }

    public JSONObject optJSONObject(String key) {
        Object object = this.opt(key);
        return object instanceof JSONObject ? (JSONObject)object : null;
    }

    public long optLong(String key) {
        return this.optLong(key, 0L);
    }

    public long optLong(String key, long defaultValue) {
        try {
            return this.getLong(key);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public String optString(String key) {
        return this.optString(key, "");
    }

    public String optString(String key, String defaultValue) {
        Object object = this.opt(key);
        return NULL.equals(object) ? defaultValue : object.toString();
    }

    private void populateMap(Object bean) {
        Class<?> klass = bean.getClass();
        boolean includeSuperClass = klass.getClassLoader() != null;
        Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            try {
                Method method = methods[i];
                if (!Modifier.isPublic(method.getModifiers())) continue;
                String name = method.getName();
                String key = "";
                if (name.startsWith("get")) {
                    key = "getClass".equals(name) || "getDeclaringClass".equals(name) ? "" : name.substring(3);
                } else if (name.startsWith("is")) {
                    key = name.substring(2);
                }
                if (key.length() <= 0 || !Character.isUpperCase(key.charAt(0)) || method.getParameterTypes().length != 0) continue;
                if (key.length() == 1) {
                    key = key.toLowerCase();
                } else if (!Character.isUpperCase(key.charAt(1))) {
                    key = key.substring(0, 1).toLowerCase() + key.substring(1);
                }
                Object result = method.invoke(bean, null);
                if (result == null) continue;
                this.map.put(key, JSONObject.wrap(result));
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public JSONObject put(String key, boolean value) throws JSONException {
        this.put(key, value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONObject put(String key, Collection<Object> value) throws JSONException {
        this.put(key, new JSONArray(value));
        return this;
    }

    public JSONObject put(String key, double value) throws JSONException {
        this.put(key, new Double(value));
        return this;
    }

    public JSONObject put(String key, int value) throws JSONException {
        this.put(key, new Integer(value));
        return this;
    }

    public JSONObject put(String key, long value) throws JSONException {
        this.put(key, new Long(value));
        return this;
    }

    public JSONObject put(String key, Map<String, Object> value) throws JSONException {
        this.put(key, new JSONObject(value));
        return this;
    }

    public JSONObject put(String key, Object value) throws JSONException {
        if (key == null) {
            throw new NullPointerException("Null key.");
        }
        if (value != null) {
            JSONObject.testValidity(value);
            this.map.put(key, value);
        } else {
            this.remove(key);
        }
        return this;
    }

    public JSONObject putOnce(String key, Object value) throws JSONException {
        if (key != null && value != null) {
            if (this.opt(key) != null) {
                throw new JSONException("Duplicate key \"" + key + "\"");
            }
            this.put(key, value);
        }
        return this;
    }

    public JSONObject putOpt(String key, Object value) throws JSONException {
        if (key != null && value != null) {
            this.put(key, value);
        }
        return this;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String quote(String string) {
        StringWriter sw = new StringWriter();
        StringBuffer stringBuffer = sw.getBuffer();
        synchronized (stringBuffer) {
            try {
                return JSONObject.quote(string, sw).toString();
            }
            catch (IOException ignored) {
                return "";
            }
        }
    }

    public static Writer quote(String string, Writer w) throws IOException {
        if (string == null || string.length() == 0) {
            w.write("\"\"");
            return w;
        }
        char c = '\u0000';
        int len = string.length();
        w.write(34);
        block9: for (int i = 0; i < len; ++i) {
            char b = c;
            c = string.charAt(i);
            switch (c) {
                case '\"': 
                case '\\': {
                    w.write(92);
                    w.write(c);
                    continue block9;
                }
                case '/': {
                    if (b == '<') {
                        w.write(92);
                    }
                    w.write(c);
                    continue block9;
                }
                case '\b': {
                    w.write("\\b");
                    continue block9;
                }
                case '\t': {
                    w.write("\\t");
                    continue block9;
                }
                case '\n': {
                    w.write("\\n");
                    continue block9;
                }
                case '\f': {
                    w.write("\\f");
                    continue block9;
                }
                case '\r': {
                    w.write("\\r");
                    continue block9;
                }
                default: {
                    if (c < ' ' || c >= '\u0080' && c < '\u00a0' || c >= '\u2000' && c < '\u2100') {
                        w.write("\\u");
                        String hhhh = Integer.toHexString(c);
                        w.write("0000", 0, 4 - hhhh.length());
                        w.write(hhhh);
                        continue block9;
                    }
                    w.write(c);
                }
            }
        }
        w.write(34);
        return w;
    }

    public Object remove(String key) {
        return this.map.remove(key);
    }

    public boolean similar(Object other) {
        try {
            if (!(other instanceof JSONObject)) {
                return false;
            }
            Set<String> set = this.keySet();
            if (!set.equals(((JSONObject)other).keySet())) {
                return false;
            }
            for (String name : set) {
                Object valueThis = this.get(name);
                Object valueOther = ((JSONObject)other).get(name);
                if (!(valueThis instanceof JSONObject ? !((JSONObject)valueThis).similar(valueOther) : (valueThis instanceof JSONArray ? !((JSONArray)valueThis).similar(valueOther) : !valueThis.equals(valueOther)))) continue;
                return false;
            }
            return true;
        }
        catch (Throwable exception) {
            return false;
        }
    }

    public static Object stringToValue(String string) {
        if (string.isEmpty()) {
            return string;
        }
        if (string.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (string.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("null")) {
            return NULL;
        }
        char b = string.charAt(0);
        if (b >= '0' && b <= '9' || b == '-') {
            try {
                if (string.indexOf(46) > -1 || string.indexOf(101) > -1 || string.indexOf(69) > -1) {
                    double d = Double.parseDouble(string);
                    if (!Double.isInfinite(d) && !Double.isNaN(d)) {
                        return d;
                    }
                } else {
                    Long myLong = new Long(string);
                    if (string.equals(myLong.toString())) {
                        if (myLong == (long)myLong.intValue()) {
                            return myLong.intValue();
                        }
                        return myLong;
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return string;
    }

    public static void testValidity(Object o) throws JSONException {
        if (o != null && (o instanceof Double ? ((Double)o).isInfinite() || ((Double)o).isNaN() : o instanceof Float && (((Float)o).isInfinite() || ((Float)o).isNaN()))) {
            throw new JSONException("JSON does not allow non-finite numbers.");
        }
    }

    public JSONArray toJSONArray(JSONArray names) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        JSONArray ja = new JSONArray();
        for (int i = 0; i < names.length(); ++i) {
            ja.put(this.opt(names.getString(i)));
        }
        return ja;
    }

    public String toString() {
        try {
            return this.toString(0);
        }
        catch (Exception e) {
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String toString(int indentFactor) throws JSONException {
        StringWriter w = new StringWriter();
        StringBuffer stringBuffer = w.getBuffer();
        synchronized (stringBuffer) {
            return this.write(w, indentFactor, 0).toString();
        }
    }

    public static String valueToString(Object value) throws JSONException {
        if (value == null || value.equals(null)) {
            return "null";
        }
        if (value instanceof JSONString) {
            String object;
            try {
                object = ((JSONString)value).toJSONString();
            }
            catch (Exception e) {
                throw new JSONException(e);
            }
            if (object instanceof String) {
                return object;
            }
            throw new JSONException("Bad value from toJSONString: " + object);
        }
        if (value instanceof Number) {
            return JSONObject.numberToString((Number)value);
        }
        if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
            return value.toString();
        }
        if (value instanceof Map) {
            Map map = (Map)value;
            return new JSONObject(map).toString();
        }
        if (value instanceof Collection) {
            Collection coll = (Collection)value;
            return new JSONArray(coll).toString();
        }
        if (value.getClass().isArray()) {
            return new JSONArray(value).toString();
        }
        return JSONObject.quote(value.toString());
    }

    public static Object wrap(Object object) {
        try {
            String objectPackageName;
            if (object == null) {
                return NULL;
            }
            if (object instanceof JSONObject || object instanceof JSONArray || NULL.equals(object) || object instanceof JSONString || object instanceof Byte || object instanceof Character || object instanceof Short || object instanceof Integer || object instanceof Long || object instanceof Boolean || object instanceof Float || object instanceof Double || object instanceof String) {
                return object;
            }
            if (object instanceof Collection) {
                Collection coll = (Collection)object;
                return new JSONArray(coll);
            }
            if (object.getClass().isArray()) {
                return new JSONArray(object);
            }
            if (object instanceof Map) {
                Map map = (Map)object;
                return new JSONObject(map);
            }
            Package objectPackage = object.getClass().getPackage();
            String string = objectPackageName = objectPackage != null ? objectPackage.getName() : "";
            if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.") || object.getClass().getClassLoader() == null) {
                return object.toString();
            }
            return new JSONObject(object);
        }
        catch (Exception exception) {
            return null;
        }
    }

    public Writer write(Writer writer) throws JSONException {
        return this.write(writer, 0, 0);
    }

    static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException, IOException {
        if (value == null || value.equals(null)) {
            writer.write("null");
        } else if (value instanceof JSONObject) {
            ((JSONObject)value).write(writer, indentFactor, indent);
        } else if (value instanceof JSONArray) {
            ((JSONArray)value).write(writer, indentFactor, indent);
        } else if (value instanceof Map) {
            Map map = (Map)value;
            new JSONObject(map).write(writer, indentFactor, indent);
        } else if (value instanceof Collection) {
            Collection coll = (Collection)value;
            new JSONArray(coll).write(writer, indentFactor, indent);
        } else if (value.getClass().isArray()) {
            new JSONArray(value).write(writer, indentFactor, indent);
        } else if (value instanceof Number) {
            writer.write(JSONObject.numberToString((Number)value));
        } else if (value instanceof Boolean) {
            writer.write(value.toString());
        } else if (value instanceof JSONString) {
            String o;
            try {
                o = ((JSONString)value).toJSONString();
            }
            catch (Exception e) {
                throw new JSONException(e);
            }
            writer.write(o != null ? o.toString() : JSONObject.quote(value.toString()));
        } else {
            JSONObject.quote(value.toString(), writer);
        }
        return writer;
    }

    static final void indent(Writer writer, int indent) throws IOException {
        for (int i = 0; i < indent; ++i) {
            writer.write(32);
        }
    }

    Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
        try {
            boolean commanate = false;
            int length = this.length();
            Iterator<String> keys = this.keys();
            writer.write(123);
            if (length == 1) {
                String key = keys.next();
                writer.write(JSONObject.quote(key.toString()));
                writer.write(58);
                if (indentFactor > 0) {
                    writer.write(32);
                }
                JSONObject.writeValue(writer, this.map.get(key), indentFactor, indent);
            } else if (length != 0) {
                int newindent = indent + indentFactor;
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (commanate) {
                        writer.write(44);
                    }
                    if (indentFactor > 0) {
                        writer.write(10);
                    }
                    JSONObject.indent(writer, newindent);
                    writer.write(JSONObject.quote(key.toString()));
                    writer.write(58);
                    if (indentFactor > 0) {
                        writer.write(32);
                    }
                    JSONObject.writeValue(writer, this.map.get(key), indentFactor, newindent);
                    commanate = true;
                }
                if (indentFactor > 0) {
                    writer.write(10);
                }
                JSONObject.indent(writer, indent);
            }
            writer.write(125);
            return writer;
        }
        catch (IOException exception) {
            throw new JSONException(exception);
        }
    }

    private static final class Null {
        Null() {
        }

        protected final Object clone() {
            return this;
        }

        public boolean equals(Object object) {
            return object == null || object == this;
        }

        public String toString() {
            return "null";
        }
    }
}

