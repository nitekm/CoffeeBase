package ncodedev.coffeebase.web.api;

import ncodedev.coffeebase.model.domain.Subscription;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SubscriptionApi extends Api {
    @POST("/subscription/save")
    Call<Subscription> saveSubscription(@Body Subscription subscription);
}
