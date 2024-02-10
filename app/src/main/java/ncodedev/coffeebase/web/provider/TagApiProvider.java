package ncodedev.coffeebase.web.provider;

import ncodedev.coffeebase.model.domain.Tag;
import ncodedev.coffeebase.web.api.TagApi;
import ncodedev.coffeebase.web.listener.TagListResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static ncodedev.coffeebase.web.provider.RetrofitApiCreator.createApi;

public class TagApiProvider extends ApiProvider {

    public static final String TAG = "TagApiProvider";

    private static TagApiProvider instance;

    public static TagApiProvider getInstance() {
        if (instance == null) {
            instance = new TagApiProvider();
        }
        return instance;
    }

    public void getAll(TagListResponseListener listener) {
        Call<List<Tag>> call = createApi(TagApi.class).getTags();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<List<Tag>> call, final Response<List<Tag>> response) {
                if (response.isSuccessful()) {
                    listener.handleGetList(response.body());
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<List<Tag>> call, final Throwable t) {
                handleCallFailedAndRetry(listener, t, () -> getAll(listener));
            }
        });
    }

    public void search(String content, TagListResponseListener listener) {
        Call<List<Tag>> call = createApi(TagApi.class).searchTags(content);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(final Call<List<Tag>> call, final Response<List<Tag>> response) {
                if (response.isSuccessful()) {
                    listener.handleSearchResult(response.body());
                } else {
                    handleErrorResponse(response, listener);
                }
            }

            @Override
            public void onFailure(final Call<List<Tag>> call, final Throwable t) {
                handleCallFailedAndRetry(listener, t, () -> search(content, listener));
            }
        });
    }
}
