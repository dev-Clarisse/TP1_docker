public class StudentRequestDto {

    @Size(max = 20)
    private String firstname;

    @NotNull
    @Size(max = 30)
    private String lastname;

    @NotNull
    private Long departmentId;

    // Constructeurs
    public StudentRequestDto() {
    }

    // Getters et Setters
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    
}
