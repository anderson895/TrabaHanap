package com.devera.trabahanap.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.LongProperty;

public class User {

    private final StringProperty userId;
    private final StringProperty username;
    private final StringProperty email;
    private final StringProperty password;
    private final StringProperty role;
    private final LongProperty createdAt;

    public User() {
        this(null, null, null, null, null, 0L);
    }

    public User(String userId, String username, String email, String password, String role, long createdAt) {
        this.userId = new SimpleStringProperty(userId);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        this.createdAt = new SimpleLongProperty(createdAt);
    }

    // --- userId ---
    public String getUserId() { return userId.get(); }
    public void setUserId(String value) { userId.set(value); }
    public StringProperty userIdProperty() { return userId; }

    // --- username ---
    public String getUsername() { return username.get(); }
    public void setUsername(String value) { username.set(value); }
    public StringProperty usernameProperty() { return username; }

    // --- email ---
    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }
    public StringProperty emailProperty() { return email; }

    // --- password ---
    public String getPassword() { return password.get(); }
    public void setPassword(String value) { password.set(value); }
    public StringProperty passwordProperty() { return password; }

    // --- role ---
    public String getRole() { return role.get(); }
    public void setRole(String value) { role.set(value); }
    public StringProperty roleProperty() { return role; }

    // --- createdAt ---
    public long getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(long value) { createdAt.set(value); }
    public LongProperty createdAtProperty() { return createdAt; }
}
