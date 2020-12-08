package com.example.coffeebase;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoffeeActivity extends AppCompatActivity {

    private ImageView imgCoffee;
    private TextView coffeeNameTxt, originTxt, txtCoffeeName, txtOrigin;
    private Button addToFavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);

        initViews();
    }

    public void initViews() {
        imgCoffee = findViewById(R.id.imgCoffee);
        coffeeNameTxt = findViewById(R.id.coffeeNameTxt);
        originTxt = findViewById(R.id.originTxt);
        txtCoffeeName = findViewById(R.id.txtCoffeeName);
        txtOrigin = findViewById(R.id.txtOrigin);
        addToFavButton = findViewById(R.id.addToFavouritesBtn);
    }
}