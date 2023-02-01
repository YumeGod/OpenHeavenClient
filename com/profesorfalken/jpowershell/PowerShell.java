/*
 * Decompiled with CFR 0.152.
 */
package com.profesorfalken.jpowershell;

import com.profesorfalken.jpowershell.OSDetector;
import com.profesorfalken.jpowershell.PowerShellCodepage;
import com.profesorfalken.jpowershell.PowerShellCommandProcessor;
import com.profesorfalken.jpowershell.PowerShellConfig;
import com.profesorfalken.jpowershell.PowerShellNotAvailableException;
import com.profesorfalken.jpowershell.PowerShellResponse;
import com.profesorfalken.jpowershell.PowerShellResponseHandler;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PowerShell
implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(PowerShell.class.getName());
    private Process p;
    private long pid = -1L;
    private PrintWriter commandWriter;
    private boolean closed = false;
    private ExecutorService threadpool;
    private static final String DEFAULT_WIN_EXECUTABLE = "powershell.exe";
    private static final String DEFAULT_LINUX_EXECUTABLE = "powershell";
    private int waitPause = 5;
    private long maxWait = 10000L;
    private File tempFolder = null;
    private boolean scriptMode = false;
    public static final String END_SCRIPT_STRING = "--END-JPOWERSHELL-SCRIPT--";

    private PowerShell() {
    }

    public PowerShell configuration(Map<String, String> config) {
        try {
            this.waitPause = Integer.valueOf(config != null && config.get("waitPause") != null ? config.get("waitPause") : PowerShellConfig.getConfig().getProperty("waitPause"));
            this.maxWait = Long.valueOf(config != null && config.get("maxWait") != null ? config.get("maxWait") : PowerShellConfig.getConfig().getProperty("maxWait"));
            this.tempFolder = config != null && config.get("tempFolder") != null ? this.getTempFolder(config.get("tempFolder")) : this.getTempFolder(PowerShellConfig.getConfig().getProperty("tempFolder"));
        }
        catch (NumberFormatException nfe) {
            logger.log(Level.SEVERE, "Could not read configuration. Using default values.", nfe);
        }
        return this;
    }

    public static PowerShell openSession() throws PowerShellNotAvailableException {
        return PowerShell.openSession(null);
    }

    public static PowerShell openSession(String customPowerShellExecutablePath) throws PowerShellNotAvailableException {
        PowerShell powerShell = new PowerShell();
        powerShell.configuration(null);
        String powerShellExecutablePath = customPowerShellExecutablePath == null ? (OSDetector.isWindows() ? DEFAULT_WIN_EXECUTABLE : DEFAULT_LINUX_EXECUTABLE) : customPowerShellExecutablePath;
        return powerShell.initalize(powerShellExecutablePath);
    }

    private PowerShell initalize(String powerShellExecutablePath) throws PowerShellNotAvailableException {
        String codePage = PowerShellCodepage.getIdentifierByCodePageName(Charset.defaultCharset().name());
        ProcessBuilder pb = OSDetector.isWindows() ? new ProcessBuilder("cmd.exe", "/c", "chcp", codePage, ">", "NUL", "&", powerShellExecutablePath, "-ExecutionPolicy", "Bypass", "-NoExit", "-NoProfile", "-Command", "-") : new ProcessBuilder(powerShellExecutablePath, "-nologo", "-noexit", "-Command", "-");
        pb.redirectErrorStream(true);
        try {
            this.p = pb.start();
            if (this.p.waitFor(5L, TimeUnit.SECONDS) && !this.p.isAlive()) {
                throw new PowerShellNotAvailableException("Cannot execute PowerShell. Please make sure that it is installed in your system. Errorcode:" + this.p.exitValue());
            }
        }
        catch (IOException | InterruptedException ex) {
            throw new PowerShellNotAvailableException("Cannot execute PowerShell. Please make sure that it is installed in your system", ex);
        }
        this.commandWriter = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(this.p.getOutputStream())), true);
        this.threadpool = Executors.newFixedThreadPool(2);
        this.pid = this.getPID();
        return this;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public PowerShellResponse executeCommand(String command) {
        String commandOutput = "";
        boolean isError = false;
        boolean timeout = false;
        this.checkState();
        PowerShellCommandProcessor commandProcessor = new PowerShellCommandProcessor("standard", this.p.getInputStream(), this.waitPause, this.scriptMode);
        Future<String> result = this.threadpool.submit(commandProcessor);
        this.commandWriter.println(command);
        try {
            if (!result.isDone()) {
                try {
                    commandOutput = result.get(this.maxWait, TimeUnit.MILLISECONDS);
                }
                catch (TimeoutException timeoutEx) {
                    timeout = true;
                    isError = true;
                    result.cancel(true);
                }
            }
        }
        catch (InterruptedException | ExecutionException ex) {
            logger.log(Level.SEVERE, "Unexpected error when processing PowerShell command", ex);
            isError = true;
        }
        finally {
            commandProcessor.close();
        }
        return new PowerShellResponse(isError, commandOutput, timeout);
    }

    public static PowerShellResponse executeSingleCommand(String command) {
        PowerShellResponse response = null;
        try (PowerShell session = PowerShell.openSession();){
            response = session.executeCommand(command);
        }
        catch (PowerShellNotAvailableException ex) {
            logger.log(Level.SEVERE, "PowerShell not available", ex);
        }
        return response;
    }

    public PowerShell executeCommandAndChain(String command, PowerShellResponseHandler ... response) {
        PowerShellResponse powerShellResponse = this.executeCommand(command);
        if (response.length > 0) {
            this.handleResponse(response[0], powerShellResponse);
        }
        return this;
    }

    private void handleResponse(PowerShellResponseHandler response, PowerShellResponse powerShellResponse) {
        try {
            response.handle(powerShellResponse);
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, "PowerShell not available", ex);
        }
    }

    public boolean isLastCommandInError() {
        return Boolean.valueOf(this.executeCommand("$?").getCommandOutput()) == false;
    }

    public PowerShellResponse executeScript(String scriptPath) {
        return this.executeScript(scriptPath, "");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public PowerShellResponse executeScript(String scriptPath, String params) {
        try (BufferedReader srcReader = new BufferedReader(new FileReader(new File(scriptPath)));){
            PowerShellResponse powerShellResponse = this.executeScript(srcReader, params);
            return powerShellResponse;
        }
        catch (FileNotFoundException fnfex) {
            logger.log(Level.SEVERE, "Unexpected error when processing PowerShell script: file not found", fnfex);
            return new PowerShellResponse(true, "Wrong script path: " + scriptPath, false);
        }
        catch (IOException ioe) {
            logger.log(Level.SEVERE, "Unexpected error when processing PowerShell script", ioe);
            return new PowerShellResponse(true, "IO error reading: " + scriptPath, false);
        }
    }

    public PowerShellResponse executeScript(BufferedReader srcReader) {
        return this.executeScript(srcReader, "");
    }

    public PowerShellResponse executeScript(BufferedReader srcReader, String params) {
        PowerShellResponse response;
        if (srcReader != null) {
            File tmpFile = this.createWriteTempFile(srcReader);
            if (tmpFile != null) {
                this.scriptMode = true;
                response = this.executeCommand(tmpFile.getAbsolutePath() + " " + params);
                this.scriptMode = false;
                tmpFile.delete();
            } else {
                response = new PowerShellResponse(true, "Cannot create temp script file!", false);
            }
        } else {
            logger.log(Level.SEVERE, "Script buffered reader is null!");
            response = new PowerShellResponse(true, "Script buffered reader is null!", false);
        }
        return response;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private File createWriteTempFile(BufferedReader srcReader) {
        BufferedWriter tmpWriter = null;
        File tmpFile = null;
        try {
            String line;
            tmpFile = File.createTempFile("psscript_" + new Date().getTime(), ".ps1", this.tempFolder);
            if (!tmpFile.exists()) {
                File file = null;
                return file;
            }
            tmpWriter = new BufferedWriter(new FileWriter(tmpFile));
            while (srcReader != null && (line = srcReader.readLine()) != null) {
                tmpWriter.write(line);
                tmpWriter.newLine();
            }
            tmpWriter.write("Write-Output \"--END-JPOWERSHELL-SCRIPT--\"");
        }
        catch (IOException ioex) {
            logger.log(Level.SEVERE, "Unexpected error while writing temporary PowerShell script", ioex);
        }
        finally {
            try {
                if (tmpWriter != null) {
                    tmpWriter.close();
                }
            }
            catch (IOException ex) {
                logger.log(Level.SEVERE, "Unexpected error when processing temporary PowerShell script", ex);
            }
        }
        return tmpFile;
    }

    @Override
    public void close() {
        if (!this.closed) {
            try {
                Future<String> closeTask = this.threadpool.submit(() -> {
                    this.commandWriter.println("exit");
                    this.p.waitFor();
                    return "OK";
                });
                if (!this.closeAndWait(closeTask) && this.pid > 0L) {
                    Logger.getLogger(PowerShell.class.getName()).log(Level.INFO, "Forcing PowerShell to close. PID: " + this.pid);
                    try {
                        Runtime.getRuntime().exec("taskkill.exe /PID " + this.pid + " /F /T");
                        this.closed = true;
                    }
                    catch (IOException e) {
                        Logger.getLogger(PowerShell.class.getName()).log(Level.SEVERE, "Unexpected error while killing powershell process", e);
                    }
                }
            }
            catch (InterruptedException | ExecutionException ex) {
                logger.log(Level.SEVERE, "Unexpected error when when closing PowerShell", ex);
            }
            finally {
                this.commandWriter.close();
                try {
                    if (this.p.isAlive()) {
                        this.p.getInputStream().close();
                    }
                }
                catch (IOException ex) {
                    logger.log(Level.SEVERE, "Unexpected error when when closing streams", ex);
                }
                if (this.threadpool != null) {
                    try {
                        this.threadpool.shutdownNow();
                        this.threadpool.awaitTermination(5L, TimeUnit.SECONDS);
                    }
                    catch (InterruptedException ex) {
                        logger.log(Level.SEVERE, "Unexpected error when when shutting down thread pool", ex);
                    }
                }
                this.closed = true;
            }
        }
    }

    private boolean closeAndWait(Future<String> task) throws InterruptedException, ExecutionException {
        boolean closed = true;
        if (!task.isDone()) {
            try {
                task.get(this.maxWait, TimeUnit.MILLISECONDS);
            }
            catch (TimeoutException timeoutEx) {
                logger.log(Level.WARNING, "Powershell process cannot be closed. Session seems to be blocked");
                task.cancel(true);
                closed = false;
            }
        }
        return closed;
    }

    private void checkState() {
        if (this.closed) {
            throw new IllegalStateException("PowerShell is already closed. Please open a new session.");
        }
    }

    private long getPID() {
        String commandOutput = this.executeCommand("$pid").getCommandOutput();
        if (!(commandOutput = commandOutput.replaceAll("\\D", "")).isEmpty()) {
            return Long.valueOf(commandOutput);
        }
        return -1L;
    }

    private File getTempFolder(String tempPath) {
        File folder;
        if (tempPath != null && (folder = new File(tempPath)).exists()) {
            return folder;
        }
        return null;
    }
}

