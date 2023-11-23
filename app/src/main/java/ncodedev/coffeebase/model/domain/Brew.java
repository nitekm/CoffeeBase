package ncodedev.coffeebase.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ncodedev.coffeebase.model.domain.process.BrewStatus;

import java.io.Serializable;
import java.util.List;

public class Brew implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("waterAmountInMl")
    @Expose
    private Integer waterAmountInMl;

    @SerializedName("waterTemp")
    @Expose
    private Integer waterTemp;

    @SerializedName("coffeeWeightInGrams")
    @Expose
    private Integer coffeeWeightInGrams;

    @SerializedName("grinderSetting")
    @Expose
    private Integer grinderSetting;

    @SerializedName("filter")
    @Expose
    private String filter;

    @SerializedName("totalTime")
    @Expose
    private Integer totalTime;

    @SerializedName("status")
    @Expose
    private BrewStatus brewStatus;

    @SerializedName("pourOvers")
    @Expose
    private List<PourOver> pourOvers;

    @SerializedName("userId")
    @Expose
    private String userId;

    public Brew(Long id) {
        this.id = id;
    }

    public Brew(BrewStatus brewStatus) {
        this.brewStatus = brewStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getWaterAmountInMl() {
        return waterAmountInMl;
    }

    public void setWaterAmountInMl(Integer waterAmountInMl) {
        this.waterAmountInMl = waterAmountInMl;
    }

    public Integer getWaterTemp() {
        return waterTemp;
    }

    public void setWaterTemp(Integer waterTemp) {
        this.waterTemp = waterTemp;
    }

    public Integer getCoffeeWeightInGrams() {
        return coffeeWeightInGrams;
    }

    public void setCoffeeWeightInGrams(Integer coffeeWeightInGrams) {
        this.coffeeWeightInGrams = coffeeWeightInGrams;
    }

    public Integer getGrinderSetting() {
        return grinderSetting;
    }

    public void setGrinderSetting(Integer grinderSetting) {
        this.grinderSetting = grinderSetting;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public BrewStatus getBrewStatus() {
        return brewStatus;
    }

    public void setBrewStatus(BrewStatus brewStatus) {
        this.brewStatus = brewStatus;
    }

    public List<PourOver> getPourOvers() {
        return pourOvers;
    }

    public void setPourOvers(List<PourOver> pourOvers) {
        this.pourOvers = pourOvers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
