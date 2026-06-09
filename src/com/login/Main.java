package com.login;

import com.login.ui.LoginFrame;

import javax.swing.*;

/**
 * Entry point for the Login application.
 * Run: java -cp ".:lib/mysql-connector-j-*.jar" com.login.Main
 */
public class Main {
    public static void main(String[] args) {
        // Use system look-and-feel where available, fallback gracefully
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Launch GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
