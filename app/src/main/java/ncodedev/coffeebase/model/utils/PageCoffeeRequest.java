package ncodedev.coffeebase.model.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @SerializedName("favourite")
    @Expose
    private Boolean favourite;

    @SerializedName("continent")
    @Expose
    private String continent;

    @SerializedName("roastProfile")
    @Expose
    private String roastProfile;

    private PageCoffeeRequest(Builder builder) {
        this.pageNumber = (builder.pageNumber != null) ? builder.pageNumber : 0;
        this.sortProperty = (builder.sortProperty != null) ? builder.sortProperty : "id";
        this.sortDirection = (builder.sortDirection != null) ? builder.sortDirection : "ASC";
        this.pageSize = (builder.pageSize != null) ? builder.pageSize : 3;
        this.favourite = builder.favourite;
        this.continent = builder.continent;
        this.roastProfile = builder.roastProfile;
    }

    public static class Builder {

        private Integer pageNumber;
        private String sortProperty;
        private String sortDirection;
        private Integer pageSize;
        private Boolean favourite;
        private String continent;
        private String roastProfile;

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

        public Builder withFavourite(Boolean favourite) {
            this.favourite = favourite;
            return this;
        }

        public Builder withContinent(String continent) {
            this.continent = continent;
            return this;
        }

        public Builder withRoastProfile(String roastProfile) {
            this.roastProfile = roastProfile;
            return this;
        }

        public PageCoffeeRequest build() {
            return new PageCoffeeRequest(this);
        }
    }
}