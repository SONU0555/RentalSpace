package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;


@Entity
@Table(name = "tenants")
public class Tenant extends BaseEnity{
    
    @OneToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private User tenant;
    
    private LocalDate rentStartDate;
    private LocalDate rentEndDate;
    private String emergencyContect;
    private String aadhaarNumber;
    private Boolean isVerified = false;
    
    public User getUser() {
        return tenant;
    }

    public void setUser(User tenant) {
        this.tenant = tenant;
    }

    public LocalDate getRentStartDate() {
        return rentStartDate;
    }

    public void setRentStartDate(LocalDate rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public LocalDate getRentEndDate() {
        return rentEndDate;
    }

    public void setRentEndDate(LocalDate rentEndDate) {
        this.rentEndDate = rentEndDate;
    }

    public String getEmergencyContect() {
        return emergencyContect;
    }

    public void setEmergencyContect(String emergencyContect) {
        this.emergencyContect = emergencyContect;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
    
}