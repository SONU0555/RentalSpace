package rentalSpacePortfolio.dto.response.admin;

import rentalSpacePortfolio.dto.response.user.UserSummaryResponse;


public class AdminSummaryResponse extends UserSummaryResponse{
    
    private Long property_assigned;
    private String location;
    private String employeeCode;
    

    public Long getProperty_assigned() {
        return property_assigned;
    }

    public void setProperty_assigned(Long property_assigned) {
        this.property_assigned = property_assigned;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

}