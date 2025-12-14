package org.lab.service;

import org.lab.enums.BugReportStatus;
import org.lab.model.*;
import org.lab.service.role.DeveloperRoleChecker;
import org.lab.service.role.TesterRoleChecker;

import java.util.UUID;

public class BugReportService {
    private final DeveloperRoleChecker developerRoleChecker;
    private final TesterRoleChecker testerRoleChecker;

    public BugReportService() {
        developerRoleChecker = new DeveloperRoleChecker();
        testerRoleChecker = new TesterRoleChecker();
    }

    public BugReport createBugReport(Project project, User author, String name) {
        developerRoleChecker.checkRoleCombined(project, author, testerRoleChecker);

        BugReport bugReport = new BugReport(UUID.randomUUID(), project.getId(), name);
        project.addBugReport(bugReport);

        return bugReport;
    }

    public void fixBugReport(User developer, Project project, BugReport bugReport) {
        developerRoleChecker.checkRole(project, developer);
        bugReport.setStatus(BugReportStatus.FIXED);
    }

    public void testBugReport(User tester, Project project, BugReport bugReport) {
        testerRoleChecker.checkRole(project, tester);

        if (bugReport.getStatus() != BugReportStatus.FIXED) {
            throw new IllegalStateException("BugReport must be fixed first");
        }

        bugReport.setStatus(BugReportStatus.TESTED);
    }

    public void closeBugReport(User tester, Project project, BugReport bugReport) {
        testerRoleChecker.checkRole(project, tester);

        if (bugReport.getStatus() != BugReportStatus.TESTED) {
            throw new IllegalStateException("BugReport must be tested first");
        }

        bugReport.setStatus(BugReportStatus.CLOSED);
    }
}