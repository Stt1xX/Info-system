package com.example.consumer.entities.enums;

import lombok.Getter;

@Getter
public enum ImportStatus {
    SUCCESS("Success"),
    FAILED("Failed"),
    IN_PROGRESS("In Progress");

    private final String displayName;

    ImportStatus(String displayName) {
        this.displayName = displayName;
    }

}
