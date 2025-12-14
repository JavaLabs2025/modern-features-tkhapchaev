package org.lab.service.role;

import org.lab.enums.UserRole;
import org.lab.model.Project;
import org.lab.model.User;
import org.lab.util.UserRoleMatcher;

public abstract sealed class RoleChecker permits ManagerRoleChecker, TeamLeaderRoleChecker, DeveloperRoleChecker, TesterRoleChecker {
    protected UserRole requiredRole;

    public void checkRole(Project project, User user) {
        UserRole userRole = project.getRoles().get(user.id());

        if (userRole != requiredRole) {
            throw new SecurityException(STR."Access denied. Required role: \{UserRoleMatcher.match(requiredRole)}");
        }
    }

    public void checkRoleCombined(Project project, User user, RoleChecker otherRoleChecker) {
        UserRole userRole = project.getRoles().get(user.id());

        if (userRole != requiredRole && userRole != otherRoleChecker.requiredRole) {
            throw new SecurityException(STR."Access denied. Required roles: \{UserRoleMatcher.match(requiredRole)}, \{UserRoleMatcher.match(otherRoleChecker.requiredRole)}");
        }
    }
}