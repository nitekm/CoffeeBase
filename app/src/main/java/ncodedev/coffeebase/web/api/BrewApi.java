package ncodedev.coffeebase.web.api;

import ncodedev.coffeebase.model.domain.Brew;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;

import java.util.List;

public interface BrewApi extends Api {

    @GET("brews")
    Call<List<Brew>> getAll();

    @PATCH("brews/detach/{brewId}/{coffeeId}")
    Call<Void> detachBrewFromCoffee(Long brewId, Integer coffeeId);

}
