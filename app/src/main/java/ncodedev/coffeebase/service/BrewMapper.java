package ncodedev.coffeebase.service;

import android.text.TextUtils;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.model.domain.PourOver;
import ncodedev.coffeebase.model.domain.process.BrewStatus;

import java.util.List;
import java.util.Optional;

public class BrewMapper {
    public static Brew mapGeneralInfo(Brew brew, String name, String method) {
        brew.setName(name);
        brew.setMethod(method);

        brew.setBrewStatus(BrewStatus.IN_PROGRESS);
        return brew;
    }

    public static Brew mapIngredients(Brew brew, String coffeeWeightTxt, String grinderSettingTxt, String waterAmountTxt, String waterTempTxt, String filterTxt) {
        Optional.of(coffeeWeightTxt)
                .filter(weight -> TextUtils.getTrimmedLength(coffeeWeightTxt) > 0)
                .map(Integer::parseInt)
                .ifPresent(brew::setCoffeeWeightInGrams);
        Optional.of(grinderSettingTxt)
                .filter(grinder -> TextUtils.getTrimmedLength(grinder) > 0)
                .map(Integer::parseInt)
                .ifPresent(brew::setGrinderSetting);
        Optional.of(waterAmountTxt)
                .filter(amount -> TextUtils.getTrimmedLength(amount) > 0)
                .map(Integer::parseInt)
                .ifPresent(brew::setWaterAmountInMl);
        Optional.of(waterTempTxt)
                .filter(temp -> TextUtils.getTrimmedLength(temp) > 0)
                .map(Integer::parseInt)
                .ifPresent(brew::setWaterTemp);
        brew.setFilter(filterTxt);

        brew.setBrewStatus(BrewStatus.IN_PROGRESS);
        return brew;
    }

    public static Brew mapPours(Brew brew, List<PourOver> pourOvers) {
        brew.setPourOvers(pourOvers);

        brew.setBrewStatus(BrewStatus.COMPLETED);
        return brew;
    }
}
