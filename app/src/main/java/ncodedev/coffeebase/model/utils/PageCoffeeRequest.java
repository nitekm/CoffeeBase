package ncodedev.coffeebase.model.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageCoffeeRequest {

    @SerializedName("pageSize")
    @Expose
    Integer pageSize = 20;

    @SerializedName("pageNumber")
    @Expose
    Integer pageNumber = 0;

    @SerializedName("sortProperty")
    @Expose
    String sortProperty = "id";

    @SerializedName("sortDirection")
    @Expose
    String sortDirection = "ASC";

    @SerializedName("favourite")
    @Expose
    Boolean favourite;

    @SerializedName("continent")
    @Expose
    String continent;

    @SerializedName("roastProfile")
    @Expose
    String roastProfile;

    public PageCoffeeRequest(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public PageCoffeeRequest(Integer pageNumber, String sortProperty, String sortDirection) {
        this.pageNumber = pageNumber;
        this.sortProperty = sortProperty;
        this.sortDirection = sortDirection;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSortProperty() {
        return sortProperty;
    }

    public void setSortProperty(String sortProperty) {
        this.sortProperty = sortProperty;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getRoastProfile() {
        return roastProfile;
    }

    public void setRoastProfile(String roastProfile) {
        this.roastProfile = roastProfile;
    }
}
