package com.login.ui;

import com.login.dao.UserDAO;
import com.login.model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.event.*;
import java.io.File;

/**
 * Login window built with Java Swing.
 */
public class LoginFrame extends JFrame {

    // ── Palette ───────────────────────────────────────────────────────────────
    private static final Color BG         = new Color(15, 23, 42);       // slate-900
    private static final Color CARD       = new Color(30, 41, 59);       // slate-800
    private static final Color ACCENT     = new Color(99, 102, 241);     // indigo-500
    private static final Color ACCENT_HOV = new Color(79, 70, 229);      // indigo-600
    private static final Color FG         = new Color(248, 250, 252);    // slate-50
    private static final Color FG_MUTED   = new Color(148, 163, 184);    // slate-400
    private static final Color BORDER     = new Color(51, 65, 85);       // slate-700
    private static final Color SUCCESS    = new Color(34, 197, 94);      // green-500
    private static final Color ERROR      = new Color(239, 68, 68);      // red-500

    // ── Components ────────────────────────────────────────────────────────────
    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JCheckBox      showPasswordBox;
    private JButton        loginButton;
    private JButton        registerButton;
    private JLabel         statusLabel;

    private final UserDAO userDAO = new UserDAO();

    // ─────────────────────────────────────────────────────────────────────────

    public LoginFrame() {
        setTitle("Login — Java JDBC App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(420, 580);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setContentPane(new BackgroundPanel());
        setLayout(new GridBagLayout());

        add(buildCard(), new GridBagConstraints());
        setVisible(true);
    }

    // ── Card Panel ────────────────────────────────────────────────────────────

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 40, 40, 40));
        card.setPreferredSize(new Dimension(360, 500));

        // Brand icon placeholder
        JLabel icon = new JLabel("🔐", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        icon.setAlignmentX(CENTER_ALIGNMENT);

        // Title
        JLabel title = styledLabel("Welcome back", 22, Font.BOLD, FG);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = styledLabel("Sign in to continue", 13, Font.PLAIN, FG_MUTED);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        // Fields
        JPanel usernamePanel = buildFieldPanel("Username", false);
        usernameField = (JTextField) usernamePanel.getClientProperty("field");
        usernamePanel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel passwordPanel = buildFieldPanel("Password", true);
        passwordField = (JPasswordField) passwordPanel.getClientProperty("field");
        passwordPanel.setAlignmentX(CENTER_ALIGNMENT);

        // Show password checkbox
        showPasswordBox = new JCheckBox("Show password");
        showPasswordBox.setBackground(CARD);
        showPasswordBox.setForeground(FG_MUTED);
        showPasswordBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordBox.setAlignmentX(CENTER_ALIGNMENT);
        showPasswordBox.addActionListener(e -> {
            if (showPasswordBox.isSelected())
                passwordField.setEchoChar('\0');
            else
                passwordField.setEchoChar('•');
        });

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Buttons
        loginButton = accentButton("Sign In");
        loginButton.addActionListener(e -> handleLogin());

        registerButton = ghostButton("Create an account");
        registerButton.addActionListener(e -> openRegisterDialog());

        // Enter key triggers login
        getRootPane().setDefaultButton(loginButton);

        // ── Assemble ───────────────────────────────────────────────────────
        card.add(icon);
        card.add(Box.createVerticalStrut(12));
        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));
        card.add(usernamePanel);
        card.add(Box.createVerticalStrut(14));
        card.add(passwordPanel);
        card.add(Box.createVerticalStrut(8));
        card.add(showPasswordBox);
        card.add(Box.createVerticalStrut(6));
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(18));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(10));
        card.add(registerButton);

        return card;
    }

    // ── Field Panel ───────────────────────────────────────────────────────────

    private JPanel buildFieldPanel(String labelText, boolean isPassword) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel label = styledLabel(labelText, 12, Font.BOLD, FG_MUTED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(CENTER_ALIGNMENT);

        JTextField field = isPassword ? new JPasswordField() : new JTextField();
        if (field instanceof JPasswordField pf) pf.setEchoChar('•');

        field.setBackground(new Color(15, 23, 42));
        field.setForeground(FG);
        field.setCaretColor(FG);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Hover / focus highlight
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 2, true),
                        new EmptyBorder(7, 11, 7, 11)));
            }
            @Override public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1, true),
                        new EmptyBorder(8, 12, 8, 12)));
            }
        });

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        panel.putClientProperty("field", field);
        return panel;
    }

    // ── Login Logic ───────────────────────────────────────────────────────────

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            setStatus("Please fill in all fields.", ERROR);
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Signing in…");

        // Run DB call off the EDT
        SwingWorker<User, Void> worker = new SwingWorker<>() {
            @Override protected User doInBackground() {
                return userDAO.authenticate(username, password);
            }
            @Override protected void done() {
                loginButton.setEnabled(true);
                loginButton.setText("Sign In");
                try {
                    User user = get();
                    if (user != null) {
                        setStatus("Welcome, " + user.getFullName() + "!", SUCCESS);
                        JOptionPane.showMessageDialog(LoginFrame.this,
                                "✅  Login successful!\n\nUser  : " + user.getUsername()
                                + "\nEmail : " + user.getEmail()
                                + "\nRole  : " + user.getRole(),
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        // TODO: open your dashboard here
                    } else {
                        setStatus("Invalid username or password.", ERROR);
                        passwordField.setText("");
                    }
                } catch (Exception ex) {
                    setStatus("Connection error. Check DB settings.", ERROR);
                }
            }
        };
        worker.execute();
    }

    // ── Register Dialog ───────────────────────────────────────────────────────

    private void openRegisterDialog() {
        RegisterDialog dialog = new RegisterDialog(this, userDAO);
        dialog.setVisible(true);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void setStatus(String text, Color color) {
        statusLabel.setText(text);
        statusLabel.setForeground(color);
    }

    private JLabel styledLabel(String text, int size, int style, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", style, size));
        lbl.setForeground(color);
        return lbl;
    }

    private JButton accentButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? ACCENT_HOV : ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleButton(btn, FG);
        btn.setOpaque(false);
        return btn;
    }

    private JButton ghostButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, FG_MUTED);
        btn.setBackground(CARD);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(8, 16, 8, 16)));
        return btn;
    }

    private void styleButton(JButton btn, Color fg) {
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
    }
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                File imgFile = new File("D:/LoginApp/src/resources/background.png");
                if (imgFile.exists()) {
                    backgroundImage = ImageIO.read(imgFile);
                } else {
                    System.err.println("Image not found: " + imgFile.getAbsolutePath());
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}