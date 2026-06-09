package com.login.dao;

import com.login.model.User;
import com.login.util.DBConnection;
import com.login.util.PasswordUtil;

import java.sql.*;

/**
 * Data Access Object for User-related database operations.
 */
public class UserDAO {

    // ── Authenticate ─────────────────────────────────────────────────────────

    /**
     * Validates credentials and returns the matching User, or null on failure.
     * Uses a PreparedStatement to prevent SQL injection.
     */
    public User authenticate(String username, String plainPassword) {
        String sql = "SELECT id, username, password, email, full_name, role "
                   + "FROM users WHERE username = ? AND is_active = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");

                    // Verify the password hash
                    if (PasswordUtil.verify(plainPassword, storedHash)) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setFullName(rs.getString("full_name"));
                        user.setRole(rs.getString("role"));
                        updateLastLogin(conn, user.getId());
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        return null;
    }

    // ── Register ──────────────────────────────────────────────────────────────

    /**
     * Inserts a new user record.
     * Returns true on success, false if username/email already exists.
     */
    public boolean register(String username, String plainPassword,
                             String email, String fullName) {
        if (usernameExists(username)) {
            System.err.println("Username already taken: " + username);
            return false;
        }

        String sql = "INSERT INTO users (username, password, email, full_name, role, is_active) "
                   + "VALUES (?, ?, ?, ?, 'user', 1)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hash(plainPassword));
            ps.setString(3, email);
            ps.setString(4, fullName);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private void updateLastLogin(Connection conn, int userId) {
        String sql = "UPDATE users SET last_login = NOW() WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not update last_login: " + e.getMessage());
        }
    }
}
