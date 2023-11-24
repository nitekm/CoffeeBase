package ncodedev.coffeebase.web.api;

import ncodedev.coffeebase.model.domain.Brew;
import ncodedev.coffeebase.model.domain.process.BrewActionDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

import java.util.List;

public interface BrewApi extends Api {

    @GET("brews")
    Call<List<Brew>> getAll();
    @POST("brews/step/init")
    Call<Brew> init(@Body Brew brew);
    @POST("brews/step/finish")
    Call<Brew> finish(@Body Brew brew);
    @PATCH("brews/execute-action")
    Call<Void> executeAction(@Body BrewActionDTO brewActionDTO);

}
