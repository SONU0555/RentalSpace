package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "admins")
public class Admin extends BaseEnity{
    
    @OneToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;
    
    @OneToOne(mappedBy = "admin")
    private Property property;
    
    private String location;            
    private String employeeCode; 

}