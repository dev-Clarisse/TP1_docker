package fr.takima.training.sampleapplication.unit;

import fr.takima.training.simpleapi.dao.DepartmentDAO;
import fr.takima.training.simpleapi.entity.Department;
import fr.takima.training.simpleapi.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentsServiceTest {

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentDAO departmentDAO;

    private final Department DEPARTMENT = Department.builder()
            .id(1L)
            .name("DepartmentTest")
            .build();

    @Test
    void testGetDepartmentByName() {
        when(departmentDAO.findDepartmentByName("DepartmentTest")).thenReturn(DEPARTMENT);
        assertEquals(DEPARTMENT, departmentService.getDepartmentByName("DepartmentTest"));
    }

    @Test
    void testGetDepartmentByNameWithNullValue() {
        assertThrows(IllegalArgumentException.class, () -> departmentService.getDepartmentByName(null));
    }

    @Test
    void testGetDepartmentByNameWithEmptyValue() {
        assertThrows(IllegalArgumentException.class, () -> departmentService.getDepartmentByName(""));
    }

    @Test
    void testGetDepartmentById() {
        when(departmentDAO.findById(1L)).thenReturn(Optional.of(DEPARTMENT));
        assertEquals(DEPARTMENT, departmentService.getDepartmentById(1L));
    }

    @Test
    void testGetDepartmentByIdWithUnknownId() {
        when(departmentDAO.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> departmentService.getDepartmentById(99L));
    }
}