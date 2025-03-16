package com.example.shared_module.entities.enums;

public enum Mood {
    LONGING("Longing"),
    GLOOM("Gloom"),
    CALM("Calm"),
    FRENZY("Frenzy");

    private final String displayName;

    // Конструктор для установки имени
    Mood(String displayName) {
        this.displayName = displayName;
    }

    // Метод для получения имени
    public String getDisplayName() {
        return displayName;
    }

    public static Mood fromDisplayName(String displayName) {
        for (Mood type : Mood.values()) {
            if (type.displayName.equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }
}