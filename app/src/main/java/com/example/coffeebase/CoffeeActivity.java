package com.example.coffeebase;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoffeeActivity extends AppCompatActivity {

    CoffeeBaseApi coffeeBaseApi;
    public static final String COFFEE_ID_KEY = "coffeeId";
    private ImageView imgCoffee;
    private TextView coffeeNameTxt, originTxt, txtCoffeeName, txtOrigin;
    private Button addToFavButton;
    private Coffee coffee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);

        initViews();

        Intent intent = getIntent();
        if(null != intent) {
            int coffeeId = intent.getIntExtra(COFFEE_ID_KEY, -1);
            if (coffeeId != -1) {
                getSingleCoffee(coffeeId);

                //addToFavourites(coffee);
            }
        }
    }

    public void initViews() {
        imgCoffee = findViewById(R.id.imgCoffee);
        coffeeNameTxt = findViewById(R.id.coffeeNameTxt);
        originTxt = findViewById(R.id.originTxt);
        txtCoffeeName = findViewById(R.id.txtCoffeeName);
        txtOrigin = findViewById(R.id.txtOrigin);
        addToFavButton = findViewById(R.id.addToFavouritesBtn);
    }

    public void getSingleCoffee(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.67:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeBaseApi = retrofit.create(CoffeeBaseApi.class);

        Call<Coffee> call = coffeeBaseApi.getSingleCoffee(id);
        call.enqueue(new Callback<Coffee>() {
            @Override
            public void onResponse(Call<Coffee> call, Response<Coffee> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CoffeeActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                coffee = response.body();
                txtCoffeeName.setText(coffee.getName());
                txtOrigin.setText(coffee.getOrigin());
            }

            @Override
            public void onFailure(Call<Coffee> call, Throwable t) {
                Toast.makeText(CoffeeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    public void addToFavourites(Coffee coffee) {
        addToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CoffeeActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                coffeeBaseApi.addToFavourites(coffee);
            }
        });
    }

     */
}
