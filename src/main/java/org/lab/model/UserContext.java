package org.lab.model;

import java.util.List;

public record UserContext(List<Project> projects, List<Ticket> tickets, List<BugReport> bugReports) { }