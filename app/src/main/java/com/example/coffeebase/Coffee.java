package com.example.coffeebase;

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
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("favourite")
    @Expose
    private boolean favourite;

    public Coffee(Integer id, String name, String origin, String roaster, String rating, String imageUrl, boolean favourite) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.roaster = roaster;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.favourite = favourite;
    }

    public Coffee(String name, String origin, String roaster, String rating, String imageUrl) {
        this.name = name;
        this.origin = origin;
        this.roaster = roaster;
        this.rating = rating;
        this.imageUrl = imageUrl;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
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
}
