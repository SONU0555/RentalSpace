package rentalSpacePortfolio.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import rentalSpacePortfolio.enums.PropertyTier;
import rentalSpacePortfolio.enums.PropertyVisbility;


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
   
   @Enumerated(EnumType.STRING)
   private PropertyVisbility visibility;
   
   @Enumerated(EnumType.STRING)
   @Column(name = "property_tier")
   private PropertyTier tier;
   
   private boolean isActive;
   
   private Double minimumRent;
   private Double maximumRent;
   
   private int step = 0;
   private String tab = "property";
   
   @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
   private List<PropertyImage> propertyImages = new ArrayList<>();
   
   @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
   private List<Amenity> amenities = new ArrayList<>();
   
   @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
   private List<FlatBooking> flatBookings = new ArrayList<>();
   
   @OneToOne
   @JoinColumn(name = "admin_id", unique = true)
   private Admin admin;
   
   @ManyToOne
   @JoinColumn(name = "owner_id")
   private User owner;
   
//   @OneToMany(mappedBy = "property")
//   private List<Building> buildings = new ArrayList();

}