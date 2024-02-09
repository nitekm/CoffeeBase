package ncodedev.coffeebase.web.provider;

import com.google.gson.Gson;
import ncodedev.coffeebase.model.error.ErrorResponse;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;

public abstract class ApiProvider {
    protected ErrorResponse handleErrorResponse(Response response) {

        ErrorResponse errorResponse = null;
        try (ResponseBody responseBody = response.errorBody()) {
            Gson gson = new Gson();
            errorResponse = gson.fromJson(responseBody.string(), ErrorResponse.class);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return errorResponse;
    }
}
