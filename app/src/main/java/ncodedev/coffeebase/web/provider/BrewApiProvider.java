package ncodedev.coffeebase.web.provider;

import android.util.Log;
import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.model.domain.process.BrewActionDTO;
import ncodedev.coffeebase.web.api.BrewApi;
import ncodedev.coffeebase.web.listener.BrewListResponseListener;
import ncodedev.coffeebase.web.listener.BrewResponseListener;
import ncodedev.coffeebase.web.listener.BrewStepResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static ncodedev.coffeebase.web.provider.RetrofitApiCreator.createApi;

public class BrewApiProvider extends ApiProvider {

    private final String TAG = this.getClass().getSimpleName();

    private static BrewApiProvider instance;

    public static BrewApiProvider getInstance() {
        if (instance == null) {
            instance = new BrewApiProvider();
        }
        return instance;
    }

    public void getAll(BrewListResponseListener listener) {
        Call<List<Brew>> call = createApi(BrewApi.class).getAll();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<List<Brew>> call, final Response<List<Brew>> response) {
                if (response.isSuccessful()) {
                    listener.handleGetList(response.body());
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<List<Brew>> call, final Throwable t) {
                Log.d(TAG, t.toString());
                handleCallFailedAndRetry(listener, t, () -> getAll(listener));
            }
        });
    }

    public void init(Brew brew, BrewStepResponseListener listener) {
        Call<Brew> call = createApi(BrewApi.class).init(brew);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Brew> call, Response<Brew> response) {
                if (response.isSuccessful()) {
                    listener.handleInitBrewStepResponse(response.body());
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(Call<Brew> call, Throwable t) {
                handleCallFailedAndRetry(listener, t, () -> init(brew, listener));
            }
        });
    }

    public void finish(Brew brew, BrewStepResponseListener listener) {
        Call<Brew> call = createApi(BrewApi.class).finish(brew);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Brew> call, Response<Brew> response) {
                if (response.isSuccessful()) {
                    listener.handleFinishBrewStepResponse(response.body());
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(Call<Brew> call, Throwable t) {
                handleCallFailedAndRetry(listener, t, () -> finish(brew, listener));
            }
        });
    }

    public void executeAction(BrewActionDTO brewActionDTO, BrewResponseListener listener) {
        Call<Void> call = createApi(BrewApi.class).executeAction(brewActionDTO);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.handleExecuteActionResult();
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
                handleCallFailedAndRetry(listener, t, () -> executeAction(brewActionDTO, listener));
            }
        });
    }
}
