package rentalSpacePortfolio.dto.response.property;



public class PropertyResponse {
    
    private String id;
    private String name;
    private String description;
    private String city;
    private String state;
    private String status;
    private String coverImage;
    private Double miniumRent;
    private Double maximumRent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

}