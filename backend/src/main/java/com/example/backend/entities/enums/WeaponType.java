package com.example.backend.entities.enums;

public enum WeaponType {
    SHOTGUN("Shotgun"),
    KNIFE("Knife"),
    BAT("Baseball Bat");

    private final String displayName;

    WeaponType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static WeaponType fromDisplayName(String displayName) {
        for (WeaponType type : WeaponType.values()) {
            if (type.displayName.equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }
}
