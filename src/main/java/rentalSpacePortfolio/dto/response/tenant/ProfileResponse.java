package rentalSpacePortfolio.dto.response.tenant;



public class ProfileResponse {
    
    private String full_name;
    private String email;
    private String phone;
    private String emergencyContect;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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

    public String getEmergencyContect() {
        return emergencyContect;
    }

    public void setEmergencyContect(String alternate_phone) {
        this.emergencyContect = alternate_phone;
    }
    
}