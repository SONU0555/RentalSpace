package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import rentalSpacePortfolio.enums.PropertyStatus;



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
   
   private String coverImage;
   private Double miniumRent;
   private Double maximumRent;
   
   @OneToOne
   @JoinColumn(name = "admin_id")
   private Admin admin;
   
   @OneToMany(mappedBy = "property")
   private List<Building> buildings = new ArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public Double getMiniumRent() {
        return miniumRent;
    }

    public void setMiniumRent(Double miniumRent) {
        this.miniumRent = miniumRent;
    }

    public Double getMaximumRent() {
        return maximumRent;
    }

    public void setMaximumRent(Double maximumRent) {
        this.maximumRent = maximumRent;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

}