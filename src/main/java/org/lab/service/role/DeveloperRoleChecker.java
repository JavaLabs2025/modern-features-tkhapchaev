package org.lab.service.role;

import org.lab.enums.UserRole;

public non-sealed class DeveloperRoleChecker extends RoleChecker {
    public DeveloperRoleChecker() {
        requiredRole = UserRole.DEVELOPER;
    }
}