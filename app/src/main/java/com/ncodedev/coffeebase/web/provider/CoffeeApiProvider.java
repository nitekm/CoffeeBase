package com.ncodedev.coffeebase.web.provider;

import android.app.Activity;
import com.ncodedev.coffeebase.model.domain.Coffee;
import com.ncodedev.coffeebase.web.api.CoffeeApi;
import com.ncodedev.coffeebase.web.listener.CoffeeListResponseListener;
import com.ncodedev.coffeebase.web.listener.CoffeeResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static com.ncodedev.coffeebase.utils.ToastUtils.showToast;
import static com.ncodedev.coffeebase.web.provider.RetrofitApiCreator.createApi;

public class CoffeeApiProvider {

    public static final String TAG = "CoffeeApiProvider";

    private static CoffeeApiProvider instance;

    public static CoffeeApiProvider getInstance() {
        if (instance == null) {
            instance = new CoffeeApiProvider();
        }
        return instance;
    }

    public void getAll(CoffeeListResponseListener listener, Activity activity) {
        Call<List<Coffee>> call = createApi(CoffeeApi.class).getCoffees();
        handleListResponse(call, listener, activity);
    }

    public void search(String content, CoffeeListResponseListener listener, Activity activity) {
        Call<List<Coffee>> call = createApi(CoffeeApi.class).searchCoffees(content);
        handleListResponse(call, listener, activity);
    }

    public void getOne(int id, CoffeeResponseListener listener, Activity activity) {
        Call<Coffee> call = createApi(CoffeeApi.class).getSingleCoffee(id);
        handleCoffeeResponse(call, listener, activity);
    }

    public void save(Coffee coffee, CoffeeResponseListener listener, Activity activity) {
        Call<Coffee> call = createApi(CoffeeApi.class).createCoffee(coffee);
        handleCoffeeResponse(call, listener, activity);
    }

    public void update(int id, Coffee coffee, CoffeeResponseListener listener, Activity activity) {
        Call<Coffee> call = createApi(CoffeeApi.class).updateCoffee(id, coffee);
        handleCoffeeResponse(call, listener, activity);
    }

    public void delete(int id, Activity activity) {
        Call<Void> call = createApi(CoffeeApi.class).deleteCoffee(id);
        handleVoidResponse(call, activity);
    }

    public void switchFavourites(int coffeeId, CoffeeResponseListener listener, Activity activity) {
        Call<Coffee> call = createApi(CoffeeApi.class).switchFavourite(coffeeId);
        handleCoffeeResponse(call, listener, activity);
    }

    private void handleListResponse(Call<List<Coffee>> call, CoffeeListResponseListener listener, Activity activity) {
        call.enqueue(new Callback<List<Coffee>>() {
            @Override
            public void onResponse(final Call<List<Coffee>> call, final Response<List<Coffee>> response) {
                if (response.isSuccessful()) {
                    listener.handleGetList(response.body());
                } else {
                    showToast(activity, "Error!" + response.message());
                }
            }

            @Override
            public void onFailure(final Call<List<Coffee>> call, final Throwable t) {
                showToast(activity, "Server is unavailable. Try again later");
            }
        });
    }

    private void handleCoffeeResponse(Call<Coffee> call, CoffeeResponseListener listener, Activity activity) {
        call.enqueue(new Callback<Coffee>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                if (response.isSuccessful()) {
                    listener.handleCoffeeResponse(response.body());
                } else {
                    showToast(activity, "Error!" + response.message());
                }
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                showToast(activity, "Server is unavailable. Try again later");
            }
        });
    }

    private void handleVoidResponse(Call<Void> call, Activity activity) {
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                if (!response.isSuccessful()) {
                    showToast(activity, "Error!" + response.message());
                }
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
                showToast(activity, "Server is unavailable. Try again later");
            }
        });
    }
}
