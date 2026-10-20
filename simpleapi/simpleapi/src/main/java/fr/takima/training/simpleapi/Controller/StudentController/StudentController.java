package fr.takima.training.simpleapi.controller;

import fr.takima.training.simpleapi.dto.StudentRequestDto;
import fr.takima.training.simpleapi.entity.Student;
import fr.takima.training.simpleapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<Object> getStudents() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getStudentById(@PathVariable(name="id") long id) {
        Optional<Student> studentOptional = Optional.ofNullable(this.studentService.getStudentById(id));
        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> addStudent(@Valid @RequestBody StudentRequestDto studentDto) {

        Student student = Student.builder()
                .firstname(studentDto.getFirstname())
                .lastname(studentDto.getLastname())
                .build();

        Student savedStudent;
        try {
            savedStudent = this.studentService.addStudent(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedStudent.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateStudent(@Valid @RequestBody StudentRequestDto studentDto, @PathVariable(name="id") long id) {
        Optional<Student> studentOptional = Optional.ofNullable(studentService.getStudentById(id));
        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = Student.builder()
                .id(id)
                .firstname(studentDto.getFirstname())
                .lastname(studentDto.getLastname())
                .build();

        this.studentService.addStudent(student);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> removeStudent(@PathVariable(name="id") long id) {
        Optional<Student> studentOptional = Optional.ofNullable(studentService.getStudentById(id));
        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        this.studentService.removeStudentById(id);

        return ResponseEntity.ok().build();
    }
}