package com.ncodedev.coffeebase.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coffee {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("roaster")
    @Expose
    private String roaster;

    @SerializedName("processing")
    @Expose
    private String processing;

    @SerializedName("roastProfile")
    @Expose
    private String roastProfile;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("continent")
    @Expose
    private String continent;

    @SerializedName("farm")
    @Expose
    private String farm;

    @SerializedName("cropHeight")
    @Expose
    private Integer cropHeight;

    @SerializedName("scaRating")
    @Expose
    private Integer scaRating;

    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("favourite")
    @Expose
    private boolean favourite;
    @SerializedName("userId")
    @Expose
    private String userId;


    public Coffee(final Integer id, final String name, final String origin, final String roaster, final String processing, final String roastProfile, final String region, final String continent, final String farm, final Integer cropHeight, final Integer scaRating, final Double rating, final String imageUrl, final boolean favourite, final String userId) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.roaster = roaster;
        this.processing = processing;
        this.roastProfile = roastProfile;
        this.region = region;
        this.continent = continent;
        this.farm = farm;
        this.cropHeight = cropHeight;
        this.scaRating = scaRating;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.favourite = favourite;
        this.userId = userId;
    }
    public Coffee(final String name, final String origin, final String roaster, final String processing, final String roastProfile, final String region, final String continent, final String farm, final Integer cropHeight, final Integer scaRating, final Double rating, final String imageUrl, final String userId) {
        this.name = name;
        this.origin = origin;
        this.roaster = roaster;
        this.processing = processing;
        this.roastProfile = roastProfile;
        this.region = region;
        this.continent = continent;
        this.farm = farm;
        this.cropHeight = cropHeight;
        this.scaRating = scaRating;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRoaster() {
        return roaster;
    }

    public void setRoaster(String roaster) {
        this.roaster = roaster;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(final String processing) {
        this.processing = processing;
    }

    public String getRoastProfile() {
        return roastProfile;
    }

    public void setRoastProfile(final String roastProfile) {
        this.roastProfile = roastProfile;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(final String continent) {
        this.continent = continent;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(final String farm) {
        this.farm = farm;
    }

    public Integer getCropHeight() {
        return cropHeight;
    }

    public void setCropHeight(final Integer cropHeight) {
        this.cropHeight = cropHeight;
    }

    public Integer getScaRating() {
        return scaRating;
    }

    public void setScaRating(final Integer scaRating) {
        this.scaRating = scaRating;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Coffee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", origin='" + origin + '\'' +
                ", roaster='" + roaster + '\'' +
                ", processing='" + processing + '\'' +
                ", roastProfile='" + roastProfile + '\'' +
                ", region='" + region + '\'' +
                ", continent='" + continent + '\'' +
                ", farm='" + farm + '\'' +
                ", cropHeight=" + cropHeight +
                ", scaRating=" + scaRating +
                ", rating=" + rating +
                ", imageUrl='" + imageUrl + '\'' +
                ", favourite=" + favourite +
                ", userId='" + userId + '\'' +
                '}';
    }
}
