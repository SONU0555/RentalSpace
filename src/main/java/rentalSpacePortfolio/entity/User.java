package rentalSpacePortfolio.entity;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.Role;

@Data
@NoArgsConstructor  
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = true)
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
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Tenant tenant;
    
    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Admin admin;
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Property> properties = new ArrayList<>();
    
//    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Booking> bookings;
    
//    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<MaintenanceRequest> maintenanceRequest;
//    
//    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<AmenityBooking> amenityBookings;

}