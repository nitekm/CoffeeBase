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

public class BrewApiProvider {

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
        handleListResponse(call, listener);
    }

    public void init(Brew brew, BrewStepResponseListener listener) {
        Call<Brew> call = createApi(BrewApi.class).init(brew);
        handleInitBrewStepResponse(call, listener);
    }

    public void finish(Brew brew, BrewStepResponseListener listener) {
        Call<Brew> call = createApi(BrewApi.class).finish(brew);
        handleFinishBrewStepResponse(call, listener);
    }

    public void executeAction(BrewActionDTO brewActionDTO, BrewResponseListener listener) {
        Call<Void> call = createApi(BrewApi.class).executeAction(brewActionDTO);
        handleVoidResponse(call, listener);
    }

    private void handleListResponse(Call<List<Brew>> call, BrewListResponseListener listener) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<List<Brew>> call, final Response<List<Brew>> response) {
                if (response.isSuccessful()) {
                    listener.handleGetList(response.body());
                } else {
                    listener.handleError();
                }
            }

            @Override
            public void onFailure(final Call<List<Brew>> call, final Throwable t) {
                Log.d(TAG, t.toString());
                listener.handleError();
            }
        });
    }

    private void handleInitBrewStepResponse(Call<Brew> call, BrewStepResponseListener listener) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Brew> call, Response<Brew> response) {
                if (response.isSuccessful()) {
                    listener.handleInitBrewStepResponse(response.body());
                } else {
                    listener.handleError();
                }
            }

            @Override
            public void onFailure(Call<Brew> call, Throwable t) {
                Log.i(TAG, t.toString());
                listener.handleError();
            }
        });
    }

    private void handleFinishBrewStepResponse(Call<Brew> call, BrewStepResponseListener listener) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Brew> call, Response<Brew> response) {
                if (response.isSuccessful()) {
                    listener.handleFinishBrewStepResponse(response.body());
                } else {
                    listener.handleError();
                }
            }

            @Override
            public void onFailure(Call<Brew> call, Throwable t) {
                Log.i(TAG, t.toString());
                listener.handleError();
            }
        });
    }

    private void handleVoidResponse(Call<Void> call, BrewResponseListener listener) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.handleExecuteActionResult();
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
