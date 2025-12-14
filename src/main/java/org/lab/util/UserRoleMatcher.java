package org.lab.util;

import org.lab.enums.UserRole;

public class UserRoleMatcher {
    public static String match(UserRole userRole) {
        return switch (userRole) {
            case MANAGER -> "manager";
            case TEAM_LEADER -> "team leader";
            case DEVELOPER -> "developer";
            case TESTER -> "tester";
            case null -> throw new IllegalArgumentException("userRole cannot be null");
        };
    }
}