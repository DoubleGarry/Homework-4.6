package ru.hogwarts.school.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(
            {
                    StudentNotFoundException.class,
                    FacultyNotFoundException.class,
                    FacultyNameNotFoundException.class,
                    AvatarNotFoundException.class
            }
    )
    public ResponseEntity<?> handleNotFound(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
