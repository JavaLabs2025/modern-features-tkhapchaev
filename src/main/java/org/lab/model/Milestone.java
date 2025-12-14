package org.lab.model;

import org.lab.enums.MilestoneStatus;
import org.lab.enums.TicketStatus;

import java.time.Instant;
import java.util.*;

public class Milestone {
    private final UUID id;
    private final UUID projectId;

    private final Instant start;
    private final Instant end;

    private final List<Ticket> tickets;

    private String name;
    private MilestoneStatus status;

    public Milestone(UUID id, UUID projectId, Instant start, Instant end, String name) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId cannot be null");

        this.start = Objects.requireNonNull(start, "start cannot be null");
        this.end = Objects.requireNonNull(end, "end cannot be null");

        if (end.isBefore(start) || end.equals(start)) {
            throw new IllegalArgumentException("end must be greater than start");
        }

        tickets = new ArrayList<>();

        this.name = Objects.requireNonNull(name, "name cannot be null");
        status = MilestoneStatus.OPEN;

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

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public List<Ticket> getTickets() {
        return Collections.unmodifiableList(tickets);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    public MilestoneStatus getStatus() {
        return status;
    }

    void setStatus(MilestoneStatus status) {
        if (status == MilestoneStatus.CLOSED && tickets.stream().anyMatch(ticket -> ticket.getStatus() != TicketStatus.DONE)) {
            throw new IllegalStateException("Milestone can be closed only when all tickets are done");
        }

        this.status = status;
    }

    public void addTicket(Ticket ticket) {
        Objects.requireNonNull(ticket, "ticket cannot be null");

        if (!projectId.equals(ticket.getProjectId())) {
            throw new IllegalArgumentException("Ticket belongs to another project");
        }

        if (!id.equals(ticket.getMilestoneId())) {
            throw new IllegalArgumentException("Ticket belongs to another milestone");
        }

        tickets.add(ticket);
    }

    public boolean removeTicket(Ticket ticket) {
        Objects.requireNonNull(ticket, "ticket cannot be null");

        return tickets.remove(ticket);
    }
}