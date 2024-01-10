package ncodedev.coffeebase.model.enums;

import ncodedev.coffeebase.R;

import java.util.Arrays;

public enum BrewMethod {

    //TODO: I'm not sure about that EMPTY thing, it's probably stupid...
    EMPTY(null),
    V60("V60"),
    ESPRESSO("espresso"),
    AEROPRESS("aeropress"),
    MOKKA_POT("mokka pot"),
    POUR_OVER_MACHINE("pour-over machine"),
    FRENCH_PRESS("french press"),
    CHEMEX("chemex");
    private String value;

    BrewMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static BrewMethod getMethodForResourceId(int resourceId) {
        if (resourceId == R.string.v60) return V60;
        if (resourceId == R.string.espresso) return ESPRESSO;
        if (resourceId == R.string.aeropress) return AEROPRESS;
        if (resourceId == R.string.mokka_pot) return MOKKA_POT;
        if (resourceId == R.string.auto_drip) return POUR_OVER_MACHINE;
        if (resourceId == R.string.french_press) return FRENCH_PRESS;
        if (resourceId == R.string.chemex) return CHEMEX;
        return null;
    }

    public static int getResourceIdForMethod(BrewMethod method) {
        int resourceId = 0;
        switch (method) {
            case V60 -> resourceId = R.string.v60;
            case ESPRESSO -> resourceId = R.string.espresso;
            case AEROPRESS -> resourceId = R.string.aeropress;
            case MOKKA_POT -> resourceId = R.string.mokka_pot;
            case POUR_OVER_MACHINE -> resourceId = R.string.auto_drip;
            case FRENCH_PRESS -> resourceId = R.string.french_press;
            case CHEMEX -> resourceId = R.string.chemex;
        }
        return resourceId;
    }

    public static BrewMethod getMethodFromString(String methodName) {
        return Arrays.stream(BrewMethod.values()).sequential().filter(
                method -> method.getValue() != null && method.getValue().equalsIgnoreCase(methodName))
                .findAny()
                .orElse(EMPTY);
    }
}
