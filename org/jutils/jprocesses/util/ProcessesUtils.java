/*
 * Decompiled with CFR 0.152.
 */
package org.jutils.jprocesses.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessesUtils {
    private static final String CRLF = "\r\n";
    private static String customDateFormat;
    private static Locale customLocale;

    private ProcessesUtils() {
    }

    public static String executeCommand(String ... command) {
        String commandOutput = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            commandOutput = ProcessesUtils.readData(processBuilder.start());
        }
        catch (IOException ex) {
            commandOutput = "";
            Logger.getLogger(ProcessesUtils.class.getName()).log(Level.SEVERE, "Error executing command", ex);
            ex.printStackTrace();
        }
        return commandOutput;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String readData(Process process) {
        StringBuilder commandOutput = new StringBuilder();
        BufferedReader processOutput = null;
        try {
            String line;
            processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = processOutput.readLine()) != null) {
                if (line.isEmpty()) continue;
                commandOutput.append(line).append(CRLF);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(ProcessesUtils.class.getName()).log(Level.SEVERE, "Error reading data", ex);
        }
        finally {
            try {
                if (processOutput != null) {
                    processOutput.close();
                }
            }
            catch (IOException ioe) {
                Logger.getLogger(ProcessesUtils.class.getName()).log(Level.SEVERE, "Error closing reader", ioe);
            }
        }
        return commandOutput.toString();
    }

    public static int executeCommandAndGetCode(String ... command) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
        }
        catch (IOException ex) {
            Logger.getLogger(ProcessesUtils.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        catch (InterruptedException ex) {
            Logger.getLogger(ProcessesUtils.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        return process.exitValue();
    }

    public static String parseWindowsDateTimeToSimpleTime(String dateTime) {
        String returnedDate = dateTime;
        if (dateTime != null && !dateTime.isEmpty()) {
            String hour = dateTime.substring(8, 10);
            String minutes = dateTime.substring(10, 12);
            String seconds = dateTime.substring(12, 14);
            returnedDate = hour + ":" + minutes + ":" + seconds;
        }
        return returnedDate;
    }

    public static String parseWindowsDateTimeToFullDate(String dateTime) {
        String returnedDate = dateTime;
        if (dateTime != null && !dateTime.isEmpty()) {
            String year = dateTime.substring(0, 4);
            String month = dateTime.substring(4, 6);
            String day = dateTime.substring(6, 8);
            String hour = dateTime.substring(8, 10);
            String minutes = dateTime.substring(10, 12);
            String seconds = dateTime.substring(12, 14);
            returnedDate = month + "/" + day + "/" + year + " " + hour + ":" + minutes + ":" + seconds;
        }
        return returnedDate;
    }

    public static String parseUnixLongTimeToFullDate(String longFormatDate) throws ParseException {
        SimpleDateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        ArrayList<String> formatsToTry = new ArrayList<String>();
        formatsToTry.addAll(Arrays.asList("MMM dd HH:mm:ss yyyy", "dd MMM HH:mm:ss yyyy"));
        ArrayList<Locale> localesToTry = new ArrayList<Locale>();
        localesToTry.addAll(Arrays.asList(Locale.getDefault(), Locale.getDefault(Locale.Category.FORMAT), Locale.ENGLISH));
        if (ProcessesUtils.getCustomDateFormat() != null) {
            formatsToTry.add(0, ProcessesUtils.getCustomDateFormat());
        }
        if (ProcessesUtils.getCustomLocale() != null) {
            localesToTry.add(0, ProcessesUtils.getCustomLocale());
        }
        ParseException lastException = null;
        for (Locale locale : localesToTry) {
            for (String format : formatsToTry) {
                SimpleDateFormat originalFormat = new SimpleDateFormat(format, locale);
                try {
                    return targetFormat.format(originalFormat.parse(longFormatDate));
                }
                catch (ParseException ex) {
                    lastException = ex;
                }
            }
        }
        throw lastException;
    }

    public static String getCustomDateFormat() {
        return customDateFormat;
    }

    public static void setCustomDateFormat(String dateFormat) {
        customDateFormat = dateFormat;
    }

    public static Locale getCustomLocale() {
        return customLocale;
    }

    public static void setCustomLocale(Locale customLocale) {
        ProcessesUtils.customLocale = customLocale;
    }
}

