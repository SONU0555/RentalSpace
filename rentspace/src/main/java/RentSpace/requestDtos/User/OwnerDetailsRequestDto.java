package RentSpace.requestDtos.User;

import jakarta.validation.constraints.NotBlank;


public class OwnerDetailsRequestDto extends UserDetailsRequestDto{
    
    @NotBlank(message = "Company name is required")
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}