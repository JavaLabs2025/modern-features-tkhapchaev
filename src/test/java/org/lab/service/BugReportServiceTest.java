package org.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.enums.BugReportStatus;
import org.lab.model.*;

import static org.junit.jupiter.api.Assertions.*;

class BugReportServiceTest {
    private UserService userService;
    private ProjectService projectService;
    private BugReportService bugReportService;

    private User manager;
    private User dev;
    private User tester;
    private Project project;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        projectService = new ProjectService();
        bugReportService = new BugReportService();

        manager = userService.register("manager");
        dev = userService.register("dev");
        tester = userService.register("tester");

        project = projectService.createProject(manager, "Project");
        projectService.addDeveloper(project, manager, dev);
        projectService.addTester(project, manager, tester);
    }

    @Test
    void developerCanCreateBugReport() {
        BugReport bug = bugReportService.createBugReport(project, dev, "Bug");

        assertEquals(BugReportStatus.NEW, bug.getStatus());
    }

    @Test
    void testerCanCreateBugReport() {
        BugReport bug = bugReportService.createBugReport(project, tester, "Bug");

        assertNotNull(bug);
    }

    @Test
    void developerCanFixBugReport() {
        BugReport bug = bugReportService.createBugReport(project, dev, "Bug");

        bugReportService.fixBugReport(dev, project, bug);

        assertEquals(BugReportStatus.FIXED, bug.getStatus());
    }

    @Test
    void testerCanTestAndCloseBugReport() {
        BugReport bug = bugReportService.createBugReport(project, dev, "Bug");

        bugReportService.fixBugReport(dev, project, bug);
        bugReportService.testBugReport(tester, project, bug);
        bugReportService.closeBugReport(tester, project, bug);

        assertEquals(BugReportStatus.CLOSED, bug.getStatus());
    }
}