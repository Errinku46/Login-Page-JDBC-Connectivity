package com.login.model;

/**
 * Represents a user record from the database.
 */
public class User {

    private int    id;
    private String username;
    private String password;   // stored as hashed value in DB
    private String email;
    private String fullName;
    private String role;       // e.g. "admin", "user"

    // ── Constructors ────────────────────────────────────────────────────────

    public User() {}

    public User(int id, String username, String email, String fullName, String role) {
        this.id       = id;
        this.username = username;
        this.email    = email;
        this.fullName = fullName;
        this.role     = role;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public int    getId()       { return id; }
    public void   setId(int id) { this.id = id; }

    public String getUsername()             { return username; }
    public void   setUsername(String u)     { this.username = u; }

    public String getPassword()             { return password; }
    public void   setPassword(String p)     { this.password = p; }

    public String getEmail()                { return email; }
    public void   setEmail(String e)        { this.email = e; }

    public String getFullName()             { return fullName; }
    public void   setFullName(String fn)    { this.fullName = fn; }

    public String getRole()                 { return role; }
    public void   setRole(String r)         { this.role = r; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email
                + "', role='" + role + "'}";
    }
}
