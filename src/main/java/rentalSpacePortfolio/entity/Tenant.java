package rentalSpacePortfolio.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tenants")
public class Tenant extends BaseEnity{
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
    private List<FlatBooking> bookedFlats = new ArrayList<>();
    
    private LocalDate rentStartDate;
    private LocalDate rentEndDate;
    private String emergencyContect;
    private String aadhaarNumber;
    private Boolean isVerified = false;
    
}