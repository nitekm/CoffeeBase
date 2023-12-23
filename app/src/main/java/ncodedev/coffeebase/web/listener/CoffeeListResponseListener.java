package ncodedev.coffeebase.web.listener;

import ncodedev.coffeebase.model.domain.Coffee;

import java.util.List;

public interface CoffeeListResponseListener {
    void handleGetList(List<Coffee> coffees);
    void handleError();
}
