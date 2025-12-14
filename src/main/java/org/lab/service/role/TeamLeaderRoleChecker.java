package org.lab.service.role;

import org.lab.enums.UserRole;

public non-sealed class TeamLeaderRoleChecker extends RoleChecker {
    public TeamLeaderRoleChecker() {
        requiredRole = UserRole.TEAM_LEADER;
    }
}