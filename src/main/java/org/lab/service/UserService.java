package org.lab.service;

import org.lab.enums.BugReportStatus;
import org.lab.enums.UserRole;
import org.lab.model.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class UserService {
    private final Map<UUID, User> users;

    public UserService() {
        users = new HashMap<>();
    }

    public User register(String login) {
        User user = User.create(login);
        users.put(user.id(), user);

        return user;
    }

    public User getById(UUID userId) {
        return users.get(userId);
    }

    public List<Project> getUserProjects(User user, Collection<Project> allProjects) {
        return allProjects.stream().filter(p -> p.getRoles().containsKey(user.id())).toList();
    }

    public List<Ticket> getUserTickets(User user, Collection<Project> allProjects) {
        return allProjects.stream()
                .flatMap(p -> p.getMilestones().stream())
                .flatMap(m -> m.getTickets().stream())
                .filter(t -> t.getAssignees().contains(user))
                .toList();
    }

    public List<BugReport> getUserBugReports(User user, Collection<Project> projects) {
        return projects.stream()
                .filter(p -> p.getRoles().get(user.id()) == UserRole.DEVELOPER)
                .flatMap(p -> p.getBugReports().stream())
                .filter(br -> br.getStatus() == BugReportStatus.NEW)
                .toList();
    }

    public UserContext getUserContext(User user, Collection<Project> projects) {
        try (var structuredTaskScope = new StructuredTaskScope.ShutdownOnFailure()) {
            var projectsTask = structuredTaskScope.fork(() -> getUserProjects(user, projects));
            var ticketsTask = structuredTaskScope.fork(() -> getUserTickets(user, projects));
            var bugReportsTask = structuredTaskScope.fork(() -> getUserBugReports(user, projects));

            try {
                structuredTaskScope.join();
                structuredTaskScope.throwIfFailed();
            } catch (InterruptedException | ExecutionException exception) {
                throw new RuntimeException(exception);
            }

            return new UserContext(projectsTask.get(), ticketsTask.get(), bugReportsTask.get());
        }
    }
}