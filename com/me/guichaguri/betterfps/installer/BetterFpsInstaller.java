/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.installer;

import com.me.guichaguri.betterfps.installer.AlgorithmTester;
import com.me.guichaguri.betterfps.installer.InstanceInstaller;
import com.me.guichaguri.betterfps.installer.VersionSelector;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class BetterFpsInstaller
extends JFrame
implements ActionListener {
    private final String installerDesc = "<html>This is the installer for <strong>BetterFps</strong><br>If you are using Forge, you just need to drop this file in the mods folder<br>It's recommended closing the Minecraft Launcher before installing.</html>";
    private final String modUrl = "http://minecraft.curseforge.com/mc-mods/229876-betterfps";
    private final String INSTALL = "install";
    private final String PAGE = "page";
    private final String CALC_ALGORITHM = "calc_algorithm";
    private final String CHANGE_FILE = "change_file";
    private final JTextField installLocation;
    private final JFileChooser fc;
    private final JDialog versionDialog = null;
    private final JComboBox versionComboBox = null;

    public static void main(String[] args) {
        String tester = System.getProperty("tester", null);
        if (tester != null) {
            JFrame load = new JFrame();
            load.add(new JLabel("Testing each algorithm..."));
            load.setSize(250, 100);
            load.setLocationRelativeTo(null);
            load.setVisible(true);
            load.requestFocusInWindow();
            AlgorithmTester.open(null, new File(tester), null, null);
            load.setVisible(false);
            return;
        }
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch (Exception exception) {
                    // empty catch block
                }
                BetterFpsInstaller installer = new BetterFpsInstaller();
                installer.setVisible(true);
            }
        });
    }

    public BetterFpsInstaller() {
        this.setTitle("BetterFps Installer");
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 5;
        c.ipady = 5;
        c.insets = new Insets(5, 5, 5, 5);
        JLabel title = new JLabel("BetterFps Installer");
        title.setFont(title.getFont().deriveFont(32.0f));
        this.add((Component)title, c);
        c.gridy = 1;
        JLabel desc = new JLabel("<html>This is the installer for <strong>BetterFps</strong><br>If you are using Forge, you just need to drop this file in the mods folder<br>It's recommended closing the Minecraft Launcher before installing.</html>");
        this.add((Component)desc, c);
        c.gridy = 2;
        c.fill = 2;
        this.installLocation = new JTextField(12);
        this.installLocation.setText(InstanceInstaller.getSuggestedMinecraftFolder().getAbsolutePath());
        this.add((Component)this.installLocation, c);
        c.gridx = 1;
        c.fill = 0;
        JButton choose = new JButton("...");
        choose.setActionCommand("change_file");
        choose.addActionListener(this);
        this.add((Component)choose, c);
        this.fc = new JFileChooser();
        this.fc.setFileSelectionMode(1);
        this.fc.setDialogTitle("Select the Minecraft Installation folder (.minecraft)");
        c.fill = 2;
        c.gridx = 0;
        c.gridy = 3;
        JButton install = new JButton("Install");
        install.setActionCommand("install");
        install.addActionListener(this);
        this.add((Component)install, c);
        c.gridy = 4;
        JButton testAlgorithms = new JButton("Test Algorithms");
        testAlgorithms.setToolTipText("Test all algorithm to see which is faster");
        testAlgorithms.setActionCommand("calc_algorithm");
        testAlgorithms.addActionListener(this);
        this.add((Component)testAlgorithms, c);
        c.gridy = 5;
        JButton page = new JButton("Official Page");
        page.setActionCommand("page");
        page.addActionListener(this);
        this.add((Component)page, c);
        this.setSize(450, 325);
        this.setResizable(false);
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        if (cmd.equals("install")) {
            File file = new File(this.installLocation.getText());
            if (!file.exists() || !file.isDirectory()) {
                JOptionPane.showMessageDialog(this, "The install location is invalid.", "Oops!", 2);
                return;
            }
            List<String> versions = InstanceInstaller.getVersions(file);
            VersionSelector.open(this, file, versions);
        } else if (cmd.equals("page")) {
            try {
                Desktop.getDesktop().browse(new URI("http://minecraft.curseforge.com/mc-mods/229876-betterfps"));
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "http://minecraft.curseforge.com/mc-mods/229876-betterfps", "URL", 1);
            }
        } else if (cmd.equals("change_file")) {
            int val = this.fc.showDialog(this, "Select");
            if (val == 0) {
                this.installLocation.setText(this.fc.getSelectedFile().getAbsolutePath());
            }
        } else if (cmd.equals("calc_algorithm")) {
            File file = new File(this.installLocation.getText());
            AlgorithmTester.open(this, file, "calc_algorithm", this);
        }
    }
}

