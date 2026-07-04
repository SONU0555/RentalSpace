package rentalSpacePortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "property_images")
public class PropertyImage extends BaseEnity{
    
    private String imageUrl;                
    private Boolean isCoverImage;     
    private Integer displayOrder;     

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsCoverImage() {
        return isCoverImage;
    }

    public void setIsCoverImage(Boolean isCoverImage) {
        this.isCoverImage = isCoverImage;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

}