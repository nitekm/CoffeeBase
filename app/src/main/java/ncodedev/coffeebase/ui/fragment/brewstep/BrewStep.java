package ncodedev.coffeebase.ui.fragment.brewstep;

import android.view.View;
import ncodedev.coffeebase.web.provider.BrewApiProvider;

public interface BrewStep {
    BrewApiProvider brewApiprovider = BrewApiProvider.getInstance();

    void initViews(View view);
    void setupStep();
}
