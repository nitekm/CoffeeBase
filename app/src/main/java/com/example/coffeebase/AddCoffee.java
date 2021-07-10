package com.example.coffeebase;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddCoffee extends AppCompatActivity {

    private String name, origin, roaster, rating, imageUrl;
    private CoffeeBaseApi coffeeBaseApi;
    private Button loadImgBtn, addToCoffeeBaseBtn;
    private ImageView imgAddCoffee;
    private EditText txtAddCoffeeName, txtAddOrigin, txtRoaster, txtPicUrl;
    private RadioGroup ratingRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coffee);

        initViews();

        addToCoffeeBaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addCoffee();
            }
        });

        loadImgBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               imageUrl = txtPicUrl.getText().toString();
               Glide.with(AddCoffee.this)
                       .asBitmap().load(imageUrl)
                       .into(imgAddCoffee);
           }
       });
    }

    private void initViews() {
        loadImgBtn = findViewById(R.id.loadImgBtn);
        addToCoffeeBaseBtn = findViewById(R.id.saveChangesBtn);
        imgAddCoffee = findViewById(R.id.imgAddCoffee);
        ratingRadioGroup = findViewById(R.id.ratingRadioGroup);
        txtAddCoffeeName = findViewById(R.id.txtAddCoffeeName);
        txtAddOrigin = findViewById(R.id.txtAddOrigin);
        txtRoaster = findViewById(R.id.txtRoaster);
        txtPicUrl = findViewById(R.id.txtPicUrl);

    }

    private void addCoffee() {
        name = txtAddCoffeeName.getText().toString();
        origin = txtAddOrigin.getText().toString();
        roaster = txtRoaster.getText().toString();
        setRating();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coffeeBaseApi = retrofit.create(CoffeeBaseApi.class);

        Coffee coffee = new Coffee(name, origin, roaster, rating, imageUrl);
        Call<Void> call = coffeeBaseApi.addToCoffeeBase(coffee);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(AddCoffee.this, "Code: " + response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(AddCoffee.this, "Coffee added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddCoffee.this, MyCoffeeBase.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddCoffee.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
}