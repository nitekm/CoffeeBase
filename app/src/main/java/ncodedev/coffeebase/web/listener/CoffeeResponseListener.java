package ncodedev.coffeebase.web.listener;

import ncodedev.coffeebase.model.domain.Coffee;

public interface CoffeeResponseListener {
    void handleCoffeeResponse(Coffee coffee);
}
