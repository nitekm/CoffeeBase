package com.ncodedev.coffeebase.web.api;

import com.ncodedev.coffeebase.model.security.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SecurityApi {

    @POST("authenticate")
    Call<Token> authenticate(@Body Token token);
}
