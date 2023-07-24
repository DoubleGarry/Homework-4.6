package ru.hogwarts.school.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class FacultyDtoOut {
    private long id;
    private String name;
    private String color;
}
