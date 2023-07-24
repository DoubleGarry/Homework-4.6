package ru.hogwarts.school.service;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;

import static java.util.Comparator.comparing;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AvatarRepository avatarRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;

    public StudentDtoOut create(StudentDtoIn studentDtoIn) {
        return studentMapper.toDto(
                studentRepository.save(
                        studentMapper.toEntity(studentDtoIn)
                )
        );
    }

    public StudentDtoOut get(long id) {
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public StudentDtoOut update(long studentId, StudentDtoIn studentDtoIn) {
        Student updatedStudent = studentRepository.findById(studentId)
                .map(student -> {
                    student.setName(studentDtoIn.getName());
                    student.setAge(studentDtoIn.getAge());
                    long facultyId = studentDtoIn.getFacultyId();
                    student.setFaculty(
                            facultyRepository.findById(facultyId)
                                    .orElseThrow(() -> new FacultyNotFoundException(facultyId))
                    );
                    return student;
                })
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        studentRepository.save(updatedStudent);
        return studentMapper.toDto(updatedStudent);
    }

    public StudentDtoOut delete(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.deleteById(id);
        return studentMapper.toDto(student);
    }

    public Collection<StudentDtoOut> findStudentsByAgeBetween(int from, int to) {
        return studentRepository.findStudentsByAgeBetween(from, to).stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public Collection<StudentDtoOut> findAll(@Nullable Integer age) {
        return Optional.ofNullable(age)
                .map(studentRepository::findStudentsByAge)
                .orElseGet(studentRepository::findAll).stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public FacultyDtoOut findStudentsFaculty(Long studentId) {
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    public Avatar findAvatarByStudentId(long studentId) {
        return avatarRepository.findByStudent_id(studentId)
                .orElseThrow(() -> new AvatarNotFoundException(studentId));
    }

    public Integer getTotalCountStudents() {
        return studentRepository.getTotalCountStudents();
    }

    public Double getAvgAgeStudents() {
        return studentRepository.getAvgAgeStudents();
    }

    public Collection<StudentDtoOut> getLastFiveStudents() {
        return studentRepository.getLastFiveStudents().stream()
                .map(studentMapper::toDto)
                .toList();
    }

    public Collection<StudentDtoOut> findAllStudentsWithNameStartedOnA() {
        return studentRepository.findAll().stream()
                .peek(student -> student.setName(student.getName().toUpperCase()))
                .map(studentMapper::toDto)
                .filter(studentDtoOut -> studentDtoOut.getName().startsWith("А"))
                .sorted(comparing(StudentDtoOut::getName))
                .toList();
    }

    public Double getAvgAgeViaApp() {
        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(0.0);
    }
}