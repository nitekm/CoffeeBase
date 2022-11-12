package com.ncodedev.coffeebase.client.provider;

import com.ncodedev.coffeebase.BuildConfig;
import com.ncodedev.coffeebase.client.api.CoffeeApi;
import com.ncodedev.coffeebase.model.security.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoffeeApiProvider  {

    public static CoffeeApi createCoffeeApi() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer " + User.getInstance().getToken())
                            .build();
                    return chain.proceed(request);
                });

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.BaseURL)
                .client(httpClient.build())
                .build();

        return retrofit.create(CoffeeApi.class);
    }
}
