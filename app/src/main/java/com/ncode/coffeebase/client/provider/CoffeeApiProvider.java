package com.ncode.coffeebase.client.provider;

import com.ncode.coffeebase.BuildConfig;
import com.ncode.coffeebase.client.api.CoffeeApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoffeeApiProvider {

    public static CoffeeApi createCoffeeApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(CoffeeApi.class);
    }
}
