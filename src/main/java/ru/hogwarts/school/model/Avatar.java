package ru.hogwarts.school.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "filePath")
@ToString(exclude = "student")
@Builder
@Entity
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    private String filePath;

    @Column(nullable = false)
    private long fileSize;

    private String mediaType;

    @Lob
    private byte[] data;

    @OneToOne(fetch = FetchType.LAZY)
    private Student student;
}
