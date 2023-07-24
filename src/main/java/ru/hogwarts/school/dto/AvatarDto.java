package ru.hogwarts.school.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class AvatarDto {
    private long id;
    private String avatarUrl;
    private long fileSize;
    private String mediaType;
    private long studentId;
}
