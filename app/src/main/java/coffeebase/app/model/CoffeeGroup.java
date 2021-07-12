package coffeebase.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class CoffeeGroup {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("groupType")
    @Expose
    private GroupType groupType;
    @SerializedName("coffees")
    @Expose
    private Set<Coffee> coffees;

    public enum GroupType {
        METHOD, ORIGIN, ROASTER
    }

    CoffeeGroup(final Integer id, final String name, final GroupType groupType, final Set<Coffee> coffees) {
        this.id = id;
        this.name = name;
        this.groupType = groupType;
        this.coffees = coffees;
    }

    CoffeeGroup(final String name, final GroupType groupType) {
        this.name = name;
        this.groupType = groupType;
    }

    Integer getId() {
        return id;
    }

    void setId(final Integer id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    void setName(final String name) {
        this.name = name;
    }

    GroupType getGroupType() {
        return groupType;
    }

    void setGroupType(final GroupType groupType) {
        this.groupType = groupType;
    }

    Set<Coffee> getCoffees() {
        return coffees;
    }

    void setCoffees(final Set<Coffee> coffees) {
        this.coffees = coffees;
    }
}
