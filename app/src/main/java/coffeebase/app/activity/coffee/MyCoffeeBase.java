package coffeebase.app.activity.coffee;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import coffeebase.app.activity.BuildConfig;
import coffeebase.app.activity.MainActivity;
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

public class MyCoffeeBase extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private GridLayoutManager gridLayoutManager;
    private CoffeeApi coffeeApi;
    private CoffeeRecViewAdapter adapter;
    private RecyclerView coffeeRecView;
    private Spinner sortSpinner;
    private ArrayList<Coffee> coffees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coffee_base);

        coffeeRecView = findViewById(R.id.coffeeRecView);
        sortSpinner = findViewById(R.id.sortSpinner);
        gridLayoutManager = new GridLayoutManager(this, 2);
        coffeeRecView.setLayoutManager(gridLayoutManager);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeApi = retrofit.create(CoffeeApi.class);

        String sortOption = parent.getItemAtPosition(position).toString();

        Call<List<Coffee>> call = coffeeApi.getCoffees();

        if (sortOption.equals("Rating asc")) {
            call = coffeeApi.getSortedByRatingAsc();
        }
        if (sortOption.equals("Rating desc")) {
           call = coffeeApi.getSortedByRatingDesc();
        }
        if (sortOption.equals("A to Z")) {
           call = coffeeApi.getSortedByNameAsc();
        }
        if (sortOption.equals("Z to A")) {
            call = coffeeApi.getSortedByNameDesc();
        }
        if (sortOption.equals("Default")) {
            call = coffeeApi.getCoffees();
        }

        call.enqueue(new Callback<List<Coffee>>() {
            @Override
            public void onResponse(Call<List<Coffee>> call, Response<List<Coffee>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyCoffeeBase.this, "Code: " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                coffees = new ArrayList<>(response.body());
                adapter = new CoffeeRecViewAdapter(MyCoffeeBase.this, coffees);
                coffeeRecView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Coffee>> call, Throwable t) {
                Toast.makeText(MyCoffeeBase.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}