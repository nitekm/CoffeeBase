package com.ncodedev.coffeebase.web.api;

import com.ncodedev.coffeebase.model.domain.Tag;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface TagApi extends Api {
    @GET("tags/search")
    Call<List<Tag>> searchTags(@Query("name") String name);

    @GET("tags")
    Call<List<Tag>> getTags();
}
