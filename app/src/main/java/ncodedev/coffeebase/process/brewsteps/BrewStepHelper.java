package ncodedev.coffeebase.process.brewsteps;

import android.app.Activity;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.web.listener.BrewStepResponseListener;
import ncodedev.coffeebase.web.provider.BrewApiProvider;

public class BrewStepHelper {
    private final BrewApiProvider brewApiProvider = BrewApiProvider.getInstance();

    public void init(Brew brew, BrewStepResponseListener listener, Activity activity) {
        brewApiProvider.init(brew, listener, activity);
    }

    public void finish(Brew brew, BrewStepResponseListener listener, Activity activity) {
        brewApiProvider.finish(brew, listener, activity);
    }
}
