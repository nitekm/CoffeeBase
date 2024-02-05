package ncodedev.coffeebase.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Coffee implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;
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
    @SerializedName("coffeeImageName")
    @Expose
    private String coffeeImageName;
    @SerializedName("favourite")
    @Expose
    private boolean favourite;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("tags")
    @Expose
    private List<Tag> tags;

    @SerializedName("brews")
    @Expose
    private List<Brew> brews;

    public Coffee(final Long id, final String name, final String origin, final String roaster,
                  final String processing, final String roastProfile, final String region, final String continent,
                  final String farm, final Integer cropHeight, final Integer scaRating, final Double rating,
                  final String coffeeImageName, final boolean favourite, final String userId, final List<Tag> tags,
                  final List<Brew> brews) {
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
        this.coffeeImageName = coffeeImageName;
        this.favourite = favourite;
        this.userId = userId;
        this.tags = tags;
        this.brews = brews;
    }
    public Coffee(final String name, final String origin, final String roaster,
                  final String processing, final String roastProfile, final String region,
                  final String continent, final String farm, final Integer cropHeight, final Integer scaRating,
                  final Double rating, final String userId, final List<Tag> tags) {
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
        this.userId = userId;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public String getRoaster() {
        return roaster;
    }

    public String getProcessing() {
        return processing;
    }

    public String getRoastProfile() {
        return roastProfile;
    }

    public String getRegion() {
        return region;
    }

    public String getContinent() {
        return continent;
    }

    public String getFarm() {
        return farm;
    }

    public Integer getCropHeight() {
        return cropHeight;
    }

    public Integer getScaRating() {
        return scaRating;
    }

    public Double getRating() {
        return rating;
    }

    public String getCoffeeImageName() {
        return coffeeImageName;
    }

    public void setCoffeeImageName(String coffeeImageName) {
        this.coffeeImageName = coffeeImageName;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public String getUserId() {
        return userId;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<Brew> getBrews() {
        return brews;
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
                ", imageUrl='" + coffeeImageName + '\'' +
                ", favourite=" + favourite +
                ", userId='" + userId + '\'' +
                ", tags=" + tags + '\'' +
                ", brews=" + brews +
                '}';
    }
}
