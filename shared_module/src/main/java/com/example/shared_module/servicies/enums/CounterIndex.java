package com.example.shared_module.servicies.enums;

public enum CounterIndex {
    CAR(0),
    COORDINATES(1),
    HUMAN(2);

    private final int value;

    CounterIndex(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
