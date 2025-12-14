package org.lab.service;

import org.lab.enums.TicketStatus;
import org.lab.model.*;
import org.lab.service.role.ManagerRoleChecker;
import org.lab.service.role.TeamLeaderRoleChecker;

import java.util.*;

public class TicketService {
    private final ManagerRoleChecker managerRoleChecker;
    private final TeamLeaderRoleChecker teamLeaderRoleChecker;

    public TicketService() {
        managerRoleChecker = new ManagerRoleChecker();
        teamLeaderRoleChecker = new TeamLeaderRoleChecker();
    }

    public Ticket createTicket(Project project, Milestone milestone, User creator, String name) {
        managerRoleChecker.checkRoleCombined(project, creator, teamLeaderRoleChecker);

        Ticket ticket = new Ticket(UUID.randomUUID(), project.getId(), milestone.getId(), name);
        milestone.addTicket(ticket);

        return ticket;
    }

    public void assignDeveloper(Project project, User actor, Ticket ticket, List<User> developers) {
        managerRoleChecker.checkRoleCombined(project, actor, teamLeaderRoleChecker);

        project.assignTicket(ticket, developers);
    }

    public void workOnTicket(User developer, Project project, Ticket ticket) {
        if (!ticket.getAssignees().contains(developer)) {
            throw new SecurityException("Ticket is not assigned to this developer");
        }

        ticket.setStatus(TicketStatus.IN_PROGRESS);
    }

    public void completeTicket(User developer, Ticket ticket) {
        if (!ticket.getAssignees().contains(developer)) {
            throw new SecurityException("Ticket is not assigned to this developer");
        }

        ticket.setStatus(TicketStatus.DONE);
    }
}