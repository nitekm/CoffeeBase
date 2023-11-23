package ncodedev.coffeebase.web.api;

import ncodedev.coffeebase.model.domain.Coffee;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface CoffeeApi extends Api {

    @GET("coffees")
    Call<List<Coffee>> getCoffees();

    @GET("coffees/search")
    Call<List<Coffee>> searchCoffees(@Query("content") String content);

    @GET("coffees/{id}")
    Call<Coffee> getSingleCoffee(@Path("id") long id);

    @Multipart
    @POST("coffees")
    Call<Coffee> createCoffee(@Part("coffee") Coffee coffee,
                              @Part MultipartBody.Part image);

    @Multipart
    @PUT("coffees/{id}")
    Call<Coffee> updateCoffee(@Path("id") long id,
                              @Part("coffee") Coffee coffeeToUpdate,
                              @Part MultipartBody.Part image);

    @PATCH("coffees/{id}")
    Call<Coffee> switchFavourite(@Path("id") long id);

    @DELETE("coffees/{id}")
    Call<Void> deleteCoffee(@Path("id") long id);

}
