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

    @GET("coffees/favourites")
    Call<List<Coffee>> getFavouriteCoffees();

    @POST("/coffees/favourites")
    Call<Coffee> addToFavourites(@Body Coffee coffee);

    //@POST("coffees/")
}
