package rentalSpacePortfolio.dto.response.tenant;

import java.util.List;



public class DashboardResponse {
    
    private Integer numberOfproperties;
    private Integer activeBookings;
    private Integer numOfAmenityBookings;
    private Integer numOfMaintenanceRequests;
    private List<?> upcommingBookings;
    private List<?> maintenanceStatus;

    public Integer getNumberOfproperties() {
        return numberOfproperties;
    }

    public void setNumberOfproperties(Integer numberOfproperties) {
        this.numberOfproperties = numberOfproperties;
    }

    public Integer getActiveBookings() {
        return activeBookings;
    }

    public void setActiveBookings(Integer activeBookings) {
        this.activeBookings = activeBookings;
    }

    public Integer getNumOfAmenityBookings() {
        return numOfAmenityBookings;
    }

    public void setNumOfAmenityBookings(Integer numOfAmenityBookings) {
        this.numOfAmenityBookings = numOfAmenityBookings;
    }

    public Integer getNumOfMaintenanceRequests() {
        return numOfMaintenanceRequests;
    }

    public void setNumOfMaintenanceRequests(Integer numOfMaintenanceRequests) {
        this.numOfMaintenanceRequests = numOfMaintenanceRequests;
    }

    public List<?> getUpcommingBookings() {
        return upcommingBookings;
    }

    public void setUpcommingBookings(List<?> upcommingBookings) {
        this.upcommingBookings = upcommingBookings;
    }

    public List<?> getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(List<?> maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }
    
    

}