package ncodedev.coffeebase.web.provider;

import android.util.Log;
import ncodedev.coffeebase.model.domain.Coffee;
import ncodedev.coffeebase.web.api.CoffeeApi;
import ncodedev.coffeebase.web.listener.CoffeeListResponseListener;
import ncodedev.coffeebase.web.listener.CoffeeResponseListener;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static ncodedev.coffeebase.web.provider.RetrofitApiCreator.createApi;

public class CoffeeApiProvider {

    public static final String TAG = "CoffeeApiProvider";

    private static CoffeeApiProvider instance;

    public static CoffeeApiProvider getInstance() {
        if (instance == null) {
            instance = new CoffeeApiProvider();
        }
        return instance;
    }

    public void getAll(CoffeeListResponseListener listener) {

        Call<List<Coffee>> call = createApi(CoffeeApi.class).getCoffees();
        handleListResponse(call, listener);
    }

    public void search(String content, CoffeeListResponseListener listener) {
        Call<List<Coffee>> call = createApi(CoffeeApi.class).searchCoffees(content);
        handleListResponse(call, listener);
    }

    public void getOne(long id, CoffeeResponseListener listener) {
        Call<Coffee> call = createApi(CoffeeApi.class).getSingleCoffee(id);
        handleCoffeeResponse(call, listener);
    }

    public void save(Coffee coffee, MultipartBody.Part image, CoffeeResponseListener listener) {
        Call<Coffee> call = createApi(CoffeeApi.class).createCoffee(coffee, image);
        handleSaveResponse(call, listener);
    }

    public void update(long id, Coffee coffee, MultipartBody.Part image, CoffeeResponseListener listener) {
        Call<Coffee> call = createApi(CoffeeApi.class).updateCoffee(id, coffee, image);
        handleSaveResponse(call, listener);
    }

    public void delete(long id, CoffeeResponseListener listener) {
        Call<Void> call = createApi(CoffeeApi.class).deleteCoffee(id);
        handleDeleteResponse(call, listener);
    }

    public void switchFavourites(long coffeeId, CoffeeResponseListener listener) {
        Call<Coffee> call = createApi(CoffeeApi.class).switchFavourite(coffeeId);
        handleCoffeeResponse(call, listener);
    }

    private void handleListResponse(Call<List<Coffee>> call, CoffeeListResponseListener listener) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<List<Coffee>> call, final Response<List<Coffee>> response) {
                if (response.isSuccessful()) {
                    listener.handleGetList(response.body());
                } else {
                    listener.handleError();
                }
            }

            @Override
            public void onFailure(final Call<List<Coffee>> call, final Throwable t) {
                listener.handleError();
                Log.d(TAG, "Retrying call");
                getAll(listener);
            }
        });
    }

    private void handleCoffeeResponse(Call<Coffee> call, CoffeeResponseListener listener) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                if (response.isSuccessful()) {
                    listener.handleCoffeeResponse(response.body());
                } else {
                    listener.handleError();
                }
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                Log.d(TAG, t.toString());
                listener.handleError();
            }
        });
    }

    void handleSaveResponse(Call<Coffee> call, CoffeeResponseListener listener) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                if (response.isSuccessful()) {
                    listener.handleSaveResponse(response.body());
                } else {
                    listener.handleError();
                }
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                Log.d(TAG, t.toString());
                listener.handleError();
            }
        });
    }

    private void handleDeleteResponse(Call<Void> call, CoffeeResponseListener listener) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.handleDeleteResponse();
                } else {

                    listener.handleError();
                }
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
                Log.i(TAG, t.toString());
                listener.handleError();
            }
        });
    }
}
