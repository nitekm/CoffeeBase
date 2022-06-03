package com.ncode.coffeebase.client.provider;

import com.ncode.coffeebase.BuildConfig;
import com.ncode.coffeebase.client.api.SecurityApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecurityApiProvider {

    public static SecurityApi createSecurityApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.BaseURL)
                .build();

        return retrofit.create(SecurityApi.class);
    }
}
