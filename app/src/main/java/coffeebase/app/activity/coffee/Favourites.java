
package coffeebase.app.activity.coffee;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import coffeebase.app.activity.BuildConfig;
import coffeebase.app.activity.R;
import coffeebase.app.api.CoffeeApi;
import coffeebase.app.model.Coffee;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

public class Favourites extends AppCompatActivity {

    private GridLayoutManager gridLayoutManager;
    private CoffeeRecViewAdapter adapter;
    private RecyclerView favRecView;
    private CoffeeApi coffeeApi;
    private ArrayList<Coffee> favouriteCoffees = new ArrayList<>();
    private ArrayList<Coffee> coffees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        favRecView = findViewById(R.id.favRecView);
        gridLayoutManager = new GridLayoutManager(this, 2);
        favRecView.setLayoutManager(gridLayoutManager);

        getFavouriteCoffees();
    }

    private void getFavouriteCoffees() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeApi = retrofit.create(CoffeeApi.class);

        Call<List<Coffee>> call = coffeeApi.getCoffees();
        call.enqueue(new Callback<List<Coffee>>() {
            @Override
            public void onResponse(Call<List<Coffee>> call, Response<List<Coffee>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Favourites.this, "Code: " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                coffees = new ArrayList<>(response.body());
                for(Coffee c:coffees) {
                    System.out.println(c.getName() + " " + c.isFavourite());
                    if(c.isFavourite() == true) favouriteCoffees.add(c);
                }
                adapter = new CoffeeRecViewAdapter(Favourites.this, favouriteCoffees);
                favRecView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Coffee>> call, Throwable t) {
                Toast.makeText(Favourites.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

