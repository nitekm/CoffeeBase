package com.ncodedev.coffeebase.client.api;

import com.ncodedev.coffeebase.model.domain.Tag;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface TagApi {
    @GET("tags/search")
    Call<List<Tag>> searchTags(@Query("name") String name);
}
