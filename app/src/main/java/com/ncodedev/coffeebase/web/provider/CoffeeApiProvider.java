package com.ncodedev.coffeebase.web.provider;

import android.app.Activity;
import com.ncodedev.coffeebase.BuildConfig;
import com.ncodedev.coffeebase.model.domain.Coffee;
import com.ncodedev.coffeebase.model.security.User;
import com.ncodedev.coffeebase.web.api.CoffeeApi;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.ncodedev.coffeebase.utils.Logger.logCall;
import static com.ncodedev.coffeebase.utils.Logger.logCallFail;
import static com.ncodedev.coffeebase.utils.ToastUtils.showToast;

public class CoffeeApiProvider {

    public static final String TAG = "CoffeeApiProvider";

    public Optional<List<Coffee>> getAll(Activity activity) {
        Call<List<Coffee>> call = ApiProvider.createApi(CoffeeApi.class).getCoffees();
        logCall(TAG, call);
        try {
            final Response<List<Coffee>> response = call.execute();
            if (response.isSuccessful()) {
                return Optional.of(response.body());
            }
            showToast(activity, response.message());
            return Optional.empty();

        } catch (IOException e) {
            logCallFail(TAG, call);
        }
        return Optional.empty();
    }

    public Optional<Coffee> getOne(Activity activity, int id) {
        Call<Coffee> call = createCoffeeApi().getSingleCoffee(id);
        logCall(TAG, call);
        try {
            final Response<Coffee> response = call.execute();
            if (response.isSuccessful()) {
                return Optional.ofNullable(response.body());
            }
            showToast(activity, response.message());
            return Optional.empty();
        } catch (IOException e) {
            logCallFail(TAG, call);
        }
        return Optional.empty();
    }

    public void save(Activity activity, Coffee coffee) {
        Call<Coffee> call = createCoffeeApi().createCoffee(coffee);
        logCall(TAG, call);
        call.enqueue(new Callback<Coffee>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                if (response.isSuccessful()) {
                    showToast(activity, "Coffee added!");
                    return;
                }
                showToast(activity, response.message());
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                logCallFail(TAG, call);
                showToast(activity, t.getMessage());
            }
        });
    }

    public void update(Activity activity, int id, Coffee coffee) {
        Call<Void> call = createCoffeeApi().updateCoffee(id, coffee);
        logCall(TAG, call);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast(activity, "Changes saved!");
                    return;
                }
                showToast(activity, response.message());
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
                logCallFail(TAG, call);
                showToast(activity, t.getMessage());
            }
        });
    }

    public void delete(Activity activity, int id) {
        Call<Void> call = createCoffeeApi().deleteCoffee(id);
            logCall(TAG, call);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(final Call<Void> call, final Response<Void> response) {
                    if (response.isSuccessful()) {
                        showToast(activity, "Coffee deleted");
                        return;
                    }
                    showToast(activity, response.message());
                }

                @Override
                public void onFailure(final Call<Void> call, final Throwable t) {
                    logCallFail(TAG, call);
                    showToast(activity, t.getMessage());
                }
            });
        }

    public void switchFavourites(Activity activity, int coffeeId) {
        Call<Void> call = createCoffeeApi().switchFavourite(coffeeId);
        logCall(TAG, call);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {

            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
                logCallFail(TAG, call);
                showToast(activity, t.getMessage());
            }
        });
    }

        public static CoffeeApi createCoffeeApi () {
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
