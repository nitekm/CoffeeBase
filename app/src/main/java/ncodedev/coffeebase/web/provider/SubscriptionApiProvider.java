package ncodedev.coffeebase.web.provider;

import android.app.Activity;
import android.util.Log;
import ncodedev.coffeebase.model.domain.Coffee;
import ncodedev.coffeebase.model.domain.Subscription;
import ncodedev.coffeebase.web.api.CoffeeApi;
import ncodedev.coffeebase.web.api.SubscriptionApi;
import ncodedev.coffeebase.web.listener.CoffeeListResponseListener;
import ncodedev.coffeebase.web.listener.CoffeeResponseListener;
import ncodedev.coffeebase.web.listener.SubscriptionResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static ncodedev.coffeebase.R.string.error;
import static ncodedev.coffeebase.R.string.server_unavailable;
import static ncodedev.coffeebase.utils.ToastUtils.showToast;
import static ncodedev.coffeebase.web.provider.RetrofitApiCreator.createApi;

public class SubscriptionApiProvider {

    public static final String TAG = "SubscriptionApiProvider";

    private static SubscriptionApiProvider instance;

    public static SubscriptionApiProvider getInstance() {
        if (instance == null) {
            instance = new SubscriptionApiProvider();
        }
        return instance;
    }

    public void saveSubscription(Subscription subscription, Activity activity) {
        Call<Subscription> call = createApi(SubscriptionApi.class).saveSubscription(subscription);
        handleSaveResponse(call, activity);
    }

    void handleSaveResponse(Call<Subscription> call, Activity activity) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Subscription> call, final Response<Subscription> response) {
                if (!response.isSuccessful()) {
                    showToast(activity, activity.getString(error) + response.message());
                }
            }

            @Override
            public void onFailure(final Call<Subscription> call, final Throwable t) {
                Log.d(TAG, t.toString());
                showToast(activity, activity.getString(server_unavailable));
            }
        });
    }
}
