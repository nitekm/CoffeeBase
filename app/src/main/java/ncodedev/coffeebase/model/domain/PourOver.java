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

    public Long getTimeInSeconds() {
        return timeInSeconds;
    }

    public Integer getWaterAmountInMl() {
        return waterAmountInMl;
    }

    public String getComment() {
        return comment;
    }

    public String getUserId() {
        return userId;
    }

    public Brew getBrew() {
        return brew;
    }
}
