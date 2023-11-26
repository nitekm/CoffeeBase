package ncodedev.coffeebase.web.provider;

import android.app.Activity;
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

import static ncodedev.coffeebase.R.string.error;
import static ncodedev.coffeebase.R.string.server_unavailable;
import static ncodedev.coffeebase.utils.ToastUtils.showToast;
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

    public void getAll(CoffeeListResponseListener listener, Activity activity) {

        Call<List<Coffee>> call = createApi(CoffeeApi.class).getCoffees();
        handleListResponse(call, listener, activity);
    }

    public void search(String content, CoffeeListResponseListener listener, Activity activity) {
        Call<List<Coffee>> call = createApi(CoffeeApi.class).searchCoffees(content);
        handleListResponse(call, listener, activity);
    }

    public void getOne(long id, CoffeeResponseListener listener, Activity activity) {
        Call<Coffee> call = createApi(CoffeeApi.class).getSingleCoffee(id);
        handleCoffeeResponse(call, listener, activity);
    }

    public void save(Coffee coffee, MultipartBody.Part image, CoffeeResponseListener listener, Activity activity) {
        Call<Coffee> call = createApi(CoffeeApi.class).createCoffee(coffee, image);
        handleSaveResponse(call, listener, activity);
    }

    public void update(long id, Coffee coffee, MultipartBody.Part image, CoffeeResponseListener listener, Activity activity) {
        Call<Coffee> call = createApi(CoffeeApi.class).updateCoffee(id, coffee, image);
        handleSaveResponse(call, listener, activity);
    }

    public void delete(long id, CoffeeResponseListener listener, Activity activity) {
        Call<Void> call = createApi(CoffeeApi.class).deleteCoffee(id);
        handleDeleteResponse(call, listener, activity);
    }

    public void switchFavourites(long coffeeId, CoffeeResponseListener listener, Activity activity) {
        Call<Coffee> call = createApi(CoffeeApi.class).switchFavourite(coffeeId);
        handleCoffeeResponse(call, listener, activity);
    }

    private void handleListResponse(Call<List<Coffee>> call, CoffeeListResponseListener listener, Activity activity) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<List<Coffee>> call, final Response<List<Coffee>> response) {
                if (response.isSuccessful()) {
                    listener.handleGetList(response.body());
                } else {
                    showToast(activity, activity.getString(error) + response.message());
                }
            }

            @Override
            public void onFailure(final Call<List<Coffee>> call, final Throwable t) {
                showToast(activity, activity.getString(server_unavailable));
                Log.d(TAG, "Retrying call");
                getAll(listener, activity);
            }
        });
    }

    private void handleCoffeeResponse(Call<Coffee> call, CoffeeResponseListener listener, Activity activity) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                if (response.isSuccessful()) {
                    listener.handleCoffeeResponse(response.body());
                } else {
                    showToast(activity, activity.getString(error) + response.message());
                }
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                Log.d(TAG, t.toString());
                showToast(activity, activity.getString(server_unavailable));
            }
        });
    }

    void handleSaveResponse(Call<Coffee> call, CoffeeResponseListener listener, Activity activity) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                if (response.isSuccessful()) {
                    listener.handleSaveResponse(response.body());
                } else {
                    showToast(activity, activity.getString(error) + response.message());
                }
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                Log.d(TAG, t.toString());
                showToast(activity, activity.getString(server_unavailable));
            }
        });
    }

    private void handleDeleteResponse(Call<Void> call, CoffeeResponseListener listener, Activity activity) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.handleDeleteResponse();
                } else {
                    showToast(activity, activity.getString(error) + response.message());
                }
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
                Log.i(TAG, t.toString());
                showToast(activity, activity.getString(server_unavailable));
            }
        });
    }
}
