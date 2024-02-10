package ncodedev.coffeebase.web.provider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.gson.Gson;
import ncodedev.coffeebase.model.error.ErrorResponse;
import ncodedev.coffeebase.web.listener.ResponseListener;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;

public abstract class ApiProvider {
    private static final String TAG = "ApiProvider";
    protected void handleErrorResponse(Response response, ResponseListener responseListener) {

        ErrorResponse errorResponse = null;
        try (ResponseBody responseBody = response.errorBody()) {
            Gson gson = new Gson();
            errorResponse = gson.fromJson(responseBody.string(), ErrorResponse.class);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        responseListener.handleResponseError(errorResponse);
    }

    protected void handleCallFailedAndRetry(ResponseListener responseListener, Throwable t, Runnable apiCall) {
        responseListener.handleCallFailed();
        Log.e(TAG, "API call failed! \n" + t);
        Log.d(TAG, "Retrying call");
        new Handler(Looper.getMainLooper()).postDelayed(apiCall, 20000);
    }

}
