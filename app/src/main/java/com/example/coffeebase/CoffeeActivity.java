package com.example.coffeebase;

import android.content.DialogInterface;
import android.content.Intent;
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

public class CoffeeActivity extends AppCompatActivity {

    CoffeeApi coffeeApi;
    public static final String COFFEE_ID_KEY = "coffeeId";
    private ImageView imgCoffee;
    private TextView txtCoffeeName, txtOrigin, txtRoaster, txtRating;
    private Button addToFavButton;
    private Coffee coffee;
    private FloatingActionButton deleteActionBtn, editActionBtn;
    private int coffeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);

        initViews();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeApi = retrofit.create(CoffeeApi.class);

        Intent intent = getIntent();
        if(null != intent) {
            coffeeId = intent.getIntExtra(COFFEE_ID_KEY, -1);
            if (coffeeId != -1) {
                getSingleCoffee(coffeeId);
                addToFavourites(coffeeId);
                deleteCoffee(coffeeId);
                editCoffee();
            }
        }
    }

    public void initViews() {
        imgCoffee = findViewById(R.id.imgCoffee);
        txtCoffeeName = findViewById(R.id.txtCoffeeName);
        txtOrigin = findViewById(R.id.txtOrigin);
        txtRoaster = findViewById(R.id.txtRoaster);
        txtRating = findViewById(R.id.txtRating);
        addToFavButton = findViewById(R.id.addToFavouritesBtn);
        deleteActionBtn = findViewById(R.id.deleteActionBtn);
        editActionBtn = findViewById(R.id.editActionBtn);

    }

    public void getSingleCoffee(int id) {
        Call<Coffee> call = coffeeApi.getSingleCoffee(id);
        call.enqueue(new Callback<Coffee>() {
            @Override
            public void onResponse(Call<Coffee> call, Response<Coffee> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CoffeeActivity.this, "Code: " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                coffee = response.body();
                txtCoffeeName.setText(coffee.getName());
                txtOrigin.setText(coffee.getOrigin());
                txtRoaster.setText(coffee.getRoaster());
                txtRating.setText(coffee.getRating());
                Glide.with(CoffeeActivity.this)
                        .asBitmap().load(coffee.getImageUrl())
                        .into(imgCoffee);

                if (coffee.isFavourite() == true) {
                    addToFavButton.setText("Remove from Favourites");
                }
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
                            Call<Void> call = coffeeApi.deleteCoffee(id);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (!response.isSuccessful()) {
                                        Toast.makeText(CoffeeActivity.this, response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
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

    public void addToFavourites(int id) {
        addToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> call = coffeeApi.switchFavourite(id);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(CoffeeActivity.this, "Code: " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //refresh activity to see button text change
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        Toast.makeText(CoffeeActivity.this,"Favourite status changed", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CoffeeActivity.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void editCoffee() {
        editActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CoffeeActivity.this);
                builder.setTitle("Edit this coffee?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        Intent intent = new Intent(CoffeeActivity.this, EditCoffee.class);
                        intent.putExtra(COFFEE_ID_KEY, coffeeId);
                        startActivity(intent);
                    }
                });
                builder.create().show();
            }
        });
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MyCoffeeBase.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
