package fr.takima.training.simpleapi.service;

import fr.takima.training.simpleapi.dao.StudentDAO;
import fr.takima.training.simpleapi.dto.StudentDto;
import fr.takima.training.simpleapi.entity.Department;
import fr.takima.training.simpleapi.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentDAO studentDAO;
    private final DepartmentService departmentService;

    @Autowired
    public StudentService(StudentDAO studentDAO, DepartmentService departmentService) {
        this.studentDAO = studentDAO;
        this.departmentService = departmentService;
    }

    public List<Student> getStudentsByDepartmentName(String departmentName) {
        validateDepartmentName(departmentName);
        return studentDAO.findStudentsByDepartment_Name(departmentName);
    }

    public int getStudentsNumberByDepartmentName(String departmentName) {
        validateDepartmentName(departmentName);
        return studentDAO.countAllByDepartment_Name(departmentName);
    }

    public List<Student> getAll() {
        return studentDAO.findAll();
    }

    public Student getStudentById(long id) {
        validateStudentId(id);

        return studentDAO.findById(id);
    }

    public Student addStudent(Student student) {
        if (student.getLastname() == null || student.getLastname().length() == 0 || student.getDepartment() == null) {
            throw new IllegalArgumentException("You can't add a student without setting a lastname and a department ID");
        }

        return this.studentDAO.save(student);
    }

    public Student create(StudentDto dto) {
        return addStudent(toEntity(null, dto));
    }

    public Student update(long id, StudentDto dto) {
        validateStudentId(id);
        return addStudent(toEntity(id, dto));
    }

    public void removeStudentById(long id) {
        validateStudentId(id);
        this.studentDAO.deleteById(id);
    }

    private Student toEntity(Long id, StudentDto dto) {
        if (dto.departmentId() == null) {
            throw new IllegalArgumentException("The department ID must not be null.");
        }
        Department department = departmentService.getDepartmentById(dto.departmentId());
        return Student.builder()
                .id(id)
                .firstname(dto.firstname())
                .lastname(dto.lastname())
                .department(department)
                .build();
    }

    private void validateDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.length() == 0) {
            throw new IllegalArgumentException("The department name must not be null or empty.");
        }
    }

    private void validateStudentId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("The student ID must be positive.");
        }
    }
}