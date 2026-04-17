package RentSpace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    
    public enum role{
        ADMIN,
        TENANT,
        OWNER
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private String password;
    private String phone;
     
    @Enumerated(EnumType.STRING)
    private role role;
    
    // common
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    //Tenant fiels
    private String address;
    private String aadhaarNumber;
    private LocalDateTime rentStartDate;
    private LocalDateTime rentEndDate;
    private Long propertyId;
    private String emergencyContect;
    
    //Owner field
    private String companyName;
    private Integer totalProperties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public role getRole() {
        return role;
    }

    public void setRole(role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public LocalDateTime getRentStartDate() {
        return rentStartDate;
    }

    public void setRentStartDate(LocalDateTime rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public LocalDateTime getRentEndDate() {
        return rentEndDate;
    }

    public void setRentEndDate(LocalDateTime rentEndDate) {
        this.rentEndDate = rentEndDate;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getEmergencyContect() {
        return emergencyContect;
    }

    public void setEmergencyContect(String emergencyContect) {
        this.emergencyContect = emergencyContect;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(Integer totalProperties) {
        this.totalProperties = totalProperties;
    }
    
    
     

}