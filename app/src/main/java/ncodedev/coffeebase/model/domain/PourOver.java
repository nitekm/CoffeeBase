package ncodedev.coffeebase.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PourOver implements Serializable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("time")
    @Expose
    private Integer time;

    @SerializedName("waterAmountInMl")
    @Expose
    private Integer waterAmountInMl;

    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("userId")
    @Expose
    private String userId;

    public PourOver(Long id, Integer time, Integer waterAmountInMl, String comment, String userId) {
        this.id = id;
        this.time = time;
        this.waterAmountInMl = waterAmountInMl;
        this.comment = comment;
        this.userId = userId;
    }

    public PourOver(Integer time, Integer waterAmountInMl) {
        this.time = time;
        this.waterAmountInMl = waterAmountInMl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
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
}
