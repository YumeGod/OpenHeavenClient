/*
 * Decompiled with CFR 0.152.
 */
package com.profesorfalken.wmi4java;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import com.profesorfalken.wmi4java.WMI4JavaUtil;
import com.profesorfalken.wmi4java.WMIException;
import com.profesorfalken.wmi4java.WMIStub;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class WMIPowerShell
implements WMIStub {
    private static final String NAMESPACE_PARAM = "-Namespace ";
    private static final String COMPUTERNAME_PARAM = "-ComputerName ";
    private static final String GETWMIOBJECT_COMMAND = "Get-WMIObject ";

    WMIPowerShell() {
    }

    private static String executeCommand(String command) throws WMIException {
        String commandResponse = null;
        try (PowerShell powerShell = null;){
            powerShell = PowerShell.openSession();
            HashMap<String, String> config = new HashMap<String, String>();
            config.put("maxWait", "20000");
            PowerShellResponse psResponse = powerShell.configuration(config).executeCommand(command);
            if (psResponse.isError()) {
                throw new WMIException("WMI operation finished in error: " + psResponse.getCommandOutput());
            }
            commandResponse = psResponse.getCommandOutput().trim();
            powerShell.close();
        }
        return commandResponse;
    }

    @Override
    public String listClasses(String namespace, String computerName) throws WMIException {
        String namespaceString = "";
        if (!"*".equals(namespace)) {
            namespaceString = namespaceString + NAMESPACE_PARAM + namespace;
        }
        return WMIPowerShell.executeCommand(GETWMIOBJECT_COMMAND + namespaceString + " -List | Sort Name");
    }

    @Override
    public String listProperties(String wmiClass, String namespace, String computerName) throws WMIException {
        String command = this.initCommand(wmiClass, namespace, computerName);
        command = command + " | ";
        command = command + "Select-Object * -excludeproperty \"_*\" | ";
        command = command + "Get-Member | select name | format-table -hidetableheader";
        return WMIPowerShell.executeCommand(command);
    }

    @Override
    public String listObject(String wmiClass, String namespace, String computerName) throws WMIException {
        String command = this.initCommand(wmiClass, namespace, computerName);
        command = command + " | ";
        command = command + "Select-Object * -excludeproperty \"_*\" | ";
        command = command + "Format-List *";
        return WMIPowerShell.executeCommand(command);
    }

    @Override
    public String queryObject(String wmiClass, List<String> wmiProperties, List<String> conditions, String namespace, String computerName) throws WMIException {
        String command = this.initCommand(wmiClass, namespace, computerName);
        List<String> usedWMIProperties = wmiProperties == null || wmiProperties.isEmpty() ? Collections.singletonList("*") : wmiProperties;
        command = command + " | ";
        command = command + "Select-Object " + WMI4JavaUtil.join(", ", usedWMIProperties) + " -excludeproperty \"_*\" | ";
        if (conditions != null && !conditions.isEmpty()) {
            for (String condition : conditions) {
                command = command + "Where-Object -FilterScript {" + condition + "} | ";
            }
        }
        command = command + "Format-List *";
        return WMIPowerShell.executeCommand(command);
    }

    private String initCommand(String wmiClass, String namespace, String computerName) {
        String command = GETWMIOBJECT_COMMAND + wmiClass + " ";
        if (!"*".equals(namespace)) {
            command = command + NAMESPACE_PARAM + namespace + " ";
        }
        if (!computerName.isEmpty()) {
            command = command + COMPUTERNAME_PARAM + computerName + " ";
        }
        return command;
    }
}

