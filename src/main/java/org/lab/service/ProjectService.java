package org.lab.service;

import org.lab.enums.MilestoneStatus;
import org.lab.enums.UserRole;
import org.lab.model.Milestone;
import org.lab.model.Project;
import org.lab.model.User;
import org.lab.service.role.ManagerRoleChecker;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProjectService {
    private final Map<UUID, Project> projects;

    private final ManagerRoleChecker managerRoleChecker;

    public ProjectService() {
        projects = new HashMap<>();
        managerRoleChecker = new ManagerRoleChecker();
    }

    public Project createProject(User creator, String name) {
        Project project = new Project(UUID.randomUUID(), name);
        project.addParticipant(creator, UserRole.MANAGER);
        projects.put(project.getId(), project);

        return project;
    }

    public Collection<Project> getAllProjects() {
        return projects.values();
    }

    public void addDeveloper(Project project, User manager, User developer) {
        managerRoleChecker.checkRole(project, manager);
        project.addParticipant(developer, UserRole.DEVELOPER);
    }

    public void addTester(Project project, User manager, User tester) {
        managerRoleChecker.checkRole(project, manager);
        project.addParticipant(tester, UserRole.TESTER);
    }

    public void assignTeamLeader(Project project, User manager, User teamLeader) {
        managerRoleChecker.checkRole(project, manager);
        project.addParticipant(teamLeader, UserRole.TEAM_LEADER);
    }

    public Milestone createMilestone(Project project, User manager, Instant start, Instant end, String name) {
        managerRoleChecker.checkRole(project, manager);
        Milestone milestone = new Milestone(UUID.randomUUID(), project.getId(), start, end, name);
        project.addMilestone(milestone);

        return milestone;
    }

    public void changeMilestoneStatus(Project project, User manager, Milestone milestone, MilestoneStatus status) {
        managerRoleChecker.checkRole(project, manager);
        project.changeMilestoneStatus(milestone, status);
    }
}