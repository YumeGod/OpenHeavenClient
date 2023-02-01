/*
 * Decompiled with CFR 0.152.
 */
package com.profesorfalken.wmi4java;

import com.profesorfalken.wmi4java.WMI4Java;
import com.profesorfalken.wmi4java.WMI4JavaUtil;
import com.profesorfalken.wmi4java.WMIException;
import com.profesorfalken.wmi4java.WMIStub;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class WMIVBScript
implements WMIStub {
    private static final String ROOT_CIMV2 = "root/cimv2";
    private static final String IMPERSONATION_VARIABLE = "Set objWMIService=GetObject(\"winmgmts:{impersonationLevel=impersonate}!\\\\";
    private static final String CRLF = "\r\n";

    WMIVBScript() {
    }

    private static String executeScript(String scriptCode) throws WMIException {
        String scriptResponse = "";
        File tmpFile = null;
        OutputStreamWriter writer = null;
        BufferedReader errorOutput = null;
        try {
            String line;
            tmpFile = File.createTempFile("wmi4java" + new Date().getTime(), ".vbs");
            writer = new FileWriter(tmpFile);
            writer.write(scriptCode);
            writer.flush();
            writer.close();
            Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/C", "cscript.exe", "/NoLogo", tmpFile.getAbsolutePath()});
            BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = processOutput.readLine()) != null) {
                if (line.isEmpty()) continue;
                scriptResponse = scriptResponse + line + CRLF;
            }
            if (scriptResponse.isEmpty()) {
                errorOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String errorResponse = "";
                while ((line = errorOutput.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    errorResponse = errorResponse + line + CRLF;
                }
                if (!errorResponse.isEmpty()) {
                    throw new WMIException("WMI operation finished in error: " + errorResponse);
                }
            }
        }
        catch (Exception ex) {
            throw new WMIException(ex.getMessage(), ex);
        }
        finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (tmpFile != null) {
                    tmpFile.delete();
                }
                if (errorOutput != null) {
                    errorOutput.close();
                }
            }
            catch (IOException ioe) {
                Logger.getLogger(WMI4Java.class.getName()).log(Level.SEVERE, "Exception closing in finally", ioe);
            }
        }
        return scriptResponse.trim();
    }

    @Override
    public String listClasses(String namespace, String computerName) throws WMIException {
        try {
            StringBuilder scriptCode = new StringBuilder(200);
            String namespaceCommand = ROOT_CIMV2;
            if (!"*".equals(namespace)) {
                namespaceCommand = namespace;
            }
            scriptCode.append(IMPERSONATION_VARIABLE).append(computerName).append("/").append(namespaceCommand).append("\")").append(CRLF);
            scriptCode.append("Set colClasses = objWMIService.SubclassesOf()").append(CRLF);
            scriptCode.append("For Each objClass in colClasses").append(CRLF);
            scriptCode.append("For Each objClassQualifier In objClass.Qualifiers_").append(CRLF);
            scriptCode.append("WScript.Echo objClass.Path_.Class").append(CRLF);
            scriptCode.append("Next").append(CRLF);
            scriptCode.append("Next").append(CRLF);
            return WMIVBScript.executeScript(scriptCode.toString());
        }
        catch (Exception ex) {
            throw new WMIException(ex.getMessage(), ex);
        }
    }

    @Override
    public String listProperties(String wmiClass, String namespace, String computerName) throws WMIException {
        try {
            StringBuilder scriptCode = new StringBuilder(200);
            String namespaceCommand = ROOT_CIMV2;
            if (!"*".equals(namespace)) {
                namespaceCommand = namespace;
            }
            scriptCode.append(IMPERSONATION_VARIABLE).append(computerName).append("/").append(namespaceCommand).append(":").append(wmiClass).append("\")").append(CRLF);
            scriptCode.append("For Each objClassProperty In objWMIService.Properties_").append(CRLF);
            scriptCode.append("WScript.Echo objClassProperty.Name").append(CRLF);
            scriptCode.append("Next").append(CRLF);
            return WMIVBScript.executeScript(scriptCode.toString());
        }
        catch (Exception ex) {
            throw new WMIException(ex.getMessage(), ex);
        }
    }

    @Override
    public String listObject(String wmiClass, String namespace, String computerName) throws WMIException {
        return this.queryObject(wmiClass, null, null, namespace, computerName);
    }

    @Override
    public String queryObject(String wmiClass, List<String> wmiProperties, List<String> conditions, String namespace, String computerName) throws WMIException {
        List<String> usedWMIProperties = wmiProperties == null || wmiProperties.isEmpty() ? WMI4Java.get().VBSEngine().computerName(computerName).namespace(namespace).listProperties(wmiClass) : wmiProperties;
        try {
            StringBuilder scriptCode = new StringBuilder(200);
            String namespaceCommand = ROOT_CIMV2;
            if (!"*".equals(namespace)) {
                namespaceCommand = namespace;
            }
            scriptCode.append(IMPERSONATION_VARIABLE).append(computerName).append("/").append(namespaceCommand).append("\")").append(CRLF);
            scriptCode.append("Set colClasses = objWMIService.SubclassesOf()").append(CRLF);
            scriptCode.append("Set wmiQueryData = objWMIService.ExecQuery(\"Select ").append("*").append(" from ").append(wmiClass);
            if (conditions != null && !conditions.isEmpty()) {
                scriptCode.append(" where ").append(WMI4JavaUtil.join(" AND ", conditions));
            }
            scriptCode.append("\")").append(CRLF);
            scriptCode.append("For Each element In wmiQueryData").append(CRLF);
            for (String wmiProperty : usedWMIProperties) {
                if (!wmiProperty.equals("ConfigOptions")) {
                    scriptCode.append("Wscript.Echo \"").append(wmiProperty).append(": \" & ").append("element.").append(wmiProperty).append(CRLF);
                    continue;
                }
                scriptCode.append("Wscript.Echo \"").append(wmiProperty).append(": \" & ").append("Join(element.").append(wmiProperty).append(", \"|\")").append(CRLF);
            }
            scriptCode.append("Next").append(CRLF);
            return WMIVBScript.executeScript(scriptCode.toString());
        }
        catch (Exception ex) {
            throw new WMIException(ex.getMessage(), ex);
        }
    }
}

