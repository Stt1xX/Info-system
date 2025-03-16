package com.example.shared_module.entities.enums;

public enum ImportStatus {
    SUCCESS("Success"),
    FAILED("Failed"),
    IN_PROGRESS("In Progress");

    private final String displayName;

    ImportStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
