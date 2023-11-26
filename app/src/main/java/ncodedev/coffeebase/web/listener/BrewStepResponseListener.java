package ncodedev.coffeebase.web.listener;

import ncodedev.coffeebase.model.domain.Brew;

public interface BrewStepResponseListener {
    void handleInitBrewStepResponse(Brew brew);
    void handleFinishBrewStepResponse(Brew brew);

}
