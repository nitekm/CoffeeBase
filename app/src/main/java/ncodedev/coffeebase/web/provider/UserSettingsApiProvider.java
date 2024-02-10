package ncodedev.coffeebase.web.provider;

import ncodedev.coffeebase.web.api.UserSettingsApi;
import ncodedev.coffeebase.web.listener.UserSettingsResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ncodedev.coffeebase.web.provider.RetrofitApiCreator.createApi;

public class UserSettingsApiProvider extends ApiProvider {
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
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.handleDeleteAccount();
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
           handleCallFailedAndRetry(listener, t, () -> deleteAccount(listener));
            }
        });
    }
}
