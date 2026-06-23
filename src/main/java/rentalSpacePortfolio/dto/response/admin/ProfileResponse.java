package rentalSpacePortfolio.dto.response.admin;



public class ProfileResponse {
    
    private String employeeCode;
    private String fullName;
    private String email;
    private String phone;
    private String locaton;

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocaton() {
        return locaton;
    }

    public void setLocaton(String locaton) {
        this.locaton = locaton;
    }
    
    

}