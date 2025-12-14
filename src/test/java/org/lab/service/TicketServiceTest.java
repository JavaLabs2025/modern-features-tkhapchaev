package org.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.enums.TicketStatus;
import org.lab.model.*;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {
    private UserService userService;
    private ProjectService projectService;
    private TicketService ticketService;

    private User manager;
    private User leader;
    private User dev;

    private Project project;
    private Milestone milestone;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        projectService = new ProjectService();
        ticketService = new TicketService();

        manager = userService.register("manager");
        leader = userService.register("leader");
        dev = userService.register("dev");

        project = projectService.createProject(manager, "Project");
        projectService.assignTeamLeader(project, manager, leader);
        projectService.addDeveloper(project, manager, dev);

        milestone = projectService.createMilestone(project, manager, Instant.now(), Instant.now().plusSeconds(1000), "M1");
    }

    @Test
    void managerCanCreateTicket() {
        Ticket ticket = ticketService.createTicket(project, milestone, manager, "Ticket");

        assertEquals("Ticket", ticket.getName());
    }

    @Test
    void teamLeaderCanCreateTicket() {
        Ticket ticket = ticketService.createTicket(project, milestone, leader, "Ticket");

        assertNotNull(ticket);
    }

    @Test
    void developerCannotCreateTicket() {
        assertThrows(SecurityException.class, () -> ticketService.createTicket(project, milestone, dev, "Ticket"));
    }

    @Test
    void developerCanWorkOnAssignedTicket() {
        Ticket ticket = ticketService.createTicket(project, milestone, manager, "Ticket");
        ticketService.assignDeveloper(project, manager, ticket, List.of(dev));

        ticketService.workOnTicket(dev, project, ticket);

        assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());
    }

    @Test
    void developerCanCompleteTicket() {
        Ticket ticket = ticketService.createTicket(project, milestone, manager, "Ticket");
        ticketService.assignDeveloper(project, manager, ticket, List.of(dev));

        ticketService.completeTicket(dev, ticket);

        assertEquals(TicketStatus.DONE, ticket.getStatus());
    }
}