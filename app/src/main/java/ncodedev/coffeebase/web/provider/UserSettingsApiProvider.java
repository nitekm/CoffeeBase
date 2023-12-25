package ncodedev.coffeebase.web.provider;

import android.util.Log;
import ncodedev.coffeebase.web.api.UserSettingsApi;
import ncodedev.coffeebase.web.listener.UserSettingsResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ncodedev.coffeebase.web.provider.RetrofitApiCreator.createApi;

public class UserSettingsApiProvider {
    public static final String TAG = "UserSettingsApiProvider";

    private static UserSettingsApiProvider instance;

    public static UserSettingsApiProvider getInstance() {
        if (instance == null) {
            instance = new UserSettingsApiProvider();
        }
        return instance;
    }

    public void deleteAccount(UserSettingsResponseListener listener) {

        Call<Void> call = createApi(UserSettingsApi.class).deleteAccount();
        handleDeleteResponse(call, listener);
    }

    private void handleDeleteResponse(Call<Void> call, UserSettingsResponseListener listener) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.handleDeleteAccount();
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
