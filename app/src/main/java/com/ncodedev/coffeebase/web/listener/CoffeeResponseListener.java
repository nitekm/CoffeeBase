package com.ncodedev.coffeebase.web.listener;

import com.ncodedev.coffeebase.model.domain.Coffee;

public interface CoffeeResponseListener {
    void handleCoffeeResponse(Coffee coffee);
}
