package ncodedev.coffeebase.web.listener;

import ncodedev.coffeebase.model.domain.Brew;

import java.util.List;

public interface BrewListResponseListener {
    void handleGetList(List<Brew> brews);
}
