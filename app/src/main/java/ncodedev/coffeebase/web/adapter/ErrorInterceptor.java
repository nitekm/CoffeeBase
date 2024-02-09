package ncodedev.coffeebase.web.adapter;

import com.google.gson.Gson;
import ncodedev.coffeebase.model.error.ErrorResponse;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ErrorInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        // We got a HTTP error
        if (!response.isSuccessful()) {

            Gson gson = new Gson();
            ErrorResponse errorResponse = gson.fromJson(response.body().charStream(), ErrorResponse.class);
            // Parse error response

        }
        return response;
    }
}
