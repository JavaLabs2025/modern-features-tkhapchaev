package org.lab.service;

import org.junit.jupiter.api.Test;
import org.lab.model.Project;
import org.lab.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserService userService = new UserService();
    private final ProjectService projectService = new ProjectService();

    @Test
    void userCanRegister() {
        User user = userService.register("alice");

        assertNotNull(user);
        assertEquals("alice", user.login());
    }

    @Test
    void userCanCreateProject() {
        User manager = userService.register("manager");

        Project project = projectService.createProject(manager, "Test Project");

        assertEquals(manager.id(), project.getRoles().keySet().iterator().next());
    }

    @Test
    void userCanSeeOwnProjects() {
        User manager = userService.register("manager");
        User dev = userService.register("dev");

        Project project = projectService.createProject(manager, "Project");
        projectService.addDeveloper(project, manager, dev);

        List<Project> projects = userService.getUserProjects(dev, List.of(project));

        assertEquals(1, projects.size());
    }
}