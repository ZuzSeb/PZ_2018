package com.zuzseb.learning.model;

public enum UserRole {

    standard("Użytkownik standardowy"),
    recruiter("Rekruter");

    private String userRole;

    UserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRole() {
        return userRole;
    }
}
