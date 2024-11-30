package com.example.backend.utils;

import com.example.backend.entities.Anntotations.SortableField;
import com.example.backend.entities.DTO.SortableFieldDTO;
import com.example.backend.entities.ManagedEntity;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Utils {
    public static String prepareDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneOffset.UTC);
        return formatter.format(instant);
    }

    public static List<SortableFieldDTO> getSortableFields(Class<?> clazz) {
        List<SortableFieldDTO> sortableFields = new ArrayList<>();
        Field[] fields = Stream.concat(Arrays.stream(clazz.getDeclaredFields()), Arrays.stream(ManagedEntity.class.getDeclaredFields()))
                .toArray(Field[]::new);
        for (Field field : fields) {
            if (field.isAnnotationPresent(SortableField.class)) {
                sortableFields.add(new SortableFieldDTO(
                        field.getAnnotation(SortableField.class).name(),
                        field.getName())
                );
            }
        }
        return sortableFields;
    }
}
