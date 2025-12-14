package org.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.enums.MilestoneStatus;
import org.lab.enums.TicketStatus;
import org.lab.enums.UserRole;
import org.lab.model.Milestone;
import org.lab.model.Project;
import org.lab.model.Ticket;
import org.lab.model.User;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {
    private ProjectService projectService;
    private UserService userService;

    private User manager;
    private User dev;

    @BeforeEach
    void setUp() {
        projectService = new ProjectService();
        userService = new UserService();

        manager = userService.register("manager");
        dev = userService.register("dev");
    }

    @Test
    void onlyManagerCanAddDeveloper() {
        Project project = projectService.createProject(manager, "Project");

        projectService.addDeveloper(project, manager, dev);

        assertEquals(UserRole.DEVELOPER, project.getRoles().get(dev.id()));
    }

    @Test
    void nonManagerCannotAddDeveloper() {
        Project project = projectService.createProject(manager, "Project");

        assertThrows(SecurityException.class, () -> projectService.addDeveloper(project, dev, dev));
    }

    @Test
    void projectAllowsOnlyOneActiveMilestone() {
        Project project = projectService.createProject(manager, "Project");

        Milestone m1 = projectService.createMilestone(project, manager, Instant.now(), Instant.now().plusSeconds(1000), "M1");
        Milestone m2 = projectService.createMilestone(project, manager, Instant.now().plusSeconds(2000), Instant.now().plusSeconds(3000), "M2");

        projectService.changeMilestoneStatus(project, manager, m1, MilestoneStatus.ACTIVE);

        assertThrows(IllegalStateException.class, () -> projectService.changeMilestoneStatus(project, manager, m2, MilestoneStatus.ACTIVE));
    }

    @Test
    void milestoneCannotBeClosedIfNotAllTicketsAreDone() {
        Project project = projectService.createProject(manager, "Project");

        projectService.addDeveloper(project, manager, dev);

        Milestone m1 = projectService.createMilestone(project, manager, Instant.now(), Instant.now().plusSeconds(1000), "M1");
        Ticket ticket = new Ticket(UUID.randomUUID(), project.getId(), m1.getId(), "Task");

        m1.addTicket(ticket);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        assertThrows(IllegalStateException.class, () -> projectService.changeMilestoneStatus(project, manager, m1, MilestoneStatus.CLOSED));
    }
}