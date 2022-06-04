package com.ncode.coffeebase.client.provider;

import com.ncode.coffeebase.BuildConfig;
import com.ncode.coffeebase.client.api.CoffeeApi;
import com.ncode.coffeebase.utils.Global;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoffeeApiProvider  {

    public static CoffeeApi createCoffeeApi() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + Global.TOKEN).build();
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
