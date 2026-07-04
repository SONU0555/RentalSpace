package rentalSpacePortfolio.dto.request.property;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class PropertyImageRequest {
    
    @NotBlank(message = "Image URL must not be empty")
    @Size(max = 2048, message = "Image URL must not exceed 2048 characters")
//    @Pattern(
//        regexp = "^https?://.*\\.(jpg|jpeg|png|webp|gif)(\\?.*)?$",
//        flags  = Pattern.Flag.CASE_INSENSITIVE,
//        message = "Must be a valid http/https URL ending in jpg, jpeg, png, webp, or gif"
//    )
    private String imageUrl;

    @NotNull(message = "isCoverImage must be true or false — it cannot be missing")
    private Boolean isCoverImage;

    @NotNull(message = "Display order is required")
    @Min(value = 1,  message = "Display order must be at least 1")
    @Max(value = 5, message = "Display order must not exceed 20")
    private Integer displayOrder; 

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

}