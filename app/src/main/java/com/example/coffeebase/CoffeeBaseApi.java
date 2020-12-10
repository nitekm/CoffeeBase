package com.example.coffeebase;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface CoffeeBaseApi {

    @GET("coffees")
    Call<List<Coffee>> getCoffees();

    @GET("/coffees/{id}")
    Call<Coffee> getSingleCoffee(@Path("id") int id);

    @POST("coffees")
    Call<Coffee> addToCoffeeBase(@Body Coffee coffee);

}
