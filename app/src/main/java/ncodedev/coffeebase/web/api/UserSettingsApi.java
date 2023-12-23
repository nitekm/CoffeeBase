package ncodedev.coffeebase.web.api;

import retrofit2.Call;
import retrofit2.http.DELETE;

public interface UserSettingsApi extends Api {

    @DELETE("user-settings/delete-account")
    Call<Void> deleteAccount();
}
