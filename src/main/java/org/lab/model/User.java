package org.lab.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record User(UUID id, String login, Instant createdAt) {
    public User {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(login, "login cannot be null");
        Objects.requireNonNull(createdAt, "createdAt cannot be null");

        if (login.isBlank()) {
            throw new IllegalArgumentException("login cannot be blank");
        }
    }

    public static User create(String login) {
        return new User(UUID.randomUUID(), login, Instant.now());
    }
}