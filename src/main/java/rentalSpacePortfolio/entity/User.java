package rentalSpacePortfolio.entity;


import jakarta.persistence.*;
import rentalSpacePortfolio.enums.Role;

@Entity
@Table(name = "users")
public class User extends BaseEnity{
    
    private String full_name;
    private String email;
    private String password;
    private String phone;
     
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private Boolean isActive = true; 
    
    @OneToOne(mappedBy = "tenant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Tenant tenant;
    
    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Admin admin;
    
    
//    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Property> properties;
    
//    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Booking> bookings;
    
//    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<MaintenanceRequest> maintenanceRequest;
//    
//    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<AmenityBooking> amenityBookings;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String name) {
        this.full_name = name;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


//    public List<Property> getProperties() {
//        return properties;
//    }
//
//    public void setProperties(List<Property> properties) {
//        this.properties = properties;
//    }

//    public List<Booking> getBookings() {
//        return bookings;
//    }
//
//    public void setBookings(List<Booking> bookings) {
//        this.bookings = bookings;
//    }

//    public List<MaintenanceRequest> getMaintenanceRequest() {
//        return maintenanceRequest;
//    }
//
//    public void setMaintenanceRequest(List<MaintenanceRequest> maintenanceRequest) {
//        this.maintenanceRequest = maintenanceRequest;
//    }
//
//    public List<AmenityBooking> getAmenityBookings() {
//        return amenityBookings;
//    }
//
//    public void setAmenityBookings(List<AmenityBooking> amenityBookings) {
//        this.amenityBookings = amenityBookings;
//    }

//    public Property getProperty() {
//        return property;
//    }
//
//    public void setProperty(Property property) {
//        this.property = property;
//    }

}