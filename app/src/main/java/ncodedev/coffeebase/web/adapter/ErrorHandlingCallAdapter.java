package ncodedev.coffeebase.web.adapter;

import com.google.gson.Gson;
import ncodedev.coffeebase.model.error.ErrorResponse;
import ncodedev.coffeebase.web.listener.CoffeeResponseListener;
import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.lang.reflect.Type;

public class ErrorHandlingCallAdapter<T> implements CallAdapter<T, Call<T>> {
    private final Type responseType;
    private final CoffeeResponseListener listener;

    public ErrorHandlingCallAdapter(Type responseType, CoffeeResponseListener listener) {
        this.responseType = responseType;
        this.listener = listener;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public Call<T> adapt(Call<T> call) {
        return new ErrorHandlingCall<>(call, listener);
    }

    private static class ErrorHandlingCall<T> implements Call<T> {
        private final Call<T> delegate;
        private final CoffeeResponseListener listener;

        ErrorHandlingCall(Call<T> delegate, CoffeeResponseListener listener) {
            this.delegate = delegate;
            this.listener = listener;
        }

        @Override
        public void enqueue(Callback<T> callback) {
            delegate.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    if (response.isSuccessful()) {
                        callback.onResponse(call, response);
                    } else {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ErrorResponse.class);
                        listener.handleError(errorResponse);
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    callback.onFailure(call, t);
                }
            });
        }

        @Override
        public Response<T> execute() throws IOException {
            return delegate.execute();
        }

        @Override
        public boolean isExecuted() {
            return delegate.isExecuted();
        }

        @Override
        public void cancel() {
            delegate.cancel();
        }

        @Override
        public boolean isCanceled() {
            return delegate.isCanceled();
        }

        @Override
        public Call<T> clone() {
            return new ErrorHandlingCall<>(delegate.clone(), listener);
        }

        @Override
        public Request request() {
            return delegate.request();
        }

        @Override
        public Timeout timeout() {
            return delegate.timeout();
        }
    }
}
