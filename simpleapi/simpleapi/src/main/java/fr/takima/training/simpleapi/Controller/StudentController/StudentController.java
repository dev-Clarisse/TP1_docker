package fr.takima.training.simpleapi.Controller.StudentController;

import fr.takima.training.simpleapi.dto.StudentDto;
import fr.takima.training.simpleapi.entity.Student;
import fr.takima.training.simpleapi.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
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
    public ResponseEntity<Object> getStudentById(@PathVariable(name = "id") long id) {
        Student student = this.studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<Object> addStudent(@Valid @RequestBody StudentDto dto) {
        Student savedStudent;
        try {
            savedStudent = this.studentService.create(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedStudent.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateStudent(@Valid @RequestBody StudentDto dto,
                                                @PathVariable(name = "id") long id) {
        if (studentService.getStudentById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.ok(studentService.update(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> removeStudent(@PathVariable(name = "id") long id) {
        if (studentService.getStudentById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        this.studentService.removeStudentById(id);
        return ResponseEntity.ok().build();
    }
}