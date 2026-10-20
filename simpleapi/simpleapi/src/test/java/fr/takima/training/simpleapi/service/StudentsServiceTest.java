package fr.takima.training.sampleapplication.unit;

import fr.takima.training.simpleapi.dao.StudentDAO;
import fr.takima.training.simpleapi.dto.StudentDto;
import fr.takima.training.simpleapi.entity.Department;
import fr.takima.training.simpleapi.entity.Student;
import fr.takima.training.simpleapi.service.DepartmentService;
import fr.takima.training.simpleapi.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentsServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentDAO studentDAO;

    @Mock
    private DepartmentService departmentService;

    private final Department department = Department.builder().id(1L).name("DepartementTest").build();
    private final Student student = Student
            .builder()
            .id(1L)
            .firstname("Firstname")
            .lastname("lastname")
            .department(department)
            .build();

    @Test
    void testGetStudentById() {
        when(studentDAO.findById(1L)).thenReturn(student);
        assertEquals(student, studentService.getStudentById(1L));
    }

    @Test
    void testGetStudentByIdWithNegativeId() {
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentById(-5));
    }

    @Test
    void testGetStudentsByDepartmentName() {
        List<Student> students = new ArrayList<>();
        students.add(student);
        when(studentDAO.findStudentsByDepartment_Name("DepartmentTest")).thenReturn(students);

        assertEquals(students, studentService.getStudentsByDepartmentName("DepartmentTest"));
    }

    @Test
    void testGetStudentsByDepartmentNameWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentsByDepartmentName(null));
    }

    @Test
    void testGetStudentsByDepartmentNameWithEmptyValue() {
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentsByDepartmentName(""));
    }

    @Test
    void testGetStudentsNumberByDepartmentName() {
        when(studentDAO.countAllByDepartment_Name("DepartmentTest")).thenReturn(1);
        assertEquals(1, studentService.getStudentsNumberByDepartmentName("DepartmentTest"));
    }

    @Test
    void testGetStudentsNumberByDepartmentNameWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentsNumberByDepartmentName(null));
    }

    @Test
    void testGetStudentsNumberByDepartmentNameWithEmptyValue() {
        assertThrows(IllegalArgumentException.class, () -> studentService.getStudentsNumberByDepartmentName(""));
    }

    @Test
    void testAddStudent() {
        when(studentDAO.save(student)).thenReturn(student);
        assertEquals(student, studentService.addStudent(student));
    }

    @Test
    void testAddStudentWithBadLastname() {
        Student studentWithNullLastname = Student.builder().id(1L).firstname("abc").department(department).build();
        assertThrows(IllegalArgumentException.class, () -> studentService.addStudent(studentWithNullLastname));

        Student studentWithEmptyLastname = Student.builder().id(1L).firstname("abc").lastname("").department(department).build();
        assertThrows(IllegalArgumentException.class, () -> studentService.addStudent(studentWithEmptyLastname));
    }

    @Test
    void testAddStudentWithoutDepartment() {
        Student studentWithoutDepartment = Student.builder().id(1L).lastname("abc").build();
        assertThrows(IllegalArgumentException.class, () -> studentService.addStudent(studentWithoutDepartment));
    }

    @Test
    void testCreateStudent() {
        StudentDto dto = new StudentDto("Firstname", "lastname", 1L);
        when(departmentService.getDepartmentById(1L)).thenReturn(department);
        when(studentDAO.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student created = studentService.create(dto);

        assertNull(created.getId());
        assertEquals("Firstname", created.getFirstname());
        assertEquals("lastname", created.getLastname());
        assertEquals(department, created.getDepartment());
    }

    @Test
    void testCreateStudentWithUnknownDepartment() {
        StudentDto dto = new StudentDto("Firstname", "lastname", 99L);
        when(departmentService.getDepartmentById(99L)).thenThrow(new IllegalArgumentException("Unknown department 99"));

        assertThrows(IllegalArgumentException.class, () -> studentService.create(dto));
        verify(studentDAO, never()).save(any(Student.class));
    }

    @Test
    void testCreateStudentWithBlankLastname() {
        StudentDto dto = new StudentDto("Firstname", "", 1L);
        when(departmentService.getDepartmentById(1L)).thenReturn(department);

        assertThrows(IllegalArgumentException.class, () -> studentService.create(dto));
        verify(studentDAO, never()).save(any(Student.class));
    }

    @Test
    void testUpdateStudent() {
        StudentDto dto = new StudentDto("Newfirst", "newlast", 1L);
        when(departmentService.getDepartmentById(1L)).thenReturn(department);
        when(studentDAO.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student updated = studentService.update(5L, dto);

        assertEquals(5L, updated.getId());
        assertEquals("Newfirst", updated.getFirstname());
        assertEquals("newlast", updated.getLastname());
        assertEquals(department, updated.getDepartment());
    }

    @Test
    void testUpdateStudentWithNegativeId() {
        StudentDto dto = new StudentDto("Newfirst", "newlast", 1L);
        assertThrows(IllegalArgumentException.class, () -> studentService.update(-5L, dto));
        verify(studentDAO, never()).save(any(Student.class));
    }

    @Test
    void testRemoveStudentById() {
        assertDoesNotThrow(() -> studentService.removeStudentById(1L));
    }

    @Test
    void testRemoveStudentWithNegativeId() {
        assertThrows(IllegalArgumentException.class, () -> studentService.removeStudentById(-5));
    }
}