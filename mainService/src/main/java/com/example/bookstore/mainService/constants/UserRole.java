package com.example.bookstore.mainService.constants;

public enum UserRole {
    BANNED("BANNED"), NORMAL("NORMAL"), ADMIN("ADMIN") , ERROR("ERROR");

    private final String roleString;

    UserRole(String roleString) {
        this.roleString = roleString;
    }

    public String getRoleName() {
        return roleString;
    }
}
