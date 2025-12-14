package org.lab.model;

import org.lab.enums.MilestoneStatus;
import org.lab.enums.UserRole;

import java.util.*;

public class Project {
    private final UUID id;

    private final List<User> participants;
    private final List<Milestone> milestones;
    private final List<BugReport> bugReports;

    private final Map<UUID, UserRole> roles;

    private String name;

    public Project(UUID id, String name) {
        this.id = id;

        participants = new ArrayList<>();
        milestones = new ArrayList<>();
        bugReports = new ArrayList<>();

        roles = new HashMap<>();

        this.name = name;

        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
    }

    public UUID getId() {
        return id;
    }

    public List<User> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    public List<Milestone> getMilestones() {
        return Collections.unmodifiableList(milestones);
    }

    public List<BugReport> getBugReports() {
        return Collections.unmodifiableList(bugReports);
    }

    public Map<UUID, UserRole> getRoles() {
        return Collections.unmodifiableMap(roles);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addParticipant(User user, UserRole role) {
        Objects.requireNonNull(user, "user cannot be null");
        Objects.requireNonNull(role, "role cannot be null");

        if (role == UserRole.MANAGER && roles.containsValue(UserRole.MANAGER)) {
            throw new IllegalStateException("Project already has a manager");
        }

        if (role == UserRole.TEAM_LEADER && roles.containsValue(UserRole.TEAM_LEADER)) {
            throw new IllegalStateException("Project already has a team leader");
        }

        if (roles.containsKey(user.id())) {
            throw new IllegalStateException("User is already in project");
        }

        roles.put(user.id(), role);
        participants.add(user);
    }

    public boolean removeParticipant(User user) {
        Objects.requireNonNull(user, "user cannot be null");
        var role = roles.remove(user.id());

        return role != null && participants.remove(user);
    }

    public void addMilestone(Milestone milestone) {
        Objects.requireNonNull(milestone, "milestone cannot be null");

        if (!id.equals(milestone.getProjectId())) {
            throw new IllegalArgumentException("Milestone belongs to another project");
        }

        if (milestone.getStatus() == MilestoneStatus.ACTIVE && milestones.stream().anyMatch(m -> m.getStatus() == MilestoneStatus.ACTIVE)) {
            throw new IllegalStateException("Only one active milestone is allowed");
        }

        milestones.add(milestone);
    }

    public boolean removeMilestone(Milestone milestone) {
        Objects.requireNonNull(milestone, "milestone cannot be null");

        return milestones.remove(milestone);
    }

    public void changeMilestoneStatus(Milestone milestone, MilestoneStatus status) {
        Objects.requireNonNull(milestone, "milestone cannot be null");
        Objects.requireNonNull(status, "status cannot be null");

        if (!milestones.contains(milestone)) {
            throw new IllegalArgumentException("Milestone is not part of this project");
        }

        if (status == MilestoneStatus.ACTIVE) {
            boolean anotherActive = milestones.stream().anyMatch(m -> m != milestone && m.getStatus() == MilestoneStatus.ACTIVE);

            if (anotherActive) {
                throw new IllegalStateException("Only one active milestone is allowed");
            }
        }

        milestone.setStatus(status);
    }

    public void assignTicket(Ticket ticket, List<User> developers) {
        Objects.requireNonNull(ticket, "ticket cannot be null");
        Objects.requireNonNull(developers, "developers cannot be null");

        if (!id.equals(ticket.getProjectId())) {
            throw new IllegalArgumentException("Ticket belongs to another project");
        }

        for (User user : developers) {
            if (roles.get(user.id()) != UserRole.DEVELOPER) {
                throw new IllegalArgumentException("Only developers can be assigned to tickets");
            }
        }

        ticket.assignUsers(List.copyOf(developers));
    }

    public void addBugReport(BugReport bugReport) {
        Objects.requireNonNull(bugReport, "bugReport cannot be null");

        if (!id.equals(bugReport.getProjectId())) {
            throw new IllegalArgumentException("BugReport belongs to another project");
        }

        bugReports.add(bugReport);
    }

    public boolean removeBugReport(BugReport bugReport) {
        Objects.requireNonNull(bugReport, "bugReport cannot be null");

        return bugReports.remove(bugReport);
    }
}