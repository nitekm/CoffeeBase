package ncodedev.coffeebase.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PourOver implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("timeInSeconds")
    @Expose
    private Long timeInSeconds;

    @SerializedName("waterAmountInMl")
    @Expose
    private Integer waterAmountInMl;

    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("brew")
    @Expose(serialize = false)
    private Brew brew;

    public PourOver(Brew brew, Long timeInSeconds, Integer waterAmountInMl) {
        this.brew = brew;
        this.timeInSeconds = timeInSeconds;
        this.waterAmountInMl = waterAmountInMl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(Long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public Integer getWaterAmountInMl() {
        return waterAmountInMl;
    }

    public void setWaterAmountInMl(Integer waterAmountInMl) {
        this.waterAmountInMl = waterAmountInMl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Brew getBrew() {
        return brew;
    }

    public void setBrew(Brew brew) {
        this.brew = brew;
    }
}
