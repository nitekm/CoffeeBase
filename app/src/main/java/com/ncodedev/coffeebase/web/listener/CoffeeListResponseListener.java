package com.ncodedev.coffeebase.web.listener;

import com.ncodedev.coffeebase.model.domain.Coffee;

import java.util.List;

public interface CoffeeListResponseListener {
    void handleGetList(List<Coffee> coffees);
}
