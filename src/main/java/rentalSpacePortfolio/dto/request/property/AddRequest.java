package rentalSpacePortfolio.dto.request.property;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;



public class AddRequest {
    
@NotBlank(message = "Name is required")
@Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
private String name;

@NotBlank(message = "Description is required")
@Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
private String description;

@NotBlank(message = "Address is required")
@Size(max = 255, message = "Address must not exceed 255 characters")
private String address;

@NotBlank(message = "City is required")
@Size(max = 100, message = "City must not exceed 100 characters")
private String city;

@NotBlank(message = "State is required")
@Size(max = 100, message = "State must not exceed 100 characters")
private String state;

@NotBlank(message = "Pin code is required")
@Pattern(regexp = "^[0-9]{6}$", message = "Pin code must be exactly 6 digits")
private String pinCode;

@NotBlank(message = "Status is required")
private String status;

@NotBlank(message = "Cover image is required")
private String coverImage;

@NotNull(message = "Minimum rent is required")
@Positive(message = "Minimum rent must be greater than 0")
private Double minimumRent;

@NotNull(message = "Maximum rent is required")
@Positive(message = "Maximum rent must be greater than 0")
private Double maximumRent;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Double getMinimumRent() {
        return minimumRent;
    }

    public void setMinimumRent(Double minimumRent) {
        this.minimumRent = minimumRent;
    }

    public Double getMaximumRent() {
        return maximumRent;
    }

    public void setMaximumRent(Double maximumRent) {
        this.maximumRent = maximumRent;
    }




}