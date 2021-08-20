package ru.kpekepsalt.ruvik.enums;

public enum Permission {
    GLOBAL("global");

    private String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
