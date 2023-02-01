/*
 * Decompiled with CFR 0.152.
 */
package com.me.guichaguri.betterfps.installer;

import com.me.guichaguri.betterfps.BetterFpsHelper;
import com.me.guichaguri.betterfps.math.JavaMath;
import com.me.guichaguri.betterfps.math.LibGDXMath;
import com.me.guichaguri.betterfps.math.RivensFullMath;
import com.me.guichaguri.betterfps.math.RivensHalfMath;
import com.me.guichaguri.betterfps.math.RivensMath;
import com.me.guichaguri.betterfps.math.TaylorMath;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AlgorithmTester
extends JFrame
implements ActionListener {
    private static final Class[] algorithms = new Class[]{JavaMath.class, VanillaMath.class, TaylorMath.class, LibGDXMath.class, RivensMath.class, RivensFullMath.class, RivensHalfMath.class};
    private static AlgorithmTester INSTANCE = null;
    private final String CHANGE_ALGORITHM = "tester_change_algorithm";
    private final String CLOSE_TESTER = "close_tester";
    private final String DESCRIPTION = "<html><center>We recommend testing a few times<br>to confirm which is the best algorithm<br><small>[Notice that this tester is still in development]</small></center></html>";
    private final File mcDir;
    private String bestAlgorithm = null;
    private String bestAlgorithmName = null;

    public static HashMap<String, Long> testAlgorithms() {
        HashMap<String, Long> results = new HashMap<String, Long>();
        for (Class algorithm : algorithms) {
            try {
                Method sin = algorithm.getDeclaredMethod("sin", Float.TYPE);
                Method cos = algorithm.getDeclaredMethod("cos", Float.TYPE);
                long startTime = System.nanoTime();
                for (int i = 0; i < 360000; ++i) {
                    float angle = (float)i / 1000.0f;
                    sin.invoke(null, Float.valueOf(angle));
                    cos.invoke(null, Float.valueOf(angle));
                }
                long endTime = System.nanoTime();
                String name = algorithm.getSimpleName();
                for (Map.Entry<String, String> e : BetterFpsHelper.helpers.entrySet()) {
                    if (!e.getValue().equals(name)) continue;
                    name = e.getKey();
                    break;
                }
                results.put(name, endTime - startTime);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return results;
    }

    public static void open(Component c, File mcDir, String calcAction, ActionListener listener) {
        if (INSTANCE != null) {
            INSTANCE.setVisible(false);
        }
        INSTANCE = new AlgorithmTester(mcDir, AlgorithmTester.testAlgorithms(), calcAction, listener);
        INSTANCE.setLocationRelativeTo(c);
        INSTANCE.setVisible(true);
        INSTANCE.requestFocusInWindow();
    }

    public AlgorithmTester(final File mcDir, HashMap<String, Long> results, String CALC_ALGORITHM, ActionListener calcAction) {
        this.mcDir = mcDir;
        this.setTitle("Test Results");
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = 2;
        c.gridx = 0;
        this.add((Component)new JLabel("<html><center>We recommend testing a few times<br>to confirm which is the best algorithm<br><small>[Notice that this tester is still in development]</small></center></html>", 0), c);
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridLayout(0, 2, 5, 0));
        long bestAlgorithmTime = 0L;
        for (Map.Entry<String, Long> e : results.entrySet()) {
            String algorithm = e.getKey();
            long v = e.getValue();
            String displayName = algorithm;
            if (BetterFpsHelper.displayHelpers.containsKey(algorithm)) {
                displayName = BetterFpsHelper.displayHelpers.get(algorithm);
            }
            if (v < bestAlgorithmTime || this.bestAlgorithm == null) {
                bestAlgorithmTime = v;
                this.bestAlgorithm = algorithm;
                this.bestAlgorithmName = displayName;
            }
            resultsPanel.add(new JLabel(displayName, 4));
            resultsPanel.add(new JLabel((float)Math.round((float)v / 1000000.0f * 100.0f) / 100.0f + "ms", 2));
        }
        this.add((Component)resultsPanel, c);
        JButton changeAlgorithm = new JButton("Change Algorithm to " + this.bestAlgorithmName);
        changeAlgorithm.setToolTipText("This will choose the best algorithm and change the config file for you.");
        changeAlgorithm.setActionCommand("tester_change_algorithm");
        changeAlgorithm.addActionListener(this);
        this.add((Component)changeAlgorithm, c);
        JButton calcAgain = new JButton("Test Again");
        if (CALC_ALGORITHM == null || calcAction == null) {
            calcAgain.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent event) {
                    AlgorithmTester.open(null, mcDir, null, null);
                }
            });
            this.setDefaultCloseOperation(3);
        } else {
            calcAgain.setActionCommand(CALC_ALGORITHM);
            calcAgain.addActionListener(calcAction);
        }
        this.add((Component)calcAgain, c);
        JButton close = new JButton("Close");
        close.setActionCommand("close_tester");
        close.addActionListener(this);
        this.add((Component)close, c);
        Dimension d = this.getPreferredSize();
        this.setSize(new Dimension(d.width + 50, d.height + 50));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        if (cmd.equals("close_tester")) {
            this.setVisible(false);
            if (this.getDefaultCloseOperation() == 3) {
                System.exit(0);
            }
        } else if (cmd.equals("tester_change_algorithm")) {
            if (!this.mcDir.exists() || !this.mcDir.isDirectory()) {
                JOptionPane.showMessageDialog(this, "The install location is invalid.", "Oops!", 2);
                return;
            }
            BetterFpsHelper.MCDIR = this.mcDir;
            this.setVisible(false);
            System.out.println(this.bestAlgorithm);
            JOptionPane.showMessageDialog(this, "The algorithm was set to " + this.bestAlgorithmName + ".\n\nNote: If the game is started, you have to restart it to take effect", "Done!", 1);
            if (this.getDefaultCloseOperation() == 3) {
                System.exit(0);
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

    private static class VanillaMath {
        private static final float[] SIN_TABLE = new float[65536];

        private VanillaMath() {
        }

        public static float sin(float val) {
            return SIN_TABLE[(int)(val * 10430.378f) & 0xFFFF];
        }

        public static float cos(float val) {
            return SIN_TABLE[(int)(val * 10430.378f + 16384.0f) & 0xFFFF];
        }

        static {
            for (int i = 0; i < 65536; ++i) {
                VanillaMath.SIN_TABLE[i] = (float)Math.sin((double)i * Math.PI * 2.0 / 65536.0);
            }
        }
    }
}

