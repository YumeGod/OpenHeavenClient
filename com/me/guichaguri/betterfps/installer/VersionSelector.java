/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.installer;

import com.me.guichaguri.betterfps.installer.InstanceInstaller;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class VersionSelector
extends JDialog
implements ActionListener {
    private static VersionSelector INSTANCE = null;
    private final String INSTALL = "install";
    private final File mcDir;
    private final JComboBox version;

    public static void open(Component c, File mcDir, List<String> versionNames) {
        if (INSTANCE != null) {
            INSTANCE.setVisible(false);
        }
        INSTANCE = new VersionSelector(mcDir, versionNames);
        INSTANCE.setLocationRelativeTo(c);
        INSTANCE.setVisible(true);
    }

    private VersionSelector(File mcDir, List<String> versionNames) {
        this.mcDir = mcDir;
        this.setTitle("Select a Version");
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = 2;
        c.gridx = 0;
        this.version = new JComboBox();
        for (String v : versionNames) {
            this.version.addItem(v);
        }
        this.add((Component)this.version, c);
        JButton install = new JButton("Install");
        install.setActionCommand("install");
        install.addActionListener(this);
        this.add((Component)install, c);
        Dimension d = this.getPreferredSize();
        this.setSize(new Dimension(d.width + 50, d.height + 50));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        if (cmd.equals("install")) {
            String ver = this.version.getSelectedItem().toString();
            if (ver.toLowerCase().contains("forge")) {
                Object[] options = new String[]{"Yes, I don't care", "No, I'll do it correctly", "What?!"};
                int r = JOptionPane.showOptionDialog(this, "Looks like you're using Forge.\nYou just need to drop the BetterFps jar file in the mods folder.\nDo you want to continue anyway?", "Forge Version", 1, 3, null, options, options[1]);
                if (r == 1) {
                    this.setVisible(false);
                    return;
                }
                if (r == 2 || r == -1) {
                    return;
                }
            }
            try {
                InstanceInstaller.install(this.mcDir, ver);
                this.setVisible(false);
                JOptionPane.showMessageDialog(this, "BetterFps was successfully installed!", "Done!", 1);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error has ocurred: " + ex.getClass().getSimpleName() + "\nTry choosing another version", "Oops!", 0);
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (!visible) {
            INSTANCE = null;
        }
    }
}

