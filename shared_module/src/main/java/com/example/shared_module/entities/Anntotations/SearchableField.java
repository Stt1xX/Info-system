package com.example.shared_module.entities.Anntotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SearchableField {
    String name();
}
