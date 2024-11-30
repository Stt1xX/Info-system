package com.example.backend.utils;

import com.example.backend.entities.DTO.FieldsDTO;
import com.example.backend.entities.ManagedEntity;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public static List<FieldsDTO> getFields(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<FieldsDTO> sortableFields = new ArrayList<>();
        Field[] fields = Stream.concat(Arrays.stream(clazz.getDeclaredFields()), Arrays.stream(ManagedEntity.class.getDeclaredFields()))
                .toArray(Field[]::new);
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotation)) {
                try {
                    Annotation ann = field.getAnnotation(annotation);
                    Method method = ann.annotationType().getMethod("name");
                    sortableFields.add(new FieldsDTO(
                            (String) method.invoke(ann),
                            field.getName())
                    );
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored){}
            }
        }
        return sortableFields;
    }
}
