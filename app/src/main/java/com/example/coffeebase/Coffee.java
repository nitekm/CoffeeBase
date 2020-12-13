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

    public Coffee(int id, String name, String origin) {
        this.id = id;
        this.name = name;
        this.origin = origin;
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
}
