package com.example.backend.repositories.DunamicQuery;

public class Specification {


    public static org.springframework.data.jpa.domain.Specification<?> hasNameContaining(String template, String varName) {
        return (root, query, criteriaBuilder) -> {
            if (template == null || template.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            if (root.get(varName).getJavaType() == Integer.class) {
                if ((template.matches("\\d+") && template.length() < 10)) {
                    return criteriaBuilder.equal(root.get(varName), Integer.parseInt(template));
                } else {
                    return criteriaBuilder.disjunction();
                }
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(varName)), "%" + template.toLowerCase() + "%");
        };
    }
}
