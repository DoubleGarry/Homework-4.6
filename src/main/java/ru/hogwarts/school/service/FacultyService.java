package ru.hogwarts.school.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.exception.FacultyNameNotFoundException;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

import static java.util.Comparator.comparing;

@Service
@AllArgsConstructor
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public FacultyDtoOut create(FacultyDtoIn facultyDtoIn) {
        return facultyMapper.toDto(
                facultyRepository.save(
                        facultyMapper.toEntity(facultyDtoIn)
                )
        );
    }

    public FacultyDtoOut get(long id) {
        return facultyMapper.toDto(
                facultyRepository.findById(id)
                        .orElseThrow(() -> new FacultyNotFoundException(id)));
    }

    public FacultyDtoOut update(long id, FacultyDtoIn facultyDtoIn) {
        Faculty updatedFaculty = facultyRepository.findById(id).
                map(faculty -> {
                    faculty.setName(facultyDtoIn.getName());
                    faculty.setColor(facultyDtoIn.getColor());
                    return faculty;
                })
                .orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.save(updatedFaculty);
        return facultyMapper.toDto(updatedFaculty);
    }

    public FacultyDtoOut delete(long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.deleteById(id);
        return facultyMapper.toDto(faculty);
    }

    public Collection<FacultyDtoOut> findByColorOrName(String colorOrName) {
        return facultyRepository.findAllByColorContainingIgnoreCaseOrNameContainingIgnoreCase(
                        colorOrName, colorOrName
                ).stream()
                .map(facultyMapper::toDto)
                .toList();
    }

    public Collection<StudentDtoOut> findStudentsByFaculty(String facultyName) {
        return facultyRepository.findByNameIgnoreCase(facultyName)
                .map(Faculty::getId)
                .map(studentRepository::findAllByFaculty_id)
                .orElseThrow(() -> new FacultyNameNotFoundException(facultyName)).stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public String getTheMostLongerFacultyName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(comparing(String::length))
                .orElse(null);
    }
}