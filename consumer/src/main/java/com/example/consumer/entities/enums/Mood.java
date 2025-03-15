package com.example.consumer.entities.enums;

import lombok.Getter;

@Getter
public enum Mood {
    LONGING("Longing"),
    GLOOM("Gloom"),
    CALM("Calm"),
    FRENZY("Frenzy");

    // Метод для получения имени
    private final String displayName;

    // Конструктор для установки имени
    Mood(String displayName) {
        this.displayName = displayName;
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