package ncodedev.coffeebase.web.api;

import ncodedev.coffeebase.model.domain.Brew;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface BrewApi extends Api {

    @GET("brews")
    Call<List<Brew>> getAll();

}
