package ru.hogwarts.school.controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(AvatarController.BASE_PATH)
public class AvatarController {
    private final StudentService studentService;
    private final AvatarService avatarService;
    public static final String BASE_PATH = "avatars";

    @GetMapping("/{id}/preview")
    @Transactional
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable("id") Long studentId) {
        Avatar avatar = studentService.findAvatarByStudentId(studentId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        httpHeaders.setContentLength(avatar.getFileSize());

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(avatar.getData());
    }

    @GetMapping("/{id}")
    @Transactional
    public void downloadAvatar(@PathVariable("id") long studentId,
                               HttpServletResponse response) throws IOException {
        Avatar avatar = studentService.findAvatarByStudentId(studentId);
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             ServletOutputStream os = response.getOutputStream()) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(avatar.getMediaType());
            response.setContentLengthLong(avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping()
    public List<AvatarDto> getAllAvatars(@RequestParam int pageNumber,
                                         @RequestParam int pageSize) {
        return avatarService.findAll(pageNumber, pageSize);
    }
}
