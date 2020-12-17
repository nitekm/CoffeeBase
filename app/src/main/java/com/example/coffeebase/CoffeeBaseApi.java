package com.example.coffeebase;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface CoffeeBaseApi {

    @GET("coffees")
    Call<List<Coffee>> getCoffees();

    @GET("/coffees/{id}")
    Call<Coffee> getSingleCoffee(@Path("id") int id);

    @POST("coffees")
    Call<Void> addToCoffeeBase(@Body Coffee coffee);

    @DELETE("coffees/{id}")
    Call<Void> deleteCoffee(@Path("id") int id);

}
