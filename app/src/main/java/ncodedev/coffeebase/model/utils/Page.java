package ncodedev.coffeebase.model.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Page<T extends Serializable> {

    @SerializedName("content")
    @Expose
    List<T> content;

    @SerializedName("last")
    @Expose
    boolean last;

    @SerializedName("number")
    @Expose
    Integer number;

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
