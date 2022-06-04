package com.ncode.coffeebase.client.api;

import com.ncode.coffeebase.model.security.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SecurityApi {

    @POST("authenticate")
    Call<Token> authenticate(@Body Token token);
}
