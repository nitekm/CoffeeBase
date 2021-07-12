package coffeebase.app.api;

import coffeebase.app.model.CoffeeGroup;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface GroupApi {

    @GET("groups")
    Call<List<CoffeeGroup>> getGroups();

    @GET("groups/{id}")
    Call<CoffeeGroup> getSingleGroup(@Path("id") int id);

    @POST("groups")
    CoffeeGroup createGroup(@Body CoffeeGroup coffeeGroup);

    @DELETE("groups/{id}")
    Void deleteGroup(@Path("id") int id);
}
