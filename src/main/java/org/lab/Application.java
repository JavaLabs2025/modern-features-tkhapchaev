package org.lab;

import org.lab.enums.MilestoneStatus;
import org.lab.model.*;
import org.lab.service.BugReportService;
import org.lab.service.ProjectService;
import org.lab.service.TicketService;
import org.lab.service.UserService;

import java.time.Instant;
import java.util.List;

public class Application {
    static void main(String[] arguments) {
        UserService userService = new UserService();
        ProjectService projectService = new ProjectService();
        TicketService ticketService = new TicketService();
        BugReportService bugReportService = new BugReportService();

        User manager = userService.register("manager");
        User teamLeader = userService.register("team_leader");
        User developer = userService.register("developer");
        User tester = userService.register("tester");

        Project project = projectService.createProject(manager, "Awesome Project");

        projectService.assignTeamLeader(project, manager, teamLeader);
        projectService.addDeveloper(project, manager, developer);
        projectService.addTester(project, manager, tester);

        Milestone milestone = projectService.createMilestone(project, manager, Instant.now(), Instant.now().plusSeconds(60 * 60 * 24 * 14), "Sprint 1");

        projectService.changeMilestoneStatus(project, manager, milestone, MilestoneStatus.ACTIVE);

        Ticket ticket = ticketService.createTicket(project, milestone, teamLeader, "Implement login feature");

        ticketService.assignDeveloper(project, teamLeader, ticket, List.of(developer));

        ticketService.workOnTicket(developer, project, ticket);
        ticketService.completeTicket(developer, ticket);

        System.out.println("Ticket status: " + ticket.getStatus());

        projectService.changeMilestoneStatus(project, manager, milestone, MilestoneStatus.CLOSED);

        System.out.println("Milestone status: " + milestone.getStatus());

        BugReport bug = bugReportService.createBugReport(project, tester, "Login button does not work on Safari");

        System.out.println("BugReport status: " + bug.getStatus());

        bugReportService.fixBugReport(developer, project, bug);
        bugReportService.testBugReport(tester, project, bug);
        bugReportService.closeBugReport(tester, project, bug);

        System.out.println("BugReport final status: " + bug.getStatus());

        UserContext developerContext = userService.getUserContext(developer, projectService.getAllProjects());

        System.out.println("\nDeveloper context:");
        System.out.println("Projects: " + developerContext.projects().size());
        System.out.println("Tickets: " + developerContext.tickets().size());
        System.out.println("BugReports: " + developerContext.bugReports().size());
    }
}