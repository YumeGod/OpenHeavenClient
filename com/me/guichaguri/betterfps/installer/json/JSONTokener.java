/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.installer.json;

import com.me.guichaguri.betterfps.installer.json.JSONArray;
import com.me.guichaguri.betterfps.installer.json.JSONException;
import com.me.guichaguri.betterfps.installer.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class JSONTokener {
    private long character;
    private boolean eof;
    private long index;
    private long line;
    private char previous;
    private final Reader reader;
    private boolean usePrevious;

    public JSONTokener(Reader reader) {
        this.reader = reader.markSupported() ? reader : new BufferedReader(reader);
        this.eof = false;
        this.usePrevious = false;
        this.previous = '\u0000';
        this.index = 0L;
        this.character = 1L;
        this.line = 1L;
    }

    public JSONTokener(InputStream inputStream) throws JSONException {
        this(new InputStreamReader(inputStream));
    }

    public JSONTokener(String s) {
        this(new StringReader(s));
    }

    public void back() throws JSONException {
        if (this.usePrevious || this.index <= 0L) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        --this.index;
        --this.character;
        this.usePrevious = true;
        this.eof = false;
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 55;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 87;
        }
        return -1;
    }

    public boolean end() {
        return this.eof && !this.usePrevious;
    }

    public boolean more() throws JSONException {
        this.next();
        if (this.end()) {
            return false;
        }
        this.back();
        return true;
    }

    public char next() throws JSONException {
        int c;
        if (this.usePrevious) {
            this.usePrevious = false;
            c = this.previous;
        } else {
            try {
                c = this.reader.read();
            }
            catch (IOException exception) {
                throw new JSONException(exception);
            }
            if (c <= 0) {
                this.eof = true;
                c = 0;
            }
        }
        ++this.index;
        if (this.previous == '\r') {
            ++this.line;
            this.character = c == 10 ? 0L : 1L;
        } else if (c == 10) {
            ++this.line;
            this.character = 0L;
        } else {
            ++this.character;
        }
        this.previous = (char)c;
        return this.previous;
    }

    public char next(char c) throws JSONException {
        char n = this.next();
        if (n != c) {
            throw this.syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
        }
        return n;
    }

    public String next(int n) throws JSONException {
        if (n == 0) {
            return "";
        }
        char[] chars = new char[n];
        for (int pos = 0; pos < n; ++pos) {
            chars[pos] = this.next();
            if (!this.end()) continue;
            throw this.syntaxError("Substring bounds error");
        }
        return new String(chars);
    }

    public char nextClean() throws JSONException {
        char c;
        while ((c = this.next()) != '\u0000' && c <= ' ') {
        }
        return c;
    }

    public String nextString(char quote) throws JSONException {
        StringBuilder sb = new StringBuilder();
        block13: while (true) {
            char c = this.next();
            switch (c) {
                case '\u0000': 
                case '\n': 
                case '\r': {
                    throw this.syntaxError("Unterminated string");
                }
                case '\\': {
                    c = this.next();
                    switch (c) {
                        case 'b': {
                            sb.append('\b');
                            continue block13;
                        }
                        case 't': {
                            sb.append('\t');
                            continue block13;
                        }
                        case 'n': {
                            sb.append('\n');
                            continue block13;
                        }
                        case 'f': {
                            sb.append('\f');
                            continue block13;
                        }
                        case 'r': {
                            sb.append('\r');
                            continue block13;
                        }
                        case 'u': {
                            sb.append((char)Integer.parseInt(this.next(4), 16));
                            continue block13;
                        }
                        case '\"': 
                        case '\'': 
                        case '/': 
                        case '\\': {
                            sb.append(c);
                            continue block13;
                        }
                    }
                    throw this.syntaxError("Illegal escape.");
                }
            }
            if (c == quote) {
                return sb.toString();
            }
            sb.append(c);
        }
    }

    public String nextTo(char delimiter) throws JSONException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c;
            if ((c = this.next()) == delimiter || c == '\u0000' || c == '\n' || c == '\r') {
                if (c != '\u0000') {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }

    public String nextTo(String delimiters) throws JSONException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c;
            if (delimiters.indexOf(c = this.next()) >= 0 || c == '\u0000' || c == '\n' || c == '\r') {
                if (c != '\u0000') {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }

    public Object nextValue() throws JSONException {
        char c = this.nextClean();
        switch (c) {
            case '\"': 
            case '\'': {
                return this.nextString(c);
            }
            case '{': {
                this.back();
                return new JSONObject(this);
            }
            case '[': {
                this.back();
                return new JSONArray(this);
            }
        }
        StringBuilder sb = new StringBuilder();
        while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
            sb.append(c);
            c = this.next();
        }
        this.back();
        String string = sb.toString().trim();
        if (string.isEmpty()) {
            throw this.syntaxError("Missing value");
        }
        return JSONObject.stringToValue(string);
    }

    public char skipTo(char to) throws JSONException {
        char c;
        try {
            long startIndex = this.index;
            long startCharacter = this.character;
            long startLine = this.line;
            this.reader.mark(1000000);
            do {
                if ((c = this.next()) != '\u0000') continue;
                this.reader.reset();
                this.index = startIndex;
                this.character = startCharacter;
                this.line = startLine;
                return c;
            } while (c != to);
        }
        catch (IOException exception) {
            throw new JSONException(exception);
        }
        this.back();
        return c;
    }

    public JSONException syntaxError(String message) {
        return new JSONException(message + this.toString());
    }

    public String toString() {
        return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
    }
}

