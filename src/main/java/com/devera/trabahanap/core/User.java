package com.devera.trabahanap.core;

import javafx.beans.property.*;

/**
 * User model using JavaFX properties for UI binding.
 * Fields:
 *  - userId: unique Firestore document id
 *  - username
 *  - email
 *  - password
 *  - createdAt: timestamp in epoch millis
 */
public class User {

    private final StringProperty userId = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final LongProperty createdAt = new SimpleLongProperty();

    public User() {}

    public User(String userId, String username, String email, String password, long createdAt) {
        this.userId.set(userId);
        this.username.set(username);
        this.email.set(email);
        this.password.set(password);
        this.createdAt.set(createdAt);
    }

    // --- Getters and Setters ---
    public String getUserId() { return userId.get(); }
    public void setUserId(String value) { userId.set(value); }
    public StringProperty userIdProperty() { return userId; }

    public String getUsername() { return username.get(); }
    public void setUsername(String value) { username.set(value); }
    public StringProperty usernameProperty() { return username; }

    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }
    public StringProperty emailProperty() { return email; }

    public String getPassword() { return password.get(); }
    public void setPassword(String value) { password.set(value); }
    public StringProperty passwordProperty() { return password; }

    public long getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(long value) { createdAt.set(value); }
    public LongProperty createdAtProperty() { return createdAt; }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + getUserId() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}
