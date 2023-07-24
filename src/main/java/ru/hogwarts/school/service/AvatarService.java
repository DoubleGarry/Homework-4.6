package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.AvatarMapper;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@AllArgsConstructor
@Transactional
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final AvatarMapper avatarMapper;

    public void uploadAvatar(Long studentId, MultipartFile avatarImage) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Path filePath = Path.of("avatarsDir", studentId + "." + getExtension(avatarImage.getOriginalFilename()));
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectory(filePath.getParent());
        }
        Files.deleteIfExists(filePath);
        try (InputStream is = avatarImage.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = avatarRepository.findByStudent_id(studentId)
                .orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarImage.getSize());
        avatar.setMediaType(avatarImage.getContentType());
        avatar.setData(avatarImage.getBytes());
        avatarRepository.save(avatar);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public List<AvatarDto> findAll(int pageNumber, int pageSize) {
        return avatarRepository.findAll(PageRequest.of(pageNumber - 1, pageSize))
                .getContent().stream()
                .map(avatarMapper::toDto)
                .toList();
    }
}