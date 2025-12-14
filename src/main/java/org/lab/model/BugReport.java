package org.lab.model;

import org.lab.enums.BugReportStatus;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class BugReport {
    private final UUID id;
    private final UUID projectId;

    private final Instant createdAt;

    private String name;
    private BugReportStatus status;

    public BugReport(UUID id, UUID projectId, String name) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId cannot be null");

        createdAt = Instant.now();

        this.name = Objects.requireNonNull(name, "name cannot be null");
        status = BugReportStatus.NEW;

        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public BugReportStatus getStatus() {
        return status;
    }

    public void setStatus(BugReportStatus status) {
        this.status = Objects.requireNonNull(status);
    }
}