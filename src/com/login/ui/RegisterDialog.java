package com.login.ui;

import com.login.dao.UserDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Modal dialog for new-user registration.
 */
public class RegisterDialog extends JDialog {

    private static final Color BG     = new Color(15, 23, 42);
    private static final Color CARD   = new Color(30, 41, 59);
    private static final Color ACCENT = new Color(99, 102, 241);
    private static final Color FG     = new Color(248, 250, 252);
    private static final Color FG_MUT = new Color(148, 163, 184);
    private static final Color BORDER = new Color(51, 65, 85);
    private static final Color SUCCESS = new Color(34, 197, 94);
    private static final Color ERROR   = new Color(239, 68, 68);

    private JTextField     fullNameField, usernameField, emailField;
    private JPasswordField passwordField, confirmField;
    private JLabel         statusLabel;
    private final UserDAO  userDAO;

    public RegisterDialog(JFrame parent, UserDAO userDAO) {
        super(parent, "Create Account", true);
        this.userDAO = userDAO;

        setSize(400, 520);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new GridBagLayout());

        add(buildPanel(), new GridBagConstraints());
    }

    private JPanel buildPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(CARD);
        p.setBorder(new EmptyBorder(32, 36, 32, 36));
        p.setPreferredSize(new Dimension(340, 470));

        JLabel title = label("Create an account", 20, Font.BOLD, FG);
        title.setAlignmentX(CENTER_ALIGNMENT);

        fullNameField  = field();
        usernameField  = field();
        emailField     = field();
        passwordField  = new JPasswordField(); styleField(passwordField); ((JPasswordField)passwordField).setEchoChar('•');
        confirmField   = new JPasswordField(); styleField(confirmField);  ((JPasswordField)confirmField).setEchoChar('•');

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);

        JButton submit = accentButton("Register");
        submit.addActionListener(e -> handleRegister());

        p.add(title);
        p.add(Box.createVerticalStrut(22));
        p.add(row("Full Name",        fullNameField));
        p.add(Box.createVerticalStrut(12));
        p.add(row("Username",         usernameField));
        p.add(Box.createVerticalStrut(12));
        p.add(row("Email",            emailField));
        p.add(Box.createVerticalStrut(12));
        p.add(row("Password",         passwordField));
        p.add(Box.createVerticalStrut(12));
        p.add(row("Confirm Password", confirmField));
        p.add(Box.createVerticalStrut(8));
        p.add(statusLabel);
        p.add(Box.createVerticalStrut(16));
        p.add(submit);

        return p;
    }

    private void handleRegister() {
        String fullName  = fullNameField.getText().trim();
        String username  = usernameField.getText().trim();
        String email     = emailField.getText().trim();
        String password  = new String(passwordField.getPassword());
        String confirm   = new String(confirmField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            status("All fields are required.", ERROR); return;
        }
        if (!password.equals(confirm)) {
            status("Passwords do not match.", ERROR); return;
        }
        if (password.length() < 6) {
            status("Password must be at least 6 characters.", ERROR); return;
        }
        if (!email.contains("@")) {
            status("Enter a valid email address.", ERROR); return;
        }

        boolean ok = userDAO.register(username, password, email, fullName);
        if (ok) {
            status("Account created! You can now log in.", SUCCESS);
            JOptionPane.showMessageDialog(this,
                    "Registration successful!\nYou can now sign in with your credentials.",
                    "Done", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            status("Username already taken or DB error.", ERROR);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JTextField field() {
        JTextField f = new JTextField();
        styleField(f);
        return f;
    }

    private void styleField(JTextField f) {
        f.setBackground(BG);
        f.setForeground(FG);
        f.setCaretColor(FG);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(7, 11, 7, 11)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
    }

    private JPanel row(String labelText, JTextField f) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setBackground(CARD);
        row.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lbl = label(labelText, 12, Font.BOLD, FG_MUT);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        row.add(lbl);
        row.add(Box.createVerticalStrut(4));
        row.add(f);
        return row;
    }

    private void status(String msg, Color color) {
        statusLabel.setText(msg);
        statusLabel.setForeground(color);
    }

    private JLabel label(String t, int size, int style, Color c) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(c);
        return l;
    }

    private JButton accentButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(FG);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        return btn;
    }
}
