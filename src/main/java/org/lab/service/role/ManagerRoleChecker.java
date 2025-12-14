package org.lab.service.role;

import org.lab.enums.UserRole;

public non-sealed class ManagerRoleChecker extends RoleChecker {
    public ManagerRoleChecker() {
        requiredRole = UserRole.MANAGER;
    }
}