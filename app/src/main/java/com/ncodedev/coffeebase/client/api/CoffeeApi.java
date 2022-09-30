package com.ncodedev.coffeebase.client.api;

import com.ncodedev.coffeebase.model.domain.Coffee;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface CoffeeApi {

    @GET("coffees")
    Call<List<Coffee>> getCoffees();

    @GET("coffees/search")
    Call<List<Coffee>> searchCoffees(@Query("content") String content);

    @GET("coffees/{id}")
    Call<Coffee> getSingleCoffee(@Path("id") int id);

    @POST("coffees")
    Call<Coffee> createCoffee(@Body Coffee coffee);

    @PUT("coffees/{id}")
    Call<Void> updateCoffee(@Path("id") int id, @Body Coffee coffeeToUpdate);

    @PATCH("coffees/{id}")
    Call<Void> switchFavourite(@Path("id") int id);

    @DELETE("coffees/{id}")
    Call<Void> deleteCoffee(@Path("id") int id);

}
