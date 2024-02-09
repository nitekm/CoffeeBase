package ncodedev.coffeebase.web.listener;

import ncodedev.coffeebase.model.domain.Coffee;

public interface CoffeeResponseListener extends ResponseListener {
    void handleCoffeeResponse(Coffee coffee);

    void handleSaveResponse(Coffee coffee);

    void handleDeleteResponse();
}
