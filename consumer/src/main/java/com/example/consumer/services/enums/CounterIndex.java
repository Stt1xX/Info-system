package com.example.consumer.services.enums;

import lombok.Getter;

@Getter
public enum CounterIndex {
    CAR(0),
    COORDINATES(1),
    HUMAN(2);

    private final int value;

    CounterIndex(int value) {
        this.value = value;
    }

}
