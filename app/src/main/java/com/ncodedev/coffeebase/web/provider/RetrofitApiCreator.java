package com.ncodedev.coffeebase.web.provider;

import com.ncodedev.coffeebase.BuildConfig;
import com.ncodedev.coffeebase.model.security.User;
import com.ncodedev.coffeebase.web.api.Api;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiCreator {
    public static <A extends Api> A createApi(Class<A> api) {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        logger.redactHeader("Authorization");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(logger)
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

        return retrofit.create(api);
    }
}
