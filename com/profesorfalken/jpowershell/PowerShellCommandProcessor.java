/*
 * Decompiled with CFR 0.152.
 */
package com.profesorfalken.jpowershell;

import com.profesorfalken.jpowershell.PowerShell;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

class PowerShellCommandProcessor
implements Callable<String> {
    private static final String CRLF = "\r\n";
    private final BufferedReader reader;
    private boolean closed = false;
    private final boolean scriptMode;
    private final int waitPause;

    public PowerShellCommandProcessor(String name, InputStream inputStream, int waitPause, boolean scriptMode) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.waitPause = waitPause;
        this.scriptMode = scriptMode;
    }

    @Override
    public String call() throws InterruptedException {
        StringBuilder powerShellOutput = new StringBuilder();
        try {
            if (this.startReading()) {
                this.readData(powerShellOutput);
            }
        }
        catch (IOException ioe) {
            Logger.getLogger(PowerShell.class.getName()).log(Level.SEVERE, "Unexpected error reading PowerShell output", ioe);
            return ioe.getMessage();
        }
        return powerShellOutput.toString().replaceAll("\\s+$", "");
    }

    private void readData(StringBuilder powerShellOutput) throws IOException {
        String line;
        while (!(null == (line = this.reader.readLine()) || this.scriptMode && line.equals("--END-JPOWERSHELL-SCRIPT--"))) {
            powerShellOutput.append(line).append(CRLF);
            if (this.scriptMode) continue;
            try {
                if (!this.closed && this.canContinueReading()) continue;
                break;
            }
            catch (InterruptedException ex) {
                Logger.getLogger(PowerShellCommandProcessor.class.getName()).log(Level.SEVERE, "Error executing command and reading result", ex);
            }
        }
    }

    private boolean startReading() throws IOException, InterruptedException {
        while (!this.reader.ready()) {
            Thread.sleep(this.waitPause);
            if (!this.closed) continue;
            return false;
        }
        return true;
    }

    private boolean canContinueReading() throws IOException, InterruptedException {
        if (!this.reader.ready()) {
            Thread.sleep(this.waitPause);
        }
        if (!this.reader.ready()) {
            Thread.sleep(50L);
        }
        return this.reader.ready();
    }

    public void close() {
        this.closed = true;
    }
}

