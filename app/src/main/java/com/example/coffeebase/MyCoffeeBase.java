package com.example.coffeebase;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class MyCoffeeBase extends AppCompatActivity {

    private GridLayoutManager gridLayoutManager;
    private TextView txtView;
    private CoffeeBaseApi coffeeBaseApi;
    private CoffeeRecViewAdapter adapter;
    private RecyclerView coffeeRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coffee_base);

        adapter = new CoffeeRecViewAdapter(this);
        coffeeRecView = findViewById(R.id.coffeeRecView);
        //gridLayoutManager = new GridLayoutManager(this, 2);
        //coffeeRecView.setLayoutManager(gridLayoutManager);
        coffeeRecView.setAdapter(adapter);

        txtView = findViewById(R.id.textView);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeBaseApi = retrofit.create(CoffeeBaseApi.class);
        getCoffees();


    }

    public void getCoffees() {
        Call<List<Coffee>> call = coffeeBaseApi.getCoffees();
        call.enqueue(new Callback<List<Coffee>>() {
            @Override
            public void onResponse(Call<List<Coffee>> call, Response<List<Coffee>> response) {
                if (!response.isSuccessful()) {
                    txtView.setText("Code " + response.code());
                    return;
                }
                List<Coffee> coffees = response.body();
                for (Coffee coffee:coffees) {
                    String content = "";
                    content += "id " + coffee.getId() + "\n";
                    content += "name " + coffee.getName() + "\n";
                    content += "origin " + coffee.getOrigin() + "\n";

                    txtView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Coffee>> call, Throwable t) {
                txtView.setText(t.getMessage());
            }
        });
    }

    public void addCoffee() {

    }

}