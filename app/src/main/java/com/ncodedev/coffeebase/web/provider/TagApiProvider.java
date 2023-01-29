package com.ncodedev.coffeebase.web.provider;

import android.app.Activity;
import com.ncodedev.coffeebase.model.domain.Tag;
import com.ncodedev.coffeebase.web.api.TagApi;
import com.ncodedev.coffeebase.web.listener.TagListResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static com.ncodedev.coffeebase.utils.Logger.logCall;
import static com.ncodedev.coffeebase.utils.Logger.logCallFail;
import static com.ncodedev.coffeebase.utils.ToastUtils.showToast;
import static com.ncodedev.coffeebase.web.provider.RetrofitApiCreator.createApi;

public class TagApiProvider {

    public static final String TAG = "TagApiProvider";

    private static TagApiProvider instance;

    public static TagApiProvider getInstance() {
        if (instance == null) {
            instance = new TagApiProvider();
        }
        return instance;
    }

    public void getAll(TagListResponseListener listener, Activity activity) {
        Call<List<Tag>> call = createApi(TagApi.class).getTags();
        handleListResponse(call, listener, activity);
    }

    public void search(String content, TagListResponseListener listener, Activity activity) {
        Call<List<Tag>> call = createApi(TagApi.class).searchTags(content);
        handleSearchResponse(call, listener, activity);
    }

    private void handleListResponse(Call<List<Tag>> call, TagListResponseListener listener, Activity activity) {
        logCall(TAG, call);
        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(final Call<List<Tag>> call, final Response<List<Tag>> response) {
                if (response.isSuccessful()) {
                    listener.handleGetList(response.body());
                } else {
                    showToast(activity, "Error!" + response.message());
                }
            }

            @Override
            public void onFailure(final Call<List<Tag>> call, final Throwable t) {
                logCallFail(TAG, call);
                showToast(activity, "Server is unavailable. Try again later");
            }
        });
    }

    private void handleSearchResponse(Call<List<Tag>> call, TagListResponseListener listener, Activity activity) {
        logCall(TAG, call);
        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(final Call<List<Tag>> call, final Response<List<Tag>> response) {
                if (response.isSuccessful()) {
                    listener.handleSearchResult(response.body());
                } else {
                    showToast(activity, "Error!" + response.message());
                }
            }

            @Override
            public void onFailure(final Call<List<Tag>> call, final Throwable t) {
                logCallFail(TAG, call);
                showToast(activity, "Server is unavailable. Try again later");
            }
        });
    }
}
