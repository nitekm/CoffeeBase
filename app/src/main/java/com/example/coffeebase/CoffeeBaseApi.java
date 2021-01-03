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

    @GET("coffees/sort/rating_asc")
    Call<List<Coffee>> getSortedByRatingAsc();

    @GET("coffees/sort/rating_desc")
    Call<List<Coffee>> getSortedByRatingDesc();

    @GET("coffees/sort/name_asc")
    Call<List<Coffee>> getSortedByNameAsc();

    @GET("coffees/sort/rating_desc")
    Call<List<Coffee>> getSortedByNameDesc();
}
