package org.lab.service.role;

import org.lab.enums.UserRole;

public non-sealed class TesterRoleChecker extends RoleChecker {
    public TesterRoleChecker() {
        requiredRole = UserRole.TESTER;
    }
}