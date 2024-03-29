package ncodedev.coffeebase.model.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.Set;

public class PageCoffeeRequest {

    @SerializedName("pageSize")
    @Expose
    private final Integer pageSize;

    @SerializedName("pageNumber")
    @Expose
    private final Integer pageNumber;

    @SerializedName("sortProperty")
    @Expose
    private final String sortProperty;

    @SerializedName("sortDirection")
    @Expose
    private final String sortDirection;

    @SerializedName("filters")
    @Expose
    private Map<String, Set<String>> filters;

    private PageCoffeeRequest(Builder builder) {
        this.pageNumber = (builder.pageNumber != null) ? builder.pageNumber : 0;
        this.sortProperty = (builder.sortProperty != null) ? builder.sortProperty : "id";
        this.sortDirection = (builder.sortDirection != null) ? builder.sortDirection : "ASC";
        this.pageSize = (builder.pageSize != null) ? builder.pageSize : 20;
        this.filters = builder.filters;
    }

    public static class Builder {

        private Integer pageNumber;
        private String sortProperty;
        private String sortDirection;
        private Integer pageSize;
        private Map<String, Set<String>> filters;

        public Builder withPageNumber(Integer pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public Builder withSortProperty(String sortProperty) {
            this.sortProperty = sortProperty;
            return this;
        }

        public Builder withSortDirection(String sortDirection) {
            this.sortDirection = sortDirection;
            return this;
        }

        public Builder withPageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder withFilters(Map<String, Set<String>> filters) {
            this.filters = filters;
            return this;
        }

        public PageCoffeeRequest build() {
            return new PageCoffeeRequest(this);
        }
    }
}