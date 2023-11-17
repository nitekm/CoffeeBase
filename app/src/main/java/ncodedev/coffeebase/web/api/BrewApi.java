package ncodedev.coffeebase.web.api;

import ncodedev.coffeebase.model.domain.Brew;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface BrewApi extends Api {

    @GET("brews")
    Call<List<Brew>> getAll();

    @POST("brews/step/init")
    Call<Brew> init(@Body Brew brew);

    @POST("brews/step/finish")
    Call<Brew> finish(@Body Brew brew);

    @PATCH("brews/detach/{brewId}/{coffeeId}")
    Call<Void> detachBrewFromCoffee(@Path("brewId") Long brewId,
                                    @Path("coffeeId") Integer coffeeId);

}
