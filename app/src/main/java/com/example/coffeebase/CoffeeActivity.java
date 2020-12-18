package com.example.coffeebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;

public class CoffeeActivity extends AppCompatActivity {

    CoffeeBaseApi coffeeBaseApi;
    public static final String COFFEE_ID_KEY = "coffeeId";
    private ImageView imgCoffee;
    private TextView coffeeNameTxt, originTxt, roasterTxt, ratingTxt, txtCoffeeName, txtOrigin, txtRoaster, txtRating;
    private Button addToFavButton;
    private Coffee coffee;
    private FloatingActionButton deleteActionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);

        initViews();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.67:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeBaseApi = retrofit.create(CoffeeBaseApi.class);

        Intent intent = getIntent();
        if(null != intent) {
            int coffeeId = intent.getIntExtra(COFFEE_ID_KEY, -1);
            if (coffeeId != -1) {
                getSingleCoffee(coffeeId);
                //addToFavourites(coffee);
                deleteCoffee(coffeeId);
            }
        }
    }

    public void initViews() {
        imgCoffee = findViewById(R.id.imgCoffee);
        coffeeNameTxt = findViewById(R.id.coffeeNameTxt);
        originTxt = findViewById(R.id.originTxt);
        roasterTxt = findViewById(R.id.roasterTxt);
        ratingTxt = findViewById(R.id.ratingTxt);
        txtCoffeeName = findViewById(R.id.txtCoffeeName);
        txtOrigin = findViewById(R.id.txtOrigin);
        txtRoaster = findViewById(R.id.txtRoaster);
        txtRating = findViewById(R.id.txtRating);
        addToFavButton = findViewById(R.id.addToFavouritesBtn);
        deleteActionBtn = findViewById(R.id.deleteActionBtn);

    }

    public void getSingleCoffee(int id) {

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
                txtRoaster.setText(coffee.getRoaster());
                txtRating.setText("" + coffee.getRating());
                setImage();
            }

            @Override
            public void onFailure(Call<Coffee> call, Throwable t) {
                Toast.makeText(CoffeeActivity.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteCoffee(int id) {
        deleteActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CoffeeActivity.this);
                builder.setTitle("Delete this coffee?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> call = coffeeBaseApi.deleteCoffee(id);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(CoffeeActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(CoffeeActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CoffeeActivity.this, MyCoffeeBase.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(CoffeeActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.create().show();
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
    public void setImage() {
        {
            File file = new File(coffee.getImageUri());
            if(file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imgCoffee.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MyCoffeeBase.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
