package ncodedev.coffeebase.web.provider;

import ncodedev.coffeebase.model.domain.Coffee;
import ncodedev.coffeebase.model.enums.RequestContext;
import ncodedev.coffeebase.model.utils.Page;
import ncodedev.coffeebase.model.utils.PageCoffeeRequest;
import ncodedev.coffeebase.web.api.CoffeeApi;
import ncodedev.coffeebase.web.listener.CoffeeListResponseListener;
import ncodedev.coffeebase.web.listener.CoffeeResponseListener;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static ncodedev.coffeebase.web.provider.RetrofitApiCreator.createApi;

public class CoffeeApiProvider extends ApiProvider{

    public static final String TAG = "CoffeeApiProvider";

    private static CoffeeApiProvider instance;

    public static CoffeeApiProvider getInstance() {
        if (instance == null) {
            instance = new CoffeeApiProvider();
        }
        return instance;
    }

    public void getAllPaged(CoffeeListResponseListener listener, PageCoffeeRequest request, RequestContext requestContext) {
        Call<Page<Coffee>> call = createApi(CoffeeApi.class).getCoffeesPaged(request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Page<Coffee>> call, final Response<Page<Coffee>> response) {
                if (response.isSuccessful()) {
                    if (requestContext == RequestContext.GET_ALL) {
                        listener.handleGetAllPage(response.body());
                    }
                    if (requestContext == RequestContext.SORT) {
                        listener.handleSortPage(response.body());
                    }
                    if (requestContext == RequestContext.FILTER) {
                        listener.handleFilterPage(response.body());
                    }
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<Page<Coffee>> call, final Throwable t) {
                handleCallFailedAndRetry(listener, t, () -> getAllPaged(listener, request, requestContext));
            }
        });
    }

    public void search(String content, CoffeeListResponseListener listener) {
        Call<List<Coffee>> call = createApi(CoffeeApi.class).searchCoffees(content);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<List<Coffee>> call, final Response<List<Coffee>> response) {
                if (response.isSuccessful()) {
                    listener.handleGetAll(response.body());
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<List<Coffee>> call, final Throwable t) {
                handleCallFailedAndRetry(listener, t, () -> search(content, listener));
            }
        });
    }

    public void getOne(long id, CoffeeResponseListener listener) {
        Call<Coffee> call = createApi(CoffeeApi.class).getSingleCoffee(id);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                if (response.isSuccessful()) {
                    listener.handleCoffeeResponse(response.body());
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                handleCallFailedAndRetry(listener, t, () -> getOne(id, listener));
            }
        });
    }

    public void save(Coffee coffee, MultipartBody.Part image, CoffeeResponseListener listener) {
        Call<Coffee> call = createApi(CoffeeApi.class).createCoffee(coffee, image);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                if (response.isSuccessful()) {
                    listener.handleSaveResponse(response.body());
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                listener.handleCallFailed();
            }
        });
    }

    public void update(long id, Coffee coffee, MultipartBody.Part image, CoffeeResponseListener listener) {
        Call<Coffee> call = createApi(CoffeeApi.class).updateCoffee(id, coffee, image);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                if (response.isSuccessful()) {
                    listener.handleSaveResponse(response.body());
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                listener.handleCallFailed();
            }
        });
    }

    public void delete(long id, CoffeeResponseListener listener) {
        Call<Void> call = createApi(CoffeeApi.class).deleteCoffee(id);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.handleDeleteResponse();
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {
                handleCallFailedAndRetry(listener, t, () -> delete(id, listener));
            }
        });
    }

    public void switchFavourites(long coffeeId, CoffeeResponseListener listener) {
        Call<Coffee> call = createApi(CoffeeApi.class).switchFavourite(coffeeId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<Coffee> call, final Response<Coffee> response) {
                if (response.isSuccessful()) {
                    listener.handleCoffeeResponse(response.body());
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<Coffee> call, final Throwable t) {
                handleCallFailedAndRetry(listener, t, () -> switchFavourites(coffeeId, listener));
            }
        });
    }
}
