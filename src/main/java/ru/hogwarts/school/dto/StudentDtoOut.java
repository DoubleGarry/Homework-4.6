package ru.hogwarts.school.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class StudentDtoOut {
    private long id;
    private String name;
    private int age;
    private FacultyDtoOut faculty;
}
