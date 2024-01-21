package ncodedev.coffeebase.web.listener;

import ncodedev.coffeebase.model.domain.Coffee;
import ncodedev.coffeebase.model.utils.Page;

import java.util.List;

public interface CoffeeListResponseListener {
    void handleGetList(List<Coffee> coffees);
    void handleGetList(Page<Coffee> coffeesPage);
    void handleError();
}
