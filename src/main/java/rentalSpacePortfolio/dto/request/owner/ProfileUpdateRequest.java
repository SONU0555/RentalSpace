package rentalSpacePortfolio.dto.request.owner;

import jakarta.validation.constraints.NotBlank;


public class ProfileUpdateRequest extends rentalSpacePortfolio.dto.request.tenant.ProfileUpdateRequest{
    
    @NotBlank(message = "Company name is required")
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    

}