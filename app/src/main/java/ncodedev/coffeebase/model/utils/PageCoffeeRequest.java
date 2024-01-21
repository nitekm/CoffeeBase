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

    public PageCoffeeRequest(Integer pageNumber) {
        this.pageNumber = pageNumber;
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
}
