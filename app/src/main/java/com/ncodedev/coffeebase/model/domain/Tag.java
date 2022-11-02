package com.ncodedev.coffeebase.model.domain;

public class Tag {
    private String name;
    private String color;

    public Tag(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
