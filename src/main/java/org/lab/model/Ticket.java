package org.lab.model;

import org.lab.enums.TicketStatus;

import java.time.Instant;
import java.util.*;

public class Ticket {
    private final UUID id;

    private final UUID projectId;
    private final UUID milestoneId;

    private final Instant createdAt;

    private final List<User> assignees;

    private String name;
    private TicketStatus status;

    public Ticket(UUID id, UUID projectId, UUID milestoneId, String name) {
        this.id = Objects.requireNonNull(id, "id cannot be null");

        this.projectId = Objects.requireNonNull(projectId, "projectId cannot be null");
        this.milestoneId = Objects.requireNonNull(milestoneId, "milestoneId cannot be null");

        createdAt = Instant.now();

        assignees = new ArrayList<>();

        this.name = Objects.requireNonNull(name, "name cannot be null");
        status = TicketStatus.NEW;

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

    public UUID getMilestoneId() {
        return milestoneId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<User> getAssignees() {
        return Collections.unmodifiableList(assignees);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = Objects.requireNonNull(status, "status cannot be null");
    }

    void assignUsers(List<User> users) {
        assignees.clear();
        assignees.addAll(users);
    }
}