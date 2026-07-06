package rentalSpacePortfolio.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rentalSpacePortfolio.enums.PropertyStatus;


@Data
@NoArgsConstructor  
@AllArgsConstructor 
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "properties")
public class Property extends BaseEnity{
    
   private String name;
   private String description;
   private String address;
   private String city;
   private String state;
   private String pinCode;
   
   @Enumerated(EnumType.STRING)
   private PropertyStatus status;
   
   private Double miniumRent;
   private Double maximumRent;
   
   @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
   private List<PropertyImage> propertyImages = new ArrayList<>();
   
   @OneToOne
   @JoinColumn(name = "admin_id", unique = true)
   private Admin admin;
   
   @ManyToOne
   @JoinColumn(name = "owner_id")
   private User owner;
   
   @OneToMany(mappedBy = "property")
   private List<Building> buildings = new ArrayList();

}