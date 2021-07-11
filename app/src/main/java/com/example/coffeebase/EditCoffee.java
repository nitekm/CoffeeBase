package com.example.coffeebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditCoffee extends AppCompatActivity {

    public static final String COFFEE_ID_KEY = "coffeeId";
    private Coffee coffee;
    private String name, origin, roaster, rating, imageUrl;
    private CoffeeApi coffeeApi;
    private Button loadImgBtn, saveChangesBtn;
    private ImageView imgAddCoffee;
    private EditText txtAddCoffeeName, txtAddOrigin, txtRoaster, txtPicUrl;
    private RadioGroup ratingRadioGroup;
    private RadioButton r1, r2, r3, r4, r5;
    private int coffeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coffee);

        initViews();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeApi = retrofit.create(CoffeeApi.class);

        Intent intent = getIntent();
        if (null != intent) {
            coffeeId = intent.getIntExtra(COFFEE_ID_KEY, -1);
            if (coffeeId != -1) {
                getSingleCoffee(coffeeId);
            }
        }

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCoffee(coffeeId);
            }
        });

        loadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUrl = txtPicUrl.getText().toString();
                Glide.with(EditCoffee.this)
                        .asBitmap().load(imageUrl)
                        .into(imgAddCoffee);
            }
        });
    }

    private void initViews() {
        loadImgBtn = findViewById(R.id.loadImgBtn);
        saveChangesBtn = findViewById(R.id.saveChangesBtn);
        imgAddCoffee = findViewById(R.id.imgAddCoffee);
        ratingRadioGroup = findViewById(R.id.ratingRadioGroup);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        r5 = findViewById(R.id.r5);
        txtAddCoffeeName = findViewById(R.id.txtAddCoffeeName);
        txtAddOrigin = findViewById(R.id.txtAddOrigin);
        txtRoaster = findViewById(R.id.txtRoaster);
        txtPicUrl = findViewById(R.id.txtPicUrl);

    }

    private void editCoffee(int id) {
        imageUrl = txtPicUrl.getText().toString();
        name = txtAddCoffeeName.getText().toString();
        origin = txtAddOrigin.getText().toString();
        roaster = txtRoaster.getText().toString();
        setRating();

        Coffee coffee = new Coffee(name, origin, roaster, rating, imageUrl);
        Call<Void> call = coffeeApi.updateCoffee(id, coffee);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(EditCoffee.this, "Code: " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(EditCoffee.this, "Changes saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditCoffee.this, MyCoffeeBase.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditCoffee.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSingleCoffee(int id) {
        Call<Coffee> call = coffeeApi.getSingleCoffee(id);
        call.enqueue(new Callback<Coffee>() {
            @Override
            public void onResponse(Call<Coffee> call, Response<Coffee> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(EditCoffee.this, "Code: " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                coffee = response.body();
                txtPicUrl.setText(coffee.getImageUrl());
                txtAddCoffeeName.setText(coffee.getName());
                txtAddOrigin.setText(coffee.getOrigin());
                txtRoaster.setText(coffee.getRoaster());
                getRating();
                Glide.with(EditCoffee.this)
                        .asBitmap().load(coffee.getImageUrl())
                        .into(imgAddCoffee);
            }

            @Override
            public void onFailure(Call<Coffee> call, Throwable t) {
                Toast.makeText(EditCoffee.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRating() {
        int id = ratingRadioGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.r1: rating="1";
                break;
            case R.id.r2: rating="2";
                break;
            case R.id.r3: rating="3";
                break;
            case R.id.r4: rating="4";
                break;
            case R.id.r5: rating="5";
                break;
        }
    }

    private void getRating() {
        int rating = Integer.parseInt(coffee.getRating());
        switch (rating) {
            case 1: r1.setChecked(true);
                break;
            case 2: r2.setChecked(true);
                break;
            case 3: r3.setChecked(true);
                break;
            case 4: r4.setChecked(true);
                break;
            case 5: r5.setChecked(true);
                break;
        }
    }
}

