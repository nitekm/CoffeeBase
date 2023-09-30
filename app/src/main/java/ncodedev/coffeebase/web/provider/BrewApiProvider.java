package ncodedev.coffeebase.web.provider;

import android.app.Activity;
import android.util.Log;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.web.api.BrewApi;
import ncodedev.coffeebase.web.listener.BrewListResponseListener;
import ncodedev.coffeebase.web.listener.BrewResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static ncodedev.coffeebase.R.string.error;
import static ncodedev.coffeebase.R.string.server_unavailable;
import static ncodedev.coffeebase.utils.ToastUtils.showToast;
import static ncodedev.coffeebase.web.provider.RetrofitApiCreator.createApi;

public class BrewApiProvider {

    private final String TAG = this.getClass().getSimpleName();

    private static BrewApiProvider instance;

    public static BrewApiProvider getInstance() {
        if (instance == null) {
            instance = new BrewApiProvider();
        }
        return instance;
    }

    public void getAll(BrewListResponseListener listener, Activity activity) {
        Call<List<Brew>> call = createApi(BrewApi.class).getAll();
        handleListResponse(call, listener, activity);
    }

    public void detachBrewFromCoffee(long brewId, int coffeeId, BrewResponseListener listener, Activity activity) {
        Call<Void> call = createApi(BrewApi.class).detachBrewFromCoffee(brewId, coffeeId);
        handleDetachResponse(call, listener, activity);

    }

    private void handleListResponse(Call<List<Brew>> call, BrewListResponseListener listener, Activity activity) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<List<Brew>> call, final Response<List<Brew>> response) {
                if (response.isSuccessful()) {
                    listener.handleGetList(response.body());
                } else {
                    showToast(activity, activity.getString(error) + response.message());
                }
            }

            @Override
            public void onFailure(final Call<List<Brew>> call, final Throwable t) {
                Log.d(TAG, t.toString());
                showToast(activity, activity.getString(server_unavailable));
            }
        });
    }

    private void handleDetachResponse(Call<Void> call, BrewResponseListener listener, Activity activity) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.handleDetachBrewFromCoffee();
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