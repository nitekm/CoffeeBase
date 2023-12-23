package ncodedev.coffeebase.web.provider;

import android.util.Log;
import ncodedev.coffeebase.model.domain.Subscription;
import ncodedev.coffeebase.web.api.SubscriptionApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void saveSubscription(Subscription subscription) {
        Call<Subscription> call = createApi(SubscriptionApi.class).saveSubscription(subscription);
        handleSaveResponse(call);
    }

    void handleSaveResponse(Call<Subscription> call) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Subscription> call, final Response<Subscription> response) {
                if (!response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(final Call<Subscription> call, final Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }
}
