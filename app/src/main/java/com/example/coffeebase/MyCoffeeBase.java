package com.example.coffeebase;

import android.content.Intent;
import android.view.View;
import android.widget.*;
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

public class MyCoffeeBase extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private GridLayoutManager gridLayoutManager;
    private CoffeeBaseApi coffeeBaseApi;
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
                Toast.makeText(MyCoffeeBase.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.67:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeBaseApi = retrofit.create(CoffeeBaseApi.class);

        String sortOption = parent.getItemAtPosition(position).toString();

        if (sortOption.equals("Rating asc")) {
            Call<List<Coffee>> call = coffeeBaseApi.getSortedByRatingAsc();
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
                    Toast.makeText(MyCoffeeBase.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }
        if (sortOption.equals("Rating desc")) {
            Call<List<Coffee>> call = coffeeBaseApi.getSortedByRatingDesc();
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
                    Toast.makeText(MyCoffeeBase.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (sortOption.equals("A to Z")) {
            Call<List<Coffee>> call = coffeeBaseApi.getSortedByNameAsc();
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
                    Toast.makeText(MyCoffeeBase.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (sortOption.equals("Z to A")) {
            Call<List<Coffee>> call = coffeeBaseApi.getSortedByNameDesc();
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
                    Toast.makeText(MyCoffeeBase.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (sortOption.equals("Default")) {
            getCoffees();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}