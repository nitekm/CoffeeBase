package ncodedev.coffeebase.web.listener;

import ncodedev.coffeebase.model.domain.Coffee;
import ncodedev.coffeebase.model.utils.Page;

import java.util.List;

public interface CoffeeListResponseListener extends ResponseListener{
    void handleGetAll(List<Coffee> coffees);
    void handleGetAllPage(Page<Coffee> coffeesPage);
    void handleSortPage(Page<Coffee> coffeePage);
    void handleFilterPage(Page<Coffee> coffeePage);
}
