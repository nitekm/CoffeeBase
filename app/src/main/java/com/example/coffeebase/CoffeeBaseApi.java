package com.example.coffeebase;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface CoffeeBaseApi {

    @GET("posts")
    Call<List<Coffee>> getCoffees();
}
