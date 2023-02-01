/*
 * Decompiled with CFR 0.152.
 */
package com.profesorfalken.wmi4java;

import com.profesorfalken.wmi4java.WMIClass;
import com.profesorfalken.wmi4java.WMIException;
import com.profesorfalken.wmi4java.WMIPowerShell;
import com.profesorfalken.wmi4java.WMIStub;
import com.profesorfalken.wmi4java.WMIVBScript;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WMI4Java {
    private static final String NEWLINE_REGEX = "\\r?\\n";
    private static final String SPACE_REGEX = "\\s+";
    private static final String GENERIC_ERROR_MSG = "Error calling WMI4Java";
    private String namespace = "*";
    private String computerName = ".";
    private boolean forceVBEngine = false;
    List<String> properties = null;
    List<String> filters = null;

    private WMI4Java() {
    }

    private WMIStub getWMIStub() {
        if (this.forceVBEngine) {
            return new WMIVBScript();
        }
        return new WMIPowerShell();
    }

    public static WMI4Java get() {
        return new WMI4Java();
    }

    public WMI4Java namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public WMI4Java computerName(String computerName) {
        this.computerName = computerName;
        return this;
    }

    public WMI4Java PowerShellEngine() {
        this.forceVBEngine = false;
        return this;
    }

    public WMI4Java VBSEngine() {
        this.forceVBEngine = true;
        return this;
    }

    public WMI4Java properties(List<String> properties) {
        this.properties = properties;
        return this;
    }

    public WMI4Java filters(List<String> filters) {
        this.filters = filters;
        return this;
    }

    public List<String> listClasses() throws WMIException {
        ArrayList<String> wmiClasses = new ArrayList<String>();
        try {
            String[] dataStringLines;
            String rawData = this.getWMIStub().listClasses(this.namespace, this.computerName);
            for (String line : dataStringLines = rawData.split(NEWLINE_REGEX)) {
                if (line.isEmpty() || line.startsWith("_")) continue;
                String[] infos = line.split(SPACE_REGEX);
                wmiClasses.addAll(Arrays.asList(infos));
            }
            HashSet<String> hs = new HashSet<String>();
            hs.addAll(wmiClasses);
            wmiClasses.clear();
            wmiClasses.addAll(hs);
        }
        catch (Exception ex) {
            Logger.getLogger(WMI4Java.class.getName()).log(Level.SEVERE, GENERIC_ERROR_MSG, ex);
            throw new WMIException(ex);
        }
        return wmiClasses;
    }

    public List<String> listProperties(String wmiClass) throws WMIException {
        ArrayList<String> foundPropertiesList = new ArrayList<String>();
        try {
            String[] dataStringLines;
            String rawData = this.getWMIStub().listProperties(wmiClass, this.namespace, this.computerName);
            for (String line : dataStringLines = rawData.split(NEWLINE_REGEX)) {
                if (line.isEmpty()) continue;
                foundPropertiesList.add(line.trim());
            }
            List<String> notAllowed = Arrays.asList("Equals", "GetHashCode", "GetType", "ToString");
            foundPropertiesList.removeAll(notAllowed);
        }
        catch (Exception ex) {
            Logger.getLogger(WMI4Java.class.getName()).log(Level.SEVERE, GENERIC_ERROR_MSG, ex);
            throw new WMIException(ex);
        }
        return foundPropertiesList;
    }

    public Map<String, String> getWMIObject(WMIClass wmiClass) {
        return this.getWMIObject(wmiClass.getName());
    }

    public Map<String, String> getWMIObject(String wmiClass) throws WMIException {
        HashMap<String, String> foundWMIClassProperties = new HashMap<String, String>();
        try {
            String[] dataStringLines;
            String rawData = this.properties != null || this.filters != null ? this.getWMIStub().queryObject(wmiClass, this.properties, this.filters, this.namespace, this.computerName) : this.getWMIStub().listObject(wmiClass, this.namespace, this.computerName);
            for (String line : dataStringLines = rawData.split(NEWLINE_REGEX)) {
                String[] entry;
                if (line.isEmpty() || (entry = line.split(":")) == null || entry.length != 2) continue;
                foundWMIClassProperties.put(entry[0].trim(), entry[1].trim());
            }
        }
        catch (WMIException ex) {
            Logger.getLogger(WMI4Java.class.getName()).log(Level.SEVERE, GENERIC_ERROR_MSG, ex);
            throw new WMIException(ex);
        }
        return foundWMIClassProperties;
    }

    public List<Map<String, String>> getWMIObjectList(WMIClass wmiClass) {
        return this.getWMIObjectList(wmiClass.getName());
    }

    public List<Map<String, String>> getWMIObjectList(String wmiClass) throws WMIException {
        ArrayList<Map<String, String>> foundWMIClassProperties = new ArrayList<Map<String, String>>();
        try {
            String[] dataStringObjects;
            String rawData = this.properties != null || this.filters != null ? this.getWMIStub().queryObject(wmiClass, this.properties, this.filters, this.namespace, this.computerName) : this.getWMIStub().listObject(wmiClass, this.namespace, this.computerName);
            for (String dataStringObject : dataStringObjects = rawData.split("\\r?\\n\\r?\\n")) {
                String[] dataStringLines = dataStringObject.split(NEWLINE_REGEX);
                HashMap<String, String> objectProperties = new HashMap<String, String>();
                for (String line : dataStringLines) {
                    String[] entry;
                    if (line.isEmpty() || (entry = line.split(":")).length != 2) continue;
                    objectProperties.put(entry[0].trim(), entry[1].trim());
                }
                foundWMIClassProperties.add(objectProperties);
            }
        }
        catch (WMIException ex) {
            Logger.getLogger(WMI4Java.class.getName()).log(Level.SEVERE, GENERIC_ERROR_MSG, ex);
            throw new WMIException(ex);
        }
        return foundWMIClassProperties;
    }

    public String getRawWMIObjectOutput(WMIClass wmiClass) {
        return this.getRawWMIObjectOutput(wmiClass.getName());
    }

    public String getRawWMIObjectOutput(String wmiClass) throws WMIException {
        String rawData;
        try {
            rawData = this.properties != null || this.filters != null ? this.getWMIStub().queryObject(wmiClass, this.properties, this.filters, this.namespace, this.computerName) : this.getWMIStub().listObject(wmiClass, this.namespace, this.computerName);
        }
        catch (WMIException ex) {
            Logger.getLogger(WMI4Java.class.getName()).log(Level.SEVERE, GENERIC_ERROR_MSG, ex);
            throw new WMIException(ex);
        }
        return rawData;
    }
}

