package com.example.coffeebase;

import android.widget.TextView;
import android.widget.Toast;
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

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MyCoffeeBase extends AppCompatActivity {

    private GridLayoutManager gridLayoutManager;
    private CoffeeBaseApi coffeeBaseApi;
    private CoffeeRecViewAdapter adapter;
    private RecyclerView coffeeRecView;
    private ArrayList<Coffee> coffees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coffee_base);

        coffeeRecView = (RecyclerView) findViewById(R.id.coffeeRecView);
        gridLayoutManager = new GridLayoutManager(this, 2);
        coffeeRecView.setLayoutManager(gridLayoutManager);

        getCoffees();
    }

    public void getCoffees() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.67:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeBaseApi = retrofit.create(CoffeeBaseApi.class);

        Call<List<Coffee>> call = coffeeBaseApi.getCoffees();
        call.enqueue(new Callback<List<Coffee>>() {
            @Override
            public void onResponse(Call<List<Coffee>> call, Response<List<Coffee>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyCoffeeBase.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                coffees = new ArrayList<>(response.body());
                adapter = new CoffeeRecViewAdapter(MyCoffeeBase.this, coffees);
                coffeeRecView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Coffee>> call, Throwable t) {
                Toast.makeText(MyCoffeeBase.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}